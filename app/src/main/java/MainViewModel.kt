package com.example.recipeapp


import android.view.View
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.data.SettingsDataStore
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ListItem(
    val id: Int,
    val title: String,
    val checked: Boolean = false
)

class MainViewModel : ViewModel(){

    private val _shoppingItems = MutableStateFlow(
        List(10) {index ->
            ListItem(id = index, title = "List item ${index + 1}")
        }
    )
    val shoppingItems: StateFlow<List<ListItem>> = _shoppingItems.asStateFlow()

    fun toggleItem(id: Int){
        _shoppingItems.value = _shoppingItems.value.map { item ->
            if (item.id == id) {
                item.copy(checked = !item.checked)
            } else {
                item
            }
        }
    }
}