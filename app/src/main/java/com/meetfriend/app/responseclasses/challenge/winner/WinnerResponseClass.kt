package com.meetfriend.app.responseclasses.challenge.winner

data class WinnerResponseClass(
    val status: Boolean,
    val message: String,
    val media_url:String,
    val result: Result
)