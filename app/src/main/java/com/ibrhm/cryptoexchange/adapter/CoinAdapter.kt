package com.ibrhm.cryptoexchange.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ibrhm.cryptoexchange.R
import com.ibrhm.cryptoexchange.model.CoinModel
import kotlinx.android.synthetic.main.item_coin.view.*
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList
import com.ibrhm.cryptoexchange.viewmodel.SharedViewModel


class CoinAdapter(val coinList: ArrayList<CoinModel>): RecyclerView.Adapter<CoinAdapter.CoinViewHolder>() {

    private var context: Context? = null
    private var marketViewModel: SharedViewModel? = null

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

        var coin: CoinModel = coinList[position]

        Glide
            .with(context!!)
            .load(coin.image_url)
            .circleCrop()
            .fitCenter()
            .into(holder.view.coinImage)

        if(coin.symbol == "SHIB" || coin.symbol == "ATLAS"){
            numberFormat.maximumFractionDigits = 6
            holder.view.coinPrice.text = numberFormat.format(coin.price)
        }else{
            numberFormat.maximumFractionDigits = 2
            holder.view.coinPrice.text = numberFormat.format(coin.price)
        }
        holder.view.txtCoinName.text = coin.name
        holder.view.txtCoinSymbol.text = coin.symbol
        holder.view.txtPercentChange24h.text = decimalFormat.format(coin.percent_change_24h) + "%"
        numberFormat.maximumFractionDigits = 0
        var volume = numberFormat.format(coin.volume_24h)
        holder.view.coinVolume.text = volume

        if(coin.percent_change_24h!! > 0){
            holder.view.txtPercentChange24h.setTextColor(Color.GREEN)
        }else{
            holder.view.txtPercentChange24h.setTextColor(Color.RED)
        }

        holder.view.setOnClickListener {
            marketViewModel?.selectedItem?.value = coin.symbol.toString()
            marketViewModel?.clickToItem?.value = true
        }
    }

    override fun getItemCount(): Int {
        return coinList.size
    }
    fun updateList(newCoinList: ArrayList<CoinModel>, context: Context?, viewModel: SharedViewModel){
        this.context = context
        coinList.clear()
        coinList.addAll(newCoinList)
        marketViewModel = viewModel
        notifyDataSetChanged()
    }
}