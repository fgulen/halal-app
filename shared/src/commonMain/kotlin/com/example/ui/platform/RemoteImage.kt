package com.example.ui.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale

// Uzak görsel. Android: Coil 2 (uygulamanın mevcut görsel yükleyicisi). iOS: şimdilik yer tutucu,
// Coil 3 ile gerçekleştirilecek. Hata/yükleme slotları çağıranın kendi UI'ı olarak verilir.
@Composable
expect fun RemoteImage(
    url: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
    crossfade: Boolean = true,
    userAgent: String? = null,
    loading: (@Composable () -> Unit)? = null,
    error: (@Composable (Throwable?) -> Unit)? = null,
)
