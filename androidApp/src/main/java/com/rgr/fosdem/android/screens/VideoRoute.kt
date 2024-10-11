package com.rgr.fosdem.android.screens

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rgr.fosdem.android.screens.common.LoadingScreen
import com.rgr.fosdem.android.screens.common.shimmerEffect
import com.rgr.fosdem.app.viewModel.VideoViewModel
import com.rgr.fosdem.domain.model.bo.VideoBo
import org.koin.androidx.compose.koinViewModel

@Composable
fun VideoRoute(
    viewModel: VideoViewModel = koinViewModel()
) {
    val state = viewModel.state.collectAsState().value
    VideoScreen(
        isLoading = state.isLoading,
        videos = state.videos,
        videosByType = state.videosForType
    )
}

@Composable
fun VideoScreen(
    isLoading: Boolean,
    videos: List<VideoBo>,
    videosByType: List<Pair<String,List<VideoBo>>>
) {
    if(isLoading) {
        LoadingScreen()
    } else {
        LazyColumn {
            item {
                if(videos.isNotEmpty()) {
                    VideoTitleThumbnailLoading(videos[0])
                }
            }
            items(videosByType) { videoType ->
                Column (
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 24.dp)
                ) {
                    Text(
                        modifier = Modifier.padding(top= 16.dp, bottom = 8.dp),
                        color = Color.Black,
                        text = videoType.first.uppercase()
                    )
                    val videoTypeToPaint = if(videoType.second.size >7) videoType.second.toMutableList().subList(0, 6) else videoType.second
                    LazyRow {
                        items(videoTypeToPaint) { videos ->
                            VideoThumbnailLoading(videos)
                        }
                    }
                }
            }
        }
    }
}

fun Context.getVideoThumbnail(url: String): Bitmap? {

    val retriever = MediaMetadataRetriever()
    retriever.setDataSource(this, Uri.parse(Uri.encode(url)))

    val timeUs = 5*1000000L
    val thumbnailBitmap = retriever.getFrameAtTime(timeUs)

    retriever.release()
    return thumbnailBitmap
}

@Composable
fun VideoThumbnailLoading(
    video: VideoBo
) {
    val context = LocalContext.current
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(Unit) {
        bitmap.value =
            context.getVideoThumbnail(video.link)
    }
    Box(
        modifier = Modifier
            .size(200.dp, 120.dp).padding(end = 8.dp),
        contentAlignment = Alignment.BottomStart
    ) {
        bitmap.value?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(20.dp))
            )
        } ?: Box(
            modifier = Modifier
                .size(200.dp, 120.dp)
                .clip(RoundedCornerShape(20.dp))
                .shimmerEffect(),
        )
        Box(
            modifier = Modifier
                .width(200.dp)
                .height(36.dp)
                .background(Color.Black.copy(alpha = 0.6f))
                .clip(RoundedCornerShape(20.dp,20.dp,20.dp,20.dp))
        ) {
            Text(
                modifier = Modifier
                    .padding(top = 8.dp, start = 8.dp),
                color = Color.White,
                text = video.name,
                fontSize = 10.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
fun VideoTitleThumbnailLoading(
    video: VideoBo
) {
    val context = LocalContext.current
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(Unit) {
        bitmap.value =
            context.getVideoThumbnail(video.link)
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        contentAlignment = Alignment.BottomStart
    ) {
        bitmap.value?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )
        } ?: Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .shimmerEffect(),
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.6f))
                .padding(8.dp),
            color = Color.White,
            text = video.name,
            fontSize = 24.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
