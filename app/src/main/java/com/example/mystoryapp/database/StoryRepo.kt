package com.example.mystoryapp.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.paging.*
import com.example.mystoryapp.api.ApiConfig
import com.example.mystoryapp.api.ApiService
import com.example.mystoryapp.api.response.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class StoryRepo(private val storyDatabase: DatabaseStory, private val apiService: ApiService) {
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

    fun getStory(storyNum: Int = 100): LiveData<Result<AllStoryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getAllStory(1, 1, storyNum)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun addStory(description: String, lat: Double = 0.0, lng: Double = 0.0, photo: File? = null): LiveData<Result<AddNewStoryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val desc = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile = photo?.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData("photo", photo?.name, requestImageFile!!)
            val response = apiService.addNewStory(desc, lat, lng, imageMultipart)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }
    
//    fun login(email: String, password: String): LiveData<Result<LoginResponse>> = liveData{
//        emit(Result.Loading)
//        try {
//            val response = apiService.login(email, password)
//            emit(Result.Success(response))
//        } catch (e: Exception) {
//            emit(Result.Error("Something went wrong"))
//        }
//    }
}