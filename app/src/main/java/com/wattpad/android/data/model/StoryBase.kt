package com.wattpad.android.data.model

import com.google.gson.annotations.SerializedName

data class StoryBase(
    @SerializedName("stories")
    var storyList: MutableList<Story>,
    @SerializedName("nextUrl")
    var nextUrl: String = ""
)