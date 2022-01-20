package com.example.filechecker.utils

import android.os.Environment
import androidx.test.platform.app.InstrumentationRegistry
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

    /**
     * Get string value from json file.
     */
    fun getJson(jsonName: String): String {
        // Load the JSON response
        return try {
            if ("".equals(jsonName)) {
                return "" // If jsonName empty, set empty body to the request or response
            } else {
                val inputStream = InstrumentationRegistry.getInstrumentation().context.assets.open("mocks/$jsonName")
                inputStream.bufferedReader().use { it.readText() }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            return "ERROR: no JSON file."
        }
    }
}