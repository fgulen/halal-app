package com.example.ui.platform

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale

// Yer tutucu: görsel yükleyici bu hedefte henüz bağlı değil.
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
    Box(modifier = modifier)
}
