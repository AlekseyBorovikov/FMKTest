package com.example.fmk.ui.user_list

import com.example.fmk.base.BaseViewModel
import com.example.fmk.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val userRepository: UserRepository,
): BaseViewModel<UserListViewState>() {

    override fun initState() = UserListViewState()

    init { loadUserList() }

    fun loadUserList() = safeLaunch {
        updateState { copy(loading = true, error = null) }
        userRepository.getUserList().fold(
            onSuccess = { users ->
                updateState { copy(loading = false, userList = users) }
            },
            onFailure = {
                updateState { copy(loading = false, error = it.message) }
            },
        )
    }
}