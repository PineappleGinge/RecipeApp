package com.example.recipeapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ingredients")
data class `Ingredient`(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val recipeId: Int,
    val name: String,
    val hasItem: Boolean = false
)
