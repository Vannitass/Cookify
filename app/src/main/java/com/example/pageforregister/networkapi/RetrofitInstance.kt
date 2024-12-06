package com.example.pageforregister.networkapi

import com.example.pageforregister.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {
    private const val BASE_URL = "http://10.192.223.155:5000" // Убедитесь, что заменили на ваш IP

    private val logging = HttpLoggingInterceptor().setLevel(
        if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
        else HttpLoggingInterceptor.Level.NONE

    )

    // Настройте OkHttpClient здесь
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .connectTimeout(15, TimeUnit.SECONDS) // Добавьте эти строки
        .readTimeout(15, TimeUnit.SECONDS)    // Добавьте эти строки
        .writeTimeout(15, TimeUnit.SECONDS)   // Добавьте эти строки
        .build()

    // Использование OkHttpClient при создании Retrofit
    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // Подключите здесь настроенный OkHttpClient
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

//    val api: ApiService by lazy {
////        Retrofit.Builder()
////            .baseUrl(BASE_URL)
////            .client(okHttpClient)
////            .addConverterFactory(GsonConverterFactory.create())
////            .build()
////            .create(ApiService::class.java)
//        retrofit.create(ApiService::class.java)
//    }
}
