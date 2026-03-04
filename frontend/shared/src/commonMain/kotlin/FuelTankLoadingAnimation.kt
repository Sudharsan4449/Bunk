import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.sin

@Composable
fun FuelTankLoadingAnimation(modifier: Modifier = Modifier) {
    val isDark = isSystemInDarkTheme()
    val tankOutlineColor = if (isDark) Color(0xFF1E88E5) else Color(0xFF0B3D91)
    val fuelLiquidColor = if (isDark) Color(0xFFFF9100) else Color(0xFFFF6B00)
    val nozzleColor = if (isDark) Color.LightGray else Color.DarkGray
    val sparkColor = Color(0xFFFFC107)

    // Structural Timeline State Holders
    val tankAlpha = remember { Animatable(0f) }
    val nozzleOffsetY = remember { Animatable(-150f) }
    val bounceY = remember { Animatable(0f) }
    val sparkAlpha = remember { Animatable(0f) }
    val fillProgress = remember { Animatable(0f) }
    val tankGlowAlpha = remember { Animatable(0f) }

    // Liquid Fluid Dynamic Surface Offset Generator
    val infiniteTransition = rememberInfiniteTransition()
    val waveOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * Math.PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    // Master Timeline Orchestrator (0 to 3000ms duration mapping securely to SplashViewModel)
    LaunchedEffect(Unit) {
        // 0 -> 400ms: Motorcycle tank metal fades into view
        launch { tankAlpha.animateTo(1f, tween(400, easing = LinearEasing)) }
        
        delay(400)
        // 400ms -> 900ms: Fuel dispensing nozzle descends smoothly 
        launch { nozzleOffsetY.animateTo(0f, tween(500, easing = FastOutSlowInEasing)) }
        
        delay(500)
        // 900ms -> 1000ms: Impact / docking bounce and spark emission
        launch { 
            bounceY.animateTo(8f, tween(50, easing = FastOutLinearInEasing))
            bounceY.animateTo(0f, tween(50, easing = LinearOutSlowInEasing))
        }
        launch { sparkAlpha.animateTo(1f, tween(50, easing = LinearEasing)) }
        launch { sparkAlpha.animateTo(0f, tween(150, easing = LinearEasing, delayMillis = 50)) }
        
        delay(200)
        // 1100ms -> 2600ms: Fuel flows continuously filling the inner contour boundary
        launch { fillProgress.animateTo(1f, tween(1500, easing = FastOutSlowInEasing)) }
        
        delay(1600)
        // 2700ms -> 3000ms: Capacity verified, subtle pulse aura reflects finish threshold
        launch { 
            tankGlowAlpha.animateTo(0.6f, tween(150, easing = FastOutSlowInEasing))
            tankGlowAlpha.animateTo(0f, tween(150, easing = LinearEasing))
        }
    }

    // Composable Render Hierarchy
    Box(
        modifier = modifier.size(180.dp, 140.dp),
        contentAlignment = Alignment.Center
    ) {
        // LAYER 1: The Liquid that conforms to the parent tank clipping plane
        Canvas(modifier = Modifier.fillMaxSize()) {
            val tankPath = getMotorcycleTankPath(size.width, size.height)
            clipPath(tankPath) {
                val waveHeight = 6.dp.toPx()
                val fillHeight = size.height * fillProgress.value
                val topY = size.height - fillHeight

                val fluidPath = Path().apply {
                    moveTo(0f, size.height)
                    lineTo(0f, topY)
                    var x = 0f
                    val pointInterval = 10f
                    // Algorithm governing smooth structural lateral waves
                    while (x <= size.width) {
                        val decay = if (fillProgress.value >= 0.98f) 0f else 1f
                        val y = topY + sin(x / 15f + waveOffset) * waveHeight * decay
                        lineTo(x, y)
                        x += pointInterval
                    }
                    lineTo(size.width, size.height)
                    close()
                }

                drawPath(path = fluidPath, color = fuelLiquidColor)
                // Reflective horizontal shine running dynamically atop the liquid
                drawRect(
                    color = Color.White.copy(alpha = 0.2f),
                    topLeft = Offset(0f, topY),
                    size = androidx.compose.ui.geometry.Size(size.width, waveHeight * 2)
                )
            }
        }
        
        // LAYER 2: The robust motorcycle metallic framework containing the liquid
        Canvas(modifier = Modifier.fillMaxSize()) {
            val tankPath = getMotorcycleTankPath(size.width, size.height)
            // Call external canvas function mapping logic here directly to maintain DrawScope context
            val strokeWidthPx = 4.dp.toPx()
            val glowWidthPx = 10.dp.toPx()
            drawPath(path = tankPath, color = tankOutlineColor.copy(alpha = tankAlpha.value), style = androidx.compose.ui.graphics.drawscope.Stroke(width = strokeWidthPx))
            if (tankGlowAlpha.value > 0f) {
                drawPath(tankPath, sparkColor.copy(alpha = tankGlowAlpha.value), style = androidx.compose.ui.graphics.drawscope.Stroke(width = glowWidthPx))
            }
            
            // Render independent sparks upon nozzle docking near the geometric coordinates of the filler cap
            if (sparkAlpha.value > 0f) {
                val capX = size.width * 0.5f
                val capY = size.width * 0.05f
                drawCircle(color = sparkColor.copy(alpha = sparkAlpha.value), radius = 12.dp.toPx(), center = Offset(capX, capY))
                drawCircle(color = Color.White.copy(alpha = sparkAlpha.value), radius = 6.dp.toPx(), center = Offset(capX, capY))
            }
        }

        // LAYER 3: The Descending Nozzle overlaid directly mapping to local bounds
        FuelNozzle(
            modifier = Modifier.fillMaxSize(),
            offsetY = nozzleOffsetY.value + bounceY.value,
            color = nozzleColor.copy(alpha = tankAlpha.value)
        )
    }
}
