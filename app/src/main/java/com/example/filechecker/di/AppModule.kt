package com.example.filechecker.di

import android.content.Context
import com.example.filechecker.provider.FileDataProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val context : Context) {

    @Singleton
    @Provides
    fun provideContext() : Context = context

    @Singleton
    @Provides
    fun provideFileDataProvider(): FileDataProvider = FileDataProvider()

}