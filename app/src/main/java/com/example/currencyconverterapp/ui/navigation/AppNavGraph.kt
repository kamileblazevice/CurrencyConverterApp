package com.example.currencyconverterapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.example.currencyconverterapp.ui.features.converter.ConverterScreen
import com.example.currencyconverterapp.ui.features.currencylist.CurrencyListScreen
import com.example.currencyconverterapp.ui.features.currencytargetlist.CurrencyTargetListScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    startDestination: Destination,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {

        navigation<Destination.AppGraph>(
            startDestination = Destination.ConverterScreen(null, null),
        ) {
            composable<Destination.ConverterScreen> { backStackEntry ->
                val args = backStackEntry.toRoute<Destination.ConverterScreen>()

                ConverterScreen(
                    selectedSourceCurrency = args.sourceId,
                    selectedTargetCurrency = args.targetId,
                )
            }

            composable<Destination.CurrencyListScreen> {
                CurrencyListScreen()
            }

            composable<Destination.CurrencyTargetListScreen> { backStackEntry ->
                val args = backStackEntry.toRoute<Destination.CurrencyTargetListScreen>()
                CurrencyTargetListScreen(args.id)
            }
        }
    }
}
