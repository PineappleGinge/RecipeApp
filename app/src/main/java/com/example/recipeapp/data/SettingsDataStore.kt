package com.example.recipeapp.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.settingsDataStore by preferencesDataStore("settings")

object SettingsKeys {
    val DARK_MODE = booleanPreferencesKey("dark_mode")
    val NOTIFICATIONS = booleanPreferencesKey("notifications")
    val DEFAULT_SERVINGS = intPreferencesKey("default_servings")
}

class SettingsDataStore(private val context: Context) {

    val darkMode: Flow<Boolean> =
        context.settingsDataStore.data.map { prefs ->
            prefs[SettingsKeys.DARK_MODE] ?: false
        }

    val notificationsEnabled: Flow<Boolean> =
        context.settingsDataStore.data.map { prefs ->
            prefs[SettingsKeys.NOTIFICATIONS] ?: true
        }

    val defaultServings: Flow<Int> =
        context.settingsDataStore.data.map { prefs ->
            prefs[SettingsKeys.DEFAULT_SERVINGS] ?: 4
        }

    suspend fun setDarkMode(value: Boolean) {
        context.settingsDataStore.edit { prefs ->
            prefs[SettingsKeys.DARK_MODE] = value
        }
    }

    suspend fun setNotifications(value: Boolean) {
        context.settingsDataStore.edit { prefs ->
            prefs[SettingsKeys.NOTIFICATIONS] = value
        }
    }

    suspend fun setDefaultServings(value: Int) {
        context.settingsDataStore.edit { prefs ->
            prefs[SettingsKeys.DEFAULT_SERVINGS] = value
        }
    }
}
