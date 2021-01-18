package com.wattpad.android.di

import android.content.Context
import androidx.room.Room
import com.wattpad.android.constants.CoreConstants
import com.wattpad.android.data.db.StoriesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * This class is providing app level instances
 */

@InstallIn(ApplicationComponent::class)
@Module
object ApplicationModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context): StoriesDatabase {
        return Room.databaseBuilder(
            appContext,
            StoriesDatabase::class.java,
            CoreConstants.DB_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun getRetrofitInstance(): Retrofit {

        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BASIC

        val client = OkHttpClient.Builder()
            .addInterceptor(logger)
            .build()

        return Retrofit.Builder()
            .baseUrl(CoreConstants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}