package com.ibrhm.cryptoexchange.repository

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.ibrhm.cryptoexchange.model.Balance
import com.ibrhm.cryptoexchange.model.User

class FirebaseAuthRepo {

    private var application: Application = Application()
    private var firebaseAuth: FirebaseAuth
    private var userLiveData: MutableLiveData<FirebaseUser>
    private var loggedOutLiveData: MutableLiveData<Boolean>

    private var database: DatabaseReference


     constructor(application: Application) {
        this.application = application
        this.firebaseAuth = FirebaseAuth.getInstance()
        this.userLiveData = MutableLiveData()
        this.loggedOutLiveData = MutableLiveData()
         this.database = Firebase.database.reference

        if (firebaseAuth.currentUser != null) {
            userLiveData.postValue(firebaseAuth.currentUser)
            loggedOutLiveData.postValue(false)
        }
    }

    fun register(email: String, password: String){
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
            if(it.isSuccessful){
                val user = User(email,1000.00)
                userLiveData.postValue(firebaseAuth.currentUser)
                database.child("users")
                    .child(firebaseAuth.uid.toString())
                    .setValue(user).addOnSuccessListener {
                        addBalanceUser()
                    }
            }else{
                Toast.makeText(application.applicationContext,
                    "Authentication failed: " + it.exception?.message,
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addBalanceUser(){
        val balance = Balance("USDT","https://assets.coingecko.com/coins/images/325/large/Tether-logo.png?1598003707",
            1000.00,1000.00)
        database.child("users").child(firebaseAuth.uid.toString())
            .child("user_balance").child("USDT").setValue(balance)
    }

    fun login(email: String, password: String){

        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
            if(it.isSuccessful){
                userLiveData.postValue(firebaseAuth.currentUser)
            }else{
                Toast.makeText(application.applicationContext,
                    "Authentication failed: " + it.exception?.message,
                Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun logOut(){
        firebaseAuth.signOut()
        loggedOutLiveData.postValue(true)
    }

    fun getUserLiveData(): MutableLiveData<FirebaseUser> {
        return userLiveData
    }

    fun getLoggedOutLiveData(): MutableLiveData<Boolean>{
        return loggedOutLiveData
    }
}