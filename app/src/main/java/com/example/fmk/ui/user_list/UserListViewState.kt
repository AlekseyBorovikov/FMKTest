package com.example.fmk.ui.user_list

import com.example.fmk.base.ViewState
import com.example.fmk.data.entity.User

data class UserListViewState(
    val loading: Boolean = false,
    val error: String? = null,
    val userList: List<User> = listOf(),
): ViewState
