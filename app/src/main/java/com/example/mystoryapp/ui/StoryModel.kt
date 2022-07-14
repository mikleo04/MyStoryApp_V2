package com.example.mystoryapp.ui

import androidx.lifecycle.*
import com.example.mystoryapp.database.StoryRepo

class StoryModel(private val storyRepository: StoryRepo) : ViewModel() {
    fun getStoryMediator() = storyRepository.getStoryMediator()
}



