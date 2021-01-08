package com.example.triviaapp.utils

import okhttp3.OkHttpClient

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    var baseUrl: String = "https://opentdb.com/";
    private val client = OkHttpClient.Builder().connectTimeout(40, TimeUnit.SECONDS)
        .writeTimeout(40, TimeUnit.SECONDS)
        .readTimeout(50, TimeUnit.SECONDS).build()

    private val retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

    fun<T> createService(bindService : Class<T>):T{
        return retrofit.create(bindService)
    }
}
