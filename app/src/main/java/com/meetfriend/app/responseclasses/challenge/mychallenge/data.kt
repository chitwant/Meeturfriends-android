package com.meetfriend.app.responseclasses.challenge.mychallenge

import com.meetfriend.app.responseclasses.ChallengeDetailDataModel
import com.meetfriend.app.responseclasses.user

data class data(
    val id: Int,
    val status: Int,
    val title: String,
    val description: String,
    val file_path: String,
    val timezone: String,
    val time_from: String,
    val time_to: String,
    val date_from: String,
    val date_to: String,
    val created_at: String,
    val user: user,
    val challenge_reactions: ChallengeDetailDataModel.ChallengeReactions
)