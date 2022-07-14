package com.example.mystoryapp.api

import androidx.viewbinding.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
    fun apiService(authenticationStr: String): ApiService{
        val client = OkHttpClient.Builder().addInterceptor(
            if(BuildConfig.DEBUG){
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            }else{
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            }
        ).addInterceptor { chain ->
            chain.proceed(request = chain.request().newBuilder().addHeader("Authorization", "Bearer $authenticationStr").build() )
        }.build()
        val retrofit = Retrofit.Builder()
            .baseUrl( "https://story-api.dicoding.dev/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ApiService::class.java)
    }
}