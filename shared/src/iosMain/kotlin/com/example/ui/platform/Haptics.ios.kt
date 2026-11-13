package com.example.ui.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.UIKit.UIImpactFeedbackGenerator
import platform.UIKit.UIImpactFeedbackStyleLight
import platform.UIKit.UINotificationFeedbackGenerator
import platform.UIKit.UINotificationFeedbackTypeSuccess

// Uyarı: Bu dosya yalnızca iOS hedefinde derlenir; Windows'ta doğrulanamaz, CI'da kontrol edilir.
// UIKit geri bildirim üreteçleri ana iş parçacığında çağrılmalı (Compose tıklaması ve
// AVCapture delegesi dispatch_get_main_queue üzerinde çalışır).
// Üreteçler bir kez oluşturulup prepare() ile hazırlanır; her titreşimde yeniden yaratılmaz.
private val impactGenerator: UIImpactFeedbackGenerator by lazy {
    UIImpactFeedbackGenerator(style = UIImpactFeedbackStyleLight).also { it.prepare() }
}

private val notificationGenerator: UINotificationFeedbackGenerator by lazy {
    UINotificationFeedbackGenerator().also { it.prepare() }
}

@Composable
actual fun rememberAppHaptics(): AppHaptics = remember {
    object : AppHaptics {
        override fun impact() {
            impactGenerator.impactOccurred()
            impactGenerator.prepare()
        }

        override fun success() = iosSuccessHaptic()
    }
}

/** Barkod algılama gibi composable dışı iOS çağrıları için. */
fun iosSuccessHaptic() {
    notificationGenerator.notificationOccurred(UINotificationFeedbackTypeSuccess)
    notificationGenerator.prepare()
}
