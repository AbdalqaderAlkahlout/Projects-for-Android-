package com.example.recipeapplication.data

import com.example.recipeapplication.Api.MealDto
import com.example.recipeapplication.Api.RetrofitClient
import com.example.recipeapplication.local.MealDao
import com.example.recipeapplication.local.MealsEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher

class MealRepository(private val mealDao: MealDao) {
    private val apiService = RetrofitClient.apiService

    suspend fun searchMeals(query: String): NetworkResult<List<MealDto>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.search(query)
                val mealsList = response.meals ?: emptyList()
                NetworkResult.Success(mealsList)
            } catch (e: Exception) {
                NetworkResult.Error("حدث خطأ: ${e.localizedMessage}", e)
            }
        }
    }

    suspend fun getMealById(id: String): NetworkResult<MealDto> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getMealById(id)
                val meal = response.meals?.firstOrNull()
                if (meal != null) {
                    NetworkResult.Success(meal)
                } else {
                    NetworkResult.Error("لم يتم العثور على الوجبة", null)
                }
            } catch (e: Exception) {
                NetworkResult.Error("حدث خطأ: ${e.localizedMessage}", e)
            }
        }
    }

    fun getAllFavoriteMeals(): Flow<List<MealsEntity>> {
        return mealDao.getAllMeals()
    }

    fun isMealFavorite(mealId: String): Flow<Boolean> {
        return mealDao.getMealById(mealId)
    }

    suspend fun insertFavoriteMeal(meal: MealsEntity) {
        withContext(Dispatchers.IO) {
            mealDao.insertMeal(meal)
        }
    }

    suspend fun deleteFavoriteMeal(meal: MealsEntity) {
        withContext(Dispatchers.IO) {
            mealDao.deleteMeal(meal)
        }
    }
}
