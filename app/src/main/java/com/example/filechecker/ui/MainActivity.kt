package com.example.filechecker.ui

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.filechecker.R
import com.example.filechecker.adapter.FileAdapter
import com.example.filechecker.provider.FileLister
import com.example.filechecker.provider.SaveFilesData
import com.example.filechecker.provider.SetListOfFiles
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class MainActivity : AppCompatActivity() {

    private lateinit var adapter: FileAdapter
    private val requestCode = 100
    private var disposable: Disposable? = null
    private lateinit var rvComments: RecyclerView
    private val getFileName = SetListOfFiles()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkGrantedPermission()
        setupRecyclerAdapter()
    }

    override fun onPause() {
        super.onPause()
        this.disposable?.dispose()
    }

    private fun setupRecyclerAdapter() {
        adapter = FileAdapter()
        rvComments = findViewById(R.id.recycler_view)
        rvComments.layoutManager = LinearLayoutManager(this)
        rvComments.setHasFixedSize(true)
        rvComments.adapter = adapter
        adapter.setUpFileList(getFileName.getList())
        adapter.onItemClick = { fileData ->
            Log.d("TAG", fileData.fileName)

            val fragment = FileInfoFragment.getNewInstance(fileData)
            fragment.show(supportFragmentManager, fragment.tag)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_filter_name -> adapter.filterList("Name")
            R.id.menu_filter_size -> adapter.filterList("Size")
            R.id.menu_filter_modify -> adapter.filterList("Modify")
            R.id.menu_save -> checkGrantedWrightPermission()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkGrantedPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                requestCode
            )
        } else {
            listExternalStorage()
        }
    }

    private fun checkGrantedWrightPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                requestCode
            )
        } else {
            val saveFilesData = SaveFilesData()
            saveFilesData.save(adapter.getFilesList())
            Toast.makeText(this, "File saved!", Toast.LENGTH_LONG).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == this.requestCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                listExternalStorage()
            } else {
                Toast.makeText(this, "Until you grant the permission, I cannot list the files", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun listExternalStorage() {
        val state = Environment.getExternalStorageState()

        if (Environment.MEDIA_MOUNTED == state || Environment.MEDIA_MOUNTED_READ_ONLY == state) {

            this.disposable =
                Observable.fromPublisher(FileLister(Environment.getExternalStorageDirectory()))
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        getFileName.setList(it)
                    }, {
                        Log.e("MainActivity", "Error in listing files from the SD card", it)
                    }, {
                        Toast.makeText(this, "Successfully listed all the files!", Toast.LENGTH_SHORT).show()
                        this.disposable?.dispose()
                        this.disposable = null
                    })
        }
    }
}