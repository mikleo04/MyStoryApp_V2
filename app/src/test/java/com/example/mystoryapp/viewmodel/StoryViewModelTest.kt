package com.example.mystoryapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import com.example.mystoryapp.ui.story.main.StoryViewModel
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

@RunWith(MockitoJUnitRunner::class)
class StoryViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var rule: MockitoRule = MockitoJUnit.rule().strictness(Strictness.LENIENT)

    @Mock
    private lateinit var storyRepository: StoryRepo
    private lateinit var storyModel: StoryViewModel

    private val dummyData = DataTestHelper.generateListStoryItem()

    @Before
    fun setUp() {
        storyModel = StoryViewModel(storyRepository)
    }

    @Test
    fun getStory() {
        val expectedResponse = MutableLiveData<PagingData<ListStoryItem>>()
        expectedResponse.value = PagingData.from(dummyData)
        Assert.assertNotNull(expectedResponse)

        Mockito.`when`(storyModel.getStoryMediator()).thenReturn(expectedResponse)

        val actualStory = storyModel.getStoryMediator()
        Mockito.verify(storyRepository).getStoryMediator()
        Assert.assertNotNull(actualStory)

        Assert.assertTrue((actualStory.value is PagingData<ListStoryItem>))
    }
}