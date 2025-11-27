package com.example.recipeapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun RecipeSearchScreen(){
    var text by remember { mutableStateOf("")}

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text("Title", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = text,
            onValueChange = {text = it},
            label = { Text("Label")},
            trailingIcon = {
                if (text.isNotEmpty()){
                    IconButton(onClick = {text = ""}) {
                        Icon(Icons.Default.Close, contentDescription = "Clear")
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)){
                repeat(4){
                    Text(
                        "Menu Item",
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}