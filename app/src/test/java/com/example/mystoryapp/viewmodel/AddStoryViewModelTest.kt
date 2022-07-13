package com.example.mystoryapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.mystoryapp.api.response.AddNewStoryResponse
import com.example.mystoryapp.database.StoryRepo
import com.example.mystoryapp.ui.story.addstory.AddStoryViewModel
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.junit.MockitoRule
import org.mockito.quality.Strictness
import java.io.File

@RunWith(MockitoJUnitRunner::class)
class AddStoryViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var rule: MockitoRule = MockitoJUnit.rule().strictness(Strictness.LENIENT)

    @Mock
    private lateinit var storyRepository: StoryRepo
    private lateinit var addStoryModel: AddStoryViewModel

    @Before
    fun setUp() {
        addStoryModel = AddStoryViewModel(storyRepository)
    }

    @Test
    fun `when Get add new story Should Not Null and Return Success`() {
        val photo = File("")
        val desc = "Desc"
        val lat = 1.0
        val lng = 1.1

        val expectedResponse = MutableLiveData<Result<AddNewStoryResponse>>()
        expectedResponse.value = Result.Success(AddNewStoryResponse(false, "success"))
        Assert.assertNotNull(expectedResponse)
        Mockito.`when`(addStoryModel.addNewStory(desc, lat, lng, photo)).thenReturn(expectedResponse)

        val actualAddNewStoryResponse = addStoryModel.addNewStory(desc, lat, lng, photo)
        Mockito.verify(storyRepository).addStory(desc, lat, lng, photo)

        Assert.assertNotNull(actualAddNewStoryResponse)
        Assert.assertTrue(actualAddNewStoryResponse.value is Result.Success)
        Assert.assertNotNull(addStoryModel.addNewStory(desc, lat, lng, photo))

        Mockito.`when`(addStoryModel.addNewStory(desc, lat, lng)).thenReturn(expectedResponse)
        Assert.assertNotNull(addStoryModel.addNewStory(desc, lat, lng))
        Mockito.verify(storyRepository).addStory(desc, lat, lng)

    }

}