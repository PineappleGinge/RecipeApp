package com.example.recipeapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
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
    // Show the highlighted recipe and its ingredient checklist.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text(
            text = "Home",
            style = MaterialTheme.typography.titleLarge
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clickable { onOpenRecipe() },
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .semantics { contentDescription = "Featured recipe card" },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = recipe?.name ?: "No recipe selected",
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }

        Text(
            "Ingredients",
            style = MaterialTheme.typography.titleMedium
        )

        if (ingredients.isEmpty()) {
            Text(
                text = "No ingredients for this recipe yet.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.padding(top = 8.dp)
            ) {
                items(ingredients) { ingredient ->
                    IngredientRow(
                        ingredient = ingredient,
                        onToggleIngredient = onToggleIngredient
                    )
                }
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
            },
            modifier = Modifier.semantics {
                contentDescription = "Mark ${ingredient.name} as owned"
            }
        )
    }
}
