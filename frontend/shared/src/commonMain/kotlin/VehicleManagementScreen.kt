import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun VehicleManagementScreen(
    partyId: String,
    viewModel: CreditPartyViewModel,
    onBack: () -> Unit
) {
    val vehicles by viewModel.vehicles.collectAsState()
    
    var showDialog by remember { mutableStateOf(false) }
    var vehicleNumber by remember { mutableStateOf("") }
    var driverName by remember { mutableStateOf("") }
    var driverPhone by remember { mutableStateOf("") }

    LaunchedEffect(partyId) {
        viewModel.fetchVehicles(partyId)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Back") }
            Spacer(modifier = Modifier.width(8.dp))
            Text("Manage Vehicles", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }

        Button(onClick = { showDialog = true }, modifier = Modifier.fillMaxWidth()) {
            Text("Add New Vehicle")
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(vehicles) { vehicle ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text(vehicle.vehicleNumber, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            if (!vehicle.driverName.isNullOrEmpty()) {
                                Text("Driver: ${vehicle.driverName}", fontSize = 14.sp, color = Color.Gray)
                            }
                        }
                        IconButton(onClick = { viewModel.deleteVehicle(vehicle.vehicleId, partyId) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                        }
                    }
                }
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Add Vehicle") },
                text = {
                    Column {
                        OutlinedTextField(value = vehicleNumber, onValueChange = { vehicleNumber = it.uppercase() }, label = { Text("Vehicle Number *") })
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(value = driverName, onValueChange = { driverName = it }, label = { Text("Driver Name") })
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(value = driverPhone, onValueChange = { driverPhone = it }, label = { Text("Driver Phone") })
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        if (vehicleNumber.isNotBlank()) {
                            viewModel.addVehicle(
                                AddVehicleRequest(
                                    partyId = partyId, 
                                    vehicleNumber = vehicleNumber, 
                                    driverName = driverName, 
                                    driverPhone = driverPhone
                                ),
                                partyId
                            )
                            showDialog = false
                            vehicleNumber = ""
                            driverName = ""
                            driverPhone = ""
                        }
                    }) { Text("Add") }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) { Text("Cancel") }
                }
            )
        }
    }
}
