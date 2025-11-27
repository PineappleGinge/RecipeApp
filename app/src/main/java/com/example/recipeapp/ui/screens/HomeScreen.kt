package com.example.recipeapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.recipeapp.ListItem


@Composable
fun HomeScreen(
    items: List<ListItem>,
    onItemCheckedChange: (Int) -> Unit,
    onOpenRecipe: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        OutlinedTextField(
            value = "",
            onValueChange = { },
            label = { Text("Search") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .clickable { onOpenRecipe() }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text("Banner title")
            }
        }

        Spacer(Modifier.height(24.dp))
        Text("Title", style = MaterialTheme.typography.titleMedium)

        Spacer(Modifier.height(8.dp))

        LazyColumn {
            items(items) { item ->
                ListItemRow(item = item, onItemCheckedChange = onItemCheckedChange)
            }
        }
    }
}

@Composable
fun ListItemRow(
    item: ListItem,
    onItemCheckedChange: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(32.dp)
                .background(
                    color = MaterialTheme.colorScheme.secondary,
                    shape = MaterialTheme.shapes.small
                ),
            contentAlignment = Alignment.Center
        ) {
            Text("A")
        }

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = item.title,
            modifier = Modifier.weight(1f)
        )

        Checkbox(
            checked = item.checked,
            onCheckedChange = { onItemCheckedChange(item.id) }
        )
    }
}
