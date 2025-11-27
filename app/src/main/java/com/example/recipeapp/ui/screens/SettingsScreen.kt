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
    darkMode: Boolean,
    notificationsEnabled: Boolean,
    defaultServings: Int,
    onDarkModeChange: (Boolean) -> Unit,
    onNotificationsChange: (Boolean) -> Unit,
    onDefaultServingsChange: (Int) -> Unit,
    onClearShoppingList: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text("Settings", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {

            // DARK MODE TOGGLE
            item {
                SettingToggle(
                    title = "Dark Mode",
                    checked = darkMode,
                    onChecked = onDarkModeChange
                )
            }

            // NOTIFICATIONS TOGGLE
            item {
                SettingToggle(
                    title = "Notifications",
                    checked = notificationsEnabled,
                    onChecked = onNotificationsChange
                )
            }

            // DEFAULT SERVINGS
            item {
                Spacer(modifier = Modifier.height(20.dp))
                Text("Default Recipe Servings", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = {
                            if (defaultServings > 1) onDefaultServingsChange(defaultServings - 1)
                        }
                    ) { Text("-") }

                    Text(text = "$defaultServings servings")

                    IconButton(
                        onClick = {
                            if (defaultServings < 12) onDefaultServingsChange(defaultServings + 1)
                        }
                    ) { Text("+") }
                }
            }

            // CLEAR SHOPPING LIST BUTTON
            item {
                Spacer(modifier = Modifier.height(32.dp))

                if (onClearShoppingList != null) {
                    Button(
                        onClick = onClearShoppingList,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
                    ) {
                        Text("Clear Shopping List")
                    }
                }
            }

            // ABOUT SECTION
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
fun SettingToggle(title: String, checked: Boolean, onChecked: (Boolean) -> Unit) {
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