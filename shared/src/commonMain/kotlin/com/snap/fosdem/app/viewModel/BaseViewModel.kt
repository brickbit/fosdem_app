package com.snap.fosdem.app.viewModel

import kotlinx.coroutines.CoroutineScope

expect open class BaseViewModel() {
    open val scope: CoroutineScope
}