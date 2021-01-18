package com.wattpad.android.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.Gson
import com.wattpad.android.data.dao.StoriesDao
import com.wattpad.android.data.db.StoriesDatabase
import com.wattpad.android.data.model.StoryBase
import com.wattpad.android.util.JsonUtil
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import kotlin.jvm.Throws

/**
 * In this class I'm testing insertion and fetching stories with fake data that using
 * from {stories_response.json} file
 */

@RunWith(AndroidJUnit4::class)
class StoryDaoTest {

    private lateinit var storiesDatabase: StoriesDatabase
    private lateinit var storiesDao: StoriesDao

    @Before
    @Throws(Exception::class)
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        storiesDatabase = Room.inMemoryDatabaseBuilder(
            context,StoriesDatabase::class.java
        ).build()
        storiesDao = storiesDatabase.getStoriesDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        storiesDatabase.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertStories() = runBlocking {
        val string = JsonUtil.convertStreamToString(
            InstrumentationRegistry.getInstrumentation().context.resources.assets.open("stories_response.json")
        )
        val storyBase = Gson().fromJson(
            string,
            StoryBase::class.java
        )
        storiesDao.insertStories(storyBase.storyList)
        Assert.assertEquals(storyBase.storyList[0].title, "Damirae: Highschool Days")

        storiesDao.insertStories(storyBase.storyList)
        val storyListFromDb = storiesDatabase.getStoriesDao().getStoriesList()
        Assert.assertEquals(storyListFromDb, storyBase.storyList)

        val singleStory = storiesDatabase.getStoriesDao().getStory("250604771")
        Assert.assertEquals(storyBase.storyList[0], singleStory)
    }
}