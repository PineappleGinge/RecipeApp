package com.example.recipeapp.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingListDao {

    @Query("SELECT * FROM shopping_list")
    suspend fun getShoppingList(): List<ShoppingListItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: ShoppingListItem)

    @Update
    suspend fun updateItem(item: ShoppingListItem)

    @Delete
    suspend fun deleteItem(item: ShoppingListItem)

    @Query("SELECT * FROM shopping_list WHERE name = :name LIMIT 1")
    suspend fun getItemByName(name: String): ShoppingListItem?

    @Query("DELETE FROM shopping_list")
    suspend fun clearAll()

    @Query("UPDATE shopping_list SET hasItem = 0")
    suspend fun clearChecks()
}
