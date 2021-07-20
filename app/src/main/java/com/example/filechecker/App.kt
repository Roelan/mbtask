package com.example.filechecker

import android.app.Application
import com.example.filechecker.di.AppComponent
import com.example.filechecker.di.AppModule

class App : Application() {

    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule())
            .build()
    }

    @JvmName("getAppComponent1")
    fun getAppComponent(): AppComponent{
        return appComponent
    }
}