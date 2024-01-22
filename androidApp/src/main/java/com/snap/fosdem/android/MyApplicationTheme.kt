package com.snap.fosdem.android

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val provider = GoogleFont.Provider(
        providerAuthority = "com.google.android.gms.fonts",
        providerPackage = "com.google.android.gms",
        certificates = R.array.com_google_android_gms_fonts_certs
    )

    val fontName = GoogleFont("Signika")

    val signikaFamily = FontFamily(
        Font(googleFont = fontName, fontProvider = provider)
    )

    val colors = if (darkTheme) {
        darkColorScheme(
            primary = Color(0xFFBB86FC),
            secondary = Color(0xFF03DAC5),
            tertiary = Color(0xFF3700B3)
        )
    } else {
        lightColorScheme(
            primary = Color(0xFFAB1B93),
            secondary = Color(0xFF03DAC5),
            tertiary = Color(0xFF3700B3),
            background = Color.White,
            surfaceTint = Color(0xFFCCCCCC),
            surface = Color.White,
            surfaceVariant = Color(0xFFCCCCCC),
        )
    }
    val typography = Typography(
        bodyLarge = TextStyle(
            fontFamily = signikaFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = signikaFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 18.sp
        ),
        bodySmall = TextStyle(
            fontFamily = signikaFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp
        ),
        titleLarge = TextStyle(
            fontWeight = FontWeight.SemiBold,
            fontSize = 48.sp,
            fontFamily = signikaFamily
        ),
        titleMedium = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 28.sp,
            fontFamily = signikaFamily
        ),
        titleSmall = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 20.sp,
            fontFamily = signikaFamily
        )
    )
    val shapes = Shapes(
        small = RoundedCornerShape(4.dp),
        medium = RoundedCornerShape(4.dp),
        large = RoundedCornerShape(0.dp)
    )

    MaterialTheme(
        colorScheme = colors,
        typography = typography,
        shapes = shapes,
        content = content
    )
}
