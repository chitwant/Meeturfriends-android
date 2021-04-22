package com.meetfriend.app.responseclasses.viewprofile

data class Result(
    val city: String,
    val cover_photo: String,
    val dob: String,
    val education: String,
    val email_or_phone: String,
    val firstName: String,
    val friends_count: Int,
    val gender: String,
    val hobbies: String,
    val id: Int,
    val lastName: String,
    val myFriend: MyFriend,
    val profile_photo: String,
    val relationship: String,
    val work: String
)