package com.wattpad.android.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wattpad.android.data.model.Story


/**
 * This {dao} interface is to provide access to Stories table
 */

@Dao
interface StoriesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStories(stories: MutableList<Story>)

    @Query("SELECT * FROM stories")
    fun getStories(): PagingSource<Int, Story>

    @Query("SELECT * FROM stories")
    fun getStoriesList(): MutableList<Story>

    @Query("SELECT * FROM stories Where id=:storyId")
    fun getStory(storyId:String): Story

    @Query("DELETE FROM stories")
    suspend fun deleteStories()

}