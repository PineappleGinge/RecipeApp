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
        if (repository.getAllRecipes().first().isEmpty()) {
            val recipeId = repository.addRecipe(
                Recipe(
                    name = "Chocolate Cake",
                    imageUrl = "https://example.com/cake.jpg"
                )
            ).toInt()

            repository.addIngredients(
                listOf(
                    Ingredient(recipeId = recipeId, name = "Flour"),
                    Ingredient(recipeId = recipeId, name = "Eggs"),
                    Ingredient(recipeId = recipeId, name = "Chocolate")
                )
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
            loadShoppingListInternal()
        }
    }

    fun deleteShoppingItem(item: ShoppingListItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteShoppingItem(item)
            loadShoppingListInternal()
        }
    }

    fun clearShoppingList() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.clearShoppingList()
            loadShoppingListInternal()
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
            loadIngredientsInternal(item.recipeId)
        }
    }

}
