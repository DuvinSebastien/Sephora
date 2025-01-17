package com.example.sephora.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext


private val SephoraLightColors = lightColorScheme(
    primary = SephoraColors.ClassicRed,
    secondary = SephoraColors.Gold,
    background = SephoraColors.White,
    surface = SephoraColors.Silver,
    onPrimary = SephoraColors.White,
    onSecondary = SephoraColors.Black,
    onBackground = SephoraColors.CharcoalGray,
    onSurface = SephoraColors.CharcoalGray
)

private val SephoraDarkColors = darkColorScheme(
    primary = SephoraColors.Gold,
    secondary = SephoraColors.ClassicRed,
    background = SephoraColors.Black,
    surface = SephoraColors.CharcoalGray,
    onPrimary = SephoraColors.Black,
    onSecondary = SephoraColors.White,
    onBackground = SephoraColors.White,
    onSurface = SephoraColors.White
)

@Composable
fun SephoraTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> SephoraDarkColors
        else -> SephoraLightColors
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}