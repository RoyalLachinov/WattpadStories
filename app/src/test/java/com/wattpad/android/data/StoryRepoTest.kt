package com.wattpad.android.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.gson.Gson
import com.wattpad.android.data.db.StoriesDatabase
import com.wattpad.android.data.model.Story
import com.wattpad.android.data.model.StoryBase
import com.wattpad.android.data.repo.StoriesRepo
import com.wattpad.android.util.CoroutineTestRule
import com.wattpad.android.util.JsonConverter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito


@ExperimentalCoroutinesApi
class StoryRepoTest {

    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private inline fun <reified T> mock(): T = Mockito.mock(T::class.java)
    private var storyDatabase: StoriesDatabase = mock()

    @Test
    fun getStoryList(){

        val mockStory = Gson().fromJson(
            JsonConverter.getJsonFile("stories_response.json"),
            StoryBase::class.java
        )
        val storyRepository = StoriesRepo(storyDatabase,FakeStoryApiService(mockStory))


        GlobalScope.launch(Dispatchers.Main){

            val  pagingDataFlow: Flow<PagingData<Story>> =  Pager(
                config = PagingConfig(pageSize = 5, enablePlaceholders = false),
                pagingSourceFactory = {
                    FakeStoryMediator(FakeStoryApiService(mockStory))
                }
            ).flow

            Assert.assertNotNull(pagingDataFlow)
            Assert.assertNotNull(storyRepository.getStoriesStream())
        }
    }

}