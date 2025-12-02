package com.example.recipeapp

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.recipeapp.data.local.AppDatabase
import com.example.recipeapp.data.local.Ingredient
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainViewModelTest {

    private lateinit var app: Application
    private lateinit var db: AppDatabase

    @Before
    fun setup() = runBlocking {
        app = ApplicationProvider.getApplicationContext()
        db = AppDatabase.getInstance(app)
        db.clearAllTables()
    }

    @After
    fun teardown() = runBlocking {
        db.clearAllTables()
    }

    @Test
    fun togglingIngredientAddsToShoppingList() = runBlocking {
        val viewModel = MainViewModel(app)

        val firstIngredient = withTimeout(3_000) {
            viewModel.ingredients
                .filter { it.isNotEmpty() }
                .first()
                .first()
        }

        viewModel.toggleIngredient(firstIngredient)

        val shoppingItems = withTimeout(3_000) {
            var items = db.shoppingListDao().getShoppingList()
            while (items.none { it.name == firstIngredient.name }) {
                delay(100)
                items = db.shoppingListDao().getShoppingList()
            }
            items
        }

        assertTrue(shoppingItems.any { it.name == firstIngredient.name })
    }

    @Test
    fun updateCurrentRecipeReplacesIngredients() = runBlocking {
        val viewModel = MainViewModel(app)

        val currentRecipe = withTimeout(3_000) {
            viewModel.selectedRecipe.filterNotNull().first()
        }
        val existingIngredients = viewModel.ingredients.first()
        val newName = "${currentRecipe.name} Updated"
        val newDescription = "Updated description"
        val newIngredients = listOf("Vanilla", "Milk")

        viewModel.updateCurrentRecipe(
            name = newName,
            ingredients = newIngredients,
            description = newDescription,
            imageUrl = null
        )

        val updatedRecipe = withTimeout(3_000) {
            db.recipeDao().getRecipeById(currentRecipe.id).filterNotNull().first {
                it.name == newName
            }
        }

        val updatedIngredients = withTimeout(3_000) {
            var ingredients: List<Ingredient> = db.ingredientDao().getIngredientsForRecipe(currentRecipe.id)
            while (ingredients.size != newIngredients.size) {
                delay(100)
                ingredients = db.ingredientDao().getIngredientsForRecipe(currentRecipe.id)
            }
            ingredients
        }

        assertEquals(newName, updatedRecipe.name)
        assertEquals(newDescription, updatedRecipe.description)
        assertEquals(newIngredients.size, updatedIngredients.size)
        newIngredients.forEach { name ->
            assertTrue(updatedIngredients.any { it.name == name })
        }
        existingIngredients.forEach { old ->
            assertFalse(updatedIngredients.any { it.name == old.name && old.name !in newIngredients })
        }
    }
}
