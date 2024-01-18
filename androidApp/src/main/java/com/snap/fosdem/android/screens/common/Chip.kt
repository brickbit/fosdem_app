package com.snap.fosdem.android.screens.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.snap.fosdem.android.mainBrushColor

@Composable
fun Chip(
    title: String
) {
    Text(
        modifier = Modifier
            .border(
                border = BorderStroke(
                    width = 2.dp,
                    brush = Brush.linearGradient(colorStops = mainBrushColor)
                ),
                shape = RoundedCornerShape(50)
            )
            .padding(vertical = 4.dp, horizontal = 8.dp),
        text = title,
        style = MaterialTheme.typography.bodySmall.copy(MaterialTheme.colorScheme.primary)
    )
}