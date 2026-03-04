import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CreditPartyDetailScreen(
    partyId: String,
    viewModel: CreditPartyViewModel,
    onBack: () -> Unit,
    onManageVehicles: () -> Unit,
    onRecordPayment: () -> Unit
) {
    val parties by viewModel.parties.collectAsState()
    val transactions by viewModel.transactions.collectAsState()
    val payments by viewModel.payments.collectAsState()

    val party = parties.find { it.partyId == partyId }

    LaunchedEffect(partyId) {
        viewModel.fetchTransactions(partyId)
        viewModel.fetchPayments(partyId)
        viewModel.fetchVehicles(partyId) // Preloads for vehicle child lists
    }

    if (party == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { 
            CircularProgressIndicator() 
        }
        return
    }

    // Determine balance color exactly matched as list screen
    val balanceRatio = party.currentBalance / party.creditLimit
    val balanceColor = when {
        balanceRatio > 0.85 -> Color(0xFFE53935)
        balanceRatio > 0.50 -> Color(0xFFFB8C00)
        else -> Color(0xFF43A047)
    }

    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Info", "Transactions", "Payments")

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Back") }
            Spacer(modifier = Modifier.width(8.dp))
            Text(party.partyName, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }

        // Prominent Balance Display Top Block
        Card(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Outstanding Balance", fontSize = 14.sp)
                Text("₹${party.currentBalance}", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = balanceColor)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Credit Limit: ₹${party.creditLimit}", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        TabRow(selectedTabIndex = selectedTabIndex, modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title) }
                )
            }
        }

        Box(modifier = Modifier.weight(1f)) {
            when (selectedTabIndex) {
                0 -> {
                    // INFO TAB
                    Column {
                        Text("Company: ${party.companyName}", fontSize = 16.sp)
                        Text("Contact: ${party.contactPerson} (${party.phone})", fontSize = 16.sp)
                        Text("Address: ${party.address}", fontSize = 16.sp)
                        Text("GST: ${party.gstNumber ?: "N/A"}", fontSize = 16.sp)
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                            Button(onClick = onManageVehicles) { Text("Manage Vehicles") }
                            Button(onClick = onRecordPayment, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF43A047))) { 
                                Text("Record Payment") 
                            }
                        }
                    }
                }
                1 -> {
                    // TRANSACTIONS TAB
                    LazyColumn {
                        items(transactions) { txn ->
                            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text(txn.date.take(10), fontWeight = FontWeight.Bold)
                                        Text("₹${txn.amount}", fontWeight = FontWeight.Bold, color = Color(0xFFE53935))
                                    }
                                    Text("${txn.fuelType} - ${txn.liters}L @ ₹${txn.pricePerLiter}", fontSize = 14.sp)
                                    Text("Vehicle: ${txn.vehicleNumber} | Pump: ${txn.pumpNumber}", fontSize = 12.sp, color = Color.Gray)
                                }
                            }
                        }
                    }
                }
                2 -> {
                    // PAYMENTS TAB
                    LazyColumn {
                        items(payments) { pay ->
                            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text(pay.date.take(10), fontWeight = FontWeight.Bold)
                                        Text("₹${pay.amount}", fontWeight = FontWeight.Bold, color = Color(0xFF43A047))
                                    }
                                    Text("Method: ${pay.method}", fontSize = 14.sp)
                                    if (!pay.notes.isNullOrEmpty()) {
                                        Text("Notes: ${pay.notes}", fontSize = 12.sp, color = Color.Gray)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
