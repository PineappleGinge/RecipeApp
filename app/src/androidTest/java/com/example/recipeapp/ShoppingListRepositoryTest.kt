package com.example.recipeapp

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.recipeapp.data.local.AppDatabase
import com.example.recipeapp.data.local.ShoppingListItem
import com.example.recipeapp.data.repository.RecipeRepository
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ShoppingListRepositoryTest {

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
    fun shoppingListCrudWorks() = runTest {
        val milk = ShoppingListItem(name = "Milk")
        repository.addShoppingItem(milk)
        val sugar = ShoppingListItem(name = "Sugar")
        repository.addShoppingItem(sugar)

        var items = repository.getShoppingList()
        assertEquals(2, items.size)

        // Update check state
        val updatedMilk = items.first { it.name == "Milk" }.copy(hasItem = true)
        repository.updateShoppingItem(updatedMilk)

        items = repository.getShoppingList()
        assertTrue(items.first { it.name == "Milk" }.hasItem)

        // Clear checks
        repository.resetShoppingListChecks()
        items = repository.getShoppingList()
        assertTrue(items.none { it.hasItem })

        // Delete one and clear all
        repository.deleteShoppingItem(items.first { it.name == "Sugar" })
        items = repository.getShoppingList()
        assertEquals(1, items.size)

        repository.clearShoppingList()
        items = repository.getShoppingList()
        assertTrue(items.isEmpty())
    }
}
