package com.example.filechecker.ui.fileslist

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.filechecker.App
import com.example.filechecker.data.FileData
import com.example.filechecker.provider.FileDataProvider
import com.example.filechecker.provider.SaveFilesData
import javax.inject.Inject

class FilesListViewModel : ViewModel() {

    private var filesListData: ArrayList<FileData> = ArrayList()
    private val fileDataProvider = FileDataProvider()

    init {
       // App().getComponent().inject(this)
    }

    fun initFileListArray() {
        filesListData = fileDataProvider.getFileDataList()
    }

    fun getFileListArray(): ArrayList<FileData> {
        Log.i("TAG", "Get File List Array")
        return filesListData
    }

    fun getSortedListByName(): ArrayList<FileData> {
        filesListData = fileDataProvider.getSortedListByName()
        return filesListData
    }

    fun getSortedListBySize(): ArrayList<FileData> {
        filesListData = fileDataProvider.getSortedListBySize()
        return filesListData
    }

    fun getSortedListByDate(): ArrayList<FileData> {
        filesListData = fileDataProvider.getSortedListByDate()
        return filesListData
    }

    fun saveFileListData() {
        val saveFilesData = SaveFilesData()
        saveFilesData.save(filesListData)
    }
}