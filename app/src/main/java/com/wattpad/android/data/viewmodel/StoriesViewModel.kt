package com.wattpad.android.data.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.wattpad.android.data.model.Story
import com.wattpad.android.data.repo.StoriesRepo
import kotlinx.coroutines.flow.Flow

class StoriesViewModel @ViewModelInject constructor(private val storiesRepo: StoriesRepo) :
    ViewModel() {

    private var currentStoryList: Flow<PagingData<Story>>? = null
    fun getStoriesFlow(isRefresh: Boolean = false): Flow<PagingData<Story>> {
        val lastResult = currentStoryList
        if (lastResult != null && !isRefresh) {
            return lastResult
        }

        val newResult = storiesRepo.getStoriesStream().cachedIn(viewModelScope)
        currentStoryList = newResult
        return newResult
    }
}