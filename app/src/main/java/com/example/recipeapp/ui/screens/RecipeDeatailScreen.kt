package com.example.recipeapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.recipeapp.data.local.Ingredient
import com.example.recipeapp.data.local.Recipe

@Composable
fun RecipeDetailScreen(
    recipe: Recipe?,
    ingredients: List<Ingredient>
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = recipe?.name ?: "Recipe Details",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Ingredients:",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(ingredients) { ingredient ->
                IngredientDetailRow(ingredient)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun IngredientDetailRow(
    ingredient: Ingredient
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = ingredient.name,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge
            )

            Checkbox(
                checked = ingredient.hasItem,
                onCheckedChange = {

                }
            )
        }
    }
}
