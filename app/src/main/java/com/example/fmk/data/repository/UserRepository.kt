package com.example.fmk.data.repository

import com.example.fmk.data.entity.User
import com.example.fmk.remote.UserService
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(private val service: UserService) {

    suspend fun getUserList(): Result<List<User>> = safeRequest {
        getResult(service.getUserList())
    }

    suspend fun getFollowerList(login: String): Result<List<User>> = safeRequest {
        getResult(service.getFollowerList(login = login))
    }

    suspend fun getUserDetail(login: String): Result<User> = safeRequest {
        getResult(service.getUserDetail(login = login))
    }



    private fun<T> getResult(response: Response<T>): Result<T> {
        if (response.isSuccessful && response.body() != null) {
            val data = response.body()!!
            return Result.success(data)
        }
        return Result.failure(Exception(response.message()))
    }

    private suspend fun<T> safeRequest(block: suspend () -> Result<T>): Result<T> {
        return try { block.invoke() } catch (e: Exception) { Result.failure(e) }
    }
}