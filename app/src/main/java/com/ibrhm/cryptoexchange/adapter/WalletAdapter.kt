package com.ibrhm.cryptoexchange.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ibrhm.cryptoexchange.R
import com.ibrhm.cryptoexchange.model.Balance
import kotlinx.android.synthetic.main.item_wallet.view.*

class WalletAdapter(val walletList: ArrayList<Balance>):
    RecyclerView.Adapter<WalletAdapter.WalletListViewHolder>(){

    private  var context: Context? = null

    class WalletListViewHolder(view: View): RecyclerView.ViewHolder(view) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WalletListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_wallet,parent,false)
        return WalletAdapter.WalletListViewHolder(view)
    }

    override fun onBindViewHolder(holder: WalletListViewHolder, position: Int) {
        var coin: Balance = walletList[position]
        Glide
            .with(context!!)
            .load(coin.coin_image)
            .circleCrop()
            .fitCenter()
            .into(holder.itemView.walletCoinImage)
        holder.itemView.coinBalance.text = coin.coin_balance.toString()
        holder.itemView.coinUsdValue.text = "%.2f".format(coin.coin_usd_value)
        holder.itemView.coinSymbol.text = coin.coin_symbol.toString()
    }

    override fun getItemCount(): Int {
        return walletList.size
    }

    fun updateList(newWalletList: ArrayList<Balance>, context: Context?){
        this.context = context
        walletList.clear()
        walletList.addAll(newWalletList)
        notifyDataSetChanged()
    }
}