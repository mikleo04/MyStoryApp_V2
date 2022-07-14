package com.example.mystoryapp.database

import android.content.Context
import com.example.mystoryapp.api.ApiConfig

object Injection {
    fun provideRepository(context: Context, authenticationStr: String = ""): StoryRepo =
        StoryRepo(DatabaseStory.getDatabase(context), apiService = ApiConfig.apiService(authenticationStr))
}