package com.example.filechecker.provider

import com.example.filechecker.data.FileData
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class FileDataProvider {

    private var filesList: ArrayList<FileData> = ArrayList()

    fun setList(fullPath: String) {
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

    fun getList(): ArrayList<FileData> {
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