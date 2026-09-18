package com.example.moodtracker

import android.app.Application
import com.di.initKoin

class MoodTrackerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
    }
}