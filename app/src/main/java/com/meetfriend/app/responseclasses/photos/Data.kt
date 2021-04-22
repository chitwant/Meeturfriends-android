package com.meetfriend.app.responseclasses.photos

data class Data(
    val extension: String,
    val file_name: String,
    val file_path: String,
    val id: Int,
    val posts_id: Int,
    val size: String,
    val type_of_media: String
)