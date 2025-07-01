package com.example.currencyconverterapp.ui.navigation

import kotlinx.serialization.Serializable

sealed interface Destination {

    @Serializable
    data object AppGraph : Destination

    @Serializable
    data class ConverterScreen(val sourceId: String?, val targetId: String?) : Destination

    @Serializable
    data object CurrencyListScreen : Destination

    @Serializable
    data class CurrencyTargetListScreen(val id: String) : Destination

}
