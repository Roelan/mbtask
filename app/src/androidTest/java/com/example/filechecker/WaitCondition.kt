package com.example.filechecker

import androidx.test.espresso.NoMatchingViewException
import com.agoda.kakao.common.assertions.BaseAssertions
import com.agoda.kakao.recycler.KRecyclerView
import com.agoda.kakao.screen.Screen.Companion.idle
import com.agoda.kakao.text.KTextView
import com.example.filechecker.logger.L
import com.example.filechecker.qa.TestApp.TAG
import junit.framework.AssertionFailedError

const val DEFAULT_TIMEOUT = 10000
const val DEFAULT_INTERVAL = 250

private fun waitCondition(
    timeout: Int = DEFAULT_TIMEOUT,
    interval: Int = DEFAULT_INTERVAL,
    desc: String = "",
    conditionCheck: () -> Boolean
) {
    val timeoutMs = timeout * 1000
    var elapsed = 0
    while (!conditionCheck()) {
        if (elapsed >= timeoutMs) throw RuntimeException("$desc took more than $timeout seconds. Test stopped.")
        idle(interval.toLong())
        elapsed += interval
    }
}

/**
 * Wait until Image View isVisible
 */
fun waitUntilVisible(view: BaseAssertions) {
    L.i(TAG, "WaitCondition - Wait 120s until view is Visible")
    waitCondition(timeout = 120) { isMyLabelIsVisible(view) }
}

private fun isMyLabelIsVisible(view: BaseAssertions): Boolean {
    return try {
        view.isVisible()
        true
    } catch (e: AssertionFailedError) {
        false
    } catch (e: NoMatchingViewException) {
        false
    }
}
