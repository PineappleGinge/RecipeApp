package com.example.recipeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.recipeapp.navigation.Screen
import com.example.recipeapp.ui.screens.*
import com.example.recipeapp.ui.theme.RecipeAppTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState




class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RecipeApp()
        }
    }
}

@OptIn(ExperimentalStdlibApi::class)
@Composable
fun RecipeApp(mainViewModel: MainViewModel = viewModel()){

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
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.Home,
                                contentDescription = screen.route
                            )
                        },
                        label = {
                            Text(
                                screen.route
                                    .replace("_", " ")
                                    .replaceFirstChar { it.uppercase() }
                            )
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

            // HOME SCREEN
            composable(Screen.Home.route) {
                val items by mainViewModel.shoppingItems.collectAsState()
                HomeScreen(
                    items = items,
                    onItemCheckedChange = { mainViewModel.toggleItem(it) },
                    onOpenRecipe = { navController.navigate(Screen.RecipeDetail.route) }
                )
            }

            // RECIPE SEARCH SCREEN
            composable(Screen.RecipeSearch.route) {
                RecipeSearchScreen()
            }

            // RECIPE DETAIL SCREEN
            composable(Screen.RecipeDetail.route) {
                val items by mainViewModel.shoppingItems.collectAsState()
                RecipeDetailScreen(
                    items = items,
                    onCheckedChange = { mainViewModel.toggleItem(it) }
                )
            }

            // SHOPPING LIST SCREEN
            composable(Screen.ShoppingList.route) {
                val items by mainViewModel.shoppingItems.collectAsState()
                ShoppingListScreen(
                    items = items,
                    onItemCheckedChange = { mainViewModel.toggleItem(it) }
                )
            }

            // SETTINGS SCREEN
            composable(Screen.Settings.route) {
                SettingsScreen()
            }
        }
    }
}