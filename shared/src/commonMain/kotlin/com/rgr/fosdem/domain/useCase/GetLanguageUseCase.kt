package com.rgr.fosdem.domain.useCase

import com.rgr.fosdem.domain.provider.LanguageProvider

class GetLanguageUseCase(
    private val provider: LanguageProvider
) {
    operator fun invoke(): List<String> {
        return provider.getLanguages()
    }
}