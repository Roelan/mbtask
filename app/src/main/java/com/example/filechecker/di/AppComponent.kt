package com.example.filechecker.di

import com.example.filechecker.ui.fileslist.FilesListViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun inject(filesListViewModel: FilesListViewModel)
}