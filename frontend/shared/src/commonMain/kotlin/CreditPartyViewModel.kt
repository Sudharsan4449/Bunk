import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.delete
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import config.ApiConfig

class CreditPartyViewModel(private val secureStorage: SecureStorage) : ViewModel() {

    private val BASE_URL = ApiConfig.BASE_URL

    private val _parties = MutableStateFlow<List<CreditPartyDTO>>(emptyList())
    val parties: StateFlow<List<CreditPartyDTO>> = _parties.asStateFlow()

    private val _vehicles = MutableStateFlow<List<VehicleDTO>>(emptyList())
    val vehicles: StateFlow<List<VehicleDTO>> = _vehicles.asStateFlow()

    private val _payments = MutableStateFlow<List<PaymentDTO>>(emptyList())
    val payments: StateFlow<List<PaymentDTO>> = _payments.asStateFlow()

    private val _transactions = MutableStateFlow<List<CreditTransactionDTO>>(emptyList())
    val transactions: StateFlow<List<CreditTransactionDTO>> = _transactions.asStateFlow()

    private val _uiState = MutableStateFlow<String>("")
    val uiState: StateFlow<String> = _uiState.asStateFlow()

    fun fetchParties() {
        val token = secureStorage.getToken() ?: return
        viewModelScope.launch {
            try {
                val response = httpClient.get("$BASE_URL/credit-parties") {
                    header("Authorization", "Bearer $token")
                }
                if (response.status.isSuccess()) {
                    _parties.value = response.body()
                }
            } catch (e: Exception) {
                _uiState.value = "Error: ${e.message}"
            }
        }
    }

    fun addParty(party: AddCreditPartyRequest) {
        val token = secureStorage.getToken() ?: return
        viewModelScope.launch {
            try {
                val response = httpClient.post("$BASE_URL/credit-parties") {
                    header("Authorization", "Bearer $token")
                    setBody(party)
                }
                if (response.status.isSuccess()) {
                    _uiState.value = "Party Created Successfully"
                    fetchParties()
                } else {
                    val errorText = response.bodyAsText()
                    _uiState.value = "Failed: $errorText"
                }
            } catch (e: Exception) {
                _uiState.value = "Error: ${e.message}"
            }
        }
    }

    fun fetchVehicles(partyId: String) {
        val token = secureStorage.getToken() ?: return
        viewModelScope.launch {
            try {
                val response = httpClient.get("$BASE_URL/vehicles/party/$partyId") {
                    header("Authorization", "Bearer $token")
                }
                if (response.status.isSuccess()) {
                    _vehicles.value = response.body()
                }
            } catch (e: Exception) {
                _uiState.value = "Error fetching vehicles"
            }
        }
    }

    fun addVehicle(vehicle: AddVehicleRequest, partyId: String) {
        val token = secureStorage.getToken() ?: return
        viewModelScope.launch {
            try {
                val response = httpClient.post("$BASE_URL/vehicles") {
                    header("Authorization", "Bearer $token")
                    setBody(vehicle)
                }
                if (response.status.isSuccess()) {
                    _uiState.value = "Vehicle Created Successfully"
                    fetchVehicles(partyId)
                } else {
                    _uiState.value = "Failed to create vehicle"
                }
            } catch (e: Exception) {
                _uiState.value = "Error: ${e.message}"
            }
        }
    }

    fun deleteVehicle(vehicleId: String, partyId: String) {
        val token = secureStorage.getToken() ?: return
        viewModelScope.launch {
            try {
                val response = httpClient.delete("$BASE_URL/vehicles/$vehicleId") {
                    header("Authorization", "Bearer $token")
                }
                if (response.status.isSuccess()) {
                    fetchVehicles(partyId)
                }
            } catch (e: Exception) {
                _uiState.value = "Error deleting vehicle"
            }
        }
    }

    fun fetchPayments(partyId: String) {
        val token = secureStorage.getToken() ?: return
        viewModelScope.launch {
            try {
                val response = httpClient.get("$BASE_URL/payments/party/$partyId") {
                    header("Authorization", "Bearer $token")
                }
                if (response.status.isSuccess()) {
                    _payments.value = response.body()
                }
            } catch (e: Exception) {
                _uiState.value = "Error fetching payments"
            }
        }
    }

    fun recordPayment(payment: RecordPaymentRequest, partyId: String) {
        val token = secureStorage.getToken() ?: return
        viewModelScope.launch {
            try {
                val response = httpClient.post("$BASE_URL/payments") {
                    header("Authorization", "Bearer $token")
                    setBody(payment)
                }
                if (response.status.isSuccess()) {
                    _uiState.value = "Payment Recorded Successfully"
                    fetchPayments(partyId)
                    fetchParties() // Balance has changed natively on backend so we fetch it
                } else {
                    _uiState.value = "Failed to record payment"
                }
            } catch (e: Exception) {
                _uiState.value = "Error: ${e.message}"
            }
        }
    }

    fun fetchTransactions(partyId: String) {
        val token = secureStorage.getToken() ?: return
        viewModelScope.launch {
            try {
                val response = httpClient.get("$BASE_URL/credit-transactions/party/$partyId") {
                    header("Authorization", "Bearer $token")
                }
                if (response.status.isSuccess()) {
                    _transactions.value = response.body()
                }
            } catch (e: Exception) {
                _uiState.value = "Error fetching transactions"
            }
        }
    }

    fun resetState() {
        _uiState.value = ""
    }
}
