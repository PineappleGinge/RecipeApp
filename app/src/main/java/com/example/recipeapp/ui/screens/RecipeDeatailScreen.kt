package com.example.recipeapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.recipeapp.data.local.Ingredient
import com.example.recipeapp.data.local.Recipe

@Composable
fun RecipeDetailScreen(
    recipe: Recipe?,
    ingredients: List<Ingredient>,
    onToggleIngredient: (Ingredient) -> Unit
) {
    if (recipe == null) {
        Text("No recipe selected")
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(recipe.name, style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(12.dp))

        Text("Ingredients", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(ingredients) { ingredient ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(ingredient.name)

                    Checkbox(
                        checked = ingredient.hasItem,
                        onCheckedChange = { onToggleIngredient(ingredient) }
                    )
                }
            }
        }
    }
}
