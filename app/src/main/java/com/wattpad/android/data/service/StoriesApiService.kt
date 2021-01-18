package com.wattpad.android.data.service

import com.wattpad.android.constants.CoreConstants
import com.wattpad.android.data.model.StoryBase
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface StoriesApiService {

    @GET(CoreConstants.STORIES)
    suspend fun getStories(@QueryMap options: Map<String, String>) : StoryBase
}