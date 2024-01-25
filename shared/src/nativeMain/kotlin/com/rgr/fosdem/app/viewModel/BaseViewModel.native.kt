package com.rgr.fosdem.app.viewModel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

actual open class BaseViewModel actual constructor() {
    actual open val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
}