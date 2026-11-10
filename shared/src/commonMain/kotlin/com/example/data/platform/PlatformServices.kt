package com.example.data.platform

// Platforma özel yetenekler için ortak sözleşmeler. Android uygulaması bunları henüz kullanmıyor
// (ekranlar P3'te ortak koda taşınınca bağlanır). iOS gerçekleştirmeleri CI'da derlenir.

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

/** Paylaşım ve e-posta. */
interface Sharer {
    fun shareText(text: String)

    fun sendEmail(address: String, subject: String, body: String)
}
