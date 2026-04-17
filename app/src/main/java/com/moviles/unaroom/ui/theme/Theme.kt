package com.moviles.unaroom.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = AppPrimary,
    onPrimary = AppBackground,
    background = AppBackground,
    onBackground = AppPrimary,
    surface = AppSurface,
    onSurface = AppPrimary
)

private val LightColorScheme = lightColorScheme(
    primary = AppPrimary,
    onPrimary = AppBackground,
    background = AppBackground,
    onBackground = AppPrimary,
    surface = AppSurface,
    onSurface = AppPrimary,
    surfaceVariant = AppSurfaceVariant,
    onSurfaceVariant = AppSecondaryText,
    outline = AppBorder,
    error = AppError
)

@Composable
fun UnaRoomTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
