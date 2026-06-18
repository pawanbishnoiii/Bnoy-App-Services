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

private val PremiumColorScheme =
  lightColorScheme(
    primary = PinkPrimary,
    onPrimary = PureWhite,
    primaryContainer = PinkLight,
    onPrimaryContainer = PinkPrimary,
    secondary = SlateMedium,
    onSecondary = PureWhite,
    secondaryContainer = SlateLight,
    onSecondaryContainer = SlateDark,
    background = PureWhite,
    onBackground = SlateDark,
    surface = PureWhite,
    onSurface = SlateDark,
    surfaceVariant = SlateLight,
    onSurfaceVariant = SlateMedium,
    error = Color(0xFFEF4444),
    onError = PureWhite
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = false, // Keep light as default for the Airbnb premium feel
  dynamicColor: Boolean = false, // Disable to force our beautiful pink/white theme
  content: @Composable () -> Unit,
) {
  val colorScheme = PremiumColorScheme

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
