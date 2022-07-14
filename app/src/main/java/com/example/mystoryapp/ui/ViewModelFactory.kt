package com.example.mystoryapp.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mystoryapp.database.Injection

class ViewModelFactory(private val context: Context, private val authenticationStr: String) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoryModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StoryModel(Injection.provideRepository(context, authenticationStr)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}