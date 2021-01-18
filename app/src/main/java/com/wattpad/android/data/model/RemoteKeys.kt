package com.wattpad.android.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Purpose of creating RemoteKeys table is that I can add another table that stores the next and
 * previous page keys for each Story.
 */

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey val storyId: String,
    val prevKey: Int?,
    val nextKey: Int?
)
