sealed class AdminScreen {
    object Dashboard : AdminScreen()
    object CreditParties : AdminScreen()
    object AddCreditParty : AdminScreen()
    data class CreditPartyDetail(val partyId: String) : AdminScreen()
    data class VehicleManagement(val partyId: String) : AdminScreen()
    data class PaymentEntry(val partyId: String) : AdminScreen()
}
