package com.ibrhm.cryptoexchange.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.ibrhm.cryptoexchange.model.CoinModel

class Repository {

    val database = Firebase.database
    val currencyRef = database.getReference("coin")

    private var coinMutableLiveData: MutableLiveData<ArrayList<CoinModel>> = MutableLiveData()
    private var loading: MutableLiveData<Boolean> = MutableLiveData()

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

    fun getCoinMutableLiveData(): MutableLiveData<ArrayList<CoinModel>>{
        return coinMutableLiveData
    }
    fun getLoading(): MutableLiveData<Boolean>{
        return loading
    }
}