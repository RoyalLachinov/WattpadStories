package com.wattpad.android.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingSource
import com.wattpad.android.constants.CoreConstants
import com.wattpad.android.data.model.Story
import com.wattpad.android.data.service.StoriesApiService
import retrofit2.HttpException
import java.io.IOException

/**
 * This class is a fake mediator class for providing fake data to repo
 */

@OptIn(ExperimentalPagingApi::class)
class FakeStoryMediator(
    private val storiesApiService: StoriesApiService
) :
    PagingSource<Int, Story>() {

    companion object {
        private const val STARTING_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
        val position = params.key ?: STARTING_PAGE_INDEX
        return try {

            val mapOfFilter = mapOf(
                "offset" to position.toString(),
                "limit" to CoreConstants.STORY_LIMIT_SIZE.toString(),
                "filter" to "new"
            )

            val storyBase = storiesApiService.getStories(mapOfFilter)
            val storyList = storyBase.storyList

            LoadResult.Page(
                data = storyList,
                prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (storyList.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

}