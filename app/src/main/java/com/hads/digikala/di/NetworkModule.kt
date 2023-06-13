package com.hads.digikala.di

import com.hads.digikala.data.remote.ApiInterface
import com.hads.digikala.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    internal fun interceptor(): HttpLoggingInterceptor{
        var logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        logging.setLevel(HttpLoggingInterceptor.Level.HEADERS)
        return logging
    }

    @Provides
    @Singleton
    fun provideOkHttp(): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(Constants.TIMEOUT_IN_SECOND, TimeUnit.SECONDS)
        .readTimeout(Constants.TIMEOUT_IN_SECOND, TimeUnit.SECONDS)
        .writeTimeout(Constants.TIMEOUT_IN_SECOND, TimeUnit.SECONDS)
        .addInterceptor(interceptor())
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideApiInterface(retrofit: Retrofit) : ApiInterface =
        retrofit.create(ApiInterface::class.java)
}