package com.example.recipeapp.data.repository

import com.example.recipeapp.data.local.Ingredient
import com.example.recipeapp.data.local.IngredientDao
import com.example.recipeapp.data.local.Recipe
import com.example.recipeapp.data.local.RecipeDao
import com.example.recipeapp.data.local.ShoppingListDao
import com.example.recipeapp.data.local.ShoppingListItem
import kotlinx.coroutines.flow.Flow

class RecipeRepository(
    private val recipeDao: RecipeDao,
    private val ingredientDao: IngredientDao,
    private val shoppingListDao: ShoppingListDao
) {

    // Stream all recipes
    fun getAllRecipes(): Flow<List<Recipe>> {
        return recipeDao.getAllRecipes()
    }

    // Stream a single recipe by id
    fun getRecipeById(id: Int): Flow<Recipe?> {
        return recipeDao.getRecipeById(id)
    }

    // Stream recipes that match the search query
    fun searchRecipes(query: String): Flow<List<Recipe>> {
        return recipeDao.searchRecipes(query)
    }

    // Insert or replace a recipe
    suspend fun addRecipe(recipe: Recipe): Long {
        return recipeDao.insertRecipe(recipe)
    }

    // Update an existing recipe
    suspend fun updateRecipe(recipe: Recipe) {
        recipeDao.updateRecipe(recipe)
    }

    // Remove a recipe
    suspend fun deleteRecipe(recipe: Recipe) {
        recipeDao.deleteRecipe(recipe)
    }

    // Insert a single ingredient
    suspend fun addIngredient(ingredient: Ingredient) {
        ingredientDao.insertIngredients(listOf(ingredient))
    }

    // Insert multiple ingredients
    suspend fun addIngredients(ingredients: List<Ingredient>) {
        ingredientDao.insertIngredients(ingredients)
    }

    // Load all ingredients for a recipe
    suspend fun getIngredientsForRecipe(recipeId: Int): List<Ingredient> {
        return ingredientDao.getIngredientsForRecipe(recipeId)
    }

    // Update a single ingredient 
    suspend fun updateIngredient(ingredient: Ingredient) {
        ingredientDao.updateIngredient(ingredient)
    }

    // Delete all ingredients for a given recipe
    suspend fun deleteIngredientsForRecipe(recipeId: Int) {
        ingredientDao.deleteByRecipeId(recipeId)
    }

    suspend fun replaceIngredientsForRecipe(recipeId: Int, ingredients: List<Ingredient>) {
        // Replace the full ingredient list to keep UI and persistence consistent after edits.
        ingredientDao.deleteByRecipeId(recipeId)
        ingredientDao.insertIngredients(ingredients)
    }

    // Reset a single ingredient's checked state by name
    suspend fun uncheckIngredientByName(name: String) {
        ingredientDao.uncheckByName(name)
    }

    // Reset all ingredient checkmarks.
    suspend fun clearAllIngredientChecks() {
        ingredientDao.clearAllChecks()
    }

    // Load the current shopping list.
    suspend fun getShoppingList(): List<ShoppingListItem> {
        return shoppingListDao.getShoppingList()
    }

    // Find a shopping item by name.
    suspend fun getShoppingItemByName(name: String): ShoppingListItem? {
        return shoppingListDao.getItemByName(name)
    }

    // Insert or replace a shopping item. 
    suspend fun addShoppingItem(item: ShoppingListItem) {
        shoppingListDao.insertItem(item)
    }

    // Insert a shopping item only if one with the same name does not exist.
    suspend fun addShoppingItemIfMissing(name: String) {
        val existing = shoppingListDao.getItemByName(name)
        if (existing == null) {
            shoppingListDao.insertItem(ShoppingListItem(name = name))
        }
    }

    // Update a shopping item. 
    suspend fun updateShoppingItem(item: ShoppingListItem) {
        shoppingListDao.updateItem(item)
    }

    // Delete a shopping item. 
    suspend fun deleteShoppingItem(item: ShoppingListItem) {
        shoppingListDao.deleteItem(item)
    }

    // Delete all shopping items. 
    suspend fun clearShoppingList() {
        shoppingListDao.clearAll()
    }

    suspend fun resetShoppingListChecks() {
        shoppingListDao.clearChecks()
    }
}
