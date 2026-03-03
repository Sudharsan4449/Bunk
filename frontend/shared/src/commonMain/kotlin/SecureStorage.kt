expect class SecureStorage() {
    fun saveToken(token: String)
    fun getToken(): String?
    fun clearToken()
}
