package com.meetfriend.app.responseclasses.challenge


data class countryResponseClass(
    val result: ArrayList<Result>,
    val status: Boolean,
    val message:String
    )