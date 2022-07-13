package com.example.mystoryapp.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mystoryapp.database.StoryRepo

class RegisterViewModel(private val storyRepository: StoryRepo) : ViewModel() {
    fun register(name: String, email: String, password: String) =  storyRepository.register(name, email, password)
}