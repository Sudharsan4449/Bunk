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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AddCreditPartyScreen(
    viewModel: CreditPartyViewModel,
    onBack: () -> Unit
) {
    var partyName by remember { mutableStateOf("") }
    var companyName by remember { mutableStateOf("") }
    var contactPerson by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var gstNumber by remember { mutableStateOf("") }
    var creditLimit by remember { mutableStateOf("") }
    
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {
        if (uiState == "Party Created Successfully") {
            viewModel.resetState()
            onBack()
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text("Add Credit Party", fontSize = 24.sp)
        }

        if (uiState.isNotEmpty() && uiState != "Party Created Successfully") {
            Text(uiState, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(bottom = 8.dp))
        }

        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
            OutlinedTextField(value = partyName, onValueChange = { partyName = it }, label = { Text("Party Name *") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = companyName, onValueChange = { companyName = it }, label = { Text("Company Name *") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = contactPerson, onValueChange = { contactPerson = it }, label = { Text("Contact Person *") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone Number *") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = address, onValueChange = { address = it }, label = { Text("Address *") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = gstNumber, onValueChange = { gstNumber = it }, label = { Text("GST Number") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = creditLimit, onValueChange = { creditLimit = it }, label = { Text("Credit Limit (₹) *") }, modifier = Modifier.fillMaxWidth())
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = {
                    if (partyName.isNotBlank() && companyName.isNotBlank() && contactPerson.isNotBlank() && phone.isNotBlank() && address.isNotBlank() && creditLimit.isNotBlank()) {
                        viewModel.addParty(
                            AddCreditPartyRequest(
                                partyName = partyName,
                                companyName = companyName,
                                contactPerson = contactPerson,
                                phone = phone,
                                address = address,
                                gstNumber = gstNumber,
                                creditLimit = (creditLimit.toDoubleOrNull() ?: 0.0)
                            )
                        )
                    } else {
                        // Handle native validation UI visually if needed, but required is robust enough for Phase 16
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Save Party", fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
