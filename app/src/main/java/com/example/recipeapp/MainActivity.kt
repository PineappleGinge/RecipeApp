package com.example.recipeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.example.recipeapp.navigation.Screen
import com.example.recipeapp.ui.screens.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.recipeapp.data.local.Recipe
import com.example.recipeapp.ui.theme.RecipeAppTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RecipeApp()
        }
    }
}

@Composable
fun RecipeApp(mainViewModel: MainViewModel = viewModel()) {

    val darkMode by mainViewModel.darkMode.collectAsState()

    RecipeAppTheme(darkTheme = darkMode) {

        val navController = rememberNavController()

        val bottomItems = listOf(
            Screen.Home,
            Screen.ShoppingList,
            Screen.RecipeSearch,
            Screen.Settings
        )

        Scaffold(
            bottomBar = {
                NavigationBar {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route

                    bottomItems.forEach { screen ->
                        NavigationBarItem(
                            selected = currentRoute == screen.route,
                            onClick = {
                                if (screen.route == Screen.Home.route) {
                                    val popped = navController.popBackStack(Screen.Home.route, inclusive = false)
                                    if (!popped) {
                                        navController.navigate(Screen.Home.route) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                } else {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            icon = {
                                val icon = when (screen) {
                                    Screen.Home -> Icons.Default.Home
                                    Screen.ShoppingList -> Icons.Default.List
                                    Screen.RecipeSearch -> Icons.Default.Search
                                    Screen.Settings -> Icons.Default.Settings
                                    else -> Icons.Default.Home
                                }
                                Icon(imageVector = icon, contentDescription = screen.route)
                            },
                            label = {
                                Text(screen.route.replace("_", " ").replaceFirstChar { it.uppercase() })
                            }
                        )
                    }
                }
            }
        ) { innerPadding ->

            NavHost(
                navController = navController,
                startDestination = Screen.Home.route,
                modifier = Modifier.padding(innerPadding)
            ) {


                composable(Screen.Home.route) {
                    val recipe by mainViewModel.selectedRecipe.collectAsState()
                    val ingredients by mainViewModel.ingredients.collectAsState()

                    HomeScreen(
                        recipe = recipe,
                        ingredients = ingredients,
                        onOpenRecipe = {
                            navController.navigate(Screen.RecipeDetail.route)
                        },
                        onToggleIngredient = { mainViewModel.toggleIngredient(it) }
                    )
                }


                composable(Screen.RecipeSearch.route) {

                    val results by mainViewModel.searchResults.collectAsState()

                    RecipeSearchScreen(
                        onSearch = { mainViewModel.searchRecipes(it) },
                        onRecipeClick = { id ->
                            mainViewModel.loadRecipe(id)
                            navController.navigate(Screen.RecipeDetail.route)
                        },
                        results = results
                    )
                }




                composable(Screen.RecipeDetail.route) {
                    val recipe by mainViewModel.selectedRecipe.collectAsState()
                    val ingredients by mainViewModel.ingredients.collectAsState()

                    RecipeDetailScreen(
                        recipe = recipe,
                        ingredients = ingredients,
                        onToggleIngredient = { mainViewModel.toggleIngredient(it) },
                        onNavigateHome = {
                            val popped = navController.popBackStack(Screen.Home.route, inclusive = false)
                            if (!popped) {
                                navController.navigate(Screen.Home.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        onNavigateBack = { navController.popBackStack() }
                    )
                }


                composable(Screen.ShoppingList.route) {

                    val shoppingList by mainViewModel.shoppingList.collectAsState()

                    ShoppingListScreen(
                        items = shoppingList,
                        onItemCheckedChange = { mainViewModel.toggleShoppingItem(it) },
                        onAddItem = { mainViewModel.addShoppingItem(it) },
                        onDeleteItem = { mainViewModel.deleteShoppingItem(it) },
                        onClearAll = { mainViewModel.clearShoppingList() }
                    )
                }



                composable(Screen.Settings.route) {
                    val notifications by mainViewModel.notificationsEnabled.collectAsState()
                    val servings by mainViewModel.defaultServings.collectAsState()

                    SettingsScreen(
                        darkMode = darkMode,
                        notificationsEnabled = notifications,
                        defaultServings = servings,
                        onDarkModeChange = { mainViewModel.toggleDarkMode(it) },
                        onNotificationsChange = { mainViewModel.toggleNotifications(it) },
                        onDefaultServingsChange = { mainViewModel.setDefaultServings(it) }
                    )
                }
            }
        }
    }
}
