package com.example.coinapp.models


import com.google.gson.annotations.SerializedName

data class Quote(
    @SerializedName("USD")
    val uSD: USD
)