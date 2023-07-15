package ru.winpenguin.todoapp.di

import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.winpenguin.todoapp.data.network.AuthInterceptor
import ru.winpenguin.todoapp.data.network.TodoApi
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class LoggingInterceptor

@Module
object NetworkModule {

    private const val BASE_URL = "https://beta.mrdekk.ru/"

    @LoggingInterceptor
    @Provides
    fun provideLoggingInterceptor(): Interceptor {
        return HttpLoggingInterceptor().apply {
            level = BODY
        }
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        @LoggingInterceptor loggingInterceptor: Interceptor,
        authInterceptor: AuthInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideTodoApi(
        okHttpClient: OkHttpClient
    ): TodoApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(TodoApi::class.java)
    }
}