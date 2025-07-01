package com.example.currencyconverterapp.di

import com.example.currencyconverterapp.BuildConfig
import com.example.currencyconverterapp.data.network.ApiService
import com.example.currencyconverterapp.data.repository.CurrencyRepository
import com.example.currencyconverterapp.ui.navigation.DefaultNavigator
import com.example.currencyconverterapp.ui.navigation.Destination
import com.example.currencyconverterapp.ui.navigation.Navigator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideBaseUrl(): String = BuildConfig.BASE_URL

    @Provides
    @Singleton
    fun provideRetrofit(baseUrl: String): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideRepository(api: ApiService): CurrencyRepository =
        CurrencyRepository(api)

    @Provides
    @Singleton
    fun provideNavigator(): Navigator =
        DefaultNavigator(startDestination = Destination.AppGraph)
}
