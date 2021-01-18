package com.wattpad.android.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wattpad.android.data.model.RemoteKeys

/**
 * This {dao} interface is to provide access to RemoteKeys table
 */

@Dao
interface RemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: MutableList<RemoteKeys>)

    @Query("SELECT * FROM remote_keys WHERE storyId = :storyId")
    suspend fun remoteKeyId(storyId: String): RemoteKeys?

    @Query("DELETE FROM remote_keys")
    suspend fun clearRemoteKeys()

    @Query("SELECT * FROM remote_keys ORDER BY nextKey DESC LIMIT 1")
    suspend fun getNextKey(): RemoteKeys
}