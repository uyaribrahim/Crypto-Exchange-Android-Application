package com.ibrhm.cryptoexchange.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ibrhm.cryptoexchange.model.CoinModel
import com.ibrhm.cryptoexchange.repository.Repository

class MarketsViewModel: ViewModel() {

    private var repository: Repository = Repository()

    var coinList: MutableLiveData<ArrayList<CoinModel>> = repository.getCoinMutableLiveData()
    var searchCurrencyList: MutableLiveData<ArrayList<CoinModel>> = repository.getSearchCurrencyLiveData()
    var rankedList: MutableLiveData<ArrayList<CoinModel>> = repository.getRankedListLiveData()
    var rankOptions: MutableLiveData<Boolean> = repository.getRankOption()
    var loading: MutableLiveData<Boolean> = repository.getLoading()

    fun getCurrencyData(){
        repository.getCurrencyData()
    }
    fun searchCurrency(key: String){
        repository.searchCurrency(key)
    }

    fun setRankOption(){
        repository.setRankOption()
    }

    fun sortRankingListByOption(){
        repository.sortRankingListByOption()
    }

}