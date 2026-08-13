package com.attiekeco.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val AttiekColorScheme = lightColorScheme(
    primary = AttiekGreen,
    onPrimary = Color.White,
    primaryContainer = AttiekGreenLight,
    onPrimaryContainer = AttiekGreenDark,

    secondary = AttiekOrange,
    onSecondary = Color.White,
    secondaryContainer = AttiekOrangeLight,
    onSecondaryContainer = AttiekOrangeDark,

    tertiary = AttiekBrown,
    onTertiary = Color.White,

    background = Color.White,
    onBackground = AttiekOnBackground,

    surface = Color.White,
    onSurface = AttiekOnBackground,
    surfaceVariant = AttiekSurfaceVariant,
    onSurfaceVariant = AttiekOnBackground
)

private val AttiekDarkColorScheme = darkColorScheme(
    primary = AttiekGreenLight,
    onPrimary = AttiekGreenDark,
    primaryContainer = AttiekGreen,
    onPrimaryContainer = AttiekGreenLight,

    secondary = AttiekOrangeLight,
    onSecondary = AttiekOrangeDark,
    secondaryContainer = AttiekOrange,
    onSecondaryContainer = AttiekOrangeLight,

    tertiary = AttiekBrown,
    onTertiary = Color.White,

    background = Color(0xFF0D1F12),
    onBackground = Color.White,

    surface = Color(0xFF0D1F12),
    onSurface = Color.White,
    surfaceVariant = Color(0xFF1A2E1D),
    onSurfaceVariant = AttiekSurfaceVariant
)

@Composable
fun AttiekEcoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) AttiekDarkColorScheme else AttiekColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AttiekTypography,
        content = content
    )
}
