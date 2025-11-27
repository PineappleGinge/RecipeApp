package com.example.recipeapp



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

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val settings = SettingsDataStore(application)
    private val _shoppingItems = MutableStateFlow(
        List(10) { index ->
            ListItem(id = index, title = "List item ${index + 1}")
        }
    )
    val shoppingItems: StateFlow<List<ListItem>> = _shoppingItems.asStateFlow()

    fun toggleItem(id: Int) {
        _shoppingItems.value = _shoppingItems.value.map {
            if (it.id == id) it.copy(checked = !it.checked) else it
        }
    }

    val darkMode = settings.darkMode.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        false
    )

    val notificationsEnabled = settings.notificationsEnabled.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        true
    )

    val defaultServings = settings.defaultServings.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        4
    )

    fun toggleDarkMode(value: Boolean) {
        viewModelScope.launch { settings.setDarkMode(value) }
    }

    fun toggleNotifications(value: Boolean) {
        viewModelScope.launch { settings.setNotifications(value) }
    }

    fun setDefaultServings(value: Int) {
        viewModelScope.launch { settings.setDefaultServings(value) }
    }
}