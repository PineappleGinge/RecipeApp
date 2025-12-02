package com.example.recipeapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.recipeapp.data.local.Ingredient
import com.example.recipeapp.data.local.Recipe

@Composable
fun RecipeDetailScreen(
    recipe: Recipe?,
    ingredients: List<Ingredient>,
    onToggleIngredient: (Ingredient) -> Unit,
    onNavigateHome: () -> Unit,
    onNavigateBack: () -> Unit,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    // Display a recipe with photo, description, and ingredient checklist.
    if (recipe == null) {
        Text("No recipe selected")
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
            Text(
                recipe.name,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            )
            Row {
                IconButton(onClick = onNavigateHome) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Go to home"
                    )
                }
                IconButton(onClick = onEdit) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit recipe"
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete recipe"
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        recipe.imageUrl?.let { url ->
            AsyncImage(
                model = url,
                contentDescription = "${recipe.name} photo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        if (!recipe.description.isNullOrBlank()) {
            Text(
                text = recipe.description,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        Text("Ingredients", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        if (ingredients.isEmpty()) {
            Text(
                text = "No ingredients listed.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
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
                            onCheckedChange = { onToggleIngredient(ingredient) },
                            modifier = Modifier.semantics {
                                contentDescription = "Toggle ${ingredient.name}"
                            }
                        )
                    }
                }
            }
        }
    }
}
