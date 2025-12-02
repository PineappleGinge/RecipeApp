package com.example.recipeapp.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface IngredientDao {

    @Query("SELECT * FROM ingredients WHERE recipeId = :recipeId")
    suspend fun getIngredientsForRecipe(recipeId: Int): List<Ingredient>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngredient(ingredients: Ingredient)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngredients(ingredients: List<Ingredient>)

    @Update
    suspend fun updateIngredient(ingredient: Ingredient)

    @Update
    suspend fun updateIngredients(ingredients: List<Ingredient>)

    @Query("UPDATE ingredients SET hasItem = 0 WHERE name = :name")
    suspend fun uncheckByName(name: String)

    @Query("UPDATE ingredients SET hasItem = 0")
    suspend fun clearAllChecks()

    @Query("DELETE FROM ingredients WHERE recipeId = :recipeId")
    suspend fun deleteByRecipeId(recipeId: Int)
}
