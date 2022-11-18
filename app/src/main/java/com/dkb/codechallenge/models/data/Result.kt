package com.dkb.codechallenge.models.data

sealed class Result<T> {
    data class SUCCESS<T>(val result: T): Result<T>()
    data class ERROR(val errorMessage: String, val exception: Exception?): Result<Nothing>()
}