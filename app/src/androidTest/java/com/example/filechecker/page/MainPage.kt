package com.example.filechecker.page

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.example.filechecker.screen.MainScreen
import com.example.filechecker.waitUntilVisible
import io.qameta.allure.kotlin.Allure.step

class MainPage : BasePage {

    private val mainScreen = MainScreen()

    override fun verifyDisplay() {
        step("MainPage: Verify display") {
            with(mainScreen) {
                waitUntilVisible(rvFileList)
                etSearchFile.check(matches(isDisplayed()))
                btnRefresh.isVisible()
            }
        }
    }

    fun enterSearchQuery(txt: String) {
        step("MainPage: Enter search query: $txt") {
            mainScreen.etSearchFile.perform(ViewActions.click(), ViewActions.replaceText(txt))
            mainScreen.etSearchFile.check(matches(ViewMatchers.withText(txt)))
            Espresso.closeSoftKeyboard()
        }
    }

    fun clickRefreshButton() {
        step("MainPage: Click on refresh button") {
            mainScreen.btnRefresh.click()
        }
    }

    fun clickListItemByText(txt: String) {
        step("MainPage: Click on list item") {
            mainScreen.rvFileList {
                isVisible()
                childWith<MainScreen.ItemList> {
                    withDescendant { containsText(txt) }
                } perform {
                    tvFileName.isVisible()
                    tvFilePath.isVisible()
                    tvFileName.click()
                }
            }
        }
    }

    fun clickListItemByPosition(pos: Int, fileName: String) {
        step("MainPage: Click item in the List by position: $pos") {
            mainScreen.rvFileList {
                childAt<MainScreen.ItemList>(pos) {
                    tvFilePath {
                        isVisible()
                        containsText(fileName)
                    }
                    tvFileName {
                        isVisible()
                        containsText(fileName)
                        click()
                    }
                }
            }
        }
    }
}