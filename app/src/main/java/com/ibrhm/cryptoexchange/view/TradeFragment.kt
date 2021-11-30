package com.ibrhm.cryptoexchange.view

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.ibrhm.cryptoexchange.R
import com.ibrhm.cryptoexchange.model.CoinModel
import com.ibrhm.cryptoexchange.repository.Repository
import com.ibrhm.cryptoexchange.viewmodel.MarketsViewModel
import kotlinx.android.synthetic.main.fragment_trade.*
import kotlinx.android.synthetic.main.item_coin.view.*


class TradeFragment : Fragment() {


    private lateinit var marketViewModel: MarketsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trade, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        marketViewModel = ViewModelProvider(requireActivity()).get(MarketsViewModel::class.java)

        marketViewModel.coinList.observe(viewLifecycleOwner, object : Observer<ArrayList<CoinModel>>{
            override fun onChanged(t: ArrayList<CoinModel>?) {
                //Log.e("###trade", t?.get(1)?.name.toString())
            }
        })

        Glide
            .with(this)
            .load("https://s2.coinmarketcap.com/static/img/coins/64x64/11212.png")
            .circleCrop()
            .into(coin_image)

        txt_sell.setOnClickListener {
            trade_cardview.strokeColor = Color.parseColor("#DC143C")
            txt_sell.setTextColor(Color.parseColor("#DC143C"))
            view_sell.setBackgroundColor(Color.parseColor("#DC143C"))

            tradeButton.text = "Sell"

            txt_buy.setTextColor(Color.parseColor("#9195a1"))
            view_buy.setBackgroundColor(Color.parseColor("#9195a1"))
        }
        txt_buy.setOnClickListener {
            trade_cardview.strokeColor = Color.parseColor("#32CD32")
            txt_sell.setTextColor(Color.parseColor("#9195a1"))
            view_sell.setBackgroundColor(Color.parseColor("#9195a1"))

            tradeButton.text = "Buy"

            txt_buy.setTextColor(Color.parseColor("#32CD32"))
            view_buy.setBackgroundColor(Color.parseColor("#32CD32"))
        }

    }
}