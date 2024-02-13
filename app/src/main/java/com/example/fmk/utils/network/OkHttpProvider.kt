package com.example.fmk.utils.network

import com.example.fmk.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

/**
 * Creates and configures the OkHttpClient
 * */
object OkHttpProvider {

    private const val TIMEOUT = 40L

    /**
     * Creates OkHttp client
     * @param interceptors: common interceptors
     * @param networkInterceptors: network interceptors
     *
     * @return [OkHttpClient]
     * */
    fun createOkHttpClient(
        interceptors: List<Interceptor>? = null,
        networkInterceptors: List<Interceptor>? = null
    ): OkHttpClient {

        // set timeouts
        val builder = OkHttpClient.Builder()
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            // set HttpLoggingInterceptor
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(httpLoggingInterceptor)
        }

        //set common interceptors
        interceptors?.forEach {
            builder.addInterceptor(it)
        }

        //set network interceptors
        networkInterceptors?.forEach {
            builder.addNetworkInterceptor(it)
        }

        return builder.build()
    }
}