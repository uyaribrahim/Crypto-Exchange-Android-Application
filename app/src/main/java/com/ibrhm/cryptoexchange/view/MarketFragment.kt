package com.ibrhm.cryptoexchange.view

import android.app.DownloadManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.ibrhm.cryptoexchange.Constants
import com.ibrhm.cryptoexchange.R
import com.ibrhm.cryptoexchange.adapter.CoinAdapter
import com.ibrhm.cryptoexchange.model.CoinModel
import kotlinx.android.synthetic.main.fragment_market.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class MarketFragment : Fragment() {

    private val coinAdapter = CoinAdapter(arrayListOf())
    private var coinArrayList: ArrayList<CoinModel> = ArrayList()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        marketCoinRecyclerView.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        marketCoinRecyclerView.adapter = coinAdapter

        marketProgressBar.visibility = View.VISIBLE
        getCoinData()


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_market, container, false)
    }

    fun getCoinData() {
        var url = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest"
        var requesQueue = Volley.newRequestQueue(this.context)
        var jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(
            Method.GET,
            url,
            JSONObject(),
            Response.Listener<JSONObject?> {
                                           try {
                                               var dataArray: JSONArray = it.getJSONArray("data")
                                               for(i in 0..50){
                                                   var dataObject = dataArray.getJSONObject(i)
                                                   var name = dataObject.getString("name")
                                                   var symbol = dataObject.getString("symbol")
                                                   var quote = dataObject.getJSONObject("quote")
                                                   var USD = quote.getJSONObject("USD")
                                                   var price: Double = USD.getDouble("price")
                                                   var volume_24h = USD.getLong("volume_24h")
                                                   var percent_change = USD.getDouble("percent_change_24h")
                                                   coinArrayList.add(CoinModel(name,symbol,price,volume_24h,percent_change))
                                               }
                                               marketProgressBar.visibility = View.GONE
                                               coinAdapter.updateList(coinArrayList)
                                           }
                                           catch (e: JSONException){
                                               e.printStackTrace()
                                               Toast.makeText(this.context,"Fail to extract json data",Toast.LENGTH_SHORT).show()
                                           }

            }, Response.ErrorListener {

            }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["X-CMC_PRO_API_KEY"] = Constants.MY_API_KEY
                return headers
                /*var params: MutableMap<String, String>? = super.getHeaders()
                if (params == null) params = HashMap()
                params[""] = ""
                //..add other headers
                return params*/
            }

        }
        requesQueue.add(jsonObjectRequest)
    }
}