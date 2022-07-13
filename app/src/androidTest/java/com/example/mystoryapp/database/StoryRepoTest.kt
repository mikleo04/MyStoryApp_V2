package com.example.mystoryapp.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import com.example.mystoryapp.api.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.junit.runner.RunWith
import org.mockito.Mock


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class StoryRepoTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()
    @Mock
    private lateinit var apiService: ApiService
    private lateinit var storyDao : StoryDao
    private lateinit var storyRepository: StoryRepo
    private lateinit var storyDatabase: DatabaseStory
    private val expectedResultStoryResponse = DataTestHelper.generateStoryResponse()

    @Before
    fun setUp() {
        apiService = FakeApiServices()
        storyDao = FakeStoryDao()
        storyDatabase = DatabaseStory.getDatabase(InstrumentationRegistry.getInstrumentation().targetContext)
        storyRepository = StoryRepo(storyDatabase, apiService)
    }
    @Suppress("UNCHECKED_CAST")
    @Test
    fun getStoryMediator() = mainCoroutineRule.runBlockingTest{
        Assert.assertNotNull(storyRepository)
        val actualResult = apiService.getAllStory(1, 1, 100)
        val storyMediator = storyRepository.getStoryMediator().getOrAwaitValue()
        Assert.assertNotNull(storyMediator)

        val actualResultItem = actualResult.listStory as ArrayList<ListStoryItem>
        for (i in actualResultItem.indices){
            val item = actualResultItem[i]
            val expected = expectedResultStoryResponse.listStory?.get(i)
            assertEquals(item.name, expected?.name)
            assertEquals(item.photoUrl, expected?.photoUrl)
            assertEquals(item.createdAt, expected?.createdAt)
            assertEquals(item.description, expected?.description)
            assertEquals(item.id, expected?.id)
            assertEquals(item.lon, expected?.lon)
            assertEquals(item.lat, expected?.lat)
        }
    }

    @Suppress("UNCHECKED_CAST")
    @Test
    fun getStory() = mainCoroutineRule.runBlockingTest{
        val actualResult = apiService.getAllStory(1, 1, 100)
        Assert.assertNotNull(actualResult)
        val actualResultItem = actualResult.listStory as ArrayList<ListStoryItem>
        for (i in actualResultItem.indices){
            val item = actualResultItem[i]
            val expected = expectedResultStoryResponse.listStory?.get(i)
            assertEquals(item.name, expected?.name)
            assertEquals(item.photoUrl, expected?.photoUrl)
            assertEquals(item.createdAt, expected?.createdAt)
            assertEquals(item.description, expected?.description)
            assertEquals(item.id, expected?.id)
            assertEquals(item.lon, expected?.lon)
            assertEquals(item.lat, expected?.lat)
        }

        val expectedResponse = DataTestHelper.generateStoryResponse()
        Assert.assertNotNull(expectedResponse)

        val actualStory = storyRepository.getStory(100).getOrAwaitValue()
        Assert.assertNotNull(actualStory)
        Assert.assertNotNull((actualResult.listStory as ArrayList<ListStoryItem>)[0].name)
    }

    @Test
    fun addStory() = mainCoroutineRule.runBlockingTest{
        val desc = "description".toRequestBody("text/plain".toMediaType())
        val addNewStoryResponse = apiService.addNewStory(desc, 0.1, 0.2, null)
        Assert.assertNotNull(addNewStoryResponse)

    }

    @Test
    fun register() = mainCoroutineRule.runBlockingTest{
        val expectedRegisterResult = DataTestHelper.generateRegisterResponse()
        val actualResult = apiService.register("fella", "fella@mail.com", "fella123")
        Assert.assertNotNull(actualResult)
        assertEquals(actualResult.error, expectedRegisterResult.error)
        assertEquals(actualResult.message, expectedRegisterResult.message)
    }

    @Test
    fun login() = mainCoroutineRule.runBlockingTest{
        val expectedLoginResponse = DataTestHelper.generateLoginResponse()
        val actualLoginResponse = apiService.login("hisName", "hisPassword")
        assertEquals(actualLoginResponse.error, expectedLoginResponse.error)
        assertEquals(actualLoginResponse.loginResult, expectedLoginResponse.loginResult)
        assertEquals(actualLoginResponse.message, expectedLoginResponse.message)
    }
}


@ExperimentalCoroutinesApi
class MainCoroutineRule(private val dispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()):
    TestWatcher(),
    TestCoroutineScope by TestCoroutineScope(dispatcher) {
    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(dispatcher)
    }

    override fun finished(description: Description?) {
        super.finished(description)
        cleanupTestCoroutines()
        Dispatchers.resetMain()
    }

}