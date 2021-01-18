package com.wattpad.android.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.Gson
import com.wattpad.android.constants.CoreConstants
import com.wattpad.android.data.dao.RemoteKeysDao
import com.wattpad.android.data.db.StoriesDatabase
import com.wattpad.android.data.model.RemoteKeys
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
 * In this class I'm testing insertion and fetching remote keys with fake data that using
 * from {stories_response.json} file
 */

@RunWith(AndroidJUnit4::class)
class RemoteKeysDaoTest {


    private lateinit var storiesDatabase: StoriesDatabase
    private lateinit var remoteKeysDao: RemoteKeysDao


    @Before
    @Throws(Exception::class)
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        storiesDatabase = Room.inMemoryDatabaseBuilder(
            context,StoriesDatabase::class.java
        ).build()
        remoteKeysDao = storiesDatabase.getRemoteKeysDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        storiesDatabase.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertRemoteKeys() = runBlocking {
        val string = JsonUtil.convertStreamToString(
            InstrumentationRegistry.getInstrumentation().context.resources.assets.open("stories_response.json")
        )
        val storyBase = Gson().fromJson(
            string,
            StoryBase::class.java
        )
        val storyList = storyBase.storyList
        val endOfPaginationReached = storyList.isEmpty()

        val prevKey = if (1 == CoreConstants.STARTING_OFFSET_INDEX) null else 1.minus(1)
        val nextKey = if (endOfPaginationReached) null else 1.plus(1)

        val keys = storyList.map {
            RemoteKeys(storyId = it.id, prevKey = prevKey, nextKey = nextKey)
        }
        storiesDatabase.getRemoteKeysDao().insertAll(keys)
        Assert.assertEquals(keys[0].storyId, "250604771")
    }
}