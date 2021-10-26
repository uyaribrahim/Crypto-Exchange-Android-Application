package com.ibrhm.cryptoexchange.view

import android.app.DownloadManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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
import com.ibrhm.cryptoexchange.viewmodel.MarketsViewModel
import kotlinx.android.synthetic.main.fragment_market.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class MarketFragment : Fragment() {

    private val coinAdapter = CoinAdapter(arrayListOf())
    private var coinArrayList: ArrayList<CoinModel> = ArrayList()
    private lateinit var marketViewModel: MarketsViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        marketCoinRecyclerView.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        marketCoinRecyclerView.adapter = coinAdapter

        marketViewModel = ViewModelProvider(this).get(MarketsViewModel::class.java)

        marketViewModel.getCurrencyData()

        observeLiveData()


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

    fun observeLiveData(){
        marketViewModel.coinList.observe(viewLifecycleOwner, Observer {
            coinAdapter.updateList(it,this.context)
        })

        marketViewModel.loading.observe(viewLifecycleOwner, Observer {
            if (it){
                marketProgressBar.visibility = View.VISIBLE
            }else{
                marketProgressBar.visibility = View.GONE
            }
        })
    }

}