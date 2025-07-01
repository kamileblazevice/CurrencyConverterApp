package com.example.currencyconverterapp.ui.features.currencytargetlist.model

sealed class CurrencyTargetListEvent {
    data class OnNavigateBack(val sourceId: String?, val targetId: String?) : CurrencyTargetListEvent()
    data class OnLoadData(val sourceId: String) : CurrencyTargetListEvent()
}
