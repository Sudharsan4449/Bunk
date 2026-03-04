import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun AdminDashboard(
    authViewModel: AuthViewModel,
    creditPartyViewModel: CreditPartyViewModel,
    onLogout: () -> Unit
) {
    val drawerProgress = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    var currentScreen by remember { mutableStateOf<AdminScreen>(AdminScreen.Dashboard) }
    
    // Smooth 60FPS Drag Spring Recovery Simulation 
    fun snapDrawer(target: Float) {
        scope.launch {
            drawerProgress.animateTo(
                targetValue = target,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy, 
                    stiffness = Spring.StiffnessLow
                )
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragEnd = {
                        // Snapping auto-recovery bounds triggers over 40% swipe
                        if (drawerProgress.value > 0.4f) snapDrawer(1f) else snapDrawer(0f)
                    },
                    onHorizontalDrag = { _, dragAmount ->
                        // Dynamically evaluate layout limits dynamically simulating touch surface
                        val drawerWidth = size.width.toFloat() * 0.7f
                        val delta = dragAmount / drawerWidth
                        scope.launch {
                            drawerProgress.snapTo((drawerProgress.value + delta).coerceIn(0f, 1f))
                        }
                    }
                )
            }
    ) {
        // LAYER 1: Dynamic Module Viewport (Swaps dynamically)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Spacer bridging Top Bar vertical spacing gracefully
            Spacer(modifier = Modifier.height(80.dp))
            
            when (val screen = currentScreen) {
                is AdminScreen.Dashboard -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "Admin Dashboard",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
                        )
                    }
                }
                is AdminScreen.CreditParties -> {
                    CreditPartyListScreen(
                        viewModel = creditPartyViewModel,
                        onAddPartyClicked = { currentScreen = AdminScreen.AddCreditParty },
                        onPartyClicked = { partyId -> currentScreen = AdminScreen.CreditPartyDetail(partyId) }
                    )
                }
                is AdminScreen.AddCreditParty -> {
                    AddCreditPartyScreen(
                        viewModel = creditPartyViewModel,
                        onBack = { currentScreen = AdminScreen.CreditParties }
                    )
                }
                is AdminScreen.CreditPartyDetail -> {
                    CreditPartyDetailScreen(
                        partyId = screen.partyId,
                        viewModel = creditPartyViewModel,
                        onBack = { currentScreen = AdminScreen.CreditParties },
                        onManageVehicles = { currentScreen = AdminScreen.VehicleManagement(screen.partyId) },
                        onRecordPayment = { currentScreen = AdminScreen.PaymentEntry(screen.partyId) }
                    )
                }
                is AdminScreen.VehicleManagement -> {
                    VehicleManagementScreen(
                        partyId = screen.partyId,
                        viewModel = creditPartyViewModel,
                        onBack = { currentScreen = AdminScreen.CreditPartyDetail(screen.partyId) }
                    )
                }
                is AdminScreen.PaymentEntry -> {
                    PaymentEntryScreen(
                        partyId = screen.partyId,
                        viewModel = creditPartyViewModel,
                        onBack = { currentScreen = AdminScreen.CreditPartyDetail(screen.partyId) }
                    )
                }
            }
        }

        // LAYER 2: Admin Navigation Left Tracking Drawer Panel
        AdminDrawerMenu(
            drawerProgress = drawerProgress.value,
            onClose = { requestedTitle -> 
                snapDrawer(0f) 
                when (requestedTitle) {
                    "Dashboard" -> currentScreen = AdminScreen.Dashboard
                    "Credit Parties" -> currentScreen = AdminScreen.CreditParties
                }
            },
            onLogout = {
                authViewModel.logout()
                onLogout()
            }
        )

        // LAYER 3: Dynamic Menu Floating Top Bar anchoring exactly onto Z-Index ceiling
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AdminMenuButton(
                drawerProgress = drawerProgress.value,
                onClick = { 
                    if (drawerProgress.value > 0.5f) snapDrawer(0f) else snapDrawer(1f)
                }
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "FuelTrack Pro", 
                fontSize = 20.sp, 
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}
