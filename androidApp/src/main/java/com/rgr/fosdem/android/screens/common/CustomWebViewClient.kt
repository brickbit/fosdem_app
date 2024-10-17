package com.rgr.fosdem.android.screens.common

import android.content.Context
import android.content.Intent
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient

class CustomWebViewClient(
    private val context: Context,
    private val hideElements: (WebView) -> Unit
): WebViewClient() {
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        if(request?.url?.toString()?.contains("mailto:") == true) {
            context.startActivity(Intent(Intent.ACTION_VIEW, request.url))
            return true
        }
        return true
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        view?.let { hideElements(it) }
    }
}