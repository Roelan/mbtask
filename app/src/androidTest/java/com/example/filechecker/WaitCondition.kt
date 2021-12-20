package com.example.filechecker

import androidx.test.espresso.NoMatchingViewException
import com.agoda.kakao.recycler.KRecyclerView
import com.agoda.kakao.screen.Screen.Companion.idle
import com.agoda.kakao.text.KTextView
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
 * Wait until text View isVisible
 */
fun waitUntilVisible(textView: KTextView) {
    waitCondition(timeout = 120) { isMyLabelIsVisible(textView) }
}

private fun isMyLabelIsVisible(textView: KTextView): Boolean {
    return try {
        textView.isVisible()
        true
    } catch (e: AssertionFailedError) {
        false
    } catch (e: NoMatchingViewException) {
        false
    }
}

/**
 * Wait until recycler View isVisible
 */
fun waitUntilVisible(recyclerView: KRecyclerView) {
    waitCondition(timeout = 120) { isMyLabelIsVisible(recyclerView) }
}

private fun isMyLabelIsVisible(recyclerView: KRecyclerView): Boolean {
    return try {
        recyclerView.isVisible()
        true
    } catch (e: AssertionFailedError) {
        false
    } catch (e: NoMatchingViewException) {
        false
    }
}
