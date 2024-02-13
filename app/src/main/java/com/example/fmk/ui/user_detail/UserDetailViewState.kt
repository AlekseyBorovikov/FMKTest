package com.example.fmk.ui.user_detail

import com.example.fmk.base.ViewState
import com.example.fmk.data.entity.User

data class UserDetailViewState(
    val loading: Boolean = false,
    val error: String? = null,
    val followerList: List<User> = listOf(),
): ViewState
