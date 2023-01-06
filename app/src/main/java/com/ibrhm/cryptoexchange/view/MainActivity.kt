package com.ibrhm.cryptoexchange.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ibrhm.cryptoexchange.R
import com.ibrhm.cryptoexchange.ui.login.LoginActivity
import com.ibrhm.cryptoexchange.viewmodel.LoggedInViewModel
import com.ibrhm.cryptoexchange.viewmodel.SharedViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var loggedInViewModel: LoggedInViewModel
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavBar)
        val navController = navHostFragment.navController

        bottomNavigationView.setupWithNavController(navController)

        loggedInViewModel = ViewModelProvider(this).get(LoggedInViewModel::class.java)
        sharedViewModel = ViewModelProvider(this).get(SharedViewModel::class.java)
        sharedViewModel.getCurrencyData()
        sharedViewModel.getUserData()

        sharedViewModel.clickToItem.observe(this, Observer {
            if (it){
                bottomNavigationView.selectedItemId = R.id.tradeFragment
            }
        })

        loggedInViewModel.getLoggetOutLiveData().observe(this, Observer {
            if (it){
                Toast.makeText(this, "Çıkış Yapıldı", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        })
    }
}