package com.example.mystoryapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.mystoryapp.ui.login.LoginViewModel
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepo
    private lateinit var login: LoginViewModel

    @Before
    fun setUp() {
        login = LoginViewModel(storyRepository)
    }
    private val password = "1234567"
    private val email = "user@mail.com"
    private val token = "qwerty"
    private val name = "User Name"
    private val userId = "Qwerty"
    private val error = false
    private val message = "success"

    @Test
    fun `when get login response Should Not Null, returned response data should equals to received data from server`() {
        val loginResult = LoginResult(name, userId, token)

        val expectedResponse = MutableLiveData<Result<LoginResponse>>()
        expectedResponse.value = Result.Success(LoginResponse(loginResult, error, message))
        Assert.assertNotNull(expectedResponse)

        Mockito.`when`(login.login(email, password)).thenReturn(expectedResponse)

        val actualLoginResult = login.login(email, password).getOrAwaitValue()
        Mockito.verify(storyRepository).login(email, password)
        Assert.assertNotNull(actualLoginResult)

        Assert.assertEquals(token, (actualLoginResult as Result.Success).data.loginResult?.token)
        Assert.assertEquals(name, actualLoginResult.data.loginResult?.name)
        Assert.assertEquals(userId, actualLoginResult.data.loginResult?.userId)
    }


}