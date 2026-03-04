import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AdminMenuButton(
    drawerProgress: Float,
    onClick: () -> Unit
) {
    // Exact interpolation scaling rotating the circular trigger dynamically linked to swipe drag.
    val rotation = drawerProgress * 90f
    
    // Complex optical illusion: Dash connects to edge while sliding, then vanishes behind fully expanded overlay
    val dashWidth = if (drawerProgress <= 0.5f) {
        drawerProgress * 2f * 32f 
    } else {
        (1f - drawerProgress) * 2f * 32f
    }
    
    val isDark = isSystemInDarkTheme()
    val bgColor = if (isDark) Color(0xFF1E88E5) else Color(0xFF0B3D91)
    
    Row(verticalAlignment = Alignment.CenterVertically) {
        // Rotating Avatar Circle Engine
        Box(
            modifier = Modifier
                .size(48.dp)
                .graphicsLayer { rotationZ = rotation }
                .clip(CircleShape)
                .background(bgColor)
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Ad", 
                color = Color.White, 
                fontWeight = FontWeight.Bold, 
                fontSize = 18.sp
            )
        }
        
        // Expanding geometric link attached explicitly to physical sliding layer mathematically
        if (dashWidth > 0f) {
            Box(
                modifier = Modifier
                    .width(dashWidth.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(bgColor)
            )
        }
    }
}
