import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

open class ViewModel {
    val viewModelScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    
    open fun onCleared() {
        // normally cancel job here
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val role: String, val email: String) : AuthState()
    data class Error(val message: String) : AuthState()
}
