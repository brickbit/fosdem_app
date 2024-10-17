package com.rgr.fosdem.android.screens.common

import android.annotation.SuppressLint
import android.webkit.WebView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun CustomWebView(
    url: String
) {
    var loading by rememberSaveable { mutableStateOf(false) }

    AndroidView(
        modifier = Modifier
            .fillMaxSize(),
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                settings.setSupportZoom(false)
                webChromeClient = CustomChromeClient(
                    isLoading = { isLoading -> loading = isLoading }
                )
                webViewClient = CustomWebViewClient(
                    context = context,
                    hideElements = {}
                )
                loadUrl(url)
            }
        }
    )
    if(loading) {
        LoadingScreen()
    }
}