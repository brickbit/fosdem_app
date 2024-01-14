package com.snap.fosdem.android.extension

import androidx.compose.ui.graphics.Color

fun String?.toColor(): Color {
    return when(this) {
        "c1" -> Color(0xFF69D2E7)
        "c2" -> Color(0xFFA7DBD8)
        "c3" -> Color(0xFFE0E4CC)
        "c4" -> Color(0xFFEF8630)
        "c5" -> Color(0xFFFA6900)
        "c6" -> Color(0xFFEA813D)
        "c7" -> Color(0xFFE7A350)
        "c8" -> Color(0xFFFCC777)
        "c9" -> Color(0xFFC3DBB9)
        "c10" -> Color(0xFFA79474)
        else -> Color(0xFFFFFFFF)
    }
}