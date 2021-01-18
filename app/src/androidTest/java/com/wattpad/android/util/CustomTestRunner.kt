package com.wattpad.android.util

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication

/**
 * Purpose of creating this class is to use the Hilt test application in instrumented tests,
 * so for this I need to configure a new test runner. This makes Hilt work for all of the
 * instrumented tests in this project.
 */


class CustomTestRunner : AndroidJUnitRunner() {

    override fun newApplication(cl: ClassLoader?, name: String?, context: Context?): Application {
        return super.newApplication(cl, HiltTestApplication::class.java.name, context)
    }
}