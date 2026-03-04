import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter

@Composable
actual fun getLoginBackgroundImagePainter(imageName: String): Painter? {
    // Fallback for native iOS platform missing manual bundled bundle assets 
    return null
}
