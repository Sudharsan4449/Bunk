package config

import android.os.Build

actual object ApiConfig {
    actual val BASE_URL: String
        get() {
            val isEmulator = Build.FINGERPRINT.contains("generic") ||
                    Build.FINGERPRINT.contains("vbox86") ||
                    Build.FINGERPRINT.contains("emulator") ||
                    Build.MODEL.contains("google_sdk") ||
                    Build.MODEL.contains("Emulator") ||
                    Build.MODEL.contains("Android SDK built for x86") ||
                    Build.BOARD == "QC_Reference_Phone"

            return if (isEmulator) {
                "https://fueltrack-backend-8oey.onrender.com/api"
            } else {
                "https://fueltrack-backend-8oey.onrender.com/api"
            }
        }
}
