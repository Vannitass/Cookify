package com.example.pageforregister.networkapi

import com.example.pageforregister.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
/**
 * Объект `RetrofitInstance` — обеспечивает создание и конфигурацию объекта Retrofit для работы с сетевыми запросами.
 *
 * Особенности:
 * - Использует `OkHttpClient` с настраиваемыми тайм-аутами и логированием.
 * - Базовый URL задаётся через константу `BASE_URL`.
 * - Поддерживает преобразование JSON-ответов с помощью `GsonConverterFactory`.
 */
object RetrofitInstance {
    /**
     * Базовый URL для API. Замените IP-адрес на ваш локальный или публичный адрес сервера.
     */

    private const val BASE_URL = "http://192.168.1.144:5000" // Убедитесь, что заменили на ваш IP
    /**
     * Логирование сетевых запросов и ответов.
     * - Уровень логирования зависит от конфигурации сборки:
     *   - `BODY` для отладки (`BuildConfig.DEBUG`).
     *   - `NONE` для релизной версии.
     */
    private val logging = HttpLoggingInterceptor().setLevel(
        if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
        else HttpLoggingInterceptor.Level.NONE

    )

    /**
     * Настроенный клиент `OkHttpClient`:
     * - Тайм-ауты для подключения, чтения и записи установлены на 15 секунд.
     * - Добавлено логирование через `HttpLoggingInterceptor`.
     */
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .connectTimeout(15, TimeUnit.SECONDS) // Добавьте эти строки
        .readTimeout(15, TimeUnit.SECONDS)    // Добавьте эти строки
        .writeTimeout(15, TimeUnit.SECONDS)   // Добавьте эти строки
        .build()

    /**
     * Экземпляр `ApiService`, создаваемый с помощью настроенного `Retrofit`.
     * Использует:
     * - Базовый URL из `BASE_URL`.
     * - Настроенный `okHttpClient`.
     * - Конвертер JSON-ответов с помощью `GsonConverterFactory`.
     */
    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // Подключите здесь настроенный OkHttpClient
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }


}
