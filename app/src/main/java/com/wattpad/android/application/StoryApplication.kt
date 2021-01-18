package com.wattpad.android.application

import android.app.Application
import dagger.hilt.android.HiltAndroidApp


/**
 * As I use Hilt dependency injection framework for creating and injecting Objects in the app so,
 * the project should has Application class which annotated with {@HiltAndroidApp}
 */

@HiltAndroidApp
class StoryApplication :Application()