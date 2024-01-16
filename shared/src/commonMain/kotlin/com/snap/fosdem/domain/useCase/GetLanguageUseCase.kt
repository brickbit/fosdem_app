package com.snap.fosdem.domain.useCase

import com.snap.fosdem.domain.provider.LanguageProvider

class GetLanguageUseCase(
    private val provider: LanguageProvider
) {
    operator fun invoke(): List<String> {
        return provider.getLanguages()
    }
}