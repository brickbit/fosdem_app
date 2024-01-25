package com.rgr.fosdem.android.provider

import android.app.LocaleManager

import android.os.Build
import android.os.LocaleList
import com.rgr.fosdem.android.BuildConfig
import com.rgr.fosdem.domain.provider.LanguageProvider
import java.util.Locale


class LanguageProviderImpl(
    private val activityProvider: ActivityProvider
): LanguageProvider {
    override fun changeLanguage(language: String) {
        val activity = activityProvider.getActivity()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            activity?.getSystemService(LocaleManager::class.java)
                ?.applicationLocales = LocaleList.forLanguageTags(language)
        } else {
            val newLocale = Locale(language)
            val configuration = activity?.resources?.configuration
            configuration?.locale = newLocale
            activity?.resources?.updateConfiguration(configuration, activity.resources.displayMetrics)
            activity?.recreate()
        }
    }

    override fun getLanguages(): List<String> {
        return BuildConfig.languages.toList()
    }
}