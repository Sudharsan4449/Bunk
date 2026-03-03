import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.call.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import io.ktor.http.isSuccess

class AuthRepository(private val secureStorage: SecureStorage) {

    // Assuming we use Android emulator locally to Node.js backend
    // 10.0.2.2 is loopback for Android emulator to localhost
    private val BASE_URL = "http://10.0.2.2:3000/api/auth"

    suspend fun login(request: LoginRequest): Result<LoginResponse> {
        return try {
            val response = httpClient.post("$BASE_URL/login") {
                setBody(request)
            }
            if (response.status.isSuccess()) {
                val data = response.body<LoginResponse>()
                data.token?.let { secureStorage.saveToken(it) }
                Result.success(data)
            } else {
                val errorMsg = response.body<LoginResponse>().message ?: "Invalid credentials"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network error: ${e.message}"))
        }
    }

    fun logout() {
        secureStorage.clearToken()
    }

    fun isUserLoggedIn(): Boolean {
        return secureStorage.getToken() != null
    }
}
