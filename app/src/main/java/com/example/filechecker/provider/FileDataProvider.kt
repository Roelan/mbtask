package com.example.filechecker.provider

import android.os.Environment
import android.util.Log
import com.example.filechecker.data.FileData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class FileDataProvider {

    private var filesList: ArrayList<FileData> = ArrayList()
    private var disposable: Disposable? = null

    private fun setList(fullPath: String) {
        val file = File(fullPath)
        val regex = """(.+)/(.+)\.(.+)""".toRegex()
        val matchResult = regex.matchEntire(fullPath)

        val fileSize = File(fullPath).length()
        val sizeInMb = fileSize / (1024.0 * 1024)
        val sizeInMbStr = "%.2f".format(sizeInMb)

        if (matchResult != null) {
            val (directory, fileName, extension) = matchResult.destructured

            filesList.add(
                FileData(
                    "$fileName.$extension",
                    directory,
                    "$sizeInMbStr MB",
                    Date(file.lastModified()).toString(),
                    extension
                )
            )
            println(
                "<<< dir: $directory | fileName: $fileName | extension: $extension | Size=${sizeInMbStr} MB | lastModified: ${
                    Date(
                        file.lastModified()
                    )
                }"
            )
        }
    }

    fun getFileDataList() : ArrayList<FileData>{
        val state = Environment.getExternalStorageState()
        Log.i("TAG", "Set File List Array")
        if (Environment.MEDIA_MOUNTED == state || Environment.MEDIA_MOUNTED_READ_ONLY == state) {
            this.disposable =
                Observable.fromPublisher(FileLister(Environment.getExternalStorageDirectory()))
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        setList(it)
                    }, {
                        Log.e("TAG", "Error in listing files from the SD card", it)
                    }, {
                        this.disposable?.dispose()
                        this.disposable = null
                    })
        }
        return filesList
    }

    fun getSortedListByName(): ArrayList<FileData> {
        filesList.sortBy { it.fileName }
        return filesList
    }

    fun getSortedListBySize(): ArrayList<FileData> {
        filesList.sortBy { it.fileSize }
        return filesList
    }

    fun getSortedListByDate(): ArrayList<FileData> {
        filesList.sortBy { it.lastModified }
        return filesList
    }
}