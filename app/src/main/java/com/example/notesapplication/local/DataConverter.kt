package com.example.notesapplication.local

import androidx.room.TypeConverter
import com.example.notesapplication.ui.theme.util.Category
import com.example.notesapplication.ui.theme.util.Priority

class DataConverter {
    @TypeConverter
    fun fromPriority(priority: Priority): String = priority.name
    @TypeConverter
    fun toPriority(name: String): Priority = Priority.valueOf(name)
    @TypeConverter
    fun fromCategory(category: Category): String = category.name
    @TypeConverter
    fun toCategory(name: String): Category = Category.valueOf(name)


}