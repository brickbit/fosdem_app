package com.snap.fosdem.android.extension

import android.content.Context
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.snap.fosdem.android.R
import com.snap.fosdem.android.c10BrushColor
import com.snap.fosdem.android.c1BrushColor
import com.snap.fosdem.android.c2BrushColor
import com.snap.fosdem.android.c3BrushColor
import com.snap.fosdem.android.c4BrushColor
import com.snap.fosdem.android.c5BrushColor
import com.snap.fosdem.android.c6BrushColor
import com.snap.fosdem.android.c7BrushColor
import com.snap.fosdem.android.c8BrushColor
import com.snap.fosdem.android.c9BrushColor
import com.snap.fosdem.android.transparentBrushColor

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

fun String?.toColorDark(): Color {
    return when(this) {
        "c1" -> Color(0xFF388694)
        "c2" -> Color(0xFF009A91)
        "c3" -> Color(0xFF808F3D)
        "c4" -> Color(0xFFE97B00)
        "c5" -> Color(0xFFF77400)
        "c6" -> Color(0xFFE19A28)
        "c7" -> Color(0xFFDB8044)
        "c8" -> Color(0xFFF99F0F)
        "c9" -> Color(0xFF5F9E43)
        "c10" -> Color(0xFF7C663C)
        else -> Color(0xFF000000)
    }
}

fun String?.toBrushColor(): Brush {
    return when(this) {
        "c1" -> Brush.verticalGradient(colorStops = c1BrushColor)
        "c2" -> Brush.verticalGradient(colorStops = c2BrushColor)
        "c3" -> Brush.verticalGradient(colorStops = c3BrushColor)
        "c4" -> Brush.verticalGradient(colorStops = c4BrushColor)
        "c5" -> Brush.verticalGradient(colorStops = c5BrushColor)
        "c6" -> Brush.verticalGradient(colorStops = c6BrushColor)
        "c7" -> Brush.verticalGradient(colorStops = c7BrushColor)
        "c8" -> Brush.verticalGradient(colorStops = c8BrushColor)
        "c9" -> Brush.verticalGradient(colorStops = c9BrushColor)
        "c10" -> Brush.verticalGradient(colorStops = c10BrushColor)
        else -> Brush.verticalGradient(colorStops = c1BrushColor)
    }
}

fun String.dayToTranslatable(context: Context): String {
    return if(this == "Saturday") {
        context.getString(R.string.schedule_saturday)
    } else {
        context.getString(R.string.schedule_sunday)
    }
}

fun String.dayFromTranslatable(context: Context): String {
    return if(this == context.getString(R.string.schedule_saturday)) {
        "Saturday"
    } else {
        "Sunday"
    }
}

fun String.allToTranslatable(context: Context): String {
    return context.getString(R.string.schedule_all)
}

fun String.allFromTranslatable(context: Context): String {
    return "All"
}