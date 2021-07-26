package com.example.filechecker.provider

import android.os.Environment
import android.util.Log
import com.example.filechecker.data.FileData
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.File

class SaveFilesData(private val filesData: List<FileData>) {

    init {
        val dispose = save()
            .subscribeOn(Schedulers.newThread())
            .subscribe({
                Log.i("TAG", it)
            }, {
                Log.e("TAG", it.localizedMessage)
            })
    }

    private fun save(): Observable<String> {
        return Observable.create { subscriber ->
            val extDir = Environment.getExternalStorageDirectory()
            File(extDir, "MyData.txt").bufferedWriter().use { out ->
                filesData.forEach {
                    out.write("FileName: ${it.fileName} | Size=${it.fileSize}\n    File path: ${it.filePath}\n    LastModified: ${it.lastModified}\n")
                }
            }
            subscriber.onNext("Done")
        }
    }
}