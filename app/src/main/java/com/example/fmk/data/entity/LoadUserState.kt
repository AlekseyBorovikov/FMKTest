package com.example.fmk.data.entity

sealed class LoadUserDetailsState {
    class Loaded(val user: User): LoadUserDetailsState()
    class Error(val e: Throwable): LoadUserDetailsState()
}