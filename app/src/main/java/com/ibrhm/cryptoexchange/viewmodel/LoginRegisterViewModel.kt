package com.ibrhm.cryptoexchange.viewmodel

import android.app.Application
import androidx.annotation.NonNull
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.ibrhm.cryptoexchange.repository.FirebaseAuthRepo

class LoginRegisterViewModel(application: Application): AndroidViewModel(application) {

    private var firebaseAuthRepo: FirebaseAuthRepo = FirebaseAuthRepo(application)
    private var userLiveData: MutableLiveData<FirebaseUser> = firebaseAuthRepo.getUserLiveData()

    fun login(email: String, password: String){
        firebaseAuthRepo.login(email,password)
    }

    fun register(email: String,password: String){
        firebaseAuthRepo.register(email, password)
    }

    fun getUserLiveData(): MutableLiveData<FirebaseUser>{
        return userLiveData
    }


}