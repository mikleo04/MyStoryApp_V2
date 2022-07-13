package com.example.mystoryapp.database

import android.content.Context
import com.example.mystoryapp.api.ApiConfig

object Injection {
    fun provideRepository(context: Context, authenticationStr: String = ""): StoryRepo {
        val database = DatabaseStory.getDatabase(context)
        val apiService = ApiConfig.getApiService2(authenticationStr)
        return StoryRepo(database, apiService)
    }
}