package com.yunta.app

import android.app.Application
import com.yunta.app.di.AppContainer

class YuntaApplication : Application() {
    val container: AppContainer by lazy { AppContainer() }
}
