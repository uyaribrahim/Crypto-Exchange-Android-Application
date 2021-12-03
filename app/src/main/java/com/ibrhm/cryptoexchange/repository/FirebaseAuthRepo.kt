package com.ibrhm.cryptoexchange.repository

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class FirebaseAuthRepo {

    private var application: Application = Application()
    private var firebaseAuth: FirebaseAuth
    private var userLiveData: MutableLiveData<FirebaseUser>
    private var loggedOutLiveData: MutableLiveData<Boolean>

     constructor(application: Application) {
        this.application = application
        this.firebaseAuth = FirebaseAuth.getInstance()
        this.userLiveData = MutableLiveData()
        this.loggedOutLiveData = MutableLiveData()

        if (firebaseAuth.currentUser != null) {
            userLiveData.postValue(firebaseAuth.currentUser)
            loggedOutLiveData.postValue(false)
        }
    }

    fun register(email: String, password: String){

        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
            if(it.isSuccessful){
                userLiveData.postValue(firebaseAuth.currentUser)
            }else{
                Toast.makeText(application.applicationContext,
                    "Authentication failed: " + it.exception?.message,
                    Toast.LENGTH_SHORT).show()
            }
        }
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