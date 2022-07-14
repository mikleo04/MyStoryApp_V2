package com.example.mystoryapp.api

import com.example.mystoryapp.api.response.AddNewStoryResponse
import com.example.mystoryapp.api.response.AllStoryResponse
import com.example.mystoryapp.api.response.LoginResponse
import com.example.mystoryapp.api.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("/v1/stories")
    suspend fun getAllStory(
        @Query("location") location: Int = 1,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): AllStoryResponse
    
    @FormUrlEncoded
    @POST("v1/register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") job: String
    ): Call<RegisterResponse>
    
    @Multipart
    @POST("/v1/stories")
    suspend fun addNewStory(
        @Part("description") description: RequestBody,
        @Part("lat") lat: Double,
        @Part("lon") lon: Double,
        @Part photo: MultipartBody.Part,
    ): AddNewStoryResponse
    
//    @FormUrlEncoded
//    @POST("v1/login")
//    suspend fun login(
//        @Field("email") name: String,
//        @Field("password") job: String
//    ): LoginResponse

    @FormUrlEncoded
    @POST("v1/login")
    fun login(
        @Field("email") name: String,
        @Field("password") job: String
    ): Call<LoginResponse>
}