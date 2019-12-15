package ru.skillbranch.gameofthrones

import android.app.Application
import android.content.Context

/*
 * Created by yasina on 2019-12-14
*/
class App: Application() {
    companion object {
        private lateinit var instanse:App

        fun applicationContext(): Context {
            return instanse.applicationContext
        }
    }

    init {
        instanse = this
    }


}