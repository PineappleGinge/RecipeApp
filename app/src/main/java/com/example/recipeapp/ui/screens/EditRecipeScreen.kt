package com.example.recipeapp.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.recipeapp.data.local.Ingredient
import com.example.recipeapp.data.local.Recipe
import java.io.File

@Composable
fun EditRecipeScreen(
    recipe: Recipe?,
    existingIngredients: List<Ingredient>,
    onSave: (name: String, ingredients: List<String>, description: String, imageUrl: String?) -> Unit,
    onCancel: () -> Unit

) {
    // Allow users to update an existing recipe and its ingredient list.
    if (recipe == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("No recipe selected", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.padding(8.dp))
            Button(onClick = onCancel) { Text("Back") }
        }
        return
    }

    val ingredientInputs = remember {
        val names = existingIngredients.map { it.name }
        if (names.isEmpty()) mutableStateListOf("") else names.toMutableList().let { mutableStateListOf(*it.toTypedArray()) }
    }
    val nameState = remember { mutableStateOf(recipe.name) }
    val descriptionState = remember { mutableStateOf(recipe.description ?: "") }
    val context = LocalContext.current
    val photoUriState = remember { mutableStateOf(recipe.imageUrl?.let { Uri.parse(it) }) }

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (!success) {
            photoUriState.value = recipe.imageUrl?.let { Uri.parse(it) }
        }
    }

    val launchCamera: () -> Unit = {
        val uri = createImageUri(context)
        photoUriState.value = uri
        uri?.let { takePictureLauncher.launch(it) }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            launchCamera()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Edit Recipe", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.padding(8.dp))

        OutlinedTextField(
            value = nameState.value,
            onValueChange = { nameState.value = it },
            label = { Text("Recipe name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.padding(4.dp))

        OutlinedTextField(
            value = descriptionState.value,
            onValueChange = { descriptionState.value = it },
            label = { Text("Description (optional)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.padding(12.dp))

        Text("Ingredients", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.padding(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    val hasPermission = ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED
                    if (hasPermission) {
                        launchCamera()
                    } else {
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Take photo")
                Spacer(modifier = Modifier.padding(4.dp))
                Text("Update photo")
            }
        }

        if (photoUriState.value != null) {
            Spacer(modifier = Modifier.padding(4.dp))
            Text(
                text = "Photo selected",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f, fill = true)
                .padding(vertical = 8.dp)
        ) {
            itemsIndexed(ingredientInputs) { index, value ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = value,
                        onValueChange = { newText ->
                            ingredientInputs[index] = newText
                        },
                        label = { Text("Ingredient ${index + 1}") },
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = {
                            if (ingredientInputs.size > 1) {
                                ingredientInputs.removeAt(index)
                            } else {
                                ingredientInputs[0] = ""
                            }
                        }
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Remove ingredient")
                    }
                }
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { ingredientInputs.add("") }) {
                Icon(Icons.Default.Add, contentDescription = "Add ingredient")
                Spacer(modifier = Modifier.padding(4.dp))
                Text("Add ingredient")
            }
        }

        Spacer(modifier = Modifier.padding(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onCancel,
                modifier = Modifier.weight(1f)
            ) { Text("Cancel") }

            Button(
                onClick = {
                    val cleanedIngredients = ingredientInputs.mapNotNull {
                        val trimmed = it.trim()
                        if (trimmed.isNotEmpty()) trimmed else null
                    }
                    val name = nameState.value.trim()
                    if (name.isNotEmpty() && cleanedIngredients.isNotEmpty()) {
                        onSave(
                            name,
                            cleanedIngredients,
                            descriptionState.value.trim(),
                            photoUriState.value?.toString()
                        )
                    }
                },
                modifier = Modifier.weight(1f)
            ) { Text("Save changes") }
        }
    }
}

private fun createImageUri(context: Context): Uri? {
    return try {
        val imageFile = File.createTempFile(
            "recipe_photo_",
            ".jpg",
            context.cacheDir
        )
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            imageFile
        )
    } catch (e: Exception) {
        null
    }
}
