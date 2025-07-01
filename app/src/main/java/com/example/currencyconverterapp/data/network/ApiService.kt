package com.example.currencyconverterapp.data.network

import com.example.currencyconverterapp.data.model.Rates
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("currencies")
    suspend fun getCurrencies(): Response<Map<String, String>>

    @GET("latest")
    suspend fun getCurrency(@Query("base") id: String): Response<Rates>
}
