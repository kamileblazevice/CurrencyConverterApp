package com.example.currencyconverterapp.ui.features.converter

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewModelScope
import com.example.currencyconverterapp.data.helper.ApiResult
import com.example.currencyconverterapp.data.repository.CurrencyRepository
import com.example.currencyconverterapp.ui.features.converter.model.ConverterEvent
import com.example.currencyconverterapp.ui.features.converter.model.ConverterState
import com.example.currencyconverterapp.ui.helper.BaseViewModel
import com.example.currencyconverterapp.ui.helper.UiState
import com.example.currencyconverterapp.ui.navigation.Destination
import com.example.currencyconverterapp.ui.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConverterViewModel @Inject constructor(
    private val repository: CurrencyRepository,
    private val navigator: Navigator,
) : BaseViewModel<ConverterEvent>() {

    private val _state =
        MutableStateFlow<UiState<ConverterState>>(
            UiState.Success(
                ConverterState(
                    null,
                    null,
                    null,
                    null
                )
            )
        )
    val state: StateFlow<UiState<ConverterState>> = _state.asStateFlow()

    private fun loadCurrency(id: String) {
        viewModelScope.launch {
            when (val result = repository.getCurrency(id)) {
                is ApiResult.Success -> {
                    val currentState = _state.value
                    if (currentState is UiState.Success) {
                        val input = currentState.data.inputValue?.toDouble()
                        val targetRate = result.data.rates[currentState.data.target]
                        val resultValue =
                            if (input != null && targetRate != null) (input * targetRate).toString() else null
                        val newConverterState = currentState.data.copy(resultValue = resultValue)
                        _state.value = UiState.Success(newConverterState)
                    }
                }

                is ApiResult.Error -> {
                    _state.value = UiState.Error(result.message)
                }
            }
        }
    }

    private fun reloadCurrency(sourceId: String, targetId: String?) {
        viewModelScope.launch {
            _state.value = UiState.Loading
            when (val result = repository.getCurrency(sourceId)) {
                is ApiResult.Success -> {
                    _state.value = UiState.Success(
                        ConverterState(
                            source = sourceId,
                            target = targetId,
                            inputValue = null,
                            resultValue = null,
                        )
                    )
                }

                is ApiResult.Error -> {
                    _state.value = UiState.Error(result.message)
                }
            }
        }
    }

    override fun onEvent(event: ConverterEvent) {
        when (event) {


            is ConverterEvent.OnSourceClicked -> {
                viewModelScope.launch {
                    navigator.navigate(
                        destination = Destination.CurrencyListScreen,
                    )
                }
            }

            is ConverterEvent.OnTargetClicked -> {
                viewModelScope.launch {
                    navigator.navigate(
                        destination = Destination.CurrencyTargetListScreen(event.id ?: ""),
                        )
                }
            }

            is ConverterEvent.OnReloadData -> reloadCurrency(
                sourceId = event.sourceId ?: "",
                targetId = event.targetId,
            )

            is ConverterEvent.OnLoadData -> {
                _state.value = UiState.Success(
                    ConverterState(
                        source = event.sourceId,
                        target = event.targetId,
                        inputValue = null,
                        resultValue = null,
                    )
                )
            }

            is ConverterEvent.OnInputChanged -> {
                if (event.input != null && !event.input.isDigitsOnly()) return
                val currentState = _state.value
                if (currentState is UiState.Success) {
                    val newConverterState = currentState.data.copy(inputValue = event.input)
                    _state.value = UiState.Success(newConverterState)
                    loadCurrency(currentState.data.source.toString())
                }
            }
        }
    }
}
