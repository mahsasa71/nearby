package com.navin.nearby2.api


import com.navin.nearby2.model.Adress
import com.navin.nearby2.model.PlaceData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface Iservice {
    @GET("v5/reverse")
    fun getAddress(
        @Header("Api-Key") key: String,
        @Query("lat") lat: Double,
        @Query("lng") longitude: Double
    ): Call<Adress>
    @GET("v1/search")
    fun searchLocation(
        @Header("Api-Key") key: String,
        @Query("term") term: String,
        @Query("lat") lat: Double,
        @Query("lng") longitude: Double
    ): Call<PlaceData>

}