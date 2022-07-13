package com.example.mystoryapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.mystoryapp.api.response.AllStoryResponse
import com.example.mystoryapp.database.StoryRepo
import com.example.mystoryapp.ui.story.maps.MapsViewModel
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class StoryMapViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepo
    private lateinit var storyMapModel: MapsViewModel
    private lateinit var allStoryResponse: AllStoryResponse

    @Before
    fun setUp() {
        storyMapModel = MapsViewModel(storyRepository)
        allStoryResponse = DataTestHelper.generateStoryResponse()
    }

    @Test
    fun `when Get story response Should Not Null, Return Success and returned response data should equals to received data from server`() {
        val expectedResponse = MutableLiveData<Result<AllStoryResponse>>()
        expectedResponse.value = Result.Success(allStoryResponse)
        Assert.assertNotNull(expectedResponse)

        Mockito.`when`(storyMapModel.getStory()).thenReturn(expectedResponse)

        val actualStory = storyMapModel.getStory().getOrAwaitValue()
        Mockito.verify(storyRepository).getStory()
        Assert.assertNotNull(actualStory)

        // check actualStory response should equals to data from server
        assertEquals(allStoryResponse.error, (actualStory as Result.Success).data.error)

        for (i in (allStoryResponse.listStory as ArrayList).indices){
            assertEquals(allStoryResponse.listStory?.get(i)?.name.toString(), actualStory.data.listStory?.get(i)?.name.toString())
            assertEquals(allStoryResponse.listStory?.get(i)?.photoUrl.toString(), actualStory.data.listStory?.get(i)?.photoUrl.toString())
            assertEquals(allStoryResponse.listStory?.get(i)?.createdAt.toString(), actualStory.data.listStory?.get(i)?.createdAt.toString())
            assertEquals(allStoryResponse.listStory?.get(i)?.description.toString(), actualStory.data.listStory?.get(i)?.description.toString())
            assertEquals(allStoryResponse.listStory?.get(i)?.id.toString(), actualStory.data.listStory?.get(i)?.id.toString())
            assertEquals(allStoryResponse.listStory?.get(i)?.lon.toString(), actualStory.data.listStory?.get(i)?.lon.toString())
            assertEquals(allStoryResponse.listStory?.get(i)?.lat.toString(), actualStory.data.listStory?.get(i)?.lat.toString())
        }
}