package com.example.prateek.smackchat.controller

import android.app.Application
import utilities.SharedPrefs

class App : Application() {

    companion object {
        lateinit var sharedPreferences: SharedPrefs
    }

    override fun onCreate() {

        sharedPreferences = SharedPrefs(applicationContext)

        super.onCreate()
    }
}