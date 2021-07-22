package com.example.filechecker.ui.fileslist

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.filechecker.data.FileData
import com.example.filechecker.provider.FileDataProvider
import com.example.filechecker.provider.SaveFilesData
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class FilesListViewModel : ViewModel() {

    private var _filesListData = MutableLiveData<List<FileData>>()
    private val fileDataProvider = FileDataProvider()

    init {
        // App().getComponent().inject(this)
    }

    suspend fun initFileListArray() = coroutineScope {
        launch {
            _filesListData.postValue(fileDataProvider.getDataList())
        }
    }

    fun getFileListArray(): MutableLiveData<List<FileData>> {
        Log.i("TAG", "Get File List Array")
        return _filesListData
    }

    fun getSortedListByName(): MutableLiveData<List<FileData>> {
        _filesListData.postValue(fileDataProvider.getSortedListByName())
        return _filesListData
    }

    fun getSortedListBySize(): MutableLiveData<List<FileData>> {
        _filesListData.postValue(fileDataProvider.getSortedListBySize())
        return _filesListData
    }

    fun getSortedListByDate(): MutableLiveData<List<FileData>> {
        _filesListData.postValue(fileDataProvider.getSortedListByDate())
        return _filesListData
    }

    suspend fun saveFileListData(filesListArray: List<FileData>)= coroutineScope {
        launch {
            val saveFilesData = SaveFilesData()
            saveFilesData.save(filesListArray)
        }
    }
}