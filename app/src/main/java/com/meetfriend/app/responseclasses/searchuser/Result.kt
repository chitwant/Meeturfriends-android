package com.meetfriend.app.responseclasses.searchuser

data class ResultUser(
    val email_or_phone: String,
    val firstName: String,
    val id: Int,
    val ismyfriend_count: Int,
    val lastName: String,
    val profile_photo: Any
)