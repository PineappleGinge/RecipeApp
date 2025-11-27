package com.example.recipeapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.recipeapp.MainViewModel
import com.example.recipeapp.data.local.Ingredient

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onOpenRecipe: () -> Unit
) {
    val recipe = viewModel.selectedRecipe.collectAsState().value
    val ingredients = viewModel.ingredients.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        // Banner for Current Recipe
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            onClick = { onOpenRecipe() }
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = recipe?.name ?: "No recipes saved yet",
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Ingredients", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(ingredients) { ingredient ->
                IngredientRow(
                    ingredient = ingredient
                )
            }
        }
    }
}

@Composable
fun IngredientRow(
    ingredient: Ingredient
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = ingredient.name,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge
        )

        Checkbox(
            checked = ingredient.hasItem,
            onCheckedChange = {
                // TODO: will update after ingredient toggle is added
            }
        )
    }
}
