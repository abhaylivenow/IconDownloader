package com.example.icondownloader.api

import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit
import okhttp3.logging.HttpLoggingInterceptor
import com.google.gson.GsonBuilder
import com.google.gson.Gson
import okhttp3.OkHttpClient


class ApiClient {
    private val BASE_URL = "https://api.pexels.com/v1/"
    private lateinit var retrofitClient: ApiClient
    private lateinit var retrofit: Retrofit
    private val builder: OkHttpClient.Builder = OkHttpClient.Builder()
    private val interceptor = HttpLoggingInterceptor()


    init {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(interceptor)
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(builder.build())
            .build()
    }

    @Synchronized
    fun getInstance(): ApiClient? {
        return ApiClient()
    }

    fun getApi(): ApiInterface {
        return retrofit.create(ApiInterface::class.java)
    }
}