package com.example.ui.camera

import android.annotation.SuppressLint
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

class BarcodeAnalyzer(
    private val onBarcodeDetected: (String) -> Unit
) : ImageAnalysis.Analyzer {

    // Retail product barcodes only. Packaging often carries a second code nearby (a QR
    // "karekod" for recipes/promos, a Pfand/deposit code, an ITF-14 case code) and scanning
    // whichever ML Kit happens to report first produced a fast, confusing "not found" instead
    // of the intended product. Excluding QR/DataMatrix/ITF/etc. stops those from matching at all.
    private val options = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(
            Barcode.FORMAT_EAN_13,
            Barcode.FORMAT_EAN_8,
            Barcode.FORMAT_UPC_A,
            Barcode.FORMAT_UPC_E
        )
        .build()
    private val scanner = BarcodeScanning.getClient(options)

    @Volatile
    private var isScanningEnabled = true

    fun setScanningEnabled(enabled: Boolean) {
        isScanningEnabled = enabled
    }

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        if (!isScanningEnabled) {
            imageProxy.close()
            return
        }

        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    if (isScanningEnabled) {
                        // When more than one barcode is in frame (e.g. a small case/outer code
                        // next to the main product barcode), the one the user is actually
                        // pointing at is physically the largest in the reticle - not necessarily
                        // first in ML Kit's result list.
                        val best = barcodes
                            .filter { !it.rawValue.isNullOrBlank() }
                            .maxByOrNull { b -> b.boundingBox?.let { it.width() * it.height() } ?: 0 }
                        val rawValue = best?.rawValue
                        if (!rawValue.isNullOrBlank()) {
                            isScanningEnabled = false
                            onBarcodeDetected(rawValue)
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Log.w("BarcodeAnalyzer", "Frame analysis failed: ${e.message}")
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        } else {
            imageProxy.close()
        }
    }
}
