package com.ibrhm.cryptoexchange.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.ibrhm.cryptoexchange.model.CoinModel
import java.util.*
import kotlin.collections.ArrayList

class Repository {

    private val database = Firebase.database
    private val currencyRef = database.getReference("coin")

    private var coinMutableLiveData: MutableLiveData<ArrayList<CoinModel>> = MutableLiveData()
    private var loading: MutableLiveData<Boolean> = MutableLiveData()
    private var searchCurrencyLiveData: MutableLiveData<ArrayList<CoinModel>> = MutableLiveData()
    private var rankedListLiveData: MutableLiveData<ArrayList<CoinModel>> = MutableLiveData()
    //if rankOption is true, list winners. is it false then list losers
    private var rankOption: MutableLiveData<Boolean> = MutableLiveData(true)

    private var emptyArrayList: ArrayList<CoinModel> = ArrayList()

    fun searchCurrency( key: String){

        var keyWord = key.lowercase()
        var newList: ArrayList<CoinModel> = ArrayList()

        if(emptyArrayList.isEmpty()){
            emptyArrayList = coinMutableLiveData.value!!
            Log.e("###", emptyArrayList.size.toString())
        }
        newList = emptyArrayList.filter {
            it.name?.lowercase()?.contains(keyWord) == true || it.symbol?.lowercase()?.contains(keyWord) == true
        } as ArrayList<CoinModel>

        searchCurrencyLiveData.postValue(newList)
    }

    fun getCurrencyData(){
        loading.value = true
        var list: ArrayList<CoinModel> = ArrayList()
        currencyRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                for(coinSnapshot in snapshot.children) {
                    val coin = coinSnapshot.getValue(CoinModel::class.java)
                    if (coin != null) {
                        list.add(coin)
                    }
                }
                loading.value = false
                coinMutableLiveData.postValue(list)

            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun setRankOption(){
        if (rankOption.value == false){
            rankOption.value = true
            sortRankingListByOption()
        }else{
            rankOption.value = false
            sortRankingListByOption()
        }

    }
    fun sortRankingListByOption(){
        val list = coinMutableLiveData.value!!
        var sortedList: List<CoinModel>
        if(rankOption.value == true){
            sortedList =  list.sortedWith(compareByDescending { it.percent_change_24h })
            sortedList = sortedList.take(6)
            rankedListLiveData.postValue(sortedList as ArrayList<CoinModel>?)
        }else{
            sortedList =  list.sortedWith(compareBy { it.percent_change_24h })
            sortedList = sortedList.take(6)
            rankedListLiveData.postValue(sortedList as ArrayList<CoinModel>?)
        }
    }

    fun getSearchCurrencyLiveData(): MutableLiveData<ArrayList<CoinModel>>{
        return searchCurrencyLiveData
    }
    fun getCoinMutableLiveData(): MutableLiveData<ArrayList<CoinModel>>{
        return coinMutableLiveData
    }
    fun getLoading(): MutableLiveData<Boolean>{
        return loading
    }
    fun getRankedListLiveData(): MutableLiveData<ArrayList<CoinModel>>{
        return rankedListLiveData
    }
    fun getRankOption(): MutableLiveData<Boolean>{
        return rankOption
    }
}