package com.example.fmk.remote

import com.example.fmk.data.entity.User
import com.example.fmk.utils.Config
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

private const val API = "users"
private const val GET_USERS = API
private const val GET_USER_DETAILS = "$API/{login}"
private const val GET_USER_FOLLOWERS = "$API/{login}/followers"

interface UserService {

    @GET(GET_USERS)
    suspend fun getUserList(
        @Header("Authorization") token: String = "Bearer ${Config.TOKEN}",
    ): Response<ArrayList<User>>

    @GET(GET_USER_DETAILS)
    suspend fun getUserDetail(
        @Header("Authorization") token: String = "Bearer ${Config.TOKEN}",
        @Path("login") login: String
    ): Response<User>

    @GET(GET_USER_FOLLOWERS)
    suspend fun getFollowerList(
        @Header("Authorization") token: String = "Bearer ${Config.TOKEN}",
        @Path("login") login: String
    ): Response<List<User>>
}