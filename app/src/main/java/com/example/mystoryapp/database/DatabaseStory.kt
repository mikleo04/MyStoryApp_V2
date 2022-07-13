package com.example.mystoryapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mystoryapp.api.response.ListStoryItem

@Database(
    entities = [ListStoryItem::class, Keys::class],
    version = 2,
    exportSchema = false
)
abstract class DatabaseStory : RoomDatabase(){
    abstract fun storyDao(): StoryDao
    abstract fun remoteKeysDao(): KeysDao

    companion object {
        @Volatile
        private var INSTANCE: DatabaseStory? = null

        @JvmStatic
        fun getDatabase(context: Context): DatabaseStory {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseStory::class.java, "database_story"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}