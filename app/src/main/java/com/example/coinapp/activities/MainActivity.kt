package com.example.coinapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.coinapp.models.CoinApiJSON
import com.example.coinapp.models.Data
import com.example.coinapp.R
import com.example.coinapp.objects.RetrofitClient
import com.example.coinapp.adapters.CoinsAdapter
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var adapter:CoinsAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private var limit = 10
    private var startNumber = 1
    private var total = 1000
    private var totalStart = 100
    private var isLoading = false
    private var sort:String = "CMC Rank"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        layoutManager = LinearLayoutManager(this)
        swipe_to_refresh.setOnRefreshListener(this)
        setupRecyclerView()
        getCoins(false)

        //New data comes as scrolling
        coin_recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val visibleItemCount = layoutManager.childCount
                val pastVisibleItem = layoutManager.findFirstVisibleItemPosition()
                val total = adapter.itemCount
                if(!isLoading && startNumber < totalStart){
                    if(visibleItemCount + pastVisibleItem >= total){
                        startNumber++
                        if(sort == "CMC Rank"){
                            getCoins(false)
                        }else{
                            getCoinsWithRank(false,sort)
                        }
                    }
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })
        fillRankMenu()

        //Sort value is changed according to selected item and recyclerview is updated
        rankedBy.setOnItemClickListener { parent, view, position, id ->
            if(position == 0){
                setAdapterForRanking("CMC Rank")
                getCoins(false)
            }else if(position == 1){
                setAdapterForRanking("price")
                getCoinsWithRank(false,sort)
            }else if(position == 2){
                setAdapterForRanking("market_cap")
                getCoinsWithRank(false,sort)
            }else if(position == 3){
                setAdapterForRanking("volume_24h")
                getCoinsWithRank(false,sort)
            }else if(position == 4){
                setAdapterForRanking("percent_change_24h")
                getCoinsWithRank(false,sort)
            }else if(position == 5){
                setAdapterForRanking("percent_change_7d")
                getCoinsWithRank(false,sort)
            }
        }
    }

    //Defaul Get Coins
    private fun getCoins(isOnRefresh: Boolean) {
        isLoading = true
        if(!isOnRefresh) progressBar.visibility = View.VISIBLE
        Handler().postDelayed({
            RetrofitClient.instance.getCoins(startNumber = (startNumber*limit-9).toString(), limitNumber = limit.toString()).enqueue(object : Callback<CoinApiJSON>{
                override fun onResponse(call: Call<CoinApiJSON>, response: Response<CoinApiJSON>) {
                    val listResponse = response.body()?.data
                    if(listResponse != null){
                        adapter.addList(listResponse as ArrayList<Data>)
                    }
                    if(startNumber == totalStart){
                    progressBar.visibility = View.GONE
                    }else{
                        progressBar.visibility = View.INVISIBLE
                    }
                    isLoading = false
                    swipe_to_refresh.isRefreshing = false
                }
                override fun onFailure(call: Call<CoinApiJSON>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "${t.message}",Toast.LENGTH_SHORT).show()
                }
            })
        },1000)
    }

    //Bringing coins according to the selected item
    private fun getCoinsWithRank(isOnRefresh: Boolean,sorting : String) {
        isLoading = true
        if(!isOnRefresh) progressBar.visibility = View.VISIBLE
        Handler().postDelayed({
            RetrofitClient.instance.getCoinsWithRank(startNumber = (startNumber*limit-9).toString(), limitNumber = limit.toString(), sortType = sorting).enqueue(object : Callback<CoinApiJSON>{
                override fun onResponse(call: Call<CoinApiJSON>, response: Response<CoinApiJSON>) {
                    val listResponse = response.body()?.data
                    if(listResponse != null){
                        adapter.addList(listResponse as ArrayList<Data>)
                    }
                    if(startNumber == totalStart){
                        progressBar.visibility = View.GONE
                    }else{
                        progressBar.visibility = View.INVISIBLE
                    }
                    isLoading = false
                    swipe_to_refresh.isRefreshing = false
                }
                override fun onFailure(call: Call<CoinApiJSON>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "${t.message}",Toast.LENGTH_SHORT).show()
                }
            })
        },1000)
    }

    //To setup RecyclerView
    private fun setupRecyclerView() {
        coin_recycler_view.setHasFixedSize(true)
        coin_recycler_view.layoutManager = layoutManager
        adapter = CoinsAdapter()
        coin_recycler_view.adapter = adapter
    }

    //To refresh if the screen is pulled down
    override fun onRefresh() {
        fillRankMenu()
        sort = "CMC Rank"
        adapter.clear()
        startNumber = 1
        getCoins(true)
    }
    private fun setAdapterForRanking(rank:String){
        sort = rank
        adapter.clear()
        startNumber = 1
    }

    //Serves to fill our spinner after onRefresh.
    private fun fillRankMenu(){
        var ranksArray = arrayListOf<String>("CMC Rank","Price","MarketCap","24h Volume","Change(24h)","Change(7d)")
        rankedBy.setText("CMC Rank")
        var rankAdapter = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item, ranksArray)
        rankedBy.setAdapter(rankAdapter)
    }

}