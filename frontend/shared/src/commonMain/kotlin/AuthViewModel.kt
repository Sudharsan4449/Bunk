import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("Fields cannot be empty")
            return
        }

        _authState.value = AuthState.Loading
        viewModelScope.launch {
            val result = repository.login(LoginRequest(email, password))
            if (result.isSuccess) {
                val data = result.getOrNull()
                if (data != null) {
                    _authState.value = AuthState.Success(data.role ?: "MANAGER", data.email ?: email)
                } else {
                    _authState.value = AuthState.Error("Invalid response")
                }
            } else {
                _authState.value = AuthState.Error(result.exceptionOrNull()?.message ?: "Unknown Error")
            }
        }
    }

    fun logout() {
        repository.logout()
        _authState.value = AuthState.Idle
    }

    fun resetState() {
        if (_authState.value is AuthState.Error) {
            _authState.value = AuthState.Idle
        }
    }
}
