package com.example.ui.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.compose.LocalPlatformContext
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageScope
import coil3.request.ImageRequest

// iOS görseli: Coil 3 (ağ: Ktor). Ağ yükleyici IosRoot'ta kurulur.
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
    val request = ImageRequest.Builder(LocalPlatformContext.current)
        .data(url)
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
