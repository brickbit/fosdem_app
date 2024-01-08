package com.snap.fosdem.android.screens.splash

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.snap.fosdem.app.viewModel.OnBoardingViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun OnBoardingRoute(
    viewModel: OnBoardingViewModel = koinViewModel(),
    onNavigate: () -> Unit
) {
    Column {
        Text(text = "OnBoarding")
        Button(onClick = { onNavigate() }) {
            Text(text = "Next")
        }
    }
}