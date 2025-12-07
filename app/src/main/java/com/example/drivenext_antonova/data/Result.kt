package com.example.drivenext_antonova.data

/**
 * Represents the result of an operation that can either succeed or fail.
 * @param T The type of data returned on success
 */
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String, val throwable: Throwable? = null) : Result<Nothing>()
    object Loading : Result<Nothing>()
    
    /**
     * Returns true if this is a Success result
     */
    val isSuccess: Boolean get() = this is Success
    
    /**
     * Returns true if this is an Error result
     */
    val isError: Boolean get() = this is Error
    
    /**
     * Returns true if this is a Loading result
     */
    val isLoading: Boolean get() = this is Loading
    
    /**
     * Returns the data if this is a Success, null otherwise
     */
    fun getOrNull(): T? = when (this) {
        is Success -> data
        else -> null
    }
    
    /**
     * Returns the data if this is a Success, or the default value otherwise
     */
    fun getOrDefault(defaultValue: @UnsafeVariance T): T = when (this) {
        is Success -> data
        else -> defaultValue
    }
    
    /**
     * Executes the given function if this is a Success result
     */
    inline fun onSuccess(action: (T) -> Unit): Result<T> {
        if (this is Success) action(data)
        return this
    }
    
    /**
     * Executes the given function if this is an Error result
     */
    inline fun onError(action: (String, Throwable?) -> Unit): Result<T> {
        if (this is Error) action(message, throwable)
        return this
    }
}