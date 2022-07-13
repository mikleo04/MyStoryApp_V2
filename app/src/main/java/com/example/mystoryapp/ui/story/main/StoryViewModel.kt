package com.example.mystoryapp.ui.story.main

import androidx.lifecycle.ViewModel
import com.example.mystoryapp.database.StoryRepo

class StoryViewModel(private val storyRepository: StoryRepo) : ViewModel() {
    fun getStoryMediator() = storyRepository.getStoryMediator()
}