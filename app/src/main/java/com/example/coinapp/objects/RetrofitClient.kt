package com.example.coinapp.objects

import com.example.coinapp.interfaces.APIRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient {
    private const val BASE_URL = "https://pro-api.coinmarketcap.com"


    val instance: APIRequest by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(APIRequest::class.java)
    }
}