package com.snap.fosdem

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform