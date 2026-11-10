package com.example.data.platform

import io.ktor.http.encodeURLParameter
import platform.Foundation.NSURL
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication

// UIActivityViewController ile paylaşım; mailto için UIApplication.openURL.
// Uyarı: Bu dosya yalnızca iOS hedefinde derlenir; Windows'ta doğrulanamaz, CI'da kontrol edilir.
class IosSharer : Sharer {

    override fun shareText(text: String) {
        val controller = UIActivityViewController(activityItems = listOf(text), applicationActivities = null)
        UIApplication.sharedApplication.keyWindow?.rootViewController
            ?.presentViewController(controller, animated = true, completion = null)
    }

    override fun sendEmail(address: String, subject: String, body: String) {
        val url = NSURL.URLWithString("mailto:$address?subject=${subject.encodeURLParameter()}&body=${body.encodeURLParameter()}") ?: return
        UIApplication.sharedApplication.openURL(url, options = emptyMap<Any?, Any>(), completionHandler = null)
    }
}
