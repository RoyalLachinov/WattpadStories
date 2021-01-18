package com.wattpad.android.data

import com.wattpad.android.data.model.StoryBase
import com.wattpad.android.data.service.StoriesApiService

/**
 * This class is a fake api service for getting fake stories list in repository test class
 */

class FakeStoryApiService(private var response: Any) : StoriesApiService{

    override suspend fun getStories(options: Map<String, String>): StoryBase {
        return response as StoryBase
    }
}