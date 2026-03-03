import androidx.compose.animation.Crossfade
import androidx.compose.runtime.*

// Simple navigation state
sealed class Screen {
    object Splash : Screen()
    object Login : Screen()
    object Dashboard : Screen()
}

@Composable
fun App() {
    val secureStorage = remember { SecureStorage() }
    val authRepository = remember { AuthRepository(secureStorage) }
    val authViewModel = remember { AuthViewModel(authRepository) }

    FuelTrackTheme {
        var currentScreen by remember {
            mutableStateOf<Screen>(Screen.Splash)
        }

        Crossfade(targetState = currentScreen) { screen ->
            when (screen) {
                is Screen.Splash -> {
                    SplashScreen(onAnimationEnd = {
                        if (authRepository.isUserLoggedIn()) {
                            currentScreen = Screen.Dashboard
                        } else {
                            currentScreen = Screen.Login
                        }
                    })
                }
                is Screen.Login -> {
                    LoginScreen(viewModel = authViewModel, onLoginSuccess = {
                        currentScreen = Screen.Dashboard
                    })
                }
                is Screen.Dashboard -> {
                    AdminDashboard(viewModel = authViewModel, onLogout = {
                        currentScreen = Screen.Login
                    })
                }
            }
        }
    }
}