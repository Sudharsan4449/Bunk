import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

// Exposes the exact algorithmic motorcycle tank path to be shared securely for masking
fun getMotorcycleTankPath(width: Float, height: Float): Path {
    return Path().apply {
        // Start near the top left corner (front of the tank)
        moveTo(width * 0.1f, height * 0.4f)
        
        // Upper curve leaning towards the top filler cap
        quadraticBezierTo(width * 0.15f, height * 0.05f, width * 0.4f, height * 0.05f)
        
        // The flat top surface (Fuel Cap Hole) for the nozzle to drop into
        lineTo(width * 0.6f, height * 0.05f)
        
        // Deep sloping curve towards the back (the seat junction area)
        cubicTo(width * 0.8f, height * 0.05f, width * 0.95f, height * 0.5f, width * 0.9f, height * 0.85f)
        
        // Bottom angular return to close the physical body structure
        quadraticBezierTo(width * 0.5f, height * 0.95f, width * 0.1f, height * 0.4f)
        close()
    }
}

@Composable
fun MotorcycleTank(
    modifier: Modifier = Modifier,
    alpha: Float,
    tankColor: Color,
    glowAlpha: Float,
    glowColor: Color,
    tankPath: Path
) {
    Canvas(modifier = modifier) {
        val strokeWidthPx = 4.dp.toPx()
        val glowWidthPx = 10.dp.toPx()

        // Core metallic outline of the tank silhouette
        drawPath(
            path = tankPath,
            color = tankColor.copy(alpha = alpha),
            style = Stroke(width = strokeWidthPx)
        )

        // Pulsing glow aura applied identically over the path at full strength when full
        if (glowAlpha > 0f) {
            drawPath(
                path = tankPath,
                color = glowColor.copy(alpha = glowAlpha),
                style = Stroke(width = glowWidthPx)
            )
        }
    }
}
