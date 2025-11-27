package com.example.recipeapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen(
    onClearShoppingList: (() -> Unit)? = null
) {

    var darkMode by remember { mutableStateOf(false) }
    var notificationsEnabled by remember { mutableStateOf(true) }
    var defaultServings by remember { mutableStateOf(4) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text("Settings", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {

            // Dark Mode Toggle
            item {
                SettingToggle(
                    title = "Dark Mode",
                    checked = darkMode,
                    onChecked = { darkMode = it }
                )
            }

            // Notifications Toggle
            item {
                SettingToggle(
                    title = "Notifications",
                    checked = notificationsEnabled,
                    onChecked = { notificationsEnabled = it }
                )
            }

            // Default Servings Selector
            item {
                Spacer(modifier = Modifier.height(12.dp))
                Text("Default Recipe Servings", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = {
                        if (defaultServings > 1) defaultServings--
                    }) {
                        Text("-")
                    }

                    Text("$defaultServings servings")

                    IconButton(onClick = {
                        if (defaultServings < 12) defaultServings++
                    }) {
                        Text("+")
                    }
                }
            }

            // Clear Shopping List button
            item {
                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { onClearShoppingList?.invoke() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
                ) {
                    Text("Clear Shopping List")
                }
            }

            // About Section
            item {
                Spacer(modifier = Modifier.height(32.dp))
                Text("About", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))

                Text("Recipe App v1.0")
                Text("Developed by: Your Name")
                Text("For education purposes.")
            }
        }
    }
}

@Composable
fun SettingToggle(
    title: String,
    checked: Boolean,
    onChecked: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(title, style = MaterialTheme.typography.bodyLarge)
        Switch(
            checked = checked,
            onCheckedChange = onChecked
        )
    }
}