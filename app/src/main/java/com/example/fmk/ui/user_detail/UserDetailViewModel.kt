package com.example.fmk.ui.user_detail

import com.example.fmk.base.BaseViewModel
import com.example.fmk.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    val userRepository: UserRepository,
): BaseViewModel<UserDetailViewState>() {

    override fun initState() = UserDetailViewState()

    fun loadFollowerList(login: String) = safeLaunch {
        updateState { copy(loading = true, error = null) }
        userRepository.getFollowerList(login).fold(
            onSuccess = { users ->
                updateState { copy(loading = false, followerList = users) }
            },
            onFailure = {
                updateState { copy(loading = false, error = it.message) }
            },
        )
    }
}