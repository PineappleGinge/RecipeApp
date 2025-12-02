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

    fun getAllRecipes(): Flow<List<Recipe>> {
        return recipeDao.getAllRecipes()
    }

    fun getRecipeById(id: Int): Flow<Recipe?> {
        return recipeDao.getRecipeById(id)
    }

    fun searchRecipes(query: String): Flow<List<Recipe>> {
        return recipeDao.searchRecipes(query)
    }

    suspend fun addRecipe(recipe: Recipe): Long {
        return recipeDao.insertRecipe(recipe)
    }

    suspend fun deleteRecipe(recipe: Recipe) {
        recipeDao.deleteRecipe(recipe)
    }

    suspend fun addIngredient(ingredient: Ingredient) {
        ingredientDao.insertIngredients(listOf(ingredient))
    }

    suspend fun addIngredients(ingredients: List<Ingredient>) {
        ingredientDao.insertIngredients(ingredients)
    }

    suspend fun getIngredientsForRecipe(recipeId: Int): List<Ingredient> {
        return ingredientDao.getIngredientsForRecipe(recipeId)
    }

    suspend fun updateIngredient(ingredient: Ingredient) {
        ingredientDao.updateIngredient(ingredient)
    }

    suspend fun deleteIngredientsForRecipe(recipeId: Int) {
        ingredientDao.deleteByRecipeId(recipeId)
    }

    suspend fun uncheckIngredientByName(name: String) {
        ingredientDao.uncheckByName(name)
    }

    suspend fun clearAllIngredientChecks() {
        ingredientDao.clearAllChecks()
    }

    suspend fun getShoppingList(): List<ShoppingListItem> {
        return shoppingListDao.getShoppingList()
    }

    suspend fun getShoppingItemByName(name: String): ShoppingListItem? {
        return shoppingListDao.getItemByName(name)
    }

    suspend fun addShoppingItem(item: ShoppingListItem) {
        shoppingListDao.insertItem(item)
    }

    suspend fun addShoppingItemIfMissing(name: String) {
        val existing = shoppingListDao.getItemByName(name)
        if (existing == null) {
            shoppingListDao.insertItem(ShoppingListItem(name = name))
        }
    }

    suspend fun updateShoppingItem(item: ShoppingListItem) {
        shoppingListDao.updateItem(item)
    }

    suspend fun deleteShoppingItem(item: ShoppingListItem) {
        shoppingListDao.deleteItem(item)
    }

    suspend fun clearShoppingList() {
        shoppingListDao.clearAll()
    }
}
