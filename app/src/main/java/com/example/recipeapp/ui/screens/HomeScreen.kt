package com.example.recipeapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.recipeapp.data.local.Ingredient
import com.example.recipeapp.data.local.Recipe

@Composable
fun HomeScreen(
    recipe: Recipe?,
    ingredients: List<Ingredient>,
    onOpenRecipe: () -> Unit,
    onToggleIngredient: (Ingredient) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Home",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clickable { onOpenRecipe() }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = recipe?.name ?: "No recipe selected",
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "Ingredients",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(ingredients) { ingredient ->
                IngredientRow(
                    ingredient = ingredient,
                    onToggleIngredient = onToggleIngredient
                )

            }
        }
    }
}

@Composable
fun IngredientRow(ingredient: Ingredient, onToggleIngredient: (Ingredient) -> Unit) {
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
                onToggleIngredient(ingredient)
            }
        )
    }
}
