package com.example.myapplication.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.myapplication.ui.screens.auth.AuthScreen
import com.example.myapplication.ui.screens.author.AuthorScreen
import com.example.myapplication.ui.screens.book.BookScreen
import com.example.myapplication.ui.screens.hub.HubScreen
import com.example.myapplication.ui.screens.profile.ProfileScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = NavRoutes.HUB) {
        composable(NavRoutes.HUB) { HubScreen(navController) }
        composable(NavRoutes.PROFILE) { ProfileScreen(navController) }
        composable(NavRoutes.AUTH) { AuthScreen(navController) }
        composable(
            route = NavRoutes.BOOK_DETAIL,
            arguments = listOf(navArgument("bookId") { type = NavType.StringType })
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getString("bookId") ?: return@composable
            BookScreen(bookId = bookId, navController = navController)
        }
        composable(
            route = NavRoutes.AUTHOR_DETAIL,
            arguments = listOf(navArgument("authorId") { type = NavType.StringType })
        ) { backStackEntry ->
            val authorId = backStackEntry.arguments?.getString("authorId") ?: return@composable
            AuthorScreen(authorId = authorId, navController = navController)
        }
    }
}
