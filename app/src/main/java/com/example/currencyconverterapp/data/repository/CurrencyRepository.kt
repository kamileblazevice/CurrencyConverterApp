package com.example.currencyconverterapp.data.repository

import com.example.currencyconverterapp.data.helper.ApiResult
import com.example.currencyconverterapp.data.helper.safeApiCall
import com.example.currencyconverterapp.data.model.Rates
import com.example.currencyconverterapp.data.network.ApiService
import javax.inject.Inject

class CurrencyRepository @Inject constructor(
    private val api: ApiService
) {

    suspend fun getCurrencies(): ApiResult<Map<String, String>> {
        return safeApiCall {
            api.getCurrencies()
        }
    }

    suspend fun getCurrency(id: String): ApiResult<Rates> {
        return safeApiCall {
            api.getCurrency(id)
        }
    }
}
