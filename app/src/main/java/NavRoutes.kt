package com.example.recipeapp.navigation

sealed class Screen(val route: String) {

    data object Home : Screen("home")
    data object RecipeSearch : Screen("recipe_search")
    data object RecipeDetail : Screen("recipe_detail")
    data object ShoppingList : Screen("shopping_list")
    data object Settings : Screen("settings")
}
