package com.example.ui.platform

import androidx.compose.runtime.Composable

/** Dokunsal geri bildirim. [impact]: hafif buton/aksiyon, [success]: barkod algılandı gibi onay. */
interface AppHaptics {
    fun impact()
    fun success()
}

/** Platforma özel haptik sağlayıcı. Compose bağlamından (LocalView vb.) alınır. */
@Composable
expect fun rememberAppHaptics(): AppHaptics
