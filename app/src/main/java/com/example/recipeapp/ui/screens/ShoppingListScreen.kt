package com.example.recipeapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.recipeapp.data.local.ShoppingListItem

@Composable
fun ShoppingListScreen(
    items: List<ShoppingListItem>,
    onItemCheckedChange: (ShoppingListItem) -> Unit,
    onAddItem: (String) -> Unit,
    onDeleteItem: (ShoppingListItem) -> Unit,
    onClearAll: () -> Unit
) {
    var text by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Add Ingredient") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = {
                if (text.isNotBlank()) {
                    onAddItem(text)
                    text = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add")
        }

        Spacer(Modifier.height(16.dp))

        LazyColumn {
            items(items) { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable { onItemCheckedChange(item) },
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(item.name)
                    Row {
                        Checkbox(
                            checked = item.hasItem,
                            onCheckedChange = { onItemCheckedChange(item) }
                        )
                        IconButton(onClick = { onDeleteItem(item) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = onClearAll,
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Clear All")
        }
    }
}
