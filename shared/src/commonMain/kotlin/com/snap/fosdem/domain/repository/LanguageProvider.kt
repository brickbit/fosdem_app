package com.snap.fosdem.domain.repository

interface LanguageProvider {
    fun changeLanguage(language: String)

    fun getLanguages(): List<String>
}