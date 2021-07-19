package com.example.filechecker.provider

import org.reactivestreams.Publisher
import org.reactivestreams.Subscriber
import java.io.File

class FileLister(private val directory: File) : Publisher<String> {

    private lateinit var subscriber: Subscriber<in String>

    override fun subscribe(s: Subscriber<in String>?) {
        if (s == null) {
            return
        }
        this.subscriber = s
        this.listFiles(this.directory)
        this.subscriber.onComplete()
    }

    /**
     * Recursively list files from a given directory.
     */
    private fun listFiles(directory: File) {
        val files = directory.listFiles()
        if (files != null) {
            for (file in files) {
                if (file != null) {
                    if (file.isDirectory) {
                        listFiles(file)
                    } else {
                        subscriber.onNext(file.absolutePath)
                    }
                }
            }
        }
    }
}