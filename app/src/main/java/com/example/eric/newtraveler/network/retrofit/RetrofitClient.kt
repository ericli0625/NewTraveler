package com.example.eric.newtraveler.network.retrofit

import com.example.eric.newtraveler.network.Config.INTERCEPTOR_LEVEL
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object RetrofitClient {

    fun getClient(): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(
                        createHttpLoggingInterceptor())
                .build()
    }

    private fun createHttpLoggingInterceptor(): Interceptor {
        return HttpLoggingInterceptor().apply {
            level = INTERCEPTOR_LEVEL
        }
    }
}