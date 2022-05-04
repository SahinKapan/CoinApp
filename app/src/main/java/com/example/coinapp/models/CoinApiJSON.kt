package com.example.coinapp.models


data class CoinApiJSON(
    val `data`: List<Data>,
    val status: Status
)