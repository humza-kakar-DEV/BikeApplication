package com.example.smartbikefyp.util

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Exception(val message: String?) : UiState<Nothing>()
    data class Success<T>(val data: T?) : UiState<T>()
}