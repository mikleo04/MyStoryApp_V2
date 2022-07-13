package com.example.mystoryapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.mystoryapp.database.StoryRepo
import com.example.mystoryapp.ui.register.RegisterViewModel
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RegisterViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepo
    private lateinit var registerModel: RegisterViewModel

    @Before
    fun setUp() {
        registerModel = RegisterViewModel(storyRepository)
    }

    private val password = "1234567"
    private val email = "user@mail.com"
    private val name = "User Name"
    private val error = false
    private val message = "success"


    @Test
    fun `when get registerResponse Should Not Null, Return Success and returned response data should equals to received data from server`() {
        val expectedResponse = MutableLiveData<Result<RegisterResponse>>()
        expectedResponse.value = Result.Success(RegisterResponse(error, message))
        Assert.assertNotNull(expectedResponse)

        Mockito.`when`(registerModel.register(name, email, password)).thenReturn(expectedResponse)

        val actualLoginResult = registerModel.register(name, email, password).getOrAwaitValue()
        Mockito.verify(storyRepository).register(name, email, password)
        Assert.assertNotNull(actualLoginResult)

        Assert.assertEquals(error, (actualLoginResult as Result.Success).data.error)
        Assert.assertEquals(message, actualLoginResult.data.message)
    }
}