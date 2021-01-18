package com.wattpad.android.data.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.wattpad.android.constants.CoreConstants
import com.wattpad.android.data.service.StoriesApiService
import com.wattpad.android.data.db.StoriesDatabase
import com.wattpad.android.data.model.RemoteKeys
import com.wattpad.android.data.model.Story
import retrofit2.HttpException
import java.io.IOException

/**
 * In this Mediator class I defined how to fetch data from the network and persist it to local database
 * when user reaches the end of the data in the database by implementing a RemoteMediator.
 */


@OptIn(ExperimentalPagingApi::class)
class StoriesMediator(
    private val storiesDatabase: StoriesDatabase,
    private val storiesApiService: StoriesApiService,
) : RemoteMediator<Int, Story>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Story>): MediatorResult {

        val offset = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyForItem(0, state)
                remoteKeys?.nextKey?.minus(CoreConstants.STORY_LIMIT_SIZE) ?: CoreConstants.STARTING_OFFSET_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForItem(1, state)
                if (remoteKeys == null) {
                    // The LoadType is PREPEND so some data was loaded before,
                    // so we should have been able to get remote keys
                    return MediatorResult.Success(
                        endOfPaginationReached = true
                    )
                } else {
                    // If the previous key is null, then we can't request more data
                    remoteKeys.prevKey ?: return MediatorResult.Success(
                        endOfPaginationReached = true
                    )
                    remoteKeys.prevKey
                }
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForItem(2, state)
                if (remoteKeys?.nextKey == null) CoreConstants.STORY_LIMIT_SIZE else remoteKeys.nextKey
            }

        }

        try {
            val mapOfFilter = mapOf(
                "offset" to offset.toString(),
                "limit" to CoreConstants.STORY_LIMIT_SIZE.toString(),
                "filter" to "new")

            val story = storiesApiService.getStories(mapOfFilter)
            val storyList = story.storyList
            val endOfPaginationReached = storyList.isEmpty()

            storiesDatabase.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    storiesDatabase.getRemoteKeysDao().clearRemoteKeys()
                    storiesDatabase.getStoriesDao().deleteStories()
                }

                val prevKey = if (offset == CoreConstants.STARTING_OFFSET_INDEX) null else offset
                val nextKey = if (endOfPaginationReached) null else offset.plus(CoreConstants.STORY_LIMIT_SIZE)

                val keys = storyList.map {
                    RemoteKeys(storyId = it.id, prevKey = prevKey, nextKey = nextKey)
                }

                storiesDatabase.getRemoteKeysDao().insertAll(keys.toMutableList())
                storiesDatabase.getStoriesDao().insertStories(storyList)

            }

            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)

        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }


    private suspend fun getRemoteKeyForItem(
        itemPosition: Int,
        state: PagingState<Int,Story>
    ): RemoteKeys? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return when (itemPosition) {
            1 -> {
                state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
                    ?.let { model ->
                        // Get the remote keys of the first items retrieved
                        storiesDatabase.getRemoteKeysDao().remoteKeyId(model.id)
                    }
            }
            2 ->{
                storiesDatabase.getRemoteKeysDao().getNextKey()
            }
            else -> {
                state.anchorPosition?.let { position ->
                    state.closestItemToPosition(position)?.id?.let { modelId ->
                        storiesDatabase.getRemoteKeysDao().remoteKeyId(modelId)
                    }
                }
            }
        }
    }
}