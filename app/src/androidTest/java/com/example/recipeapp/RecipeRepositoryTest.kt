package com.example.recipeapp

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.recipeapp.data.local.AppDatabase
import com.example.recipeapp.data.local.Ingredient
import com.example.recipeapp.data.local.Recipe
import com.example.recipeapp.data.local.RecipeRepository
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RecipeRepositoryTest {

    private lateinit var db: AppDatabase
    private lateinit var repository: RecipeRepository

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        db = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        repository = RecipeRepository(
            recipeDao = db.recipeDao(),
            ingredientDao = db.ingredientDao(),
            shoppingListDao = db.shoppingListDao()
        )
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun testInsertRecipeWithIngredientsAndImage() = runTest {

        val imageUrl = "https://live.staticflickr.com/3305/3328844393_de8e2bb524_b.jpg"

        val recipe = Recipe(
            id = 1,
            name = "Chocolate Cake",
            imageUrl = imageUrl,
        )

        repository.addRecipe(recipe)

        val ingredients = listOf(
            Ingredient(recipeId = 1, name = "Flour"),
            Ingredient(recipeId = 1, name = "Eggs"),
            Ingredient(recipeId = 1, name = "Chocolate")
        )

        ingredients.forEach {
            repository.addIngredient(it)
        }

        val loadedRecipe = repository.getRecipeById(1)
        Assert.assertNotNull(loadedRecipe)
        Assert.assertEquals("Chocolate Cake", loadedRecipe?.name)
        Assert.assertEquals(imageUrl, loadedRecipe?.imageUrl)

        val loadedIngredients = repository.getIngredientsForRecipe(1)
        Assert.assertEquals(3, loadedIngredients.size)
        Assert.assertEquals("Flour", loadedIngredients[0].name)
    }
}