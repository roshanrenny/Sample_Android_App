package com.example.sampleandroidapp.mvvm

import retrofit2.http.GET
import retrofit2.Call

interface ApiService {

    @GET("jokes/random")

    fun getdetails(): Call<DataModel>

}

