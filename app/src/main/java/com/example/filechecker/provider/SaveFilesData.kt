package com.example.filechecker.provider

import android.os.Environment
import com.example.filechecker.data.FileData
import java.io.*


class SaveFilesData {

    fun save(filesData: List<FileData>) {

        val extDir = Environment.getExternalStorageDirectory()
        try {
            File(extDir, "MyData.txt").bufferedWriter().use { out ->
                filesData.forEach {
                    out.write( "FileName: ${it.fileName} | Size=${it.fileSize}\n    File path: ${it.filePath}\n    LastModified: ${it.lastModified}\n")
                }
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}