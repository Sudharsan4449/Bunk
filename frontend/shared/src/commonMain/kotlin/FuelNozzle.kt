import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun FuelNozzle(
    modifier: Modifier = Modifier,
    offsetY: Float,
    color: Color
) {
    Canvas(modifier = modifier.offset(y = offsetY.dp)) {
        val width = size.width
        val height = size.height

        val pipeWidth = width * 0.3f
        val pipeX = width * 0.35f
        
        // Downward dispensing rigid metal pipe
        drawRect(
            color = color,
            topLeft = Offset(pipeX, 0f),
            size = Size(pipeWidth, height * 0.6f)
        )
        
        // Refined nozzle curved tip aiming horizontally into the tank inlet
        drawArc(
            color = color,
            startAngle = 0f, 
            sweepAngle = 90f, 
            useCenter = false,
            topLeft = Offset(pipeX, height * 0.4f),
            size = Size(width * 0.6f, height * 0.5f),
            style = Stroke(width = pipeWidth)
        )
        
        // Industrial mechanical trigger block attached to main shaft
        drawRect(
            color = color,
            topLeft = Offset(pipeX - width * 0.3f, height * 0.2f),
            size = Size(width * 0.3f, height * 0.15f)
        )
        
        // Grip handle
        drawRect(
            color = color,
            topLeft = Offset(pipeX - width * 0.45f, height * 0.1f),
            size = Size(width * 0.15f, height * 0.35f)
        )
    }
}
