package com.meetfriend.app.responseclasses.challenge.mychallenge

data class MyChallengeResponseClass(
    val status: Boolean,
    val message: String,
    val media_url:String,
    val result: Result
)