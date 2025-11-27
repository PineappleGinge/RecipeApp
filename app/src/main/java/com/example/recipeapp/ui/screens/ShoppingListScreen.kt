package com.example.recipeapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.recipeapp.data.local.Recipe

@Composable
fun RecipeSearchScreen(
    onRecipeSelected: (Int) -> Unit,
    searchRecipes: (String) -> List<Recipe>
) {
    var query by remember { mutableStateOf("") }
    var results by remember { mutableStateOf(emptyList<Recipe>()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        OutlinedTextField(
            value = query,
            onValueChange = {
                query = it
                results = searchRecipes(query)   // LIVE SEARCH
            },
            label = { Text("Search Recipes") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        LazyColumn {
            items(results) { recipe ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable { onRecipeSelected(recipe.id) }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(recipe.name, style = MaterialTheme.typography.titleMedium)
                        Text("Tap to view", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}
