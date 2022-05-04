package com.example.coinapp.interfaces

import com.example.coinapp.models.CoinApiJSON
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface APIRequest {

    @Headers("X-CMC_PRO_API_KEY: ff22299b-1d7a-49bf-861c-976100069d18")
    @GET("/v1/cryptocurrency/listings/latest")
    fun getCoins(
        @Query("start") startNumber: String,
        @Query("limit") limitNumber:String
    ) : Call<CoinApiJSON>

    @Headers("X-CMC_PRO_API_KEY: ff22299b-1d7a-49bf-861c-976100069d18")
    @GET("/v1/cryptocurrency/listings/latest")
    fun getCoinsWithRank(
        @Query("start") startNumber: String,
        @Query("limit") limitNumber:String,
        @Query("sort")  sortType:String
    ) : Call<CoinApiJSON>

}