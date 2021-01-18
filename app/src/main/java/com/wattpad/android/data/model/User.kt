package com.wattpad.android.data.model

import com.google.gson.annotations.SerializedName

data class User constructor(
    @SerializedName("name")
    var name: String = "",
    @SerializedName("avatar")
    var avatar: String = "",
    @SerializedName("fullname")
    var fullName: String = ""
)
