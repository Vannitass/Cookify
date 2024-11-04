package com.example.pageforregister.networkapi

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("greet")
    fun greet(
        @Query("purpose") purpose: String,
        @Query("login") login: String,
        @Query("email") email: String,
        @Query("password") password: String
    ): Call<GreetResponse>
}
