package com.wattpad.android.ui

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.wattpad.android.R
import com.wattpad.android.util.ToastMatcher
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * In MainActivityTest class I'm just checking if stories recycler view is displayed or not, and
 * checking if Toast text matches with predefined text.
 */

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class MainActivityTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Test
    fun testMainUI() {
        val mainActivityScenario = ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.recycler_view_stories))
            .check(matches(isDisplayed()))

        onView(withText(R.string.offline_message)).inRoot(ToastMatcher())
            .check(matches(isDisplayed()))

        onView(withText(R.string.offline_message)).inRoot(ToastMatcher())
            .check(matches(withText("The app can be used offline after the list is persisted")))
    }
}