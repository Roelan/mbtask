package com.example.filechecker.page

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import androidx.test.uiautomator.Until
import io.qameta.allure.kotlin.Allure.step

class AlertPage {

    /**
     * Wait UI element for 5 sec
     * Click on the UI element
     * @param uiElementName: name of the UI element
     */
    fun waitAndClickUIElement(uiElementName: String, timeOut: Long = 5000) {
        step("Wait UI element: $uiElementName for 5 sec and click on it") {
            val uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
            uiDevice.wait(Until.hasObject(By.text(uiElementName)), timeOut)
            val view = uiDevice.findObject(UiSelector().text(uiElementName))
            view.click()
        }
    }

    /**
     * Wait until the text appears on the screen
     * @param screenText: the text that is displayed on the screen
     */
    fun checkScreenText(screenText: String, timeOut: Long = 10000) {
        step("Wait until text appears on the screen: $screenText") {
            val uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
            uiDevice.wait(Until.hasObject(By.text(screenText)), timeOut)
            Espresso.onView(ViewMatchers.withText(screenText))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        }
    }
}