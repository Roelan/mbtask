package com.example.filechecker.provider

import android.util.Log
import com.example.filechecker.data.FileData
import java.io.File
import kotlin.collections.ArrayList

class FileDataProvider {

    private var filesList: ArrayList<FileData> = ArrayList()

    fun getDummyDataList(): ArrayList<FileData> {
        filesList.add(
            FileData(
                "MyData",
                "/0/narnia",
                "200tb",
                "never",
                "exe"
            )
        )
        val results: ArrayList<String> = ArrayList()
        File("/storage/emulated/0").walkBottomUp().forEach {
            if (it.isFile) {
                results.add(it.name)
                filesList.add(
                    FileData(
                        fileName = it.name,
                        filePath = it.path,
                        fileSize = "${it.length() / (1024.0 * 1024)}Mb",
                        lastModified = "${it.lastModified()}",
                        fileExtension = it.extension
                    )
                )
                Log.i("TAG", "<<< dir: ${it.path} | fileName: ${it.name} | extension: ${it.extension} | Size=${it.length() / (1024.0 * 1024)}Mb\" | lastModified: ${it.lastModified()}")
            }
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