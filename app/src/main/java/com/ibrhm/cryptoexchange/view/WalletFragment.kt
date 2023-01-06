package com.ibrhm.cryptoexchange.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ibrhm.cryptoexchange.R
import com.ibrhm.cryptoexchange.adapter.WalletAdapter
import com.ibrhm.cryptoexchange.viewmodel.SharedViewModel
import kotlinx.android.synthetic.main.fragment_wallet.*

class WalletFragment : Fragment() {

    private lateinit var marketViewModel: SharedViewModel
    private val walletAdapter = WalletAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wallet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        marketViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        marketViewModel.updateUserBalance()
        marketViewModel.getUserBalance()


        marketViewModel.userLiveData.observe(viewLifecycleOwner, Observer {
            if(it != null){
                val totalUSD = it.user_usd_value
                //val balanceList = it.user_balance
                txt_total_usd.text = "= $"+"%.2f".format(totalUSD)
            }
        })
        marketViewModel.userBalanceLiveData.observe(viewLifecycleOwner, Observer {
            if(it != null){
                walletAdapter.updateList(it,context)
            }
        })

    }
    private fun initRecyclerView(){
        walletRecyclerView.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.VERTICAL,false)
        walletRecyclerView.adapter = walletAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        walletRecyclerView.adapter = null
    }

}