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
        return try {
            val response = httpClient.post("$BASE_URL/auth/login") {
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
