package com.example.recipeapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.recipeapp.ListItem

@Composable
fun RecipeDetailScreen(
    items: List<ListItem>,
    onCheckedChange: (Int) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text("Title", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier
                .size(160.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text("Image")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Title", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(items) { item ->
                DetailListItemRow(item = item, onCheckedChange = onCheckedChange)
            }
        }
    }
}

@Composable
fun DetailListItemRow(item: ListItem, onCheckedChange: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(item.title, modifier = Modifier.weight(1f))

        Checkbox(
            checked = item.checked,
            onCheckedChange = { onCheckedChange(item.id) }
        )
    }
}