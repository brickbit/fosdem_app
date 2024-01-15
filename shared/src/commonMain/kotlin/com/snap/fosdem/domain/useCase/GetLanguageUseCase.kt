package com.snap.fosdem.domain.useCase

import com.snap.fosdem.domain.repository.LanguageProvider

class GetLanguageUseCase(
    private val provider: LanguageProvider
) {
    operator fun invoke(): List<String> {
        return provider.getLanguages()
    }
}