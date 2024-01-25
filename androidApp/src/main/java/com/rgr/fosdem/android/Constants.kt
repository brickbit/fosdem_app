package com.rgr.fosdem.android

import android.content.Context
import android.content.res.Configuration
import androidx.compose.ui.graphics.Color
import java.security.AccessController.getContext


val mainBrushColor = arrayOf(
    0.0f to Color(0xFFAB1B93),
    0.4f to Color(0xFF8D0E87),
    1f to Color(0xFF620072)
)

fun transparentBrushColor(context: Context) = if(context.resources.configuration.uiMode and
    Configuration.UI_MODE_NIGHT_MASK != Configuration.UI_MODE_NIGHT_YES) {
    arrayOf(
        0.0f to Color.White.copy(alpha = 0.1f),
        0.35f to Color.White.copy(alpha = 0.7f),
        1f to Color.White.copy(alpha = 1f)
    )
} else {
    arrayOf(
        0.0f to Color(0xFF1C1B1F).copy(alpha = 0.1f),
        0.35f to Color(0xFF1C1B1F).copy(alpha = 0.7f),
        1f to Color(0xFF1C1B1F).copy(alpha = 1f)
    )
}

fun transparentBrushColorReversed(context: Context) = if(context.resources.configuration.uiMode and
    Configuration.UI_MODE_NIGHT_MASK != Configuration.UI_MODE_NIGHT_YES) {
    arrayOf(
        0.0f to Color.White.copy(alpha = 1f),
        0.9f to Color.White.copy(alpha = 0.88f),
        1f to Color.White.copy(alpha = 0.1f)
    )
} else {
    arrayOf(
        0.0f to Color(0xFF1C1B1F).copy(alpha = 1f),
        0.9f to Color(0xFF1C1B1F).copy(alpha = 0.88f),
        1f to Color(0xFF1C1B1F).copy(alpha = 0.1f)
    )
}

val c1BrushColor = arrayOf(
    0.0f to Color(0xFF69D2E7).copy(alpha = 1f),
    0.35f to Color(0xFF47AFC6).copy(alpha = 1f),
    1f to Color(0xFF388694).copy(alpha = 1f)
)

val c2BrushColor = arrayOf(
    0.0f to Color(0xFFA7DBD8).copy(alpha = 1f),
    0.35f to Color(0xFF2CACA5).copy(alpha = 1f),
    1f to Color(0xFF009A91).copy(alpha = 1f)
)

val c3BrushColor = arrayOf(
    0.0f to Color(0xFFB4CDA8).copy(alpha = 1f),
    0.35f to Color(0xFF72A154).copy(alpha = 1f),
    1f to Color(0xFF5B8142).copy(alpha = 1f)
)

val c4BrushColor = arrayOf(
    0.0f to Color(0xFFEDBB1E).copy(alpha = 1f),
    0.35f to Color(0xFFECA311).copy(alpha = 1f),
    1f to Color(0xFFE97B00).copy(alpha = 1f)
)

val c5BrushColor = arrayOf(
    0.0f to Color(0xFFF9B900).copy(alpha = 1f),
    0.35f to Color(0xFFF8A000).copy(alpha = 1f),
    1f to Color(0xFFF77400).copy(alpha = 1f)
)

val c6BrushColor = arrayOf(
    0.0f to Color(0xFFE8C737).copy(alpha = 1f),
    0.35f to Color(0xFFE4B030).copy(alpha = 1f),
    1f to Color(0xFFE19A28).copy(alpha = 1f)
)

val c7BrushColor = arrayOf(
    0.0f to Color(0xFFEEB857).copy(alpha = 1f),
    0.35f to Color(0xFFE7A350).copy(alpha = 1f),
    1f to Color(0xFFDB8044).copy(alpha = 1f)
)

val c8BrushColor = arrayOf(
    0.0f to Color(0xFFFCC777).copy(alpha = 1f),
    0.35f to Color(0xFFFAB040).copy(alpha = 1f),
    1f to Color(0xFFF99F0F).copy(alpha = 1f)
)

val c9BrushColor = arrayOf(
    0.0f to Color(0xFFC3DBB9).copy(alpha = 1f),
    0.35f to Color(0xFF79AE62).copy(alpha = 1f),
    1f to Color(0xFF5F9E43).copy(alpha = 1f)
)

val c10BrushColor = arrayOf(
    0.0f to Color(0xFFA79474).copy(alpha = 1f),
    0.35f to Color(0xFF917D58).copy(alpha = 1f),
    1f to Color(0xFF7C663C).copy(alpha = 1f)
)