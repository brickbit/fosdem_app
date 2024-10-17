package com.rgr.fosdem.android.screens.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.rgr.fosdem.android.R
import com.rgr.fosdem.android.mainBrushColor

@Composable
fun SelectableChip(
    title: String,
    isActive: Boolean,
    onClick: (String) -> Unit
) {
    if(isActive) {
        Row(
            modifier = Modifier
                .background(
                    brush = Brush.linearGradient(colorStops = mainBrushColor),
                    shape = RoundedCornerShape(50)
                )
                .padding(vertical = 4.dp, horizontal = 8.dp)
                .clickable { onClick(title) },
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(Color.White)
            )
            Image(
                painter = painterResource(id = R.drawable.ic_close),
                contentDescription = null,
                colorFilter = ColorFilter.tint(Color.White)
            )
        }
    } else {
        Text(
            modifier = Modifier
                .clickable { onClick(title) }
                .border(
                    border = BorderStroke(width = 2.dp, color = Color.Gray),
                    shape = RoundedCornerShape(50)
                )
                .padding(vertical = 4.dp, horizontal = 8.dp),
            text = title,
            style = MaterialTheme.typography.bodyMedium.copy(Color.Gray)
        )
    }
}