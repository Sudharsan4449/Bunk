import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AdminDrawerMenu(
    drawerProgress: Float,
    onClose: (String?) -> Unit,
    onLogout: () -> Unit
) {
    // Map the 0f-1f progress state accurately to physics space explicitly
    val drawerOffset = -1f + drawerProgress
    
    // Smooth darkness envelope transitions physically linked dynamically
    val dimAlpha = drawerProgress * 0.5f

    // Render dimming interceptor layer if menu is interacting
    if (dimAlpha > 0f) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = dimAlpha))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onClose(null) } // Close when tapping the dark dim bounds outside drawer
        )
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val drawerWidth = maxWidth * 0.7f
        val isDark = isSystemInDarkTheme()
        val drawerBgColor = if (isDark) Color(0xFF1E1E1E) else Color.White
        
        Surface(
            modifier = Modifier
                .width(drawerWidth)
                .fillMaxHeight()
                .offset(x = drawerWidth * drawerOffset),
            color = drawerBgColor,
            shadowElevation = 16.dp
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Administrator Header Section
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(24.dp)
                ) {
                    Column {
                        Icon(
                            imageVector = Icons.Default.AccountCircle, 
                            contentDescription = "Admin Avatar", 
                            modifier = Modifier.size(64.dp), 
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Admin Panel", 
                            fontSize = 20.sp, 
                            fontWeight = FontWeight.Bold, 
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "Santhoshvb557@gmail.com", 
                            fontSize = 14.sp, 
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                    }
                }
                
                // Primary Core Navigation Links
                val items = listOf(
                    "Dashboard" to Icons.Default.List, 
                    "Credit Parties" to Icons.Default.Person, 
                    "Fuel Stock" to Icons.Default.Settings, 
                    "Transactions" to Icons.Default.List, 
                    "Reports" to Icons.Default.List, 
                    "Settings" to Icons.Default.Settings
                )
                
                LazyColumn(modifier = Modifier.weight(1f).padding(top = 16.dp)) {
                    items(items) { (title, icon) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onClose(title) }
                                .padding(horizontal = 24.dp, vertical = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(icon, contentDescription = title, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(title, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                }
                
                // Action Destructive Bottom Logout Anchor
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onLogout() }
                        .padding(horizontal = 24.dp, vertical = 24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Person, contentDescription = "Logout", tint = MaterialTheme.colorScheme.error)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Logout", 
                        fontSize = 16.sp, 
                        color = MaterialTheme.colorScheme.error, 
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
