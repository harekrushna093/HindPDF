package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = HindRedPrimary,
    onPrimary = Color.White,
    primaryContainer = HindDarkSurface,
    onPrimaryContainer = Color.White,
    secondary = HindGoldPro,
    onSecondary = Color.Black,
    tertiary = HindBlueAccent,
    background = HindDarkBg,
    onBackground = Color.White,
    surface = HindDarkCard,
    onSurface = Color.White,
    surfaceVariant = Color(0xFF27272A),
    onSurfaceVariant = Color(0xFFA1A1AA),
    outline = Color(0xFF3F3F46)
  )

private val LightColorScheme =
  lightColorScheme(
    primary = HindRedPrimary,
    onPrimary = Color.White,
    primaryContainer = HindRedLight,
    onPrimaryContainer = HindRedDark,
    secondary = HindGoldProDark,
    onSecondary = Color.White,
    secondaryContainer = HindGoldLight,
    onSecondaryContainer = Color(0xFF78350F),
    tertiary = HindBlueAccent,
    background = HindLightBg,
    onBackground = HindTextMain,
    surface = HindLightCard,
    onSurface = HindTextMain,
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = HindTextMuted,
    outline = HindLightBorder
  )

@Composable
fun HindPDFTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Preserve brand identity by default
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }
      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}

