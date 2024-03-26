package com.example.sampleandroidapp.apipost

import com.google.gson.annotations.SerializedName

data class Model(
    val userId: Int,
    val title: String,
    @SerializedName("body") val text: String
) {
    var id: Int? = null
}
