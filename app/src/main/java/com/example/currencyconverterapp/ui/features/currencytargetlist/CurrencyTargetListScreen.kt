package com.example.currencyconverterapp.ui.features.currencytargetlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.currencyconverterapp.R
import com.example.currencyconverterapp.ui.features.currencylist.CurrencyItem
import com.example.currencyconverterapp.ui.features.currencytargetlist.model.CurrencyTargetListEvent
import com.example.currencyconverterapp.ui.helper.UiState
import com.example.currencyconverterapp.ui.shared.ErrorView
import com.example.currencyconverterapp.ui.shared.LoadingView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyTargetListScreen(
    sourceId: String,
    viewModel: CurrencyTargetListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onEvent(CurrencyTargetListEvent.OnLoadData(sourceId))
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.currency_list_screen_title))
                },
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            when (state) {
                UiState.Loading -> {
                    LoadingView()
                }

                is UiState.Error -> {
                    ErrorView {
                        viewModel.onEvent(CurrencyTargetListEvent.OnLoadData(sourceId))
                    }
                }

                is UiState.Success<*> -> {
                    CurrencyTargetList(currencies = (state as UiState.Success<Map<String, Double>>).data) { id ->
                        viewModel.onEvent(CurrencyTargetListEvent.OnNavigateBack(sourceId, id))
                    }
                }
            }
        }
    }
}

@Composable
fun CurrencyTargetList(currencies: Map<String, Double>, onCurrencyClicked: (String) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        items(currencies.toList()) { currency ->
            CurrencyTargetItem(currency = currency) {
                onCurrencyClicked(currency.first)
            }
            HorizontalDivider()
        }
    }
}

@Composable
fun CurrencyTargetItem(currency: Pair<String, Double>, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(dimensionResource(R.dimen.margin_medium))
    ) {
        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.margin_medium)))

        Column {
            Text(
                text = currency.first,
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = currency.second.toString(),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CurrencyItemPreview() {
    CurrencyItem(
        currency = mapOf(
            "id" to "id"
        ).toList().first()
    ) {}
}
