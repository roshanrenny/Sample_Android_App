package com.example.sampleandroidapp.apipost

import com.example.sampleandroidapp.apipost.Model
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface Methods {
    @GET("posts")
    fun getPosts(): Call<List<Model>>

    @POST("posts")
    @FormUrlEncoded
    fun createPost(
        @Field("userId") userId: Int,
        @Field("title") title: String,
        @Field("text") text: String
    ): Call<Model>

    @DELETE("posts/{id}")
    fun deletePost(@Path("id") id: Int): Call<Void>

    @PATCH("posts/{id}")
    fun patchPost(@Path("id") id: Int, @Body updatedData: Model): Call<Model>
}
