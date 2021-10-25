package com.ibrhm.cryptoexchange.adapter

import android.app.Application
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ibrhm.cryptoexchange.Constants
import com.ibrhm.cryptoexchange.R
import com.ibrhm.cryptoexchange.model.CoinModel
import kotlinx.android.synthetic.main.item_coin.view.*
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class CoinAdapter(val coinList: ArrayList<CoinModel>): RecyclerView.Adapter<CoinAdapter.CoinViewHolder>() {

    private var coinImgUrl: Array<String> = Constants.coinImageUrlList
    private var context: Context? = null

    private var decimalFormat: DecimalFormat = DecimalFormat("#.##")
    private var numberFormat = NumberFormat.getCurrencyInstance(Locale.US)

    class CoinViewHolder(var view: View): RecyclerView.ViewHolder(view) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_coin,parent,false)
        return CoinViewHolder(view)
    }

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {

        if (position <= 29){
            var url = coinImgUrl[position]
            Glide
                .with(context!!)
                .load(url)
                .circleCrop()
                .into(holder.view.coinImage)
        }

        var coin: CoinModel = coinList[position]
        holder.view.txtCoinName.text = coin.name
        holder.view.txtCoinSymbol.text = coin.symbol
        holder.view.coinPrice.text = "$" + decimalFormat.format(coin.price)
        holder.view.txtPercentChange24h.text = decimalFormat.format(coin.percent_change_24h) + "%"
        numberFormat.maximumFractionDigits = 0
        var volume = numberFormat.format(coin.volume_24h)
        holder.view.coinVolume.text = volume

        if(coin.percent_change_24h!! > 0){
            holder.view.txtPercentChange24h.setTextColor(Color.GREEN)
        }else{
            holder.view.txtPercentChange24h.setTextColor(Color.RED)
        }
    }

    override fun getItemCount(): Int {
        return coinList.size
    }
    fun updateList(newCoinList: ArrayList<CoinModel>, context: Context?){
        this.context = context
        coinList.clear()
        coinList.addAll(newCoinList)
        notifyDataSetChanged()
    }
}