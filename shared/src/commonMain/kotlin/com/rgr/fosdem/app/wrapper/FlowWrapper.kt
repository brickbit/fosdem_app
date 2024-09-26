package com.rgr.fosdem.app.wrapper

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FlowWrapper<T>(private val stateFlow: StateFlow<T>) {

    /**
     * Observes changes in the StateFlow and invokes the callback with the new value.
     *
     * This method launches a coroutine on the Main dispatcher to collect values
     * from the StateFlow and pass them to the provided callback function.
     *
     * @param callback A function that will be called with the new value each time
     *                 the StateFlow emits a new value.
     */
    fun observe(callback: (T) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            stateFlow.collect {
                callback(it)
            }
        }
    }
}