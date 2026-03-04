import kotlinx.serialization.Serializable

@Serializable
data class CreditPartyDTO(
    val partyId: String,
    val partyName: String,
    val companyName: String,
    val contactPerson: String,
    val phone: String,
    val address: String,
    val gstNumber: String? = null,
    val creditLimit: Double,
    val currentBalance: Double,
    val status: String
)

@Serializable
data class VehicleDTO(
    val vehicleId: String,
    val partyId: String,
    val vehicleNumber: String,
    val driverName: String? = null,
    val driverPhone: String? = null
)

@Serializable
data class PaymentDTO(
    val paymentId: String,
    val partyId: String,
    val amount: Double,
    val method: String,
    val notes: String? = null,
    val date: String
)

@Serializable
data class CreditTransactionDTO(
    val transactionId: String,
    val partyId: String,
    val vehicleId: String,
    val vehicleNumber: String,
    val fuelType: String,
    val liters: Double,
    val pricePerLiter: Double,
    val amount: Double,
    val paymentType: String,
    val pumpNumber: Int,
    val date: String
)

@Serializable
data class AddCreditPartyRequest(
    val partyName: String,
    val companyName: String,
    val contactPerson: String,
    val phone: String,
    val address: String,
    val gstNumber: String? = null,
    val creditLimit: Double
)

@Serializable
data class AddVehicleRequest(
    val partyId: String,
    val vehicleNumber: String,
    val driverName: String? = null,
    val driverPhone: String? = null
)

@Serializable
data class RecordPaymentRequest(
    val partyId: String,
    val amount: Double,
    val method: String,
    val notes: String? = null
)
