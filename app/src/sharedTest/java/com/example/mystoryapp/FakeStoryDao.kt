package com.example.mystoryapp

import androidx.paging.PagingSource
import com.example.mystoryapp.api.response.ListStoryItem
import com.example.mystoryapp.database.StoryDao

class FakeStoryDao: StoryDao {
    private var storyData = DataTestHelper.generateListStoryItem()
    override suspend fun insertStory(story: List<ListStoryItem>) {
        for (s in story) storyData.add(s)
    }

    override fun getAllStory(): PagingSource<Int, ListStoryItem> {
        return (storyData as PagingSource<Int, ListStoryItem>)
    }

    override suspend fun deleteAll() {
        storyData.clear()
    }
}