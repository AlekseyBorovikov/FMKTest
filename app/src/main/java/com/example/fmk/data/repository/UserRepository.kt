package com.example.fmk.data.repository

import com.example.fmk.data.entity.User
import com.example.fmk.remote.UserService
import javax.inject.Inject

class UserRepository @Inject constructor(private val service: UserService) {

    suspend fun getUserList(): Result<List<User>> {
        try {
            val response = service.getUserList()
            if (response.isSuccessful && response.body() != null) {
                val data = response.body()!!
                val userList = data.map {
                    getUserDetail(it.login) ?: it
                }
                return Result.success(userList)
            }
            return Result.failure(Exception(response.message()))
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    suspend fun getFollowerList(login: String): Result<List<User>> {
        try {
            val response = service.getFollowerList(login = login)
            if (response.isSuccessful && response.body() != null) {
                val data = response.body()!!
                val userList = data.map {
                    getUserDetail(it.login) ?: it
                }
                return Result.success(userList)
            }
            return Result.failure(Exception(response.message()))
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    suspend fun getUserDetail(login: String): User? {
        try {
            val response = service.getUserDetail(login = login)
            if (response.isSuccessful && response.body() != null) {
                return response.body()
            }
            return null
        } catch (e: Exception) {
            return null
        }
    }
}