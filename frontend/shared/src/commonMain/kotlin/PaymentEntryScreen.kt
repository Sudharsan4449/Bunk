import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PaymentEntryScreen(
    partyId: String,
    viewModel: CreditPartyViewModel,
    onBack: () -> Unit
) {
    var amount by remember { mutableStateOf("") }
    var method by remember { mutableStateOf("Cash") }
    var notes by remember { mutableStateOf("") }
    
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {
        if (uiState == "Payment Recorded Successfully") {
            viewModel.resetState()
            onBack()
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Back") }
            Spacer(modifier = Modifier.width(8.dp))
            Text("Record Payment", fontSize = 24.sp)
        }

        if (uiState.isNotEmpty() && uiState != "Payment Recorded Successfully") {
            Text(uiState, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(bottom = 8.dp))
        }

        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
            OutlinedTextField(
                value = amount, 
                onValueChange = { amount = it }, 
                label = { Text("Amount Received (₹) *") }, 
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            Text("Payment Method", modifier = Modifier.padding(bottom = 8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Button(
                    onClick = { method = "Cash" },
                    colors = ButtonDefaults.buttonColors(containerColor = if (method == "Cash") MaterialTheme.colorScheme.primary else Color.Gray)
                ) { Text("Cash") }
                
                Button(
                    onClick = { method = "Bank Transfer" },
                    colors = ButtonDefaults.buttonColors(containerColor = if (method == "Bank Transfer") MaterialTheme.colorScheme.primary else Color.Gray)
                ) { Text("Bank Transfer") }
                
                Button(
                    onClick = { method = "UPI" },
                    colors = ButtonDefaults.buttonColors(containerColor = if (method == "UPI") MaterialTheme.colorScheme.primary else Color.Gray)
                ) { Text("UPI") }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = notes, 
                onValueChange = { notes = it }, 
                label = { Text("Notes (Optional)") }, 
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Button(
                onClick = {
                    if (amount.isNotBlank() && (amount.toDoubleOrNull() ?: 0.0) > 0) {
                        viewModel.recordPayment(
                            RecordPaymentRequest(
                                partyId = partyId, 
                                amount = amount.toDouble(), 
                                method = method, 
                                notes = notes
                            ),
                            partyId
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF43A047))
            ) {
                Text("Confirm Payment", fontSize = 16.sp)
            }
        }
    }
}
