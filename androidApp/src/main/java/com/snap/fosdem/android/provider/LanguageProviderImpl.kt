package com.snap.fosdem.android.provider

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.snap.fosdem.android.BuildConfig
import com.snap.fosdem.domain.provider.LanguageProvider

class LanguageProviderImpl(
    private val context: Context
): LanguageProvider {
    override fun changeLanguage(language: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.getSystemService(LocaleManager::class.java)
                .applicationLocales = LocaleList.forLanguageTags(language)
        } else {
            AppCompatDelegate.setApplicationLocales(
                LocaleListCompat.forLanguageTags(
                    language
                )
            )
        }
    }

    override fun getLanguages(): List<String> {
        return BuildConfig.languages.toList()
    }
}