package com.example.recipeapplication.Api

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("search.php")
    suspend fun search(
        @Query("s") query: String
    ): MealsResponseDto

    @GET("lookup.php")
    suspend fun getMealById(
        @Query("i") id: String
    ): MealsResponseDto
    companion object {
        const val BASE_URL = "https://www.themealdb.com/api/json/v1/1/"
    }
}