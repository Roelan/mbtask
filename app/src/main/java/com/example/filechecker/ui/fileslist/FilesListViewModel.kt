package com.example.filechecker.ui.fileslist

import android.os.Environment
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.filechecker.data.FileData
import com.example.filechecker.provider.FileDataProvider
import com.example.filechecker.provider.FileLister
import com.example.filechecker.provider.SaveFilesData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class FilesListViewModel : ViewModel() {

    private var filesListData:  ArrayList<FileData> = ArrayList()
    var disposable: Disposable? = null
    private val fileDataProvider = FileDataProvider()

    @Inject lateinit var fileData: FileDataProvider

    fun setFileListArray() {
        val state = Environment.getExternalStorageState()
        Log.i("TAG", "Set File List Array")
        if (Environment.MEDIA_MOUNTED == state || Environment.MEDIA_MOUNTED_READ_ONLY == state) {
            this.disposable =
                Observable.fromPublisher(FileLister(Environment.getExternalStorageDirectory()))
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        fileDataProvider.setList(it)
                    }, {
                        Log.e("TAG", "Error in listing files from the SD card", it)
                    }, {
                        //         Toast.makeText(this, "Successfully listed all the files!", Toast.LENGTH_SHORT).show()
                        this.disposable?.dispose()
                        this.disposable = null
                    })
        }
        filesListData = fileDataProvider.getList()
    }

    fun getFileListArray(): ArrayList<FileData>   {
        Log.i("TAG", "Get File List Array")
        return filesListData
    }

    fun getSortedListByName(): ArrayList<FileData>  {
        filesListData = fileDataProvider.getSortedListByName()
        return filesListData
    }

    fun getSortedListBySize():ArrayList<FileData>  {
        filesListData = fileDataProvider.getSortedListBySize()
        return filesListData
    }

    fun getSortedListByDate(): ArrayList<FileData> {
        filesListData = fileDataProvider.getSortedListByDate()
        return filesListData
    }

    fun saveFileListData(){
        val saveFilesData = SaveFilesData()
        saveFilesData.save(filesListData)
    }
}