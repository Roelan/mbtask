package com.example.filechecker

import android.os.Environment
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.IOUtils
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ForAssetFiles {

    fun getStorageDirectory(): File {
        return if (Environment.getExternalStorageState() == null) {
            val filePath = File(Environment.getDataDirectory().absolutePath + "/FileChecker/")
            if (!filePath.exists())
                filePath.mkdirs()
            filePath
        } else {
            val filePath =
                File(Environment.getExternalStorageDirectory().absolutePath + "/FileChecker/")
            if (!filePath.exists())
                filePath.mkdirs()
            filePath
        }
    }

    /**
     * Method that copies file from assets to Internal storage {@link Environment#DIRECTORY_DOWNLOADS} folder
     * @return a File from Internal Storage with exported file name
     * @throws IOException in case something goes wrong
     */
    @Throws(IOException::class)
    fun extractFileFromAsset(folderName: String, fileName: String): File? {

        val testDir = getStorageDirectory()
        val fileToScan = File(testDir, fileName)

        val inputStream =
            androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().context.assets.open(
                "$folderName/$fileName"
            )
        inputStream.use {
            val outputStream = FileOutputStream(fileToScan)
            outputStream.use {
                IOUtils.copy(inputStream, outputStream)
            }
        }
        return fileToScan
    }
}