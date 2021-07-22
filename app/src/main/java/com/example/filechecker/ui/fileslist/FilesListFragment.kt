package com.example.filechecker.ui.fileslist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.filechecker.R
import com.example.filechecker.adapter.FileAdapter
import com.example.filechecker.ui.fileinfo.FileInfoFragment
import kotlinx.android.synthetic.main.files_list_fragment.*


class FilesListFragment : Fragment(R.layout.files_list_fragment) {

    private lateinit var adapter: FileAdapter
    private val viewModel by viewModels<FilesListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v = inflater.inflate(R.layout.files_list_fragment, container, false)
        setHasOptionsMenu(true)
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.initFileListArray()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        adapter = FileAdapter()
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
        viewModel.getFileListArray().observe(viewLifecycleOwner, { fileList -> adapter.setUpFileList(fileList) })
        adapter.onItemClick = { fileData ->
            Log.d("TAG", "${fileData.fileName} - was clicked.")
            val fragment = FileInfoFragment.getNewInstance(fileData)
            activity?.let { fragment.show(it.supportFragmentManager, fragment.tag) }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_filter_name -> viewModel.getSortedListByName().observe(viewLifecycleOwner, { fileList -> adapter.setUpFileList(fileList)})
            R.id.menu_filter_size -> viewModel.getSortedListBySize().observe(viewLifecycleOwner, { fileList -> adapter.setUpFileList(fileList)})
            R.id.menu_filter_modify -> viewModel.getSortedListByDate().observe(viewLifecycleOwner, { fileList -> adapter.setUpFileList(fileList)})
            R.id.menu_save -> viewModel.saveFileListData()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        fun newInstance() = FilesListFragment()
    }
}