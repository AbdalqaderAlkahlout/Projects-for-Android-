package com.example.recipeapplication.Api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MealsResponseDto(
    @SerialName("meals")
    val meals: List<MealDto>?

)
@Serializable
data class MealDto(
    @SerialName("idMeal")
    val id : String,
    @SerialName("strMeal")
    val name : String,
    @SerialName("strCategory")
    val category:String?= null,
    @SerialName("strArea")
    val area: String?=null,
    @SerialName("strInstructions")
    val instructions:String?=null,
    @SerialName("strMealThumb")
    val imageUrl:String?=null,
)

