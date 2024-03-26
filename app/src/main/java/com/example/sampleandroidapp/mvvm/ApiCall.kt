
package com.example.sampleandroidapp.mvvm

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiCall {
    fun getdetails(callback: (DataModel) -> Unit) { val intercepter = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder().apply { this.addInterceptor(intercepter)
                .connectTimeout(3, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(25, TimeUnit.SECONDS)
        }.build()
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://api.chucknorris.io/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service: ApiService = retrofit.create(ApiService::class.java)

        val call: Call<DataModel> = service.getdetails()

        call.enqueue(object : Callback<DataModel> {
            override fun onResponse(call: Call<DataModel>, response: Response<DataModel>) {
                if (response.isSuccessful) {
                    val details: DataModel = response.body() as DataModel
                    callback(details)
                }
            }

            override fun onFailure(call: Call<DataModel>, t: Throwable) {
            }
        })
    }
}
