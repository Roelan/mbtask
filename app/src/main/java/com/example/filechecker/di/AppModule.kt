package com.example.filechecker.di

import android.app.Application
import android.content.Context
import com.example.filechecker.provider.FileDataProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule() {

    @Provides
    fun provideFileDataProvider() : FileDataProvider = FileDataProvider()

}