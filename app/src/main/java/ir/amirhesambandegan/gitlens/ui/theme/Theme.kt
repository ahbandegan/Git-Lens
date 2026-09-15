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
// GitLens Colors
// ─────────────────────────────────────────────

private val Indigo = Color(0xFF4F46E5)
private val IndigoLight = Color(0xFF6366F1)
private val IndigoDark = Color(0xFF3730A3)

private val Cyan = Color(0xFF06B6D4)
private val CyanLight = Color(0xFF22D3EE)
private val CyanDark = Color(0xFF0891B2)

private val Purple = Color(0xFF8B5CF6)


// ─────────────────────────────────────────────
// Dark Theme
// ─────────────────────────────────────────────

private val DarkColorScheme = darkColorScheme(
    primary = IndigoLight,
    onPrimary = Color.White,

    primaryContainer = IndigoDark,
    onPrimaryContainer = Color(0xFFE0E7FF),

    secondary = CyanLight,
    onSecondary = Color(0xFF00363D),

    secondaryContainer = CyanDark,
    onSecondaryContainer = Color(0xFFB6F4FF),

    tertiary = Purple,
    onTertiary = Color.White,

    background = Color(0xFF050816),
    onBackground = Color(0xFFF8FAFC),

    surface = Color(0xFF0F172A),
    onSurface = Color(0xFFF8FAFC),

    surfaceVariant = Color(0xFF172033),
    onSurfaceVariant = Color(0xFFCBD5E1),

    outline = Color(0xFF334155),
    outlineVariant = Color(0xFF1E293B),

    error = Color(0xFFFF6B6B),
    onError = Color.White,
)


// ─────────────────────────────────────────────
// Light Theme
// ─────────────────────────────────────────────

private val LightColorScheme = lightColorScheme(
    primary = Indigo,
    onPrimary = Color.White,

    primaryContainer = Color(0xFFE0E7FF),
    onPrimaryContainer = Color(0xFF1E1B4B),

    secondary = CyanDark,
    onSecondary = Color.White,

    secondaryContainer = Color(0xFFCFFAFE),
    onSecondaryContainer = Color(0xFF083344),

    tertiary = Color(0xFF7C3AED),
    onTertiary = Color.White,

    background = Color(0xFFF8FAFC),
    onBackground = Color(0xFF0F172A),

    surface = Color.White,
    onSurface = Color(0xFF0F172A),

    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = Color(0xFF475569),

    outline = Color(0xFFCBD5E1),
    outlineVariant = Color(0xFFE2E8F0),

    error = Color(0xFFDC2626),
    onError = Color.White,
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