package com.ibrhm.cryptoexchange.repository

import android.os.Handler
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.ibrhm.cryptoexchange.model.Balance
import com.ibrhm.cryptoexchange.model.CoinModel
import com.ibrhm.cryptoexchange.model.User
import kotlinx.coroutines.delay
import java.util.*
import kotlin.collections.ArrayList


class Repository {


    private var database: DatabaseReference = Firebase.database.reference
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    private var coinMutableLiveData: MutableLiveData<ArrayList<CoinModel>> = MutableLiveData()
    private var loading: MutableLiveData<Boolean> = MutableLiveData()
    private var searchCurrencyLiveData: MutableLiveData<ArrayList<CoinModel>> = MutableLiveData()
    private var rankedListLiveData: MutableLiveData<ArrayList<CoinModel>> = MutableLiveData()
    //if rankOption is true, list winners. is it false then list losers
    private var rankOption: MutableLiveData<Boolean> = MutableLiveData(true)

    private var userMutableLiveData: MutableLiveData<User> = MutableLiveData()
    private var userBalanceMutableLiveData: MutableLiveData<ArrayList<Balance>> = MutableLiveData()

    private var emptyArrayList: ArrayList<CoinModel> = ArrayList()
    private var balanceList: ArrayList<String> = ArrayList()

    private var coinList: ArrayList<CoinModel> = ArrayList()

    fun updateUserBalance(){
        val list = coinMutableLiveData.value
        if (list != null) {
            for (item in list){
                if(balanceList.contains(item.symbol)){
                    val dbRef = database.child("users").child("${auth.uid}")
                        .child("user_balance").child(item.symbol.toString())

                    dbRef.get().addOnSuccessListener {
                        var coinData = it.getValue(Balance::class.java)
                        var coinValue: Double = coinData?.coin_balance!!
                        var usdValue = item.price?.times(coinValue)
                        dbRef.child("coin_usd_value").setValue(usdValue)
                    }
                }
            }
        }
    }

    fun buyCoin(balance: Double, imageUrl: String?, symbol: String, usdValue: Double){

        val balanceObj = Balance(symbol,imageUrl,balance,usdValue)
        val dbRef = database.child("users").child("${auth.uid}")
            .child("user_balance")
        dbRef.child(symbol).get().addOnSuccessListener { symbolSnapShot ->
            if(!symbolSnapShot.exists()){
                dbRef.child(symbol).setValue(balanceObj).addOnCompleteListener {
                    updateCurrentTether(usdValue)
                }
            }else{
                var coinData = symbolSnapShot.getValue(Balance::class.java)
                var coinUsdValue = coinData?.coin_usd_value!! + usdValue
                var coinValue = coinData?.coin_balance!! + balance
                val updatedBalance = Balance(symbol,imageUrl,coinValue,coinUsdValue)
                dbRef.child(symbol).setValue(updatedBalance).addOnCompleteListener {
                    updateCurrentTether(usdValue)
                }
                Log.e("ROOT",dbRef.key.toString())
            }

        }
    }

    private fun updateCurrentTether(spent: Double){
        val dbRef = database.child("users").child("${auth.uid}")
            .child("user_balance")
        dbRef.child("USDT").get().addOnSuccessListener {
            var tether = it.getValue(Balance::class.java)
            var avaliable = tether?.coin_balance?.minus(spent)

            val updatedTether = Balance(tether?.coin_symbol, tether?.coin_image
                , avaliable, avaliable)
            dbRef.child("USDT").setValue(updatedTether)

        }
    }

    fun getUserBalance(){
        var list: ArrayList<Balance> = ArrayList()
        var totalUSDValue: Double
        database.child("users").child("${auth.uid}")
            .child("user_balance")
           .addValueEventListener(object : ValueEventListener{
               override fun onDataChange(snapshot: DataSnapshot) {
                   list.clear()
                   totalUSDValue = 0.0
                   for(coin in snapshot.children) {
                       Log.e("SNAPSHOT",coin.key.toString())
                       balanceList.add(coin.key.toString())
                       val coin = coin.getValue(Balance::class.java)
                       if (coin != null) {
                           list.add(coin)
                           totalUSDValue += coin.coin_usd_value!!
                       }
                   }
                   userBalanceMutableLiveData.postValue(list)
                   database.child("users").child("${auth.uid}")
                       .child("user_usd_value").setValue(totalUSDValue)
               }
               override fun onCancelled(error: DatabaseError) {
                   TODO("Not yet implemented")
               }

           })
    }

    fun getUserData(){
        database.child("users").child(auth.uid.toString()).addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                userMutableLiveData.postValue(snapshot.getValue(User::class.java))
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun searchCurrency( key: String){
        //TODO("Bu saçma kontrolü kaldırmayı unutma yeirne fonksiyon yazılacak")
        if(key != "!*"){
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
        }else{
            searchCurrencyLiveData.postValue(null)
        }

    }

    fun getCurrencyData(){
        loading.value = true
        database.child("coin").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                coinList.clear()
                for(coinSnapshot in snapshot.children) {
                    val coin = coinSnapshot.getValue(CoinModel::class.java)
                    if (coin != null) {
                        coinList.add(coin)
                    }
                }
                loading.value = false
                coinMutableLiveData.postValue(coinList)

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

    fun getUserBalanceMutableLiveData(): MutableLiveData<ArrayList<Balance>>{
        return userBalanceMutableLiveData
    }
    fun getUserMutableLiveData(): MutableLiveData<User>{
        return userMutableLiveData
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