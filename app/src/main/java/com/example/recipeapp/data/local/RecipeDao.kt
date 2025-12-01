package com.example.recipeapp.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {

    @Query("SELECT * FROM recipes")
    fun getAllRecipes(): Flow<List<Recipe>>

    @Query("SELECT * FROM recipes WHERE id = :id")
    fun getRecipeById(id: Int): Flow<Recipe?>

    @Query("SELECT * FROM recipes WHERE name LIKE '%' || :query || '%'")
    fun searchRecipes(query: String): Flow<List<Recipe>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: Recipe): Long

    @Delete
    suspend fun deleteRecipe(recipe: Recipe)
}
