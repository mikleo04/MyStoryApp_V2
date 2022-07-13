package com.example.mystoryapp.database

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.*
import java.util.stream.IntStream

class DatabaseStoryTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    private lateinit var storyDatabase: DatabaseStory
    private lateinit var storyDao: StoryDao
    lateinit var context: Context

    @Before
    fun initDb() {
        context = ApplicationProvider.getApplicationContext<Context>()
        storyDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            DatabaseStory::class.java
        ).build()
        storyDao = storyDatabase.storyDao()
    }
    @After
    fun closeDb() = storyDatabase.close()

    @Test
    fun storyDao() {
        val result = storyDatabase.storyDao()
        Assert.assertNotNull(result)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun remoteKeysDao() = runBlockingTest{
        val x = ArrayList<Keys>()
        for (i in IntStream.range(1, 100)){
            x. add(Keys(
                "A${i}", i-1, i+1
            ))
        }
        storyDatabase.remoteKeysDao().insertAll(x)

        Assert.assertNotNull(storyDatabase.remoteKeysDao().getRemoteKeysId("A1"))
    }

    @Test
    fun getDatabase() {
        val db = DatabaseStory.getDatabase(context)
        Assert.assertNotNull(db)
    }

}