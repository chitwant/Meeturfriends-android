package com.meetfriend.app.modals.friendsuggestion

data class Data(
    val firstName: String,
    val id: Int,
    val ismyfriend_count: Int,
    val friend_id: Int,
    val lastName: String,
    val profile_photo: String,
    var sentOrNot: Boolean=false
)