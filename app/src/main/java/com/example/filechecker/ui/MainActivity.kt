package com.example.filechecker.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.filechecker.R
import com.example.filechecker.ui.fileslist.FilesListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        requestPermissions()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, FilesListFragment.newInstance())
                .commitNow()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0 && grantResults.isNotEmpty()) {
            for (i in grantResults.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("TAG", "${permissions[i]} granted.")
                }
            }
        }
    }

    private fun hasWriteExternalStoragePermission() =
        ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

    private fun hasReadExternalStoragePermission() =
        ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

    private fun requestPermissions() {
        Log.d("TAG", "Request for permissions")
        val permissionsToRequest = mutableListOf<String>()
        if (!hasReadExternalStoragePermission()) permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        if (!hasWriteExternalStoragePermission()) permissionsToRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (permissionsToRequest.isNotEmpty()) ActivityCompat.requestPermissions(this, permissionsToRequest.toTypedArray(), 0)
    }
}