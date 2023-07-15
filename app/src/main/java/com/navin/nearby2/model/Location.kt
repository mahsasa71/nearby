package com.navin.nearby2.model

import com.google.gson.annotations.SerializedName

data class Location (

    @SerializedName("x")
    var longitude: String,
    @SerializedName("y")
    var latitude: String,
    )