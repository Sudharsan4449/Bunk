import platform.Foundation.*

actual class SecureStorage {
    // Note: Due to KMP C-interop complexity with Security.framework, 
    // keeping this as standard UserDefaults for the template compilation. 
    // In a fully native iOS environment, we use KeychainSwift or a specific KMP Keychain library.
    private val defaults = NSUserDefaults.standardUserDefaults()
    private val TOKEN_KEY = "jwt_token_keychain_fallback"

    actual fun saveToken(token: String) {
        defaults.setObject(token, TOKEN_KEY)
    }

    actual fun getToken(): String? {
        return defaults.stringForKey(TOKEN_KEY)
    }

    actual fun clearToken() {
        defaults.removeObjectForKey(TOKEN_KEY)
    }
}
