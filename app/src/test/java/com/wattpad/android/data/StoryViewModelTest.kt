package com.wattpad.android.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.gson.Gson
import com.wattpad.android.data.model.Story
import com.wattpad.android.data.model.StoryBase
import com.wattpad.android.data.repo.StoriesRepo
import com.wattpad.android.data.viewmodel.StoriesViewModel
import com.wattpad.android.util.JsonConverter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class StoryViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    //Mock
    private inline fun <reified T> mock(): T = Mockito.mock(T::class.java)
    private val storyRepository = mock<StoriesRepo>()
    private val storyViewModel = StoriesViewModel(storyRepository)

    private val dispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun getStoryList(){
        /**
         * In this function I tried get mock story list. As I use Paging 3 for storyList in the MainActivity,
         * below test case might not be correct, because could not find proper test example from android.developers.com
         * If I would use LiveData it'd be very easy to write test case
         */

        val mockStoryBase = Gson().fromJson(
            JsonConverter.getJsonFile("stories_response.json"),
            StoryBase::class.java
        )

        GlobalScope.launch(Dispatchers.Main) {

            val  pagingDataFlow: Flow<PagingData<Story>> = Pager(
                config = PagingConfig(pageSize = 1, enablePlaceholders = false),
                pagingSourceFactory = {
                    FakeStoryMediator(FakeStoryApiService(mockStoryBase))
                }
            ).flow

            Mockito.`when`(storyViewModel.getStoriesFlow(true))
                .thenReturn(pagingDataFlow)
        }
    }
}