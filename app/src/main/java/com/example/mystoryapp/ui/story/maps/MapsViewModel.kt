package com.example.mystoryapp.ui.story.maps

import androidx.lifecycle.ViewModel
import com.example.mystoryapp.database.StoryRepo

class MapsViewModel(private val storyRepository: StoryRepo) : ViewModel() {
    fun getStory(storyNum: Int = 100) = storyRepository.getStory(storyNum)
}