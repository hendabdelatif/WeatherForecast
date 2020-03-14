package com.hend.weatherforecastchallenge.ui

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.plusAssign
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hend.weatherforecastchallenge.R
import com.hend.weatherforecastchallenge.base.BaseActivity
import com.hend.weatherforecastchallenge.ui.navigation.KeepStateNavigator


class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupBottomNavigationBar()
    }

    private fun setupBottomNavigationBar() {

        val navView = findViewById<BottomNavigationView>(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)!!

        val navigator = KeepStateNavigator(this, navHostFragment.childFragmentManager, R.id.nav_host_fragment)
        navController.navigatorProvider += navigator

        navController.setGraph(R.navigation.mobile_navigation)
        navView.setupWithNavController(navController)
    }

}
