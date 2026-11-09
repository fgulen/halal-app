package com.example.data.model

enum class HalalStatus(val label: String, val description: String) {
    HELAL("Helal", "Güvenle tüketilebilir. İslami usullere uygun helal sertifikalı veya sakıncalı katkı maddesi içermez."),
    HARAM("Haram", "Tüketilmesi kesinlikle uygun değildir! İçeriğinde dinen yasaklanmış maddeler (Domuz türevleri, alkol vb.) bulunmaktadır."),
    SUPHELI("Şüpheli", "Dikkat edilmesi önerilir! İçeriğindeki bazı katkı maddeleri (E471 vb.) hayvansal veya bitkisel kökenli olabilir."),
    BULUNAMADI("Bilinmiyor", "Ürün veritabanımızda henüz kayıtlı değil. İçerik ve E-kodlarını manuel kontrol edebilirsiniz.")
}
