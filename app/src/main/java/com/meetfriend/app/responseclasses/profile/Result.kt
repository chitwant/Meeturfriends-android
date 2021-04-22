package com.meetfriend.app.responseclasses.profile

data class Result(
    val city: String,
    val cover_photo: Any,
    val dob: String,
    val education: String,
    val email_or_phone: String,
    val firstName: String,
    val gender: String,
    val hobbies: String,
    val id: Int,
    val lastName: String,
    val profile_photo: Any,
    val relationship: String,
    val work: String
)