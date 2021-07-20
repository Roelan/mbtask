package com.example.filechecker.ui.fileslist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.filechecker.R
import com.example.filechecker.adapter.FileAdapter
import com.example.filechecker.ui.fileinfo.FileInfoFragment

class FilesListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FileAdapter

    companion object {
        fun newInstance() = FilesListFragment()
    }

    private lateinit var viewModel: FilesListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v = inflater.inflate(R.layout.files_list_fragment, container, false)
        setHasOptionsMenu(true)
        recyclerView = v.findViewById(R.id.recyclerView)
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FilesListViewModel::class.java)
        viewModel.setFileListArray()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        adapter = FileAdapter()
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
        adapter.setUpFileList(viewModel.getFileListArray())
        adapter.onItemClick = { fileData ->
            Log.d("TAG", "${fileData.fileName} - was clicked.")
            val fragment = FileInfoFragment.getNewInstance(fileData)
            activity?.let { fragment.show(it.supportFragmentManager, fragment.tag) }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_filter_name -> adapter.setUpFileList(viewModel.getSortedListByName())
            R.id.menu_filter_size -> adapter.setUpFileList(viewModel.getSortedListBySize())
            R.id.menu_filter_modify -> adapter.setUpFileList(viewModel.getSortedListByDate())
            R.id.menu_save -> viewModel.saveFileListData()
        }
        return super.onOptionsItemSelected(item)
    }
}