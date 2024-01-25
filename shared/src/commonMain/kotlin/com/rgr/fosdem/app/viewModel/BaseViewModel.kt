package com.rgr.fosdem.app.viewModel

import kotlinx.coroutines.CoroutineScope

expect open class BaseViewModel() {
    open val scope: CoroutineScope
}