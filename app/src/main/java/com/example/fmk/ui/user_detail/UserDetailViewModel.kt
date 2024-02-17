package com.example.fmk.ui.user_detail

import com.example.fmk.base.BaseViewModel
import com.example.fmk.data.entity.User
import com.example.fmk.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    val userRepository: UserRepository,
): BaseViewModel<UserDetailViewState>() {

    override fun initState() = UserDetailViewState()

    fun setUser(user: User) {
        updateState { copy(user = user) }
        if (!user.hasDetails()) loadDetails(user.login)
        loadFollowerList(user.login)
    }

    fun loadDetails(login: String) = safeLaunch {
        updateState { copy(loading = true, error = null) }
        userRepository.getUserDetail(login).fold(
            onSuccess = { users ->
                updateState { copy(loading = false, user = users) }
            },
            onFailure = {
                updateState { copy(loading = false, error = it.message) }
            },
        )
    }

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