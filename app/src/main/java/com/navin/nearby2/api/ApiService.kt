package com.navin.nearby2.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiService {
    var retrofit=Retrofit.Builder()
        .baseUrl("https://api.neshan.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}