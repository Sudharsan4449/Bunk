import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class SplashViewModel : ViewModel() {
    private val _isSplashFinished = MutableStateFlow(false)
    val isSplashFinished: StateFlow<Boolean> = _isSplashFinished.asStateFlow()

    init {
        startSplashAnimation()
    }

    private fun startSplashAnimation() {
        viewModelScope.launch {
            // Wait for 3.1 seconds allowing the 3000ms motorcycle fuel animation to finish
            delay(3100)
            _isSplashFinished.value = true
        }
    }
}
