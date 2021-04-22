package com.meetfriend.app.responseclasses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CommonResponseClass(
    val message: String,
    val status: Boolean,
    val result: Int,
    @SerializedName("uid")
    @Expose
    val uid: Int? = null,
    @SerializedName("username")
    @Expose
    val username: String? = null,
    @SerializedName("channelName")
    @Expose
    val channelName: String? = null,
    @SerializedName("token")
    @Expose
    val token: String? = null
)