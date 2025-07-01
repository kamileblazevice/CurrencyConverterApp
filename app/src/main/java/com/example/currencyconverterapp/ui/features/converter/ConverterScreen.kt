package com.example.currencyconverterapp.ui.features.converter

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.currencyconverterapp.R
import com.example.currencyconverterapp.ui.features.converter.model.ConverterEvent
import com.example.currencyconverterapp.ui.features.converter.model.ConverterState
import com.example.currencyconverterapp.ui.helper.UiState
import com.example.currencyconverterapp.ui.shared.ErrorView
import com.example.currencyconverterapp.ui.shared.LoadingView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConverterScreen(
    selectedSourceCurrency: String?,
    selectedTargetCurrency: String?,
    viewModel: ConverterViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onEvent(ConverterEvent.OnLoadData(selectedSourceCurrency, selectedTargetCurrency))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.converter_screen_title))
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
                        viewModel.onEvent(
                            ConverterEvent.OnReloadData(
                                sourceId = selectedSourceCurrency,
                                targetId = selectedTargetCurrency,
                            )
                        )
                    }
                }

                is UiState.Success<*> -> {
                    ConverterView(
                        source = (state as UiState.Success<ConverterState>).data.source,
                        target = (state as UiState.Success<ConverterState>).data.target,
                        inputValue = (state as UiState.Success<ConverterState>).data.inputValue,
                        resultValue = (state as UiState.Success<ConverterState>).data.resultValue,
                        onSourceClicked = {
                            viewModel.onEvent(ConverterEvent.OnSourceClicked)
                        },
                        onTargetClicked = { id ->
                            viewModel.onEvent(ConverterEvent.OnTargetClicked(id))
                        },
                        onInputChanged = { input ->
                            viewModel.onEvent(ConverterEvent.OnInputChanged(input))
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ConverterView(
    source: String?,
    target: String?,
    inputValue: String?,
    resultValue: String?,
    onSourceClicked: () -> Unit,
    onTargetClicked: (String?) -> Unit,
    onInputChanged: (String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        item {
            ConverterSourceItem(
                source = source,
                target = target,
                placeholderText = stringResource(R.string.converter_screen_source_placeholder),
                inputValue = inputValue,
                onSourceClicked = { onSourceClicked() },
                onInputChanged = { input -> onInputChanged(input) })

            if (source != null) {
                HorizontalDivider()
                ConverterTargetItem(
                    target = target,
                    placeholderText = stringResource(R.string.converter_screen_target_placeholder),
                    resultValue = resultValue,
                ) {
                    onTargetClicked(source)
                }
            }
        }
    }
}

@Composable
fun ConverterSourceItem(
    source: String?,
    target: String?,
    placeholderText: String,
    inputValue: String?,
    onSourceClicked: () -> Unit,
    onInputChanged: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.margin_small)),
        shape = RoundedCornerShape(dimensionResource(R.dimen.margin_small))
    ) {
        Row(
            modifier = Modifier
                .padding(dimensionResource(R.dimen.margin_medium)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onSourceClicked() }
            ) {
                Text(
                    text = source ?: placeholderText,
                )
            }
            Column(
                modifier = Modifier.weight(1f)
            ) {
                if (target != null)
                    TextField(
                        value = inputValue ?: "",
                        onValueChange = { input -> onInputChanged(input) },
                        placeholder = { Text(text = stringResource(R.string.converter_screen_source_number_placeholder)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
            }
        }
    }
}

@Composable
fun ConverterTargetItem(
    target: String?,
    placeholderText: String,
    resultValue: String?,
    onTargetClicked: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.margin_small)),
        shape = RoundedCornerShape(dimensionResource(R.dimen.margin_small))
    ) {
        Row(
            modifier = Modifier
                .padding(dimensionResource(R.dimen.margin_medium)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onTargetClicked() }
            ) {
                Text(
                    text = target ?: placeholderText,
                )
            }
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = resultValue ?: "",
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ConverterItemPreview() {
    ConverterView(
        source = "EUR",
        target = "USD",
        inputValue = "50",
        resultValue = "100",
        onSourceClicked = { },
        onTargetClicked = { },
        onInputChanged = { },
    )
}
