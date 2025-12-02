package com.example.recipeapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AddRecipeScreen(
    onSave: (name: String, ingredients: List<String>, imageUrl: String, description: String) -> Unit,
    onCancel: () -> Unit
) {
    val ingredientInputs = remember { mutableStateListOf("") }
    val nameState = remember { androidx.compose.runtime.mutableStateOf("") }
    val imageState = remember { androidx.compose.runtime.mutableStateOf("") }
    val descriptionState = remember { androidx.compose.runtime.mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Add Recipe", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.padding(8.dp))

        OutlinedTextField(
            value = nameState.value,
            onValueChange = { nameState.value = it },
            label = { Text("Recipe name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.padding(4.dp))

        OutlinedTextField(
            value = imageState.value,
            onValueChange = { imageState.value = it },
            label = { Text("Image URL (optional)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.padding(4.dp))

        OutlinedTextField(
            value = descriptionState.value,
            onValueChange = { descriptionState.value = it },
            label = { Text("Description (optional)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.padding(12.dp))

        Text("Ingredients", style = MaterialTheme.typography.titleMedium)

        LazyColumn(
            modifier = Modifier
                .weight(1f, fill = true)
                .padding(vertical = 8.dp)
        ) {
            itemsIndexed(ingredientInputs) { index, value ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = value,
                        onValueChange = { newText ->
                            ingredientInputs[index] = newText
                        },
                        label = { Text("Ingredient ${index + 1}") },
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = {
                            if (ingredientInputs.size > 1) {
                                ingredientInputs.removeAt(index)
                            } else {
                                ingredientInputs[0] = ""
                            }
                        }
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Remove ingredient")
                    }
                }
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { ingredientInputs.add("") }) {
                Icon(Icons.Default.Add, contentDescription = "Add ingredient")
                Spacer(modifier = Modifier.padding(4.dp))
                Text("Add ingredient")
            }
        }

        Spacer(modifier = Modifier.padding(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onCancel,
                modifier = Modifier.weight(1f)
            ) { Text("Cancel") }

            Button(
                onClick = {
                    val cleanedIngredients = ingredientInputs.mapNotNull {
                        val trimmed = it.trim()
                        if (trimmed.isNotEmpty()) trimmed else null
                    }
                    val name = nameState.value.trim()
                    if (name.isNotEmpty() && cleanedIngredients.isNotEmpty()) {
                        onSave(
                            name,
                            cleanedIngredients,
                            imageState.value.trim(),
                            descriptionState.value.trim()
                        )
                    }
                },
                modifier = Modifier.weight(1f)
            ) { Text("Save") }
        }
    }
}
