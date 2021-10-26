package com.ibrhm.cryptoexchange.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ibrhm.cryptoexchange.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavBar)
        val navController = navHostFragment.navController

        bottomNavigationView.setupWithNavController(navController)

    }
}