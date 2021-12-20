package com.example.filechecker.screen

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.agoda.kakao.recycler.KRecyclerItem
import com.agoda.kakao.recycler.KRecyclerView
import com.agoda.kakao.screen.Screen
import com.agoda.kakao.text.KButton
import com.agoda.kakao.text.KTextView
import com.example.filechecker.R
import org.hamcrest.Matcher

class MainScreen : Screen<MainScreen>() {

    val etSearchFile: ViewInteraction = onView(withId(R.id.dataSearchEditText))
    val btnRefresh = KButton { withId(R.id.refreshButton) }

    //Issues list
    val rvFileList = KRecyclerView({
        withId(R.id.recyclerView)
    }, itemTypeBuilder = {
        itemType(MainScreen::ItemList)
    })

    class ItemList(parent: Matcher<View>) : KRecyclerItem<ItemList>(parent) {
        val tvFileName = KTextView(parent) { withId(R.id.fileNameTextView) }
        val tvFilePath = KTextView(parent) { withId(R.id.filePathTextView) }
    }
}