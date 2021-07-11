package com.example.filechecker.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.filechecker.R
import com.example.filechecker.data.FileData

class FileAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val filesList: List<FileData> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.cell_file_atributs_item, parent, false)
        return FileViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is FileViewHolder) {
            holder.bind(filesList[position])
        }
    }

    override fun getItemCount(): Int {
        return filesList.size
    }

    class FileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var tvFileName = itemView.findViewById<TextView>(R.id.file_name)
        private var tvFilePath = itemView.findViewById<TextView>(R.id.file_path)

        fun bind (fileData: FileData) {
            tvFileName.text = fileData.fileName
            tvFilePath.text = fileData.filePath
        }
    }
}