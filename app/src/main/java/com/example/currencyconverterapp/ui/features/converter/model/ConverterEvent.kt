package com.example.currencyconverterapp.ui.features.converter.model

sealed class ConverterEvent {
    data object OnSourceClicked : ConverterEvent()
    data class OnTargetClicked(val id: String?) : ConverterEvent()
    data class OnReloadData(val sourceId: String?, val targetId: String?) : ConverterEvent()
    data class OnLoadData(val sourceId: String?, val targetId: String?) : ConverterEvent()
    data class OnInputChanged(val input: String?) : ConverterEvent()
}
