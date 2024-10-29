package com.example.pageforregister.networkapi

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("greet")
    fun greet(@Query("name") name: String): Call<GreetResponse>
}
