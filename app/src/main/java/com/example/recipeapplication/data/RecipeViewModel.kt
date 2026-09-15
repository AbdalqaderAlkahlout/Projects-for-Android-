package com.example.recipeapplication.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.recipeapplication.Api.MealDto
import com.example.recipeapplication.local.MealDao
import com.example.recipeapplication.local.MealsDatabase
import com.example.recipeapplication.local.MealsEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

sealed interface ScreenStatus {
    object Loading : ScreenStatus
    object Idle : ScreenStatus
    data class Success(val meals: List<MealDto>) : ScreenStatus
    data class Error(val message: String) : ScreenStatus
}

class RecipeViewModel(
    application: Application,
) : AndroidViewModel(application) {

    private val database = MealsDatabase.getDatabase(application)
    private val mealDao = database.mealDao()
    private val repository = MealRepository(mealDao)

    private val _uiState = MutableStateFlow<ScreenStatus>(ScreenStatus.Idle)
    val uiState: StateFlow<ScreenStatus> = _uiState.asStateFlow()

    private val _mealDetailState = MutableStateFlow<NetworkResult<MealDto>>(NetworkResult.Loading)
    val mealDetailState: StateFlow<NetworkResult<MealDto>> = _mealDetailState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val favoriteMeals: StateFlow<List<MealDto>> = repository.getAllFavoriteMeals()
        .map { entities -> entities.map { it.toMealDto() } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    init {
        observeSearchQuery()
        _searchQuery.value = "chicken"
    }

    fun isFavorite(mealId: String) = repository.isMealFavorite(mealId)

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    @OptIn(FlowPreview::class)
    private fun observeSearchQuery() {
        viewModelScope.launch {
            _searchQuery.debounce(500.milliseconds)
                .distinctUntilChanged()
                .collect { query ->
                    if (query.isNotBlank()) {
                        searchMeal(query)
                    }
                }
        }
    }

    private fun searchMeal(query: String) {
        viewModelScope.launch {
            _uiState.value = ScreenStatus.Loading
            when (val result = repository.searchMeals(query)) {
                is NetworkResult.Success -> {
                    if (result.data.isEmpty()) {
                        _uiState.value = ScreenStatus.Error("لم يتم العثور على أي وجبات بهذا الاسم")
                    } else {
                        _uiState.value = ScreenStatus.Success(result.data)
                    }
                }
                is NetworkResult.Error -> {
                    _uiState.value = ScreenStatus.Error(result.message)
                }
                else -> {}
            }
        }
    }

    fun getMealById(id: String) {
        viewModelScope.launch {
            _mealDetailState.value = NetworkResult.Loading
            _mealDetailState.value = repository.getMealById(id)
        }
    }

    fun toggleFavorite(meal: MealDto, isCurrentlyFavorite: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            if (isCurrentlyFavorite) {
                repository.deleteFavoriteMeal(meal.toEntity())
            } else {
                repository.insertFavoriteMeal(meal.toEntity())
            }
        }
    }

    private fun MealDto.toEntity(): MealsEntity {
        return MealsEntity(
            id = this.id,
            name = this.name,
            imageUrl = this.imageUrl,
            category = this.category,
            area = this.area,
            instructions = this.instructions
        )
    }

    private fun MealsEntity.toMealDto(): MealDto {
        return MealDto(
            id = this.id,
            name = this.name,
            imageUrl = this.imageUrl,
            category = this.category,
            area = this.area,
            instructions = this.instructions
        )
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RecipeViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return RecipeViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
