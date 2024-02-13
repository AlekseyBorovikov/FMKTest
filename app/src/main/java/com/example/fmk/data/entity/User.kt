package com.example.fmk.data.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class User(
    val id: Int,
    val login: String,
    val email: String?,
    val blog: String?,
    val company: String?,
    val location: String?,
    val bio: String?,
    @SerializedName("avatar_url") val avatarUrl: String,
    @SerializedName("followers_url") val followersUrl: String,
    @SerializedName("public_repos") val reposCount: Int?,
    @SerializedName("followers") val followersCount: Int?,
): Serializable