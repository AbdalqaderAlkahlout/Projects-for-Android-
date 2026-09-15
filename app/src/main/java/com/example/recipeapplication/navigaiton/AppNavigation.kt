package com.example.recipeapplication.navigaiton

import android.app.Application
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.recipeapplication.Screen.DetailScreens
import com.example.recipeapplication.Screen.FavoriteScreen
import com.example.recipeapplication.Screen.Screen
import com.example.recipeapplication.Screen.SearchScreen
import com.example.recipeapplication.data.RecipeViewModel

sealed class BottomNavigationItem(val route: Screen, val icon: ImageVector, val label: String) {
    object Search : BottomNavigationItem(route = Screen.Search, Icons.Default.Search, "Search")
    object Favorite :
        BottomNavigationItem(route = Screen.Favorite, Icons.Default.Favorite, "Favorite")

}

@Composable
fun AppNavigation() {
    val context = LocalContext.current
    val recipeViewModel: RecipeViewModel = viewModel(
        factory = RecipeViewModel.Factory(context.applicationContext as Application)
    )
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val bottomNavItem = listOf(
        BottomNavigationItem.Search,
        BottomNavigationItem.Favorite
    )
    val shouldShowBottomBar = bottomNavItem.any { item ->
        currentDestination?.hierarchy?.any { it.hasRoute(item.route::class) } == true

    }
    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar) {
                NavigationBar {
                    bottomNavItem.forEach { item ->
                        val selected = currentDestination?.hierarchy?.any {
                            it.hasRoute(item.route::class)
                        } == true
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(imageVector = item.icon, contentDescription = item.label)
                            },
                            label = { Text(text = item.label) }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Search,
            modifier = Modifier.padding(paddingValues),
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(400)
                )+ fadeIn(animationSpec = tween(400))
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(400)
                )+ fadeOut(animationSpec = tween(400))
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(400)
                )+ fadeIn(animationSpec = tween(400))

            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(400)
                )+ fadeOut(animationSpec = tween(400))
            }

        ) {
            composable<Screen.Search> {
                SearchScreen(onMealClick = { meal ->
                    navController.navigate(Screen.DetailMeals(meal.id))
                }, viewModel = recipeViewModel)
            }
            composable<Screen.Favorite> {
                FavoriteScreen(
                    viewModel = recipeViewModel,
                    onClick = { mealId ->
                        navController.navigate(Screen.DetailMeals(mealId))
                    })
            }
            composable<Screen.DetailMeals> { backStackEntry ->
                val args = backStackEntry.toRoute<Screen.DetailMeals>()
                DetailScreens(
                    mealId = args.mealId,
                    viewModel = recipeViewModel,
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
        }

    }
}
