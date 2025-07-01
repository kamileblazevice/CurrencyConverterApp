package com.example.currencyconverterapp.ui.shared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.currencyconverterapp.R

@Composable
fun ErrorView(onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(text = stringResource(R.string.error_screen_text))
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.margin_small)))
        Button(onClick = { onRetry() }) {
            Text(text = stringResource(R.string.error_screen_button_text))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ErrorViewPreview() {
    ErrorView {}
}
