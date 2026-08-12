package com.attiekeco.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = Orange80,
    secondary = Green80,
    tertiary = OrangeGrey80,
    background = SurfaceDark,
    surface = SurfaceDark,
    onPrimary = Black,
    onSecondary = Black,
    onBackground = White,
    onSurface = White,
    error = Error
)

private val LightColorScheme = lightColorScheme(
    primary = Orange40,
    secondary = Green40,
    tertiary = OrangeGrey40,
    background = White,
    surface = SurfaceLight,
    onPrimary = White,
    onSecondary = White,
    onTertiary = White,
    onBackground = OnSurface,
    onSurface = OnSurface,
    error = Error
)

@Composable
fun AttiekEcoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
