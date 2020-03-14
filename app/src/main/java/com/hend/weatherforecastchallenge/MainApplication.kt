package com.hend.weatherforecastchallenge

import android.content.Context
import androidx.multidex.MultiDexApplication

class MainApplication : MultiDexApplication() {

    init { INSTANCE = this }

    companion object {

        lateinit var INSTANCE: MainApplication
            private set

        val applicationContext: Context get() { return INSTANCE.applicationContext }
    }
}