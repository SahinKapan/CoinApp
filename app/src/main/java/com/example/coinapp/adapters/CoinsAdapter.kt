package com.example.coinapp.adapters

import android.content.Intent
import android.graphics.Color
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.coinapp.activities.DetailActivity
import com.example.coinapp.models.Data
import com.example.coinapp.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_layout.view.*
import java.util.*
import kotlin.Int
import kotlin.with


class CoinsAdapter : RecyclerView.Adapter<CoinsAdapter.CoinsViewHolder>() {
    private var list = mutableListOf<Data>()
    inner class CoinsViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

        //The bind function undertakes the process of connecting the views in the item layout we created for the items to the incoming data.
        fun bind(data: Data){
            with(itemView){
                coinName.text = data.name
                decideTextSize(coinName,data.name)
                coinSymbol.text = data.symbol
                plusOrMinus(coinChange,data.quote.uSD.percentChange24h,data.quote.uSD.price)
                sixOrTwoDecimal(coinPrice,data.quote.uSD.price)
                greenOrRed(coinChange,data.quote.uSD.percentChange24h)
                makeFormat(OneChange,data.quote.uSD.percentChange1h)
                makeFormat(TwentyFourChange,data.quote.uSD.percentChange24h)
                makeFormat(SevenDayChange,data.quote.uSD.percentChange7d)
                makeFormat(ThirtyDayChange,data.quote.uSD.percentChange30d)
                makeFormatForTwo(VolumeTwentyFour,data.quote.uSD.volume24h)
                makeFormatForTwo(CirculatingSupply,data.circulatingSupply)
                controlMaxSupply(MaximumSupply,data.maxSupply.toDouble())
                makeFormatForTwo(MarketCap,data.quote.uSD.marketCap)
                makeFormatForTwo(MarketCapDominance,data.quote.uSD.marketCapDominance)
                Picasso.get().load(
                    StringBuilder("https://s2.coinmarketcap.com/static/img/coins/32x32/").append(data.id.toString())
                        .append(".png").toString()).into(coinIcon)
            }
        }

        //The init is used to send the data shown on the detail screen and open the detail screen if the user clicks on an item.
        init {
            itemView.setOnClickListener{ v: View ->
                val intent = Intent(v.context, DetailActivity::class.java)
                intent.putExtra("CoinName",itemView.coinName.text.toString())
                intent.putExtra("CoinSymbol",itemView.coinSymbol.text.toString())
                intent.putExtra("CoinChange",itemView.coinChange.text.toString())
                intent.putExtra("CoinPrice",itemView.coinPrice.text.toString())
                intent.putExtra("OneHourChange",itemView.OneChange.text.toString())
                intent.putExtra("TwentyFourChange",itemView.TwentyFourChange.text.toString())
                intent.putExtra("SevenDayChange",itemView.SevenDayChange.text.toString())
                intent.putExtra("ThirtyDayChange",itemView.ThirtyDayChange.text.toString())
                intent.putExtra("VolumeTwentyFour",itemView.VolumeTwentyFour.text.toString())
                intent.putExtra("CirculatingSupply",itemView.CirculatingSupply.text.toString())
                intent.putExtra("MaximumSupply",itemView.MaximumSupply.text.toString())
                intent.putExtra("MarketCap",itemView.MarketCap.text.toString())
                intent.putExtra("MarketCapDominance",itemView.MarketCapDominance.text.toString())
                v.context.startActivity(intent)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinsViewHolder {
        val view =  LayoutInflater.from(parent.context).inflate(R.layout.item_layout,parent,false)
        return CoinsViewHolder(view)
    }

    override fun onBindViewHolder(holder: CoinsViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
    //To add list
    fun addList(items: ArrayList<Data>){
        list.addAll(items)
        notifyDataSetChanged()
    }
    // To clear
    fun clear(){
        list.clear()
        notifyDataSetChanged()
    }

    //for positive data to have a '+' sign at the beginning
    fun makeFormat(textView: TextView, data:Double){
        if(data < 0){
            textView.text = "%,.2f".format(Locale.ENGLISH, data)+"%"
        }else{
            textView.text = "+"+"%,.2f".format(Locale.ENGLISH, data)+"%"
        }
    }

    //for +0.91%(+26,045$) display on main screen
    fun plusOrMinus(textView: TextView, data:Double,price:Double){
        if(data<0){
            textView.text = "%,.2f".format(Locale.ENGLISH, data)+"% ("+"%,.3f".format(Locale.ENGLISH, (data*price/100))+ " $)"
        }else{
            textView.text = "+"+"%,.2f".format(Locale.ENGLISH, data)+"% (+"+"%,.3f".format(Locale.ENGLISH, (data*price/100))+ " $)"
        }
    }

    //We allow 6 decimals to be displayed after the comma if the coin's price is below $1
    fun sixOrTwoDecimal(textView: TextView,data:Double){
        if(data < 1.0){
            textView.text = "%,.6f".format(Locale.ENGLISH, data)
        }
        else{
            makeFormatForTwo(textView,data)
        }
    }
    //The minus percentage here makes its color red. If it's positive it makes it green.
    fun greenOrRed(textView: TextView, data: Double){
        textView.setTextColor(if(data < 0)
            Color.parseColor("#EE6B67")
        else
            Color.parseColor("#82D685"))
    }
    //It converts to Locale.English format and allows 2 decimal numbers after the comma.
    fun makeFormatForTwo(textView: TextView,data:Double){
        textView.text = "%,.2f".format(Locale.ENGLISH, data)
    }
    //It finds coins with no max supply.
    fun controlMaxSupply(textView: TextView,data: Double){
        if(data == 0.00){
            textView.text = "It is not known."
        }else{
            textView.text = "%,.2f".format(Locale.ENGLISH, data)
        }
    }

    //It decides to size of textview according to number of letter of coin name
    fun decideTextSize(textView: TextView,data:String){
        if(data.count() > 17){
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12F)
        }
    }


}