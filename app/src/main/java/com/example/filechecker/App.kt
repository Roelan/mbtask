package com.example.filechecker

import android.app.Application
import com.example.filechecker.di.AppComponent
import com.example.filechecker.di.AppModule
import com.example.filechecker.di.DaggerAppComponent

class App : Application() {

    private val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
            .appModule(AppModule())
            .build()
    }

    fun getComponent() = appComponent
}