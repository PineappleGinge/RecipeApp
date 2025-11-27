package com.example.recipeapp.data.local

class RecipeRepository(
    private val recipeDao: RecipeDao,
    private val ingredientDao: IngredientDao,
    private val shoppingListDao: ShoppingListDao
) {

    fun getAllRecipes(): List<Recipe> {
        return recipeDao.getAllRecipes()
    }

    fun getRecipeById(id: Int): Recipe? {
        return recipeDao.getRecipeById(id)
    }

    suspend fun searchRecipes(query: String): List<Recipe> {
        return recipeDao.searchRecipes(query)
    }


    suspend fun addRecipe(recipe: Recipe) {
        recipeDao.insertRecipe(recipe)
    }

    suspend fun deleteRecipe(recipe: Recipe) {
        recipeDao.deleteRecipe(recipe)
    }

    suspend fun addIngredient(ingredient: Ingredient) {
        ingredientDao.insertIngredients(listOf(ingredient))
    }


    fun getIngredientsForRecipe(recipeId: Int): List<Ingredient> {
        return ingredientDao.getIngredientsForRecipe(recipeId)
    }


    suspend fun updateIngredient(ingredient: Ingredient) {
        ingredientDao.updateIngredient(ingredient)
    }

    fun getShoppingList(): List<ShoppingListItem> {
        return shoppingListDao.getShoppingList()
    }

    suspend fun addShoppingItem(item: ShoppingListItem) {
        shoppingListDao.insertItem(item)
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
