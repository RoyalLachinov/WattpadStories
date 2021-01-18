package com.wattpad.android.di

import com.wattpad.android.data.db.StoriesDatabase
import com.wattpad.android.data.repo.StoriesRepo
import com.wattpad.android.data.service.StoriesApiService
import com.wattpad.android.data.viewmodel.StoriesViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import retrofit2.Retrofit

/**
 * This class is providing Activity level instances but all provider function are {@ActivityRetainedScoped}
 * their instances will not be recreated on configuration changes.
 */

@InstallIn(ActivityRetainedComponent::class)
@Module
object ViewModelModule {

    @Provides
    @ActivityRetainedScoped
    fun provideStoryApiService(retrofit: Retrofit): StoriesApiService {
        return retrofit.create(StoriesApiService::class.java)
    }

    @Provides
    @ActivityRetainedScoped
    fun provideStoryRepo(storiesDatabase: StoriesDatabase,
                            storiesApiService: StoriesApiService)
    : StoriesRepo {
        return StoriesRepo(storiesDatabase,storiesApiService)
    }

    @Provides
    @ActivityRetainedScoped
    fun provideStoryViewModel(storiesRepo: StoriesRepo):StoriesViewModel{
        return StoriesViewModel(storiesRepo)
    }
}