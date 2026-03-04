import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CreditPartyListScreen(
    viewModel: CreditPartyViewModel,
    onAddPartyClicked: () -> Unit,
    onPartyClicked: (String) -> Unit
) {
    val parties by viewModel.parties.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.fetchParties()
    }

    val filteredParties = parties.filter {
        it.partyName.contains(searchQuery, ignoreCase = true) || it.companyName.contains(searchQuery, ignoreCase = true)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.weight(1f),
                label = { Text("Search Parties") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            FloatingActionButton(
                onClick = onAddPartyClicked,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Party", tint = Color.White)
            }
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(filteredParties) { party ->
                CreditPartyCard(party = party, onClick = { onPartyClicked(party.partyId) })
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun CreditPartyCard(party: CreditPartyDTO, onClick: () -> Unit) {
    // Determine balance color based on threshold logic implicitly requested
    val balanceRatio = party.currentBalance / party.creditLimit
    val balanceColor = when {
        balanceRatio > 0.85 -> Color(0xFFE53935) // Red -> near credit limit
        balanceRatio > 0.50 -> Color(0xFFFB8C00) // Orange -> medium balance
        else -> Color(0xFF43A047) // Green -> low balance
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = party.partyName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = party.status,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (party.status == "ACTIVE") Color(0xFF1E88E5) else Color.Gray,
                    modifier = Modifier
                        .background(
                            color = if (party.status == "ACTIVE") Color(0xFF1E88E5).copy(alpha = 0.1f) else Color.Gray.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = party.companyName, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text("Outstanding Balance", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("₹${party.currentBalance}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = balanceColor)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("Credit Limit", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("₹${party.creditLimit}", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
                }
            }
        }
    }
}
