package com.ibrhm.cryptoexchange.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ibrhm.cryptoexchange.model.Balance
import com.ibrhm.cryptoexchange.model.CoinModel
import com.ibrhm.cryptoexchange.model.User
import com.ibrhm.cryptoexchange.repository.Repository

class SharedViewModel: ViewModel() {

    private var repository: Repository = Repository()

    var coinList: MutableLiveData<ArrayList<CoinModel>> = repository.getCoinMutableLiveData()
    var searchCurrencyList: MutableLiveData<ArrayList<CoinModel>> = repository.getSearchCurrencyLiveData()
    var rankedList: MutableLiveData<ArrayList<CoinModel>> = repository.getRankedListLiveData()
    var rankOptions: MutableLiveData<Boolean> = repository.getRankOption()
    var loading: MutableLiveData<Boolean> = repository.getLoading()
    var selectedItem: MutableLiveData<String> = MutableLiveData("BTC")
    var clickToItem: MutableLiveData<Boolean> = MutableLiveData()

    var userLiveData: MutableLiveData<User> = repository.getUserMutableLiveData()
    var userBalanceLiveData: MutableLiveData<ArrayList<Balance>> = repository.getUserBalanceMutableLiveData()


    fun getUserData(){
        repository.getUserData()
    }
    fun getUserBalance(){
        repository.getUserBalance()
    }
    fun updateUserBalance(){
        repository.updateUserBalance()
    }
    fun buyCoin(coin: CoinModel,balance: Double){
        val imageUrl = coin.image_url
        val symbol = coin.symbol.toString()
        val usdValue = balance * coin.price!!
        repository.buyCoin(balance,imageUrl,symbol,usdValue)
    }
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

    override fun onCleared() {
        super.onCleared()
        Log.e("VIEWMODEL","Temizlendi"+coinList.value?.size.toString())
    }

}