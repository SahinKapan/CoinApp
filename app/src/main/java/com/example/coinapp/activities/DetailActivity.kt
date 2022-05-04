package com.example.coinapp.activities

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.coinapp.R
import kotlinx.android.synthetic.main.activity_detail.*


class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        takeValues()

        //To going back to main activity
        backButton.setOnClickListener {
            finish()
            super.onBackPressed()
        }
    }

    //The minus percentage here makes its color red. If it's positive it makes it green.
    private fun greenOrRed(textView: TextView){
        textView.setTextColor(if(textView.text.toString()[0] == '-')
            Color.parseColor("#EE6B67")
        else
            Color.parseColor("#82D685"))
    }

    //To take values from mainactivity and to give to views
    private fun takeValues(){

        nameCoin.text = intent.getStringExtra("CoinName")
        priceCoin.text = "$ "+intent.getStringExtra("CoinPrice")
        symbolCoin.text = intent.getStringExtra("CoinSymbol").toString()
        changeCoin.text = intent.getStringExtra("CoinChange")
        changeOneHour.text = intent.getStringExtra("OneHourChange")
        greenOrRed(changeOneHour)
        changeTwentyFourHours.text = intent.getStringExtra("TwentyFourChange")
        greenOrRed(changeTwentyFourHours)
        changeSevenDays.text = intent.getStringExtra("SevenDayChange")
        greenOrRed(changeSevenDays)
        changeThirtyDays.text = intent.getStringExtra("ThirtyDayChange")
        greenOrRed(changeThirtyDays)
        volumeTwentyFourEdit.text = "$ " +intent.getStringExtra("VolumeTwentyFour")
        circulatingSupplyEdit.text = intent.getStringExtra("CirculatingSupply")
        maximumSupplyEdit.text = intent.getStringExtra("MaximumSupply")
        marketCapEdit.text = "$ " + intent.getStringExtra("MarketCap")
        marketCapDominanceEdit.text = intent.getStringExtra("MarketCapDominance") + "%"
    }




}