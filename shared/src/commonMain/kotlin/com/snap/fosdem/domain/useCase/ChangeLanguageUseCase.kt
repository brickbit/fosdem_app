package com.snap.fosdem.domain.useCase

import com.snap.fosdem.domain.repository.LanguageProvider

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