package com.example.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.network.ktor3.KtorNetworkFetcherFactory
import com.example.data.local.createIosAppDatabase
import com.example.data.local.createIosUserPreferences
import com.example.data.model.AppLanguage
import com.example.data.repository.ProductRepository
import com.example.data.platform.IosSharer
import com.example.ui.platform.IosCameraOverlay
import com.example.ui.platform.LocalSharer
import com.example.ui.platform.rememberIosPhotoCapture
import com.example.ui.screens.MainHalalScreen
import com.example.ui.theme.HalalKontrolTheme
import com.example.ui.viewmodel.HalalScannerViewModel

// iOS kök görünümü. Android'deki MainActivity'nin karşılığı: bağımlılıklar burada bağlanır.
@Composable
fun IosRoot() {
    val viewModel = remember { createIosViewModel() }
    val sharer = remember { IosSharer() }
    CompositionLocalProvider(LocalSharer provides sharer) {
        HalalKontrolTheme {
            Surface(modifier = Modifier.fillMaxSize()) {
                MainHalalScreen(
                    viewModel = viewModel,
                    photoCaptureLauncher = rememberIosPhotoCapture { text -> viewModel.onPhotoTextRecognized(text) },
                    cameraScanner = { language: AppLanguage, onBarcodeScanned, onCloseScanner, onOpenManualEntry ->
                        IosCameraOverlay(
                            language = language,
                            onBarcodeScanned = onBarcodeScanned,
                            onCloseScanner = onCloseScanner,
                            onOpenManualEntry = onOpenManualEntry
                        )
                    }
                )
            }
        }
    }
}

private fun createIosViewModel(): HalalScannerViewModel {
    SingletonImageLoader.setSafe { context ->
        ImageLoader.Builder(context)
            .components { add(KtorNetworkFetcherFactory()) }
            .build()
    }
    val repository = ProductRepository(createIosAppDatabase().productDao())
    return HalalScannerViewModel(repository, createIosUserPreferences())
}
