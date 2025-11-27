package com.example.recipeapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.data.SettingsDataStore
import com.example.recipeapp.data.local.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
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
        loadShoppingList()
        loadDefaultRecipe()
    }

    private fun loadShoppingList() {
        viewModelScope.launch(Dispatchers.IO) {
            _shoppingList.value = repository.getShoppingList()
        }
    }

    fun addShoppingItem(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addShoppingItem(ShoppingListItem(name = name))
            _shoppingList.value = repository.getShoppingList()
        }
    }

    fun toggleShoppingItem(item: ShoppingListItem) {
        viewModelScope.launch(Dispatchers.IO) {
            val updated = item.copy(hasItem = !item.hasItem)
            repository.updateShoppingItem(updated)
            _shoppingList.value = repository.getShoppingList()
        }
    }

    fun deleteShoppingItem(item: ShoppingListItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteShoppingItem(item)
            _shoppingList.value = repository.getShoppingList()
        }
    }

    fun clearShoppingList() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.clearShoppingList()
            _shoppingList.value = repository.getShoppingList()
        }
    }

    private fun loadDefaultRecipe() {
        viewModelScope.launch(Dispatchers.IO) {
            val recipes = repository.getAllRecipes()
            if (recipes.isNotEmpty()) {
                val first = recipes.first()
                _selectedRecipe.value = first
                _ingredients.value = repository.getIngredientsForRecipe(first.id)
            }
        }
    }

    fun loadRecipe(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val recipe = repository.getRecipeById(id)
            _selectedRecipe.value = recipe
            if (recipe != null) {
                _ingredients.value = repository.getIngredientsForRecipe(recipe.id)
            }
        }
    }

    private fun loadIngredients(recipeId: Int) {
        viewModelScope.launch {
            _ingredients.value = repository.getIngredientsForRecipe(recipeId)
        }
    }

    fun searchRecipes(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val results = repository.searchRecipes(query)
            _searchResults.value = results
        }
    }


    fun toggleIngredient(item: Ingredient) {
        viewModelScope.launch {
            val updated = item.copy(hasItem = !item.hasItem)
            repository.updateIngredient(updated)
            loadIngredients(item.recipeId)
        }
    }

}
