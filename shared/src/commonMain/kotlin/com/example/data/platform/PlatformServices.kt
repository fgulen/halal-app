package com.example.data.platform

// Platforma özel yetenekler için ortak sözleşmeler. Android gerçekleştirmeleri androidMain'de,
// iOS gerçekleştirmeleri iosMain'de (yalnız CI'da derlenir).

/** Kamera ile barkod okuma. Kamera önizlemesi UI tarafında bağlanır. */
interface BarcodeScanner {
    /** Her okunan barkod için [onBarcode] çağrılır. Dinleme [stop] çağrılana kadar sürer. */
    fun start(onBarcode: (String) -> Unit)

    fun stop()
}

/** Fotoğraftan metin okuma (içindekiler listesi). */
interface TextRecognizer {
    suspend fun recognize(jpegBytes: ByteArray): String
}

/** Paylaşım, e-posta ve kısa bildirim. */
interface Sharer {
    /** Sistem paylaşım sayfasını açar. */
    fun shareText(subject: String, text: String, chooserTitle: String)

    /** E-posta uygulamasını açar. Uygulama yoksa false döner. */
    fun sendEmail(address: String, subject: String, body: String): Boolean

    /** Kısa bildirim (Android: Toast). */
    fun showToast(message: String)
}
