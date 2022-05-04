package com.example.coinapp.models


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("circulating_supply")
    val circulatingSupply: Double,
    @SerializedName("cmc_rank")
    val cmcRank: Int,
    @SerializedName("date_added")
    val dateAdded: String,
    val id: Int,
    @SerializedName("last_updated")
    val lastUpdated: String,
    @SerializedName("max_supply")
    val maxSupply: Long,
    val name: String,
    @SerializedName("num_market_pairs")
    val numMarketPairs: Int,
    val platform: Any,
    val quote: Quote,
    @SerializedName("self_reported_circulating_supply")
    val selfReportedCirculatingSupply: Double,
    @SerializedName("self_reported_market_cap")
    val selfReportedMarketCap: Double,
    val slug: String,
    val symbol: String,
    val tags: List<String>,
    @SerializedName("total_supply")
    val totalSupply: Double
)