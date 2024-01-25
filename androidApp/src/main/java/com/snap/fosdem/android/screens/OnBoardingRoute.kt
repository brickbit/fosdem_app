package com.snap.fosdem.android.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.snap.fosdem.android.R
import com.snap.fosdem.android.mainBrushColor
import com.snap.fosdem.app.viewModel.OnBoardingViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun OnBoardingRoute(
    viewModel: OnBoardingViewModel = koinViewModel(),
    onNavigate: () -> Unit
) {
    OnBoardingScreen(
        onContinueClicked = {
            viewModel.saveOnBoarding()
            onNavigate()
        },
        onSkipClicked = {
            viewModel.saveOnBoarding()
            onNavigate()
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnBoardingScreen(
    onContinueClicked: () -> Unit,
    onSkipClicked: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    Box(
        contentAlignment = Alignment.BottomCenter
    ) {
        HorizontalPager(
            modifier = Modifier.fillMaxSize(),
            state = pagerState
        ) { page ->
            when(page) {
                0 -> ContentForPage(
                    title = stringResource(R.string.on_boarding_title_1),
                    description = stringResource(R.string.on_boarding_description_1),
                    onSkipClicked = onSkipClicked
                )
                1 -> ContentForPage(
                    title = stringResource(R.string.on_boarding_title_2),
                    description = stringResource(R.string.on_boarding_description_2),
                    onSkipClicked = onSkipClicked
                )
                else -> ContentForPage(
                    title = stringResource(R.string.on_boarding_title_3),
                    description = stringResource(R.string.on_boarding_description_3),
                    hasButton = true,
                    onContinueClicked = onContinueClicked,
                    onSkipClicked = onSkipClicked
                )
            }

        }
        OnBoardingDots(pagerState = pagerState)
    }
}

@Composable
fun ContentForPage(
    title: String,
    description: String,
    hasButton: Boolean = false,
    onContinueClicked: () -> Unit = {},
    onSkipClicked: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.End
    ) {
        Text(
            modifier = Modifier
                .padding(16.dp)
                .clickable{ onSkipClicked() },
            text = stringResource(R.string.favourite_skip),
            style = MaterialTheme.typography.titleSmall.copy(Color.Gray)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.size(dimensionResource(id = R.dimen.splash_image_size)),
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = stringResource(R.string.splash_fosdem_logo_description)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
            if (hasButton) {
                Text(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .background(
                            brush = Brush.linearGradient(colorStops = mainBrushColor),
                            shape = CircleShape
                        )
                        .padding(vertical = 12.dp, horizontal = 32.dp)
                        .clickable { onContinueClicked() },
                    text = stringResource(R.string.on_boarding_next_button),
                    style = MaterialTheme.typography.titleSmall.copy(Color.White)
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnBoardingDots(
    pagerState: PagerState
) {
    Row(
        Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(bottom = 32.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(pagerState.pageCount) { iteration ->
            val color = if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .clip(CircleShape)
                    .background(color)
                    .size(8.dp)
            )

        }
    }
}