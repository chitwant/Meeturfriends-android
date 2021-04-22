package com.meetfriend.app.responseclasses.challenge

data class Result(
    val id: Int,
    val sortname: String,
    val name: String,
    val phonecode: Int,
    val status: Int,
    var checked:Boolean
)