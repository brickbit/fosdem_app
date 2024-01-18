package com.snap.fosdem.android.screens.common

import android.webkit.WebChromeClient
import android.webkit.WebView

class CustomChromeClient(
    private val isLoading: (Boolean) -> Unit
): WebChromeClient() {

    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        isLoading(newProgress != 100)
        super.onProgressChanged(view, newProgress)
    }
}