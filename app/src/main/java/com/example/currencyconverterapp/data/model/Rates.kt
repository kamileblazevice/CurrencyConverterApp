package com.example.currencyconverterapp.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Rates(
    val base: String,
    val date: String,
    val rates: Map<String, Double>
)
