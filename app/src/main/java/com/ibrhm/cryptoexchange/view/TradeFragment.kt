package com.ibrhm.cryptoexchange.view

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.ibrhm.cryptoexchange.R
import com.ibrhm.cryptoexchange.model.CoinModel
import com.ibrhm.cryptoexchange.viewmodel.SharedViewModel
import kotlinx.android.synthetic.main.fragment_trade.*
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates


class TradeFragment : Fragment() {


    private lateinit var marketViewModel: SharedViewModel
    private var newList: ArrayList<CoinModel> = ArrayList()

    private var decimalFormat: DecimalFormat = DecimalFormat("#.##")
    private var numberFormat = NumberFormat.getCurrencyInstance(Locale.US)

    private var globalCoinPrice: Double = 0.0
    private var price by Delegates.notNull<Double>()
    private lateinit var coin: CoinModel

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

        marketViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        marketViewModel.clickToItem.value = false

        marketViewModel.coinList.observe(viewLifecycleOwner, Observer {
            newList = it.filter { it ->
                it.symbol?.equals(marketViewModel.selectedItem.value.toString()) == true
            } as ArrayList<CoinModel>

            coin = newList.first()

            updateUI(coin)

        })

        tradeButton.setOnClickListener {
            if(tradeButton.text == "Buy"){
                val amount: Double = edittext_amount_coin.text.toString().toDouble()
                marketViewModel.buyCoin(coin,amount)
            }
        }

        edittext_amount_coin.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s!!.isNotEmpty()){
                    val amount: Float = edittext_amount_coin.text.toString().toFloat()
                    price = amount*globalCoinPrice
                    edittext_amount_usd.setText("%.2f".format(price),TextView.BufferType.EDITABLE)
                }else{
                    edittext_amount_usd.text.clear()
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        txt_sell.setOnClickListener {
            updateTradeOption()
        }
        txt_buy.setOnClickListener {
            updateTradeOption()
        }

    }
    @SuppressLint("SetTextI18n")
    private fun updateUI(coin: CoinModel){
        val imageUrl = coin.image_url
        val name = coin.name
        val percentChange24h = coin.percent_change_24h
        val price = coin.price
        val symbol = coin.symbol
        val volume = coin.volume_24h
        globalCoinPrice = price!!

        Glide
            .with(this)
            .load(imageUrl)
            .circleCrop()
            .fitCenter()
            .into(coin_image)

        if(symbol == "SHIB" || symbol == "ATLAS"){
            numberFormat.maximumFractionDigits = 6
            coin_price.text = numberFormat.format(price)
        }else{
            numberFormat.maximumFractionDigits = 2
            coin_price.text = numberFormat.format(price)
        }
        numberFormat.maximumFractionDigits = 0

        title.text = "$symbol/USD"
        coin_name.text = name
        coin_change_24h.text = decimalFormat.format(percentChange24h) + "%"
        coin_volume_24h.text = numberFormat.format(volume)
        input_price.setText(coin_price.text.toString(),TextView.BufferType.EDITABLE)
        txt_amount_coin.text = "Amount\n($symbol)"

        when(greaterThanZero(percentChange24h)){
            true -> coin_change_24h.setTextColor(Color.GREEN)
            false -> coin_change_24h.setTextColor(Color.RED)
        }
    }
    private fun greaterThanZero(value: Double?): Boolean{
        if (value!!>0){
            return true
        }
        return false
    }
    private fun updateTradeOption(){
        when(tradeButton.text.toString()){
            "Sell" -> setToBuy()
            "Buy" -> setToSell()
        }
    }
    private fun setToBuy(){
        trade_cardview.strokeColor = Color.parseColor("#32CD32")
        txt_sell.setTextColor(Color.parseColor("#9195a1"))
        view_sell.setBackgroundColor(Color.parseColor("#9195a1"))

        tradeButton.text = getString(R.string.buy)

        txt_buy.setTextColor(Color.parseColor("#32CD32"))
        view_buy.setBackgroundColor(Color.parseColor("#32CD32"))
    }
    private fun setToSell(){
        trade_cardview.strokeColor = Color.parseColor("#DC143C")
        txt_sell.setTextColor(Color.parseColor("#DC143C"))
        view_sell.setBackgroundColor(Color.parseColor("#DC143C"))

        tradeButton.text = getString(R.string.sell)

        txt_buy.setTextColor(Color.parseColor("#9195a1"))
        view_buy.setBackgroundColor(Color.parseColor("#9195a1"))
    }
}