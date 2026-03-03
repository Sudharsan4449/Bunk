import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

actual class SecureStorage {
    private val PREFS_NAME = "fueltrack_secure_prefs"
    private val TOKEN_KEY = "jwt_token"

    // Context is injected via AppContext object initialized in Android APP
    private val sharedPreferences by lazy {
        val context = AppContext.get()
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        EncryptedSharedPreferences.create(
            PREFS_NAME,
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    actual fun saveToken(token: String) {
        sharedPreferences.edit().putString(TOKEN_KEY, token).apply()
    }

    actual fun getToken(): String? {
        return sharedPreferences.getString(TOKEN_KEY, null)
    }

    actual fun clearToken() {
        sharedPreferences.edit().remove(TOKEN_KEY).apply()
    }
}

object AppContext {
    private var context: Context? = null
    fun set(ctx: Context) { context = ctx }
    fun get(): Context = context ?: throw IllegalStateException("Context not initialized")
}
