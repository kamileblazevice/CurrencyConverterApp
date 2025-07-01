package com.example.currencyconverterapp.data.helper

import retrofit2.Response

suspend fun <T> safeApiCall(
    apiCall: suspend () -> Response<T>
): ApiResult<T> {
    return try {
        val response = apiCall()
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                ApiResult.Success(body)
            } else {
                ApiResult.Error("Response body is null")
            }
        } else {
            ApiResult.Error("Error ${response.code()}: ${response.message()}")
        }
    } catch (e: Exception) {
        ApiResult.Error("Network error: ${e.localizedMessage}", e)
    }
}
