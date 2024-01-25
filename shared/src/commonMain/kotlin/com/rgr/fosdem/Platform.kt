package com.rgr.fosdem

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform