package com.hend.weatherforecastchallenge.base

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.hend.weatherforecastchallenge.R
import com.hend.weatherforecastchallenge.connection.ConnectionLiveData

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {

    lateinit var snackBar: Snackbar
    lateinit var connectionLiveData: ConnectionLiveData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        connectionLiveData = ConnectionLiveData(this)

        snackBar = Snackbar.make(
            findViewById(android.R.id.content),
            getString(R.string.no_internet_connection),
            Snackbar.LENGTH_INDEFINITE
        )

        connectionLiveData.observe(this, Observer { isConnected ->
            isConnected?.let {
                if (isConnected) {
                    snackBar.dismiss()
                } else {
                    snackBar.show()
                }
            }
        })

    }
}