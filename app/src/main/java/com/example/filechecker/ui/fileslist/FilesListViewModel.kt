package com.example.filechecker.ui.fileslist

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.filechecker.App
import com.example.filechecker.data.FileData
import com.example.filechecker.provider.FileDataProvider
import com.example.filechecker.provider.SaveFilesData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class FilesListViewModel : ViewModel() {

    private var _filesListData = MutableLiveData<List<FileData>>()

    @Inject
    lateinit var fileDataProvider : FileDataProvider

    init {
         App().getComponent().inject(this)
    }

    fun initFileListArray() {
        CoroutineScope(Dispatchers.IO).launch {
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

    fun saveFileListData(filesListArray: List<FileData>) {
        SaveFilesData(filesListArray)
    }
}