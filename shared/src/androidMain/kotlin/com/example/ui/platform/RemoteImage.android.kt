package com.example.ui.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageScope
import coil.request.ImageRequest

@Composable
actual fun RemoteImage(
    url: String?,
    contentDescription: String?,
    modifier: Modifier,
    contentScale: ContentScale,
    crossfade: Boolean,
    userAgent: String?,
    loading: (@Composable () -> Unit)?,
    error: (@Composable (Throwable?) -> Unit)?,
) {
    val request = ImageRequest.Builder(LocalContext.current)
        .data(url)
        .apply { if (userAgent != null) setHeader("User-Agent", userAgent) }
        .crossfade(crossfade)
        .build()
    if (loading == null && error == null) {
        AsyncImage(
            model = request,
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = modifier
        )
    } else {
        val loadingSlot: (@Composable SubcomposeAsyncImageScope.(AsyncImagePainter.State.Loading) -> Unit)? =
            loading?.let { slot -> { _ -> slot() } }
        val errorSlot: (@Composable SubcomposeAsyncImageScope.(AsyncImagePainter.State.Error) -> Unit)? =
            error?.let { slot -> { state -> slot(state.result.throwable) } }
        SubcomposeAsyncImage(
            model = request,
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = modifier,
            loading = loadingSlot,
            error = errorSlot
        )
    }
}
