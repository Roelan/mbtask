package com.example.filechecker.di

import com.example.filechecker.provider.FileDataProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule() {

    @Singleton
    @Provides
    fun provideFileDataProvider(): FileDataProvider = FileDataProvider()

}