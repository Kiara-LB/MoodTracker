package com.example.moodtracker

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform