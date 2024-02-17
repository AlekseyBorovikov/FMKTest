package com.example.fmk.di

import com.example.fmk.data.repository.UserRepository
import com.example.fmk.remote.UserService
import com.example.fmk.ui.user_list.UserListAdapter
import com.example.fmk.utils.Config
import com.example.fmk.utils.UserDetailsDownloader
import com.example.fmk.utils.network.OkHttpProvider
import com.example.fmk.utils.network.RetrofitManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

const val BASE_URL = "base_url"

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    @Named(value = BASE_URL)
    fun provideBaseUrl(): String = Config.BASE_URL

    /**
     * Provides [OkHttpClient]
     * @return [OkHttpClient]
     */
    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient = OkHttpProvider.createOkHttpClient()

    /**
     * Provide [Retrofit] instance for tv program.
     */
    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        @Named(value = BASE_URL) baseUrl: String,
    ): Retrofit = RetrofitManager.getRetrofit(okHttpClient, baseUrl)

    /**
     * Provides [UserService] instance
     * @param retrofit: instance for creating [UserService]
     * @return [UserService]
     */
    @Singleton
    @Provides
    fun provideVerificationService(retrofit: Retrofit): UserService = retrofit.create(UserService::class.java)

    /**
     * Provides [UserDetailsDownloader] instance
     * @param repository: instance for creating [UserRepository]
     * @return [UserDetailsDownloader]
     */
    @Provides
    fun provideUserDetailDownloader(repository: UserRepository) = UserDetailsDownloader<UserListAdapter.UserListViewHolder>(repository)
}