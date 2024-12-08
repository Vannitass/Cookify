package com.example.pageforregister.networkapi

import com.example.pageforregister.MainPage.Item
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.Query
import retrofit2.http.Part
import retrofit2.http.POST

interface ApiService {
    @GET("greet")
    fun greet(
        @Query("purpose") purpose: String,
        @Query("login") login: String,
        @Query("email") email: String,
        @Query("password") password: String
    ): Call<GreetResponse>
    @Multipart
    @POST("upload_recipe")
    fun uploadRecipe(
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part("content") content: RequestBody,
        @Part("author") author: RequestBody,
        @Part image: MultipartBody.Part
    ): Call<ResponseBody>
    @GET("get_recipes")
    fun getRecipes(): Call<List<Item>>
}