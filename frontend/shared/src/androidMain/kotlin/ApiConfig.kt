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
                "http://10.0.2.2:3000/api"
            } else {
                "http://localhost:3000/api" // Localhost works via adb reverse tcp:3000 tcp:3000
            }
        }
}
