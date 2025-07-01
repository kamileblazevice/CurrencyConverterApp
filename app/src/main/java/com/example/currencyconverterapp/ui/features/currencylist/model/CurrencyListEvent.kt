package com.example.currencyconverterapp.ui.features.currencylist.model

sealed class CurrencyListEvent {
    data class OnNavigateBack(val sourceId: String?) : CurrencyListEvent()
    data object OnLoadData : CurrencyListEvent()
}
