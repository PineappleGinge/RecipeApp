package com.example.recipeapp.data.repository

import com.example.recipeapp.data.local.*

class RecipeRepository(
    private val recipeDao: RecipeDao,
    private val ingredientDao: IngredientDao,
    private val shoppingListDao: ShoppingListDao
) {

    suspend fun addRecipe(recipe: Recipe) {
        recipeDao.insertRecipe(recipe)
    }

    suspend fun getAllRecipes(): List<Recipe> {
        return recipeDao.getAllRecipes()
    }

    suspend fun addIngredient(ingredient: Ingredient) {
        ingredientDao.insertIngredient(ingredient)
    }

    suspend fun getIngredientsForRecipe(recipeId: Int): List<Ingredient> {
        return ingredientDao.getIngredientsForRecipe(recipeId)
    }

    suspend fun addShoppingItem(item: ShoppingListItem) {
        shoppingListDao.insertItem(item)
    }

    suspend fun getShoppingList(): List<ShoppingListItem> {
        return shoppingListDao.getShoppingItems()
    }

    suspend fun deleteShoppingItem(item: ShoppingListItem) {
        shoppingListDao.deleteItem(item)
    }

    suspend fun clearShoppingList() {
        shoppingListDao.clearAll()
    }
}
