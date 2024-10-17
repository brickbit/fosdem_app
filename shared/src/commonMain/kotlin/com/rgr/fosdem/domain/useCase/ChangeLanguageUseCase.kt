package com.rgr.fosdem.domain.useCase

import com.rgr.fosdem.domain.provider.LanguageProvider

class ChangeLanguageUseCase(
    private val provider: LanguageProvider
) {
    operator fun invoke(language: String) {
        when(language) {
            "Español" -> provider.changeLanguage("es")
            "Français" -> provider.changeLanguage("fr")
            "English" -> provider.changeLanguage("en")
            else -> provider.changeLanguage("es")
        }

    }
}