package com.example.recipeapplication.data

sealed interface NetworkResult<out T> {
    data class Success <out  T>(val data: T): NetworkResult<T>
    data class  Error(val message: String , val cause: Throwable?): NetworkResult<Nothing>
    object Loading: NetworkResult<Nothing>
}