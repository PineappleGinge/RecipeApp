package com.example.recipeapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.recipeapp.data.local.Recipe

@Composable
fun RecipeSearchScreen(
    searchRecipes: (String) -> List<Recipe>,
    onRecipeSelected: (Int) -> Unit
) {
    var query by remember { mutableStateOf("") }
    var results by remember { mutableStateOf<List<Recipe>>(emptyList()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // SEARCH BAR
        OutlinedTextField(
            value = query,
            onValueChange = {
                query = it
                results = searchRecipes(it)      // ← Calls VM search
            },
            label = { Text("Search Recipes") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // RESULTS LIST
        LazyColumn {
            items(results) { recipe ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clickable {
                            onRecipeSelected(recipe.id)   // ← Load recipe
                        }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = recipe.name,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Tap to view",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}
