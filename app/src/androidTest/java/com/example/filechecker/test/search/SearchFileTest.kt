package com.example.filechecker.test.search

import com.example.filechecker.utils.ForAssetFiles
import com.example.filechecker.test.BaseTest
import org.junit.Before
import org.junit.Test

class SearchFileTest : BaseTest() {

    @Before
    fun before() {
        val forAssetFiles = ForAssetFiles()
        forAssetFiles.extractFileFromAsset("assets", "MyFile.txt")
    }

    @Test
    fun searchFileTest() {
        mainPage.verifyDisplay()
        mainPage.enterSearchQuery("MyFile.txt")
        mainPage.clickRefreshButton()
        mainPage.clickListItemByText("MyFile")
    }
}