package com.wattpad.android.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.wattpad.android.data.dao.RemoteKeysDao
import com.wattpad.android.data.dao.StoriesDao
import com.wattpad.android.data.model.RemoteKeys
import com.wattpad.android.data.model.Story

@Database(
    entities = [Story::class, RemoteKeys::class],
    version = 1,
    exportSchema = false
)
abstract class StoriesDatabase : RoomDatabase() {
    abstract fun getStoriesDao(): StoriesDao
    abstract fun getRemoteKeysDao(): RemoteKeysDao
}