package com.ibrhm.cryptoexchange.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ibrhm.cryptoexchange.R
import com.ibrhm.cryptoexchange.adapter.CoinAdapter
import com.ibrhm.cryptoexchange.viewmodel.SharedViewModel
import kotlinx.android.synthetic.main.fragment_market.*

class MarketFragment : Fragment() {

    private val coinAdapter = CoinAdapter(arrayListOf())
    private lateinit var marketViewModel: SharedViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        marketViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        observeLiveData()
        marketCoinRecyclerView.startLayoutAnimation()

        marketSearchBar.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s!!.isNotEmpty()){
                    marketViewModel.searchCurrency(marketSearchBar.text.toString())
                }else{
                    marketViewModel.coinList?.value?.let { coinAdapter.updateList(it,context,marketViewModel) }
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
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

    private fun initRecyclerView(){
        marketCoinRecyclerView.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        marketCoinRecyclerView.adapter = coinAdapter
    }

    private fun observeLiveData(){
        marketViewModel.coinList.observe(viewLifecycleOwner, Observer {
            coinAdapter.updateList(it,this.context,marketViewModel)
        })

        marketViewModel.loading.observe(viewLifecycleOwner, Observer {
            if (it){
                marketProgressBar.visibility = View.VISIBLE
            }else{
                marketProgressBar.visibility = View.GONE
            }
        })

        marketViewModel.searchCurrencyList.observe(viewLifecycleOwner, {
            if(it != null ){
                coinAdapter.updateList(it,this.context,marketViewModel)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        marketViewModel.searchCurrency("!*")
    }
    override fun onDestroyView() {
        super.onDestroyView()
        marketCoinRecyclerView.adapter = null
    }

}