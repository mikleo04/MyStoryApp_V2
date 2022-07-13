package com.example.mystoryapp

import com.example.mystoryapp.api.ApiService
import com.example.mystoryapp.api.response.AddNewStoryResponse
import com.example.mystoryapp.api.response.AllStoryResponse
import com.example.mystoryapp.api.response.LoginResponse
import com.example.mystoryapp.api.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

class FakeApiServices: ApiService {
    override suspend fun register(name: String, email: String, password: String): RegisterResponse {
        return DataTestHelper.generateRegisterResponse()
    }

    override suspend fun login(name: String, password: String): LoginResponse {
        return DataTestHelper.generateLoginResponse()
    }

    override suspend fun addNewStory(
        description: RequestBody,
        lat: Double,
        lon: Double,
        photo: MultipartBody.Part?
    ): AddNewStoryResponse = DataTestHelper.generateAddNewStoryResponse()

    override suspend fun getAllStory(location: Int, page: Int, size: Int): AllStoryResponse {
        return DataTestHelper.generateStoryResponse()
    }
}