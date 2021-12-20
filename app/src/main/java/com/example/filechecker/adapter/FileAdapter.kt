package com.example.filechecker.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.filechecker.R
import com.example.filechecker.data.FileData

class FileAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var filesList: ArrayList<FileData> = ArrayList()
    private var sourceList: ArrayList<FileData> = ArrayList()
    var onItemClick: ((FileData) -> Unit)? = null

    fun setUpFileList(filesList: List<FileData>) {
        this.sourceList.clear()
        this.sourceList.addAll(filesList)
        filter(query = "")
    }

    fun filter(query: String) {
        filesList.clear()
        sourceList.forEach {
            if (it.fileName.contains(query, ignoreCase = true)) {
                filesList.add(it)
            } else {
                it.filePath?.let { city ->
                    if (city.contains(query, ignoreCase = true))
                        filesList.add(it)
                }
            }
        }
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

        private var tvFileName = itemView.findViewById<TextView>(R.id.fileNameTextView)
        private var tvFilePath = itemView.findViewById<TextView>(R.id.filePathTextView)

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