package com.example.mystoryapp

import com.example.mystoryapp.api.response.*
import java.util.stream.IntStream

object DataHelper {
    private const val photoUrl: String = "https://"
    private const val createdAt: String = "18-April-2022"
    private const val name: String = "name"
    private const val description: String = ""
    private const val lon: Double = 1.2
    private const val id: String = "id"
    private const val lat: Double = 2.3
    private const val error = false
    private const val message = "Success"

    fun generateStoryResponse(): AllStoryResponse {
        val listStoryItem = generateListStoryItem()
        return AllStoryResponse(
            listStoryItem,
            error,
            message
        )
    }

    fun generateListStoryItem(): ArrayList<ListStoryItem>{
        val list = arrayListOf<ListStoryItem>()

        for (i in IntStream.range(1, 100)){
            val listStoryItem = ListStoryItem(
                "$photoUrl $i",
                "$createdAt $i",
                "$name $i",
                "$description $i",
                lon * i,
                "${id}_${i}",
                lat *i
            )
            list.add(listStoryItem)
        }
        return list
    }

    fun generateLoginResponse(): LoginResponse {
        val loginResult = LoginResult(
            "Mark",
            "12345",
            "abc4def2"
        )
        return LoginResponse(loginResult, false, "success")
    }

    fun generateRegisterResponse(): RegisterResponse {
        return RegisterResponse(
            error,
            message
        )
    }

    fun generateAddNewStoryResponse(): AddNewStoryResponse {
        return AddNewStoryResponse(
            error, message
        )
    }
}