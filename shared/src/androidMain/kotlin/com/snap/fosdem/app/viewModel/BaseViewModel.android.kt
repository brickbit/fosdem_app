package com.snap.fosdem.app.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope

actual open class BaseViewModel actual constructor(): ViewModel() {
    actual open val scope: CoroutineScope = viewModelScope
}