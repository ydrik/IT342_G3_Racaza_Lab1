package com.annimemo

import android.app.Application

class AnniMemoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize application-level components here
        // - Firebase
        // - Logging
        // - Analytics
        // - Crash reporting
    }
}
