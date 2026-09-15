package com.example.recipeapplication.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meals")
data class MealsEntity(
    @PrimaryKey
    val id : String,
    val name : String,
    val imageUrl:String? = null,
    val category:String?= null,
    val area: String?=null,
    val instructions:String?=null,
)
