package com.example.mystoryapp.api

import androidx.viewbinding.BuildConfig
import com.example.mystoryapp.constant.Constant.API_BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
    var BASE_URL = API_BASE_URL
    fun getApiService2(authenticationStr: String): ApiService{
        val authStr = "Bearer $authenticationStr"

        val loggingInterceptor = if(BuildConfig.DEBUG){
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        }else{
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        }

        val client = OkHttpClient.Builder().addInterceptor(loggingInterceptor).addInterceptor { chain ->
            chain.proceed(request = chain.request().newBuilder().addHeader("Authorization", authStr).build() )
        }.build()
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ApiService::class.java)
    }
}