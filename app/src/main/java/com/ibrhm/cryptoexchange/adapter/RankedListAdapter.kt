package com.ibrhm.cryptoexchange.adapter

import android.annotation.SuppressLint
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
import kotlinx.android.synthetic.main.item_ranking_list.view.*
import kotlinx.android.synthetic.main.item_ranking_list.view.coinPrice
import kotlinx.android.synthetic.main.item_ranking_list.view.txtCoinSymbol
import kotlinx.android.synthetic.main.item_ranking_list.view.txtPercentChange24h
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class RankedListAdapter(val rankedList: ArrayList<CoinModel>):
    RecyclerView.Adapter<RankedListAdapter.RankedListViewHolder>() {

    private lateinit var context: Context
    private var decimalFormat: DecimalFormat = DecimalFormat("#.##")
    private var numberFormat = NumberFormat.getCurrencyInstance(Locale.US)

    class RankedListViewHolder(view: View): RecyclerView.ViewHolder(view)  {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankedListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_ranking_list,parent,false)
        return RankedListAdapter.RankedListViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RankedListViewHolder, position: Int) {

        var coin: CoinModel = rankedList[position]

        Glide
            .with(context)
            .load(coin.image_url)
            .circleCrop()
            .fitCenter()
            .into(holder.itemView.rankedListCoinImage)

        if(coin.percent_change_24h!! > 0){
            holder.itemView.txtPercentChange24h.setTextColor(Color.GREEN)
        }else{
            holder.itemView.txtPercentChange24h.setTextColor(Color.RED)
        }

        holder.itemView.txtCoinSymbol.text = coin.symbol + "/USD"
        holder.itemView.coinPrice.text = numberFormat.format(coin.price)
        holder.itemView.txtPercentChange24h.text = decimalFormat.format(coin.percent_change_24h) +"%"
    }

    override fun getItemCount(): Int {
        return rankedList.size
    }

    fun updateList(newCoinList: ArrayList<CoinModel>, context: Context){
        this.context = context
        rankedList.clear()
        rankedList.addAll(newCoinList)
        notifyDataSetChanged()
    }
}