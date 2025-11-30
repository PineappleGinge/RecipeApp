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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import com.example.recipeapp.data.local.Recipe

@Composable
fun RecipeSearchScreen(
    onSearch: (String) -> Unit,
    onRecipeClick: (Int) -> Unit,
    results: List<Recipe>
) {

    var query by remember { mutableStateOf("") }
    var navigateOnResult by remember { mutableStateOf(false) }

    LaunchedEffect(results, navigateOnResult) {
        if (navigateOnResult && results.isNotEmpty()) {
            navigateOnResult = false
            onRecipeClick(results.first().id)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {


        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Search Recipes") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    if (query.isNotBlank()) {
                        navigateOnResult = true
                        onSearch(query)
                    }
                }
            )
        )


        Spacer(modifier = Modifier.height(16.dp))


        LazyColumn {
            items(results) { recipe ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clickable {
                            onRecipeClick(recipe.id)
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
