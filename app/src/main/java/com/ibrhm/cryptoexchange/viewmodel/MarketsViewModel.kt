package com.ibrhm.cryptoexchange.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ibrhm.cryptoexchange.model.CoinModel
import com.ibrhm.cryptoexchange.repository.Repository

class MarketsViewModel: ViewModel() {

    private var repository: Repository = Repository()

    var coinList: MutableLiveData<ArrayList<CoinModel>> = repository.getCoinMutableLiveData()
    var loading: MutableLiveData<Boolean> = repository.getLoading()

    fun getCurrencyData(){
        repository.getCurrencyData()
    }
}