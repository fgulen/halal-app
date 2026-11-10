@file:OptIn(ExperimentalForeignApi::class)

package com.example.ui.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import com.example.data.platform.TextRecognizer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import platform.Foundation.NSData
import platform.Foundation.create
import platform.UIKit.UIApplication
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIImagePickerControllerDelegateProtocol
import platform.UIKit.UIImagePickerControllerOriginalImage
import platform.UIKit.UIImagePickerControllerSourceType
import platform.UIKit.UINavigationControllerDelegateProtocol
import platform.UIKit.UIViewController
import platform.Vision.VNImageRequestHandler
import platform.Vision.VNRecognizeTextRequest
import platform.Vision.VNRecognizedText
import platform.Vision.VNRecognizedTextObservation
import platform.Vision.VNRequestTextRecognitionLevelAccurate
import platform.darwin.NSObject
import platform.posix.memcpy
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

// Fotoğraftan içindekiler: UIImagePickerController (kamera, yoksa galeri) + Vision metin tanıma.
// Uyarı: Yalnızca iOS hedefinde derlenir; CI'da kontrol edilir, cihazda elle denenmeli.

/** Fotoğraf çekme eylemi. Metin tanınınca [onTextRecognized] çağrılır. */
@Composable
fun rememberIosPhotoCapture(onTextRecognized: (String) -> Unit): () -> Unit {
    val callback by rememberUpdatedState(onTextRecognized)
    val scope = rememberCoroutineScope()
    return remember {
        {
            presentPicker { jpeg ->
                if (jpeg != null) {
                    scope.launch {
                        val text = IosTextRecognizer().recognize(jpeg)
                        callback(text)
                    }
                }
            }
        }
    }
}

class IosTextRecognizer : TextRecognizer {
    override suspend fun recognize(jpegBytes: ByteArray): String = withContext(Dispatchers.Default) {
        val data = jpegBytes.toNSData()
        suspendCoroutine { continuation ->
            val request = VNRecognizeTextRequest { request, _ ->
                val lines = (request?.results ?: emptyList<Any?>())
                    .filterIsInstance<VNRecognizedTextObservation>()
                    .mapNotNull { observation -> (observation.topCandidates(1u).firstOrNull() as? VNRecognizedText)?.string }
                continuation.resume(lines.joinToString("\n"))
            }
            request.recognitionLevel = VNRequestTextRecognitionLevelAccurate
            request.recognitionLanguages = listOf("tr", "de", "fr", "en")
            val handler = VNImageRequestHandler(data = data, options = emptyMap<Any?, Any>())
            handler.performRequests(listOf(request), null)
        }
    }
}

// Delegate'in yaşam süresi: picker kapanana kadar tutulmalı.
private var activePickerDelegate: PickerDelegate? = null

private fun presentPicker(onImage: (ByteArray?) -> Unit) {
    val presenter = topViewController() ?: run {
        onImage(null)
        return
    }
    val picker = UIImagePickerController()
    picker.sourceType =
        if (UIImagePickerController.isSourceTypeAvailable(UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypeCamera)) {
            UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypeCamera
        } else {
            UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypePhotoLibrary
        }
    val delegate = PickerDelegate(onImage)
    activePickerDelegate = delegate
    picker.delegate = delegate
    presenter.presentViewController(picker, animated = true, completion = null)
}

private fun topViewController(): UIViewController? {
    var top = UIApplication.sharedApplication.keyWindow?.rootViewController
    while (top?.presentedViewController != null) {
        top = top.presentedViewController
    }
    return top
}

private class PickerDelegate(private val onImage: (ByteArray?) -> Unit) :
    NSObject(), UIImagePickerControllerDelegateProtocol, UINavigationControllerDelegateProtocol {

    override fun imagePickerController(picker: UIImagePickerController, didFinishPickingMediaWithInfo: Map<Any?, *>) {
        val image = didFinishPickingMediaWithInfo[UIImagePickerControllerOriginalImage] as? UIImage
        val jpeg = image?.let { UIImageJPEGRepresentation(it, 0.8) }?.toByteArray()
        picker.dismissViewControllerAnimated(true, completion = null)
        activePickerDelegate = null
        onImage(jpeg)
    }

    override fun imagePickerControllerDidCancel(picker: UIImagePickerController) {
        picker.dismissViewControllerAnimated(true, completion = null)
        activePickerDelegate = null
        onImage(null)
    }
}

private fun ByteArray.toNSData(): NSData = usePinned { pinned ->
    NSData.create(bytes = pinned.addressOf(0), length = size.toULong())
}

private fun NSData.toByteArray(): ByteArray {
    val length = this.length.toInt()
    val out = ByteArray(length)
    if (length > 0) {
        out.usePinned { pinned -> memcpy(pinned.addressOf(0), this.bytes, this.length) }
    }
    return out
}
