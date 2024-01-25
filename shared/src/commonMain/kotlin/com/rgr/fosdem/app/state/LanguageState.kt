package com.rgr.fosdem.app.state

sealed class LanguageState {
        data object NoLanguages: LanguageState()
        data class LanguageLoaded(val list: List<String>): LanguageState()
    }