package ir.amirhesambandegan.gitlens.ui.theme

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

// ─────────────────────────────────────────────
// GitLens Full Blue-Harmony Palette
// ─────────────────────────────────────────────

private val BrandBlue = Color(0xFF1565C0)
private val BrandBlueLight = Color(0xFF42A5F5)
private val BrandBlueDark = Color(0xFF0D47A1)

private val SkyBlue = Color(0xFF0288D1)
private val SkyBlueLight = Color(0xFF4FC3F7)
private val SkyBlueDark = Color(0xFF01579B)


// ─────────────────────────────────────────────
// Dark Theme (Complete M3 Surface Hierarchy)
// ─────────────────────────────────────────────

private val DarkColorScheme = darkColorScheme(
    primary = BrandBlueLight,
    onPrimary = Color(0xFF0A192F),
    primaryContainer = BrandBlueDark,
    onPrimaryContainer = Color(0xFFE3F2FD),
    inversePrimary = BrandBlue,

    secondary = SkyBlueLight,
    onSecondary = Color(0xFF002F44),
    secondaryContainer = SkyBlueDark,
    onSecondaryContainer = Color(0xFFE1F5FE),

    tertiary = Color(0xFF81D4FA),
    onTertiary = Color(0xFF0A192F),
    tertiaryContainer = Color(0xFF004C6D),
    onTertiaryContainer = Color(0xFFE1F5FE),

    background = Color(0xFF0A1118),
    onBackground = Color(0xFFE2E8F0),

    // Complete Material 3 Surface Hierarchy
    surface = Color(0xFF101D2C),
    onSurface = Color(0xFFE2E8F0),
    surfaceVariant = Color(0xFF1B2A3D),
    onSurfaceVariant = Color(0xFF90A4AE),
    inverseSurface = Color(0xFFE2E8F0),
    inverseOnSurface = Color(0xFF101D2C),

    surfaceDim = Color(0xFF080E14),
    surfaceBright = Color(0xFF1A2A3E),
    surfaceContainerLowest = Color(0xFF05090F),
    surfaceContainerLow = Color(0xFF0E1824),
    surfaceContainer = Color(0xFF132030),
    surfaceContainerHigh = Color(0xFF19283B),
    surfaceContainerHighest = Color(0xFF203248),

    outline = Color(0xFF37474F),
    outlineVariant = Color(0xFF263238),
    scrim = Color(0xFF000000),

    error = Color(0xFFEF5350),
    onError = Color(0xFF3B0000),
    errorContainer = Color(0xFF751C1A),
    onErrorContainer = Color(0xFFFFDAD6),
)


// ─────────────────────────────────────────────
// Light Theme (Complete M3 Surface Hierarchy)
// ─────────────────────────────────────────────

private val LightColorScheme = lightColorScheme(
    primary = BrandBlue,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE3F2FD),
    onPrimaryContainer = Color(0xFF0D47A1),
    inversePrimary = BrandBlueLight,

    secondary = SkyBlue,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE1F5FE),
    onSecondaryContainer = Color(0xFF01579B),

    tertiary = Color(0xFF0288D1),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFE1F5FE),
    onTertiaryContainer = Color(0xFF01579B),

    background = Color(0xFFF4F7FB),
    onBackground = Color(0xFF102A43),

    // Complete Material 3 Surface Hierarchy
    surface = Color.White,
    onSurface = Color(0xFF102A43),
    surfaceVariant = Color(0xFFEBF1F5),
    onSurfaceVariant = Color(0xFF486581),
    inverseSurface = Color(0xFF1B2A3D),
    inverseOnSurface = Color(0xFFF4F7FB),

    surfaceDim = Color(0xFFD9E2EC),
    surfaceBright = Color(0xFFFFFFFF),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFF8FAFC),
    surfaceContainer = Color(0xFFEEF2F7),
    surfaceContainerHigh = Color(0xFFE6ECF4),
    surfaceContainerHighest = Color(0xFFDFE6EF),

    outline = Color(0xFFBCCCDC),
    outlineVariant = Color(0xFFD9E2EC),
    scrim = Color(0xFF000000),

    error = Color(0xFFD32F2F),
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
)


// ─────────────────────────────────────────────
// GitLens Theme
// ─────────────────────────────────────────────

@Composable
fun GitLensTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current

            if (darkTheme) {
                dynamicDarkColorScheme(context)
            } else {
                dynamicLightColorScheme(context)
            }
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
    )
}