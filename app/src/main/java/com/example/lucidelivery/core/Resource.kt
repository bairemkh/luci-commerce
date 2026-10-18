package com.example.lucidelivery.core

sealed interface Resource<out T> {
    data class Loading<T>(val cached: T? = null) : Resource<T>
    data class Success<T>(val data: T) : Resource<T>
    data class Error<T>(val message: String, val cached: T? = null) : Resource<T>
}

fun <T> Resource<T>.dataOrNull(): T? = when (this) {
    is Resource.Success -> data
    is Resource.Loading -> cached
    is Resource.Error -> cached
}