package com.example.recipeapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shopping_list")
data class ShoppingListItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val hasItem: Boolean = false
)
