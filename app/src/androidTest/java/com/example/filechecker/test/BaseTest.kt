package com.example.filechecker.test

import android.Manifest
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import com.example.filechecker.page.MainPage
import com.example.filechecker.ui.MainActivity
import org.junit.Rule

abstract class BaseTest {

    @Rule
    @JvmField
    var rule = ActivityTestRule(MainActivity::class.java, true, true)

    @Rule
    @JvmField
    val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    @Rule
    @JvmField
    val permissionRuleRead: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.READ_EXTERNAL_STORAGE)

    val mainPage = MainPage()
}