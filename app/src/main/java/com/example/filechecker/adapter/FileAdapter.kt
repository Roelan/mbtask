package com.example.filechecker.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.filechecker.R
import com.example.filechecker.data.FileData

class FileAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var filesList: List<FileData> = ArrayList()
    var onItemClick: ((FileData) -> Unit)? = null

    fun setUpFileList(filesList: List<FileData>) {
        this.filesList = filesList
        notifyDataSetChanged()
    }

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

    inner class FileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var tvFileName = itemView.findViewById<TextView>(R.id.file_name)
        private var tvFilePath = itemView.findViewById<TextView>(R.id.file_path)

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(filesList[adapterPosition])
            }
        }

        fun bind(myFileData: FileData) {
            tvFileName.text = myFileData.fileName
            tvFilePath.text = myFileData.filePath
        }
    }
}