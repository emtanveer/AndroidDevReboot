package com.codingtroops.repositories

import androidx.test.espresso.core.internal.deps.dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

object DependencyContainer {

    // Provide OkHttpClient with logging interceptor
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // Log detailed HTTP request/response
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor) // Add the logging interceptor to OkHttp client
            .build()
    }

    // Retrofit instance with OkHttp client
    private val okHttpClient = provideOkHttpClient()

    val repositoriesRetrofitClient: RepositoriesApiService = Retrofit.Builder()
        .baseUrl("https://api.github.com/search/") // GitHub API base URL
        .client(okHttpClient) // Add OkHttpClient with logging
        .addConverterFactory(GsonConverterFactory.create()) // Convert JSON responses to POJOs
        .build()
        .create(RepositoriesApiService::class.java)
}