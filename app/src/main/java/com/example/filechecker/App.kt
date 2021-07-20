package com.example.filechecker

import android.app.Application
import com.example.filechecker.di.AppComponent
import com.example.filechecker.di.AppModule
import com.example.filechecker.di.DaggerAppComponent

class App : Application() {

    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule())
            .build()
    }

    fun getComponent() : AppComponent = appComponent
}