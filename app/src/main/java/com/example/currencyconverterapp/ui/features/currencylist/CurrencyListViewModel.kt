package com.example.currencyconverterapp.ui.features.currencylist

import androidx.lifecycle.viewModelScope
import com.example.currencyconverterapp.data.helper.ApiResult
import com.example.currencyconverterapp.data.repository.CurrencyRepository
import com.example.currencyconverterapp.ui.features.currencylist.model.CurrencyListEvent
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
class CurrencyListViewModel @Inject constructor(
    private val repository: CurrencyRepository,
    private val navigator: Navigator,
) : BaseViewModel<CurrencyListEvent>() {

    private val _state = MutableStateFlow<UiState<Map<String, String>>>(UiState.Loading)
    val state: StateFlow<UiState<Map<String, String>>> = _state.asStateFlow()

    init {
        loadCurrencies()
    }

    private fun loadCurrencies() {
        viewModelScope.launch {
            _state.value = UiState.Loading
            when (val result = repository.getCurrencies()) {
                is ApiResult.Success -> {
                    _state.value = UiState.Success(result.data)
                }

                is ApiResult.Error -> {
                    _state.value = UiState.Error(result.message)
                }
            }
        }
    }

    override fun onEvent(event: CurrencyListEvent) {
        when (event) {
            is CurrencyListEvent.OnNavigateBack -> {
                viewModelScope.launch {
                    navigator.navigate(
                        destination = Destination.ConverterScreen(
                            sourceId = event.sourceId,
                            targetId = null,
                        ),
                    )
                }
            }

            is CurrencyListEvent.OnLoadData -> loadCurrencies()
        }
    }
}
