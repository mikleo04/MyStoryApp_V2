package com.example.mystoryapp.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.*
import com.example.mystoryapp.api.ApiService
import com.example.mystoryapp.api.response.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class StoryRepo(private val storyDatabase: DatabaseStory, private val apiService: ApiService) {
    fun addStory(description: String, lat: Double = 0.0, lng: Double = 0.0, photo: File? = null): LiveData<Result<AddNewStoryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val imageMultipart: MultipartBody.Part =
                MultipartBody.Part.createFormData(
                    "photo",
                    photo?.name,
                    photo?.asRequestBody("image/jpeg".toMediaTypeOrNull())!!
                )
            
            emit(Result.Success(apiService.addNewStory(description.toRequestBody("text/plain".toMediaType()), lat, lng, imageMultipart)))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }
    fun getStory(requestCount: Int = 50): LiveData<Result<AllStoryResponse>> = liveData {
        emit(Result.Loading)
        try {
            emit(Result.Success(apiService.getAllStory(1, 1, requestCount)))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }
    fun getStoryMediator(): LiveData<PagingData<ListStoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5,
                initialLoadSize = 1
            ),
            remoteMediator = StoryMediator(storyDatabase, apiService),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }

}