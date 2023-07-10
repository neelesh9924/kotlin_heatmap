package com.example.kotlin_heatmap

import retrofit2.Call
import retrofit2.http.GET

interface Retrofit_Interface {

    @get:GET("getdata")
    val posts : Call<PostModel?>?

    companion object{
        const val BASE_URL = "http://192.168.4.1/"
    }
}