package com.example.recipeapplication.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MealDao
{
    @Query("SELECT * FROM meals")
    fun getAllMeals(): Flow<List<MealsEntity>>
    @Query("SELECT EXISTS(SELECT 1 FROM meals WHERE id = :mealId)")
    fun getMealById(mealId: String): Flow<Boolean>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(meal: MealsEntity)
    @Delete
    suspend fun deleteMeal(meal: MealsEntity)


}