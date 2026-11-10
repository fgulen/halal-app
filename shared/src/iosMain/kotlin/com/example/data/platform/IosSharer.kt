package com.example.data.platform

import io.ktor.http.encodeURLParameter
import platform.Foundation.NSURL
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication

// UIActivityViewController ile paylaşım; mailto için UIApplication.openURL.
// Uyarı: Bu dosya yalnızca iOS hedefinde derlenir; Windows'ta doğrulanamaz, CI'da kontrol edilir.
class IosSharer : Sharer {

    override fun shareText(subject: String, text: String, chooserTitle: String) {
        val controller = UIActivityViewController(activityItems = listOf(text), applicationActivities = null)
        UIApplication.sharedApplication.keyWindow?.rootViewController
            ?.presentViewController(controller, animated = true, completion = null)
    }

    override fun sendEmail(address: String, subject: String, body: String): Boolean {
        val url = NSURL.URLWithString(
            "mailto:$address?subject=${subject.encodeURLParameter()}&body=${body.encodeURLParameter()}"
        ) ?: return false
        UIApplication.sharedApplication.openURL(url, options = emptyMap<Any?, Any>(), completionHandler = null)
        return true
    }

    // iOS'ta Toast yok; şimdilik sessiz. Gerekirse UIAlertController ile değiştirilir.
    override fun showToast(message: String) = Unit
}
