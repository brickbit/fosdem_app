package com.rgr.fosdem.app.wrapper

import kotlinx.coroutines.flow.StateFlow
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic

class FlowWrapperFactory {
    /**
     * Creates a FlowWrapper instance for the given StateFlow.
     *
     * This method can be called from Java code and allows for
     * easy integration with Kotlin coroutines and state management.
     *
     * @param stateFlow The StateFlow instance to observe.
     * @return A FlowWrapper that wraps the provided StateFlow.
     */
    companion object {
        @JvmStatic
        @JvmOverloads
        fun <T> create(stateFlow: StateFlow<T>): FlowWrapper<T> {
            // Creates and returns an instance of FlowWrapper with the given StateFlow
            return FlowWrapper(stateFlow)
        }
    }
}