package com.example.currencyconverterapp.ui.features.currencytargetlist

import androidx.lifecycle.viewModelScope
import com.example.currencyconverterapp.data.helper.ApiResult
import com.example.currencyconverterapp.data.repository.CurrencyRepository
import com.example.currencyconverterapp.ui.features.currencytargetlist.model.CurrencyTargetListEvent
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
class CurrencyTargetListViewModel @Inject constructor(
    private val repository: CurrencyRepository,
    private val navigator: Navigator,
) : BaseViewModel<CurrencyTargetListEvent>() {

    private val _state = MutableStateFlow<UiState<Map<String, Double>>>(UiState.Loading)
    val state: StateFlow<UiState<Map<String, Double>>> = _state.asStateFlow()

    private fun loadCurrencies(sourceId: String) {
        viewModelScope.launch {
            _state.value = UiState.Loading
            when (val result = repository.getCurrency(sourceId)) {
                is ApiResult.Success -> {
                    _state.value = UiState.Success(result.data.rates)
                }

                is ApiResult.Error -> {
                    _state.value = UiState.Error(result.message)
                }
            }
        }
    }

    override fun onEvent(event: CurrencyTargetListEvent) {
        when (event) {
            is CurrencyTargetListEvent.OnNavigateBack -> {
                viewModelScope.launch {
                    navigator.navigate(
                        destination = Destination.ConverterScreen(
                            sourceId = event.sourceId ?: event.targetId,
                            targetId = if (event.sourceId == null) null else event.targetId,
                        ),
                    )
                }
            }

            is CurrencyTargetListEvent.OnLoadData -> loadCurrencies(event.sourceId)
        }
    }
}
