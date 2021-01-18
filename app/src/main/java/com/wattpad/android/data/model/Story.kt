package com.wattpad.android.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "stories")
data class Story constructor(
    @PrimaryKey
    @SerializedName("id")
    var id: String = "",
    @SerializedName("title")
    var title: String = "",
    @SerializedName("user")
    @Embedded(prefix = "user_")
    var user: User = User(),
    @SerializedName("cover")
    var cover: String = "",
    @SerializedName("modifyDate")
    var modifyDate: String = ""
)