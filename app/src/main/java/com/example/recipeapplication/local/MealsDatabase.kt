package com.example.recipeapplication.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MealsEntity::class], version = 1,exportSchema = true)
abstract class MealsDatabase: RoomDatabase() {
    abstract fun mealDao(): MealDao
companion object{
    @Volatile
    private var INSTANCE: MealsDatabase? = null
    fun getDatabase(context: Context): MealsDatabase{
        return INSTANCE ?: synchronized(this){
            val instance = Room.databaseBuilder(
                context.applicationContext,
                klass = MealsDatabase::class.java,
                name = "meals_database"

            ).build()
            INSTANCE = instance
            instance
        }
    }
}
}