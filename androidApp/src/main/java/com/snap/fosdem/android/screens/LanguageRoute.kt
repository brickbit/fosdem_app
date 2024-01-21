package com.snap.fosdem.android.screens

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.snap.fosdem.android.R
import com.snap.fosdem.app.state.LanguageState
import com.snap.fosdem.app.viewModel.LanguageViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun LanguageRoute(
    viewModel: LanguageViewModel = koinViewModel(),
    navigateBack: () -> Unit
) {
    val state = viewModel.state.collectAsState().value
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.getLanguages()
    }
    when(state) {
        is LanguageState.LanguageLoaded -> {
            LanguageScreen(
                languages = state.list,
                onClickAction = {
                    viewModel.onChangeLanguage(it)
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                        (context as Activity).recreate()
                    }
                    navigateBack()
                }
            )
        }
        LanguageState.NoLanguages -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = stringResource(R.string.languages_no_languages))
            }
        }
    }
}

@Composable
fun LanguageScreen(
    languages: List<String>,
    onClickAction: (String) -> Unit
) {
    LazyColumn {
        items(languages) { language ->
            LanguageItem(
                language = language,
                onClickAction = onClickAction
            )
        }
    }
}

@Composable
fun LanguageItem(
    language: String,
    onClickAction: (String) -> Unit
) {
    ListItem(
        modifier = Modifier
            .clickable { onClickAction(language) },
        headlineContent = {
            Text(
                text = language,
                style = MaterialTheme.typography.titleSmall
            )
        },
        colors = ListItemDefaults.colors(containerColor = Color.White)
    )
    Divider(color = Color(0xFFCCCCCC))
}