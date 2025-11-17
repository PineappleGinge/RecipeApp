package com.example.recipeapp.navigation


import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ListItem(
    val id: Int,
    val title: String,
    val checked: Boolean = false
)



