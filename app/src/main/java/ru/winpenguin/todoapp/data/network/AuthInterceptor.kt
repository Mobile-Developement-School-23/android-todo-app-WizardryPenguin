package ru.winpenguin.todoapp.data.network

import okhttp3.Interceptor
import okhttp3.Response
import ru.winpenguin.todoapp.BuildConfig
import java.io.IOException

class AuthInterceptor : Interceptor {

    private val token = BuildConfig.apiToken

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .header("Authorization", "Bearer $token")
            .build()
        return chain.proceed(request)
    }
}