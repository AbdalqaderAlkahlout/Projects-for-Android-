package com.example.notesapplication.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "notes_preferences")
enum class SortOrder {
    BY_DATE,
    BY_PRIORITY
}

class PreferencesRepositoryNotes(private val context: Context) {
    // key for saving the sort order and isDarkTheme
    private  object PreferencesKeys {
        val SORT_ORDER = stringPreferencesKey("sort_order")
        val IS_DARK_THEME = booleanPreferencesKey("is_dark_theme")

    }
    // read the sort order and isDarkTheme
    val isDarkTheme: Flow<Boolean> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }.map { preferences ->
            preferences[PreferencesKeys.IS_DARK_THEME] ?: false
        }
    val sortOrder: Flow<SortOrder> = context.dataStore.data
        .catch {exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }.map { preferences ->
            val orderName = preferences[PreferencesKeys.SORT_ORDER] ?: SortOrder.BY_DATE.name
            SortOrder.valueOf(orderName)

        }
    // save the sort order and isDarkTheme
    suspend fun setDarkTheme(isDark: Boolean){
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_DARK_THEME] = isDark
        }
    }
    suspend fun setSortOrder(order: SortOrder){
        context.dataStore.edit {preferences ->
            preferences[PreferencesKeys.SORT_ORDER] = order.name
        }
    }

}