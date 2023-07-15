package ru.winpenguin.todoapp.data.network

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

/**
 * Добавляет авторизационный заголовок в сетевые запросы
 */
class AuthInterceptor @Inject constructor() : Interceptor {

    // Было сделано через BuildConfig.apiToken,
    // но для упрощения запуска при проверке домашки - захардкодила
    private val token = "squeak"

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .header("Authorization", "Bearer $token")
            .build()
        return chain.proceed(request)
    }
}