package com.meetfriend.app.responseclasses.challenge

data class StateResponseClass(
    val result: ArrayList<Result>,
    val status: Boolean,
    val message:String
)