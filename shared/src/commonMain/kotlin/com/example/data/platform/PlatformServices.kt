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

/** Paylaşım, e-posta, mağaza ve kısa bildirim. */
interface Sharer {
    /** Sistem paylaşım sayfasını açar. [subject] null ise konu eklenmez. */
    fun shareText(subject: String?, text: String, chooserTitle: String)

    /** E-posta uygulamasını açar. Uygulama yoksa false döner. */
    fun sendEmail(address: String, subject: String, body: String): Boolean

    /** Uygulamanın mağaza sayfasını açar (Android: market://, yoksa web). */
    fun openAppStore()

    /** Kısa bildirim (Android: Toast). */
    fun showToast(message: String)
}
