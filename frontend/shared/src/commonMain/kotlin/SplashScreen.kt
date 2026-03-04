import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SplashScreen(viewModel: SplashViewModel, onAnimationEnd: () -> Unit) {
    val isFinished by viewModel.isSplashFinished.collectAsState()

    LaunchedEffect(isFinished) {
        if (isFinished) {
            onAnimationEnd()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(vertical = 64.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Section
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "FuelTrack Pro",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // Center Section - Petrol Tank Liquid Fluid Dynamic Animation
            FuelTankLoadingAnimation(modifier = Modifier.padding(32.dp))

            // Bottom Section Tagline
            Text(
                text = "Fueling Smart Operations",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                fontWeight = FontWeight.Medium
            )
        }
    }
}
