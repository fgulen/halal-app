@file:OptIn(ExperimentalForeignApi::class)

package com.example.ui.platform

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.UIKitView
import com.example.data.model.AppLanguage
import com.example.data.model.AppStrings
import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFoundation.AVCaptureConnection
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVCaptureDeviceInput
import platform.AVFoundation.AVCaptureMetadataOutput
import platform.AVFoundation.AVCaptureMetadataOutputObjectsDelegateProtocol
import platform.AVFoundation.AVCaptureOutput
import platform.AVFoundation.AVCaptureSession
import platform.AVFoundation.AVCaptureVideoPreviewLayer
import platform.AVFoundation.AVLayerVideoGravityResizeAspectFill
import platform.AVFoundation.AVMediaTypeVideo
import platform.AVFoundation.AVMetadataMachineReadableCodeObject
import platform.AVFoundation.AVMetadataObjectTypeCode128Code
import platform.AVFoundation.AVMetadataObjectTypeEAN13Code
import platform.AVFoundation.AVMetadataObjectTypeEAN8Code
import platform.AVFoundation.AVMetadataObjectTypeUPCECode
import platform.CoreGraphics.CGRect
import platform.CoreGraphics.CGRectMake
import platform.UIKit.UIView
import platform.darwin.NSObject
import platform.darwin.dispatch_get_main_queue

// iOS barkod tarayıcısı: AVFoundation metadata çıktısı, UIKit önizlemesi Compose içinde (UIKitView).
// Uyarı: Yalnızca iOS hedefinde derlenir; CI'da kontrol edilir, cihazda elle denenmeli.
@Composable
fun IosBarcodeCamera(modifier: Modifier, onBarcodeScanned: (String) -> Unit) {
    val holder = remember { ScannerHolder() }
    UIKitView(
        factory = { holder.create(onBarcodeScanned) },
        modifier = modifier,
        onRelease = { holder.stop() }
    )
}

@Composable
fun IosCameraOverlay(
    language: AppLanguage,
    onBarcodeScanned: (String) -> Unit,
    onCloseScanner: () -> Unit,
    onOpenManualEntry: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        IosBarcodeCamera(modifier = Modifier.fillMaxSize(), onBarcodeScanned = onBarcodeScanned)
        Row(
            modifier = Modifier.align(Alignment.TopEnd).padding(16.dp)
        ) {
            Button(onClick = onCloseScanner) { Text(AppStrings.getClose(language)) }
        }
        Column(
            modifier = Modifier.align(Alignment.BottomCenter).padding(24.dp)
        ) {
            TextButton(onClick = onOpenManualEntry) {
                Text(AppStrings.getManualBarcodeTitle(language))
            }
        }
    }
}

private class ScannerHolder {
    private var session: AVCaptureSession? = null
    private var delegate: MetadataDelegate? = null
    private var container: PreviewContainer? = null

    fun create(onCode: (String) -> Unit): UIView {
        val view = PreviewContainer()
        container = view
        val device = AVCaptureDevice.defaultDeviceWithMediaType(AVMediaTypeVideo) ?: return view
        val input = AVCaptureDeviceInput.deviceInputWithDevice(device, null) ?: return view
        val captureSession = AVCaptureSession()
        if (captureSession.canAddInput(input)) captureSession.addInput(input)
        val output = AVCaptureMetadataOutput()
        if (captureSession.canAddOutput(output)) captureSession.addOutput(output)
        val metadataDelegate = MetadataDelegate(onCode)
        output.setMetadataObjectsDelegate(metadataDelegate, dispatch_get_main_queue())
        output.metadataObjectTypes = listOf(
            AVMetadataObjectTypeEAN13Code,
            AVMetadataObjectTypeEAN8Code,
            AVMetadataObjectTypeUPCECode,
            AVMetadataObjectTypeCode128Code
        )
        val preview = AVCaptureVideoPreviewLayer(session = captureSession)
        preview.videoGravity = AVLayerVideoGravityResizeAspectFill
        view.layer.addSublayer(preview)
        view.previewLayer = preview
        session = captureSession
        delegate = metadataDelegate
        captureSession.startRunning()
        return view
    }

    fun stop() {
        session?.stopRunning()
        session = null
        delegate = null
        container = null
    }
}

private class PreviewContainer : UIView(frame = CGRectMake(0.0, 0.0, 0.0, 0.0)) {
    var previewLayer: AVCaptureVideoPreviewLayer? = null

    override fun layoutSubviews() {
        super.layoutSubviews()
        previewLayer?.frame = bounds
    }
}

private class MetadataDelegate(private val onCode: (String) -> Unit) :
    NSObject(), AVCaptureMetadataOutputObjectsDelegateProtocol {

    private var lastCode: String? = null

    override fun captureOutput(
        output: AVCaptureOutput,
        didOutputMetadataObjects: List<*>,
        fromConnection: AVCaptureConnection
    ) {
        val code = didOutputMetadataObjects
            .filterIsInstance<AVMetadataMachineReadableCodeObject>()
            .firstOrNull()
            ?.stringValue
            ?: return
        if (code == lastCode) return
        lastCode = code
        onCode(code)
    }
}
