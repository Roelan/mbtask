package com.example.filechecker.ui.fileinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.filechecker.R
import com.example.filechecker.data.FileData
import kotlinx.android.synthetic.main.file_info_fragment.*

class FileInfoFragment : DialogFragment() {

    companion object {
        fun getNewInstance(fileData: FileData) : FileInfoFragment {
            val fileInfoFragment = FileInfoFragment()
            val args = Bundle()
            args.putParcelable("fileData", fileData)
            fileInfoFragment.arguments = args
            return fileInfoFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v: View = inflater.inflate(R.layout.file_info_fragment, container, false)
        val myFileData : FileData = arguments?.getParcelable("fileData")!!

        fileNameText.text = myFileData.fileName
        fileSizeText.text = myFileData.fileSize
        filePathText.text = myFileData.filePath
        fileLastModifiedText.text = myFileData.lastModified
        closeButton.setOnClickListener { dismiss() }
        return v
    }

}