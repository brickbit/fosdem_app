package com.rgr.fosdem.domain.provider

interface LanguageProvider {
    fun changeLanguage(language: String)

    fun getLanguages(): List<String>
}