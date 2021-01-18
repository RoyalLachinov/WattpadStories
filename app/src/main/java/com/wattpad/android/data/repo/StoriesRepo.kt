package com.wattpad.android.data.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.wattpad.android.constants.CoreConstants
import com.wattpad.android.data.db.StoriesDatabase
import com.wattpad.android.data.mediator.StoriesMediator
import com.wattpad.android.data.model.Story
import com.wattpad.android.data.service.StoriesApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * In this repo class I defined source of PagingData in the {pagingSourceFactory} block
 * which is local database
 */

class StoriesRepo @Inject constructor(
    private val storiesDatabase: StoriesDatabase,
    private val storiesApiService: StoriesApiService
) {

    fun getStoriesStream(): Flow<PagingData<Story>> {
        return Pager(
            config = PagingConfig(
                pageSize = CoreConstants.STORY_LIMIT_SIZE,
                enablePlaceholders = false
            ),
            remoteMediator = StoriesMediator(
                storiesDatabase,
                storiesApiService
            ),
            pagingSourceFactory = {
                storiesDatabase.getStoriesDao().getStories()
            }
        ).flow
    }
}