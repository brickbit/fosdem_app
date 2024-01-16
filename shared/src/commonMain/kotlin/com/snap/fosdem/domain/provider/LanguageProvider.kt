package com.snap.fosdem.domain.provider

interface LanguageProvider {
    fun changeLanguage(language: String)

    fun getLanguages(): List<String>
}