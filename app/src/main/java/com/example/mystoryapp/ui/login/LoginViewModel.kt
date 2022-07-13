package com.example.mystoryapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mystoryapp.api.ApiConfig
import com.example.mystoryapp.api.response.LoginResponse
import com.example.mystoryapp.database.StoryRepo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val storyRepository: StoryRepo) : ViewModel(){
    fun login(email: String, password: String) = storyRepository.login(email, password)
}