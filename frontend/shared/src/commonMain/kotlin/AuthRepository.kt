import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.call.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import io.ktor.http.isSuccess

import config.ApiConfig

class AuthRepository(private val secureStorage: SecureStorage) {

    // Using dynamically tunneled url locally to Node.js backend
    private val BASE_URL = ApiConfig.BASE_URL

    suspend fun login(request: LoginRequest): Result<LoginResponse> {
        var currentAttempt = 0
        val maxRetries = 2
        val retryDelay = 1500L

        while (true) {
            try {
                val response = httpClient.post("$BASE_URL/auth/login") {
                    setBody(request)
                }
                if (response.status.isSuccess()) {
                    val data = response.body<LoginResponse>()
                    data.token?.let { secureStorage.saveToken(it) }
                    return Result.success(data)
                } else {
                    val errorMsg = response.body<LoginResponse>().message ?: "Invalid credentials"
                    return Result.failure(Exception(errorMsg))
                }
            } catch (e: Exception) {
                if (currentAttempt < maxRetries) {
                    currentAttempt++
                    kotlinx.coroutines.delay(retryDelay)
                } else {
                    return Result.failure(Exception("Network timeout: Server is sleeping. Pleas try again."))
                }
            }
        }
    }

    fun logout() {
        secureStorage.clearToken()
    }

    fun isUserLoggedIn(): Boolean {
        return secureStorage.getToken() != null
    }
}
