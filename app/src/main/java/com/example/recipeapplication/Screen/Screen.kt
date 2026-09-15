package com.example.recipeapplication.Screen

import com.example.recipeapplication.Api.MealDto
import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    object Search : Screen()

    @Serializable
    object Favorite : Screen()
    @Serializable
    data class DetailMeals(val mealId: String) : Screen()
}