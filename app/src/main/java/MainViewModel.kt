package com.example.recipeapp

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.example.recipeapp.data.local.*
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.data.local.*
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val repository = RecipeRepository(
        db.recipeDao(),
        db.ingredientDao(),
        db.shoppingListDao()
    )

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
        viewModelScope.launch {
            _shoppingList.value = repository.getShoppingList()
        }
    }

    fun addShoppingItem(name: String) {
        viewModelScope.launch {
            repository.addShoppingItem(
                ShoppingListItem(name = name)
            )
            loadShoppingList()
        }
    }

    fun toggleShoppingItem(item: ShoppingListItem) {
        viewModelScope.launch {
            val updated = item.copy(hasItem = !item.hasItem)
            repository.updateShoppingItem(updated)
            loadShoppingList()
        }
    }

    fun deleteShoppingItem(item: ShoppingListItem) {
        viewModelScope.launch {
            repository.deleteShoppingItem(item)
            loadShoppingList()
        }
    }

    fun clearShoppingList() {
        viewModelScope.launch {
            repository.clearShoppingList()
            loadShoppingList()
        }
    }

    private fun loadDefaultRecipe() {
        viewModelScope.launch {
            val recipes = repository.getAllRecipes()
            if (recipes.isNotEmpty()) {
                val firstRecipe = recipes.first()
                _selectedRecipe.value = firstRecipe
                loadIngredients(firstRecipe.id)
            }
        }
    }

    fun loadRecipe(id: Int) {
        viewModelScope.launch {
            val recipe = repository.getRecipeById(id)
            _selectedRecipe.value = recipe
            recipe?.let {
                loadIngredients(it.id)
            }
        }
    }

    private fun loadIngredients(recipeId: Int) {
        viewModelScope.launch {
            _ingredients.value = repository.getIngredientsForRecipe(recipeId)
        }
    }

    fun searchRecipes(query: String): List<Recipe> {
        return repository.searchRecipes(query)
    }
}