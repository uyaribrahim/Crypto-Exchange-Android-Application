package com.ibrhm.cryptoexchange.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ibrhm.cryptoexchange.R
import com.ibrhm.cryptoexchange.model.CoinModel
import com.ibrhm.cryptoexchange.viewmodel.MarketsViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import java.text.NumberFormat
import java.util.*

class HomeFragment : Fragment() {

    private lateinit var marketViewModel: MarketsViewModel
    private var numberFormat = NumberFormat.getCurrencyInstance(Locale.US)
    private var newList: ArrayList<CoinModel> = ArrayList()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        numberFormat.maximumFractionDigits = 0

        marketViewModel = ViewModelProvider(requireActivity()).get(MarketsViewModel::class.java)

        marketViewModel.getCurrencyData()

        marketViewModel.coinList.observe(viewLifecycleOwner, Observer {

            newList = it.filter { it ->
                it.symbol?.lowercase()?.equals("btc") == true ||
                        it.symbol?.lowercase()?.equals("eth") == true ||
                        it.symbol?.lowercase()?.equals("sol") == true
            } as ArrayList<CoinModel>

            btc_price.text = numberFormat.format(newList[0].price)
            eth_price.text = numberFormat.format(newList[1].price)
            sol_price.text = numberFormat.format(newList[2].price)

            btc_volume.text = numberFormat.format(newList[0].volume_24h)
            eth_volume.text = numberFormat.format(newList[1].volume_24h)
            sol_volume.text = numberFormat.format(newList[2].volume_24h)

        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }
}