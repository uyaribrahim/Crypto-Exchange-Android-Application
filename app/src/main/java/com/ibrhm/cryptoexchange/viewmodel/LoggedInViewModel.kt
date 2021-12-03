package com.ibrhm.cryptoexchange.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.ibrhm.cryptoexchange.repository.FirebaseAuthRepo

class LoggedInViewModel(application: Application) : AndroidViewModel(application) {

    private var firebaseAuthRepo = FirebaseAuthRepo(application)
    private var userLiveData: MutableLiveData<FirebaseUser> = firebaseAuthRepo.getUserLiveData()
    private var loggedOutLiveData: MutableLiveData<Boolean> = firebaseAuthRepo.getLoggedOutLiveData()

    fun logOut(){
        firebaseAuthRepo.logOut()
    }
    fun getUserLiveData(): MutableLiveData<FirebaseUser>{
        return userLiveData
    }
    fun getLoggetOutLiveData(): MutableLiveData<Boolean>{
        return loggedOutLiveData
    }
}