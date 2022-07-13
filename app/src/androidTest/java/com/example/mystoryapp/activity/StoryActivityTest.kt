package com.example.mystoryapp.activity

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.mystoryapp.EspressoIdlingResource
import com.example.mystoryapp.JsonConverter
import com.example.mystoryapp.api.ApiConfig
import com.example.mystoryapp.ui.story.main.StoryActivity
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.stream.IntStream

@RunWith(AndroidJUnit4::class)
@MediumTest
class StoryActivityTest {
    private val mockWebServer = MockWebServer()

    @Before
    fun setUp() {
        mockWebServer.start(8080)
        ApiConfig.BASE_URL = "http://127.0.0.1:8080/"
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun getData_success() {
        ActivityScenario.launch(StoryActivity::class.java)
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(JsonConverter.readStringFromFile("AllStoryResponse_OnSuccess.json"))
        mockWebServer.enqueue(mockResponse)

        Espresso.onView(withId(R.id.stories_rv_story_list))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        for (i in IntStream.range(1, 3)) {
            Espresso.onView(ViewMatchers.withText("User $i"))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            Espresso.onView(ViewMatchers.withText("Lorem Ipsum $i"))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        }
    }
}