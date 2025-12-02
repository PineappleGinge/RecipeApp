package com.example.recipeapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.data.SettingsDataStore
import com.example.recipeapp.data.local.AppDatabase
import com.example.recipeapp.data.local.Ingredient
import com.example.recipeapp.data.local.Recipe
import com.example.recipeapp.data.local.ShoppingListItem
import com.example.recipeapp.data.repository.RecipeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val _searchResults = MutableStateFlow<List<Recipe>>(emptyList())
    val searchResults = _searchResults

    private val db = AppDatabase.getInstance(application)
    private val repository = RecipeRepository(
        db.recipeDao(),
        db.ingredientDao(),
        db.shoppingListDao()
    )

    private val settings = SettingsDataStore(application)

    val darkMode: StateFlow<Boolean> = settings.darkMode.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = false
    )

    val notificationsEnabled: StateFlow<Boolean> = settings.notificationsEnabled.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = true
    )

    val defaultServings: StateFlow<Int> = settings.defaultServings.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = 4
    )

    fun toggleDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            settings.setDarkMode(enabled)
        }
    }

    fun toggleNotifications(enabled: Boolean) {
        viewModelScope.launch {
            settings.setNotifications(enabled)
        }
    }

    fun setDefaultServings(value: Int) {
        viewModelScope.launch {
            settings.setDefaultServings(value)
        }
    }

    private val _shoppingList = MutableStateFlow<List<ShoppingListItem>>(emptyList())
    val shoppingList: StateFlow<List<ShoppingListItem>> = _shoppingList.asStateFlow()

    private val _selectedRecipe = MutableStateFlow<Recipe?>(null)
    val selectedRecipe: StateFlow<Recipe?> = _selectedRecipe

    private val _ingredients = MutableStateFlow<List<Ingredient>>(emptyList())
    val ingredients: StateFlow<List<Ingredient>> = _ingredients.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            seedIfEmpty()
            loadDefaultRecipeInternal()
            loadShoppingListInternal()
        }
    }

    private suspend fun loadShoppingListInternal() {
        _shoppingList.value = repository.getShoppingList()
    }

    private suspend fun seedIfEmpty() {
        val existing = repository.getAllRecipes().first()
        val placeholder = "https://images.unsplash.com/photo-1512621776951-a57141f2eefd"
        val seeds = listOf(
            "Chocolate Cake" to listOf("Flour", "Eggs", "Chocolate", "Sugar", "Butter", "Baking powder"),
            "Vanilla Cupcakes" to listOf("Flour", "Sugar", "Butter", "Eggs", "Vanilla", "Baking powder", "Milk"),
            "Apple Pie" to listOf("Apples", "Pie crust", "Sugar", "Butter", "Cinnamon", "Nutmeg", "Lemon juice"),
            "Bacon and Cabbage" to listOf("Bacon", "Cabbage", "Potatoes", "Onion", "Butter", "Salt", "Pepper"),
            "Garlic Roasted Potatoes" to listOf("Potatoes", "Olive oil", "Garlic", "Rosemary", "Salt", "Pepper")
        )

        seeds.forEach { (name, ingredientNames) ->
            if (existing.none { it.name == name }) {
                val recipeId = repository.addRecipe(
                    Recipe(
                        name = name,
                        imageUrl = placeholder
                    )
                ).toInt()
                repository.addIngredients(
                    ingredientNames.map { Ingredient(recipeId = recipeId, name = it) }
                )
            }
        }
    }

    fun addRecipeWithIngredients(
        name: String,
        ingredients: List<String>,
        imageUrl: String? = null,
        description: String? = null
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val recipeId = repository.addRecipe(
                Recipe(
                    name = name,
                    imageUrl = imageUrl,
                    description = description
                )
            ).toInt()
            repository.addIngredients(
                ingredients.map { Ingredient(recipeId = recipeId, name = it) }
            )
        }
    }

    private suspend fun loadDefaultRecipeInternal() {
        val recipes = repository.getAllRecipes().first()
        if (recipes.isNotEmpty()) {
            val first = recipes.first()
            _selectedRecipe.value = first
            _ingredients.value = repository.getIngredientsForRecipe(first.id)
        }
    }

    private suspend fun loadIngredientsInternal(recipeId: Int) {
        _ingredients.value = repository.getIngredientsForRecipe(recipeId)
    }

    fun loadShoppingList() {
        viewModelScope.launch(Dispatchers.IO) {
            loadShoppingListInternal()
        }
    }

    fun addShoppingItem(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addShoppingItem(ShoppingListItem(name = name))
            loadShoppingListInternal()
        }
    }

    fun toggleShoppingItem(item: ShoppingListItem) {
        viewModelScope.launch(Dispatchers.IO) {
            val updated = item.copy(hasItem = !item.hasItem)
            repository.updateShoppingItem(updated)
            if (!updated.hasItem) {
                repository.uncheckIngredientByName(updated.name)
                refreshSelectedIngredients()
            }
            loadShoppingListInternal()
        }
    }

    fun deleteShoppingItem(item: ShoppingListItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteShoppingItem(item)
            repository.uncheckIngredientByName(item.name)
            refreshSelectedIngredients()
            loadShoppingListInternal()
        }
    }

    fun clearShoppingList() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.clearShoppingList()
            repository.clearAllIngredientChecks()
            refreshSelectedIngredients()
            loadShoppingListInternal()
        }
    }

    private suspend fun refreshSelectedIngredients() {
        val currentRecipe = _selectedRecipe.value
        if (currentRecipe != null) {
            loadIngredientsInternal(currentRecipe.id)
        }
    }

    fun loadRecipe(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val recipe = repository.getRecipeById(id).first()
            _selectedRecipe.value = recipe
            if (recipe != null) {
                loadIngredientsInternal(recipe.id)
            }
        }
    }

    fun searchRecipes(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val results = repository.searchRecipes(query).first()
            _searchResults.value = results
        }
    }


    fun toggleIngredient(item: Ingredient) {
        viewModelScope.launch(Dispatchers.IO) {
            val updated = item.copy(hasItem = !item.hasItem)
            repository.updateIngredient(updated)
            if (updated.hasItem) {
                repository.addShoppingItemIfMissing(updated.name)
                loadShoppingListInternal()
            }
            loadIngredientsInternal(item.recipeId)
        }
    }

    fun deleteCurrentRecipe(onComplete: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val current = _selectedRecipe.value ?: return@launch
            repository.deleteIngredientsForRecipe(current.id)
            repository.deleteRecipe(current)

            val remaining = repository.getAllRecipes().first()
            if (remaining.isNotEmpty()) {
                val next = remaining.first()
                _selectedRecipe.value = next
                _ingredients.value = repository.getIngredientsForRecipe(next.id)
            } else {
                _selectedRecipe.value = null
                _ingredients.value = emptyList()
            }

            withContext(Dispatchers.Main) {
                onComplete()
            }
        }
    }

}
