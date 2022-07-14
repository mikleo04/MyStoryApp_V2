package com.example.mystoryapp.ui.story.addstory

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.mystoryapp.api.response.AddNewStoryResponse
import com.example.mystoryapp.database.StoryRepo
import java.io.File
import com.example.mystoryapp.database.Result

class AddStoryViewModel(private val storyRepository: StoryRepo) : ViewModel() {
    fun addNewStory(description: String, lat: Double, lng: Double, photo: File? = null):
        LiveData<Result<AddNewStoryResponse>> = storyRepository.addStory(description, lat, lng, photo)
}