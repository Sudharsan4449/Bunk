import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val LightPrimary = Color(0xFF0B3D91)
val LightBackground = Color(0xFFF5F7FA)
val LightAccent = Color(0xFFFF6B00)
val LightText = Color(0xFF333333)
val LightCard = Color(0xFFFFFFFF)

val DarkPrimary = Color(0xFF1E88E5)
val DarkBackground = Color(0xFF121212)
val DarkAccent = Color(0xFFFF9100)
val DarkText = Color(0xFFE0E0E0)
val DarkCard = Color(0xFF1E1E1E)

private val LightColorScheme = lightColorScheme(
    primary = LightPrimary,
    background = LightBackground,
    surface = LightCard,
    secondary = LightAccent,
    onPrimary = Color.White,
    onBackground = LightText,
    onSurface = LightText
)

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    background = DarkBackground,
    surface = DarkCard,
    secondary = DarkAccent,
    onPrimary = Color.White,
    onBackground = DarkText,
    onSurface = DarkText
)

@Composable
fun FuelTrackTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        content = content
    )
}
