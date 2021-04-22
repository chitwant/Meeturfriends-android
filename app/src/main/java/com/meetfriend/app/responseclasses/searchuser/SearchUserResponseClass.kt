package com.meetfriend.app.responseclasses.searchuser

data class SearchUserResponseClass(
    val base_url: String,
    val media_url: String,
    val message: String,
    val result: ArrayList<ResultUser>,
    val status: Boolean
)