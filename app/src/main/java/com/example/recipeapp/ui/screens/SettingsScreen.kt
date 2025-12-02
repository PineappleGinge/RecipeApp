package com.example.recipeapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics

@Composable
fun SettingsScreen(
    darkMode: Boolean,
    notificationsEnabled: Boolean,
    defaultServings: Int,
    onDarkModeChange: (Boolean) -> Unit,
    onNotificationsChange: (Boolean) -> Unit,
    onDefaultServingsChange: (Int) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            "Settings",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        SettingToggle(
            title = "Dark Mode",
            checked = darkMode,
            onChecked = onDarkModeChange,
            description = "Toggle dark theme"
        )

        SettingToggle(
            title = "Notifications",
            checked = notificationsEnabled,
            onChecked = onNotificationsChange,
            description = "Enable app notifications"
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "Default Recipe Servings",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Button(
                onClick = {
                    if (defaultServings > 1)
                        onDefaultServingsChange(defaultServings - 1)
                }
            ) {
                Text("-")
            }

            Text(
                "$defaultServings servings",
                style = MaterialTheme.typography.bodyLarge
            )

            Button(
                onClick = {
                    if (defaultServings < 12)
                        onDefaultServingsChange(defaultServings + 1)
                }
            ) {
                Text("+")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text("About", style = MaterialTheme.typography.titleMedium)
        Text("Recipe App")
    }
}

@Composable
fun SettingToggle(
    title: String,
    checked: Boolean,
    onChecked: (Boolean) -> Unit,
    description: String
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
            onCheckedChange = onChecked,
            modifier = Modifier.semantics {
                contentDescription = description
            }
        )
    }
}
