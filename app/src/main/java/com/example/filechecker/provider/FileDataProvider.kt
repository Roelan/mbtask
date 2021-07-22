package com.example.filechecker.provider

import android.util.Log
import com.example.filechecker.data.FileData
import java.io.File
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList

class FileDataProvider {

    private var filesList: ArrayList<FileData> = ArrayList()

    fun getDataList(): ArrayList<FileData> {
        filesList.clear()
        // dummy data for test
        filesList.add(
            FileData(
                "MyData.exe",
                "/0/narnia",
                "200tb",
                "never",
                "exe"
            )
        )

        val df = DecimalFormat("#.##")
        File("/storage/emulated/0").walkBottomUp().forEach {
            if (it.isFile) {
                filesList.add(
                    FileData(
                        fileName = it.name,
                        filePath = it.path,
                        fileSize = "${df.format(it.length() / (1024.0 * 1024))}Mb",
                        lastModified = "${Date(it.lastModified())}",
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