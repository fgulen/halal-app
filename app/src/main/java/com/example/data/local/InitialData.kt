package com.example.data.local

import com.example.data.model.EAdditive
import com.example.data.model.HalalStatus

object InitialData {
    val sampleProducts = listOf(
        // HARAM PRODUCTS
        ProductEntity(
            barcode = "4001686301265",
            name = "Haribo Goldbären (İthal Jelibon)",
            brand = "Haribo",
            category = "Şekerleme & Jelibon",
            status = HalalStatus.HARAM,
            halalCertificate = null,
            harmfulOrSuspiciousIngredients = listOf("Domuz Jelatini (E441 / Pork Gelatine)"),
            allIngredients = listOf(
                "Glikoz şurubu",
                "Şeker",
                "Domuz Jelatini (E441)",
                "Dekstroz",
                "Meyve suyu konsantresi",
                "Sitrik asit (E330)",
                "Balmumu (E901)",
                "Karnauba mumu (E903)"
            ),
            reasonOrDetails = "Bu ürünün Avrupa üretimlerinde domuz derisi ve kemiklerinden elde edilen hayvansal Domuz Jelatini (E441) kullanılmaktadır. İslami fıkha göre domuzun hiçbir parçası helal kabul edilmez.",
            alternatives = listOf(
                "Haribo Türkiye Üretimi (Sığır Jelatinli - Helal Sertifikalı)",
                "Bebeto Jelibon (Helal Sertifikalı)",
                "Koska Lokum & Meyveli Şekerleme"
            ),
            imageUrl = null
        ),
        ProductEntity(
            barcode = "7622210449283",
            name = "Milka Daim Çikolata (Alkol Aromalı)",
            brand = "Milka",
            category = "Çikolata",
            status = HalalStatus.HARAM,
            halalCertificate = null,
            harmfulOrSuspiciousIngredients = listOf("Etil Alkol (Alkol / Likör Aroması)", "E476 (Şüpheli Emülgatör)"),
            allIngredients = listOf(
                "Şeker",
                "Kakao yağı",
                "Yağsız süt tozu",
                "Kakao kitlesi",
                "Tereyağı",
                "Etil Alkol / Likör Özütü",
                "Soya lesitini",
                "Poligliserol polirisinoleat (E476)"
            ),
            reasonOrDetails = "İçeriğinde doğrudan etil alkol / likör aroması bulunmaktadır. İslam dini sarhoşluk veren her türlü alkollü maddeyi ve türevlerini haram kılmıştır.",
            alternatives = listOf(
                "Eti Karam %70 Bitter Çikolata (TSE Helal Sertifikalı)",
                "Torku Sütlü Çikolata (GİMDES Helal Sertifikalı)",
                "Ülker Çikolatalı Gofret"
            ),
            imageUrl = null
        ),
        ProductEntity(
            barcode = "3017620422003",
            name = "Kırmızı Meyveli Şekerleme & Sakız",
            brand = "TuttiFrutti",
            category = "Şekerleme",
            status = HalalStatus.HARAM,
            halalCertificate = null,
            harmfulOrSuspiciousIngredients = listOf("Karmin / Cochineal (E120 Böcek Boyası)", "E471 (Mono ve Digliseritler)"),
            allIngredients = listOf(
                "Şeker",
                "Sakız mayası",
                "Glikoz şurubu",
                "Renklendirici: Karmin (E120)",
                "Emülgatör (E471)",
                "Aroma verici"
            ),
            reasonOrDetails = "Renklendirici olarak kullanılan Karmin (E120), ezilmiş kurutulmuş kalkan biti (Dactylopius coccus) böceklerinden üretilir. Helal gıda fıkhına ve sertifikasyon kuruluşlarına göre böcek kökenli katkı maddeleri haram kabul edilmektedir.",
            alternatives = listOf(
                "Pancar Kırmızısı (E162) ile renklendirilmiş helal şekerlemeler",
                "Falım Sakız (Katkısız)",
                "Kent Meybon (Helal Uygunluk Belgeli)"
            ),
            imageUrl = null
        ),
        ProductEntity(
            barcode = "8690504018241",
            name = "SweetLand İthal Marshmallow",
            brand = "SweetLand",
            category = "Şekerleme",
            status = HalalStatus.HARAM,
            halalCertificate = null,
            harmfulOrSuspiciousIngredients = listOf("Domuz Jelatini (Porcine Gelatin E441)"),
            allIngredients = listOf(
                "Glikoz şurubu",
                "Şeker",
                "Domuz Jelatini (E441)",
                "Mısır nişastası",
                "Doğal vanilya aroması"
            ),
            reasonOrDetails = "Ürün içeriğinde domuz jelatini kullanıldığı üretici ambalajında açıkça belirtilmiştir.",
            alternatives = listOf(
                "Helal Sertifikalı Türk Marshmallowları",
                "Tadelle & Sarelle Ürünleri",
                "Hacı Şerif Lokum Çeşitleri"
            ),
            imageUrl = null
        ),

        // SUSPICIOUS (ŞÜPHELİ) PRODUCTS
        ProductEntity(
            barcode = "8690637012345",
            name = "Peynir & Baharat Çeşnili Cips",
            brand = "ÇıtırCips",
            category = "Atıştırmalık & Cips",
            status = HalalStatus.SUPHELI,
            halalCertificate = null,
            harmfulOrSuspiciousIngredients = listOf(
                "E471 (Yağ Asitlerinin Mono ve Digliseritleri - Kaynağı Belirsiz)",
                "Peynir Altı Suyu Tozu (Hayvansal Rennet Mayası Şüphesi)"
            ),
            allIngredients = listOf(
                "Mısır unu",
                "Bitkisel yağ (Palm)",
                "Peynir altı suyu tozu",
                "Tuz",
                "Emülgatör: E471",
                "Lezzet artırıcı (E621)",
                "Baharat çeşnisi"
            ),
            reasonOrDetails = "E471 katkı maddesi bitkisel veya hayvansal (domuz/sığır) yağlardan üretilebilir. Pakette 'Bitkisel kökenli' ibaresi veya yetkili bir kurumdan Helal Sertifikası yer almamaktadır.",
            alternatives = listOf(
                "Tadım Fırınlanmış Cips",
                "Patos Helal Sertifikalı Serisi",
                "Ev yapımı fırınlanmış lavaş cipsi"
            ),
            imageUrl = null
        ),
        ProductEntity(
            barcode = "8690526011122",
            name = "Kremalı Sandviç Bisküvi (İthal)",
            brand = "SnackCo",
            category = "Bisküvi & Gofret",
            status = HalalStatus.SUPHELI,
            halalCertificate = null,
            harmfulOrSuspiciousIngredients = listOf(
                "E471 (Mono ve Digliseritler)",
                "E472e (Diasetil Tartarik Asit Esterleri)",
                "E904 (Gomalak / Shellac Böcek Salgısı)"
            ),
            allIngredients = listOf(
                "Buğday unu",
                "Şeker",
                "Karma bitkisel ve hayvansal katı yağ",
                "Emülgatörler (E471, E472e)",
                "Kabartıcılar (E500, E503)",
                "Parlatıcı: E904 (Shellac)"
            ),
            reasonOrDetails = "E471 ve E472e emülgatörlerinin hayvansal kaynaklı olma riski bulunmaktadır. Ayrıca parlaklık verici E904 (Gomalak) böcek salgısından elde edildiği için şüpheli kategoridedir.",
            alternatives = listOf(
                "Eti Burçak Bisküvi",
                "Torku Tam Ruşeymli Bisküvi",
                "Ülker Biskrem (Helal belgeli)"
            ),
            imageUrl = null
        ),
        ProductEntity(
            barcode = "8690123456789",
            name = "Tavuk Aromalı Hazır Çorba",
            brand = "SoupMaster",
            category = "Hazır Gıda",
            status = HalalStatus.SUPHELI,
            halalCertificate = null,
            harmfulOrSuspiciousIngredients = listOf(
                "E631 (Disodyum İnosinat - Hayvansal Köken Şüphesi)",
                "Tavuk Aroması (Helal Kesim Belirsiz)"
            ),
            allIngredients = listOf(
                "Mısır nişastası",
                "İyotlu tuz",
                "Tavuk yağı ve aroması",
                "Lezzet artırıcılar (E621, E627, E631)",
                "Zerdeçal"
            ),
            reasonOrDetails = "E631 lezzet artırıcısı genellikle hayvansal et dokularından izole edilir. Tavuk etinin İslami usullere uygun helal kesim olup olmadığı belirsizdir.",
            alternatives = listOf(
                "Knorr Türkiye Helal Standartlı Çorbalar",
                "GİMDES Sertifikalı Hazır Çorbalar",
                "Geleneksel Ev Tarhanası"
            ),
            imageUrl = null
        ),
        ProductEntity(
            barcode = "8690000000012",
            name = "Dilimli Tost Ekmeği (L-Sistein Şüphesi)",
            brand = "BakeryPlus",
            category = "Fırın Ürünleri",
            status = HalalStatus.SUPHELI,
            halalCertificate = null,
            harmfulOrSuspiciousIngredients = listOf(
                "E920 (L-Sistein Un Geliştirici - İnsan saçı/hayvan kılı kaynağı)",
                "E471 (Emülgatör)"
            ),
            allIngredients = listOf(
                "Buğday unu",
                "İçme suyu",
                "Ekmek mayası",
                "Bitkisel margarin",
                "Emülgatör (E471)",
                "Un işlem maddesi: E920 (L-Sistein)"
            ),
            reasonOrDetails = "E920 (L-Sistein), un hamurunun elastikiyetini artırmak için kullanılır ve çoğunlukla insan saçı, domuz kılı veya ördek tüyünden kimyasal yolla elde edilir. Sentetik veya mikrobiyal kökenli olduğu belgelenmelidir.",
            alternatives = listOf(
                "Halk Ekmek Katkısız Ekmek Çeşitleri",
                "GİMDES Sertifikalı Un ve Ekmekler",
                "Katkısız Ekşi Mayalı Köy Ekmeği"
            ),
            imageUrl = null
        ),

        // HELAL CERTIFIED & SAFE PRODUCTS
        ProductEntity(
            barcode = "8690526055554",
            name = "Eti Karam %70 Kakaolu Bitter Çikolata",
            brand = "Eti",
            category = "Çikolata",
            status = HalalStatus.HELAL,
            halalCertificate = "TSE Helal Sertifikası (TS OIC/SMIIC 1)",
            harmfulOrSuspiciousIngredients = emptyList(),
            allIngredients = listOf(
                "Kakao kitlesi",
                "Şeker",
                "Kakao yağı",
                "Kakao tozu",
                "Emülgatör (Bitkisel Soya Lesitini E322)",
                "Doğal vanilya aroması"
            ),
            reasonOrDetails = "TSE Helal Uygunluk Sertifikasına sahiptir. İçeriğinde kullanılan emülgatör %100 bitkisel soya lesitinidir. Hiçbir hayvansal katkı, domuz yağı veya alkol türevi içermez.",
            alternatives = emptyList(),
            imageUrl = null
        ),
        ProductEntity(
            barcode = "8690637000001",
            name = "Torku Banada Kakaolu Fındık Kreması",
            brand = "Torku",
            category = "Kahvaltılık",
            status = HalalStatus.HELAL,
            halalCertificate = "GİMDES Helal ve Tayyib Sertifikalı",
            harmfulOrSuspiciousIngredients = emptyList(),
            allIngredients = listOf(
                "Pancar şekeri (%100 Doğal)",
                "Fındık (%13.5)",
                "Bitkisel yağ (Ayçiçek, Pamuk)",
                "Yağsız süt tozu",
                "Kakao tozu (%7)",
                "Emülgatör (Bitkisel Ayçiçek Lesitini)"
            ),
            reasonOrDetails = "GİMDES Helal ve Tayyib Sertifikasına sahiptir. %100 yerli şeker pancarından üretilmiştir. Glikoz şurubu, domuz türevi, alkol veya şüpheli emülgatör içermez.",
            alternatives = emptyList(),
            imageUrl = null
        ),
        ProductEntity(
            barcode = "8690767112233",
            name = "Sütaş Doğal Günlük Süt",
            brand = "Sütaş",
            category = "Süt & Süt Ürünleri",
            status = HalalStatus.HELAL,
            halalCertificate = "TSE Helal Sertifikası (HAK Akrediteli)",
            harmfulOrSuspiciousIngredients = emptyList(),
            allIngredients = listOf(
                "Pastörize İnek Sütü (%100 Doğal ve Katkısız)"
            ),
            reasonOrDetails = "TSE Helal Sertifikalıdır. Hiçbir koruyucu, kıvam artırıcı veya yabancı katkı maddesi içermeyen saf süt.",
            alternatives = emptyList(),
            imageUrl = null
        ),
        ProductEntity(
            barcode = "8690504000116",
            name = "Ülker Çikolatalı Gofret",
            brand = "Ülker",
            category = "Bisküvi & Gofret",
            status = HalalStatus.HELAL,
            halalCertificate = "TSE Helal Uygunluk Belgesi",
            harmfulOrSuspiciousIngredients = emptyList(),
            allIngredients = listOf(
                "Şeker",
                "Buğday unu",
                "Bitkisel yağlar (Palm, Palm Çekirdeği)",
                "Fındık püresi",
                "Kakao yağı ve kitlesi",
                "Peyniraltı suyu tozu",
                "Emülgatörler (Bitkisel Soya Lesitini, E476 Bitkisel PGPR)"
            ),
            reasonOrDetails = "TSE Helal Belgeli tesislerde üretilmektedir. Kullanılan yağlar ve emülgatörler tamamen bitkisel kaynaklıdır.",
            alternatives = emptyList(),
            imageUrl = null
        ),
        ProductEntity(
            barcode = "8690637889900",
            name = "Pınar Doğal Yoğurt",
            brand = "Pınar",
            category = "Süt Ürünleri",
            status = HalalStatus.HELAL,
            halalCertificate = "TSE Helal Sertifikası",
            harmfulOrSuspiciousIngredients = emptyList(),
            allIngredients = listOf(
                "Pastörize İnek Sütü",
                "Canlı Yoğurt Kültürü"
            ),
            reasonOrDetails = "Jelatin, nişasta ve kimyasal kıvam artırıcı içermez. %100 doğal maya ile mayalanmıştır.",
            alternatives = emptyList(),
            imageUrl = null
        ),
        ProductEntity(
            barcode = "8690123999999",
            name = "Peyman Dor Leo Antep Fıstığı",
            brand = "Peyman",
            category = "Kuruyemiş",
            status = HalalStatus.HELAL,
            halalCertificate = "GİMDES & TSE Helal Uygunluk",
            harmfulOrSuspiciousIngredients = emptyList(),
            allIngredients = listOf(
                "Kavrulmuş Antep Fıstığı",
                "Deniz Tuzu"
            ),
            reasonOrDetails = "Doğal kavrulmuş kuruyemiş, hiçbir yapay katkı maddesi veya koruyucu içermez.",
            alternatives = emptyList(),
            imageUrl = null
        ),
        ProductEntity(
            barcode = "8690777000123",
            name = "Çaykur Rize Turist Çayı",
            brand = "Çaykur",
            category = "İçecek & Çay",
            status = HalalStatus.HELAL,
            halalCertificate = "TSE Helal Sertifikalı",
            harmfulOrSuspiciousIngredients = emptyList(),
            allIngredients = listOf(
                "%100 Doğal Karadeniz Siyah Çayı"
            ),
            reasonOrDetails = "Katkısız, boyasız ve aromasız %100 doğal Türk çayı.",
            alternatives = emptyList(),
            imageUrl = null
        )
    )

    val eAdditivesDirectory = listOf(
        EAdditive(
            code = "E441",
            name = "Jelatin (Gelatin)",
            status = HalalStatus.HARAM,
            origin = "Hayvansal (Çoğunlukla Domuz derisi/kemiği)",
            description = "Gıda sanayisinde kıvam verici ve jelleştirici olarak kullanılır. İthal ürünlerde çoğunlukla domuzdan elde edilir. Yalnızca İslami usule uygun kesilmiş sığırdan elde edildiği sertifikalandırılmışsa helaldir.",
            commonUsage = "Jelibon, lokum, marshmallow, pasta jöleleri, kapsül ilaçlar, yoğurt"
        ),
        EAdditive(
            code = "E120",
            name = "Karmin / Kosinil (Carmine)",
            status = HalalStatus.HARAM,
            origin = "Böcek (Dactylopius coccus kalkan biti)",
            description = "Ezilmiş dişi böceklerden elde edilen parlak kırmızı renk pigmentidir. Fıkıh kurallarına göre haşerat tüketimi caiz olmadığından helal kabul edilmez.",
            commonUsage = "Kırmızı şekerlemeler, çilekli sütler, salam, sosis, ruj, allık, içecekler"
        ),
        EAdditive(
            code = "E920",
            name = "L-Sistein (L-Cysteine)",
            status = HalalStatus.SUPHELI,
            origin = "İnsan saçı, domuz kılı, ördek tüyü veya sentetik",
            description = "Hamur işlerinde unun elastikiyetini artırmak ve kabarmasını kolaylaştırmak için kullanılır. Kaynağının sentetik veya mikrobiyal olduğu belgelenmelidir.",
            commonUsage = "Tost ekmekleri, yufka, pizza tabanları, unlu mamuller"
        ),
        EAdditive(
            code = "E471",
            name = "Mono ve Digliseritler",
            status = HalalStatus.SUPHELI,
            origin = "Bitkisel yağlar veya Hayvansal yağlar (Domuz/Sığır)",
            description = "Su ve yağı birbirine bağlayan emülgatördür. Bitkisel kökenli olanları helaldir ancak ambalajda 'bitkisel' veya helal sertifikası yoksa şüphelidir.",
            commonUsage = "Çikolata, dondurma, margarin, bisküvi, cips, kek, hazır soslar"
        ),
        EAdditive(
            code = "E472a-e",
            name = "Yağ Asidi Esterleri",
            status = HalalStatus.SUPHELI,
            origin = "Bitkisel veya Hayvansal yağlar",
            description = "E471 türevi emülgatörlerdir. Kaynağı net olarak bitkisel belirtilmedikçe şüpheli kabul edilir.",
            commonUsage = "Ekmek geliştiriciler, mayonez, soslar, hazır tatlılar"
        ),
        EAdditive(
            code = "E904",
            name = "Gomalak (Shellac)",
            status = HalalStatus.SUPHELI,
            origin = "Böcek Salgısı (Kerria lacca)",
            description = "Lak böceğinin reçineli salgısından üretilen parlatıcı ajandır. Çoğu helal standardında şüpheli veya sakıncalı kabul edilir.",
            commonUsage = "Draje şekerler, çikolata kaplamaları, hap kaplamaları, meyve parlatma"
        ),
        EAdditive(
            code = "E631",
            name = "Disodyum İnosinat",
            status = HalalStatus.SUPHELI,
            origin = "Hayvansal (Domuz/Et) veya Maya Fermantasyonu",
            description = "Lezzet artırıcı gıda katkısıdır. Hayvansal et artıklarından üretilebildiği için helal kesim kaynağı teyit edilmelidir.",
            commonUsage = "Cipsler, hazır çorbalar, bulyonlar, hazır erişteler (Noodle)"
        ),
        EAdditive(
            code = "E100",
            name = "Kurkumin (Zerdeçal Sarısı)",
            status = HalalStatus.HELAL,
            origin = "Bitkisel (Zerdeçal kökü)",
            description = "Doğal zerdeçal bitkisinden elde edilen güvenli sarı renklendiricidir.",
            commonUsage = "Hardal, peynir, tereyağı, kek, çorbalar"
        ),
        EAdditive(
            code = "E322",
            name = "Lesitin (Lecithin)",
            status = HalalStatus.HELAL,
            origin = "Bitkisel (Soya veya Ayçiçek)",
            description = "Soya fasulyesi veya ayçiçeğinden elde edilen doğal ve helal emülgatördür. Çok nadiren yumurtadan da elde edilir.",
            commonUsage = "Çikolatalar, ezmeler, bisküviler, bebek mamaları"
        ),
        EAdditive(
            code = "E330",
            name = "Sitrik Asit (Limon Tuzu)",
            status = HalalStatus.HELAL,
            origin = "Bitkisel / Mikrobiyal fermantasyon",
            description = "Narenciye meyvelerinde doğal olarak bulunan veya şeker fermantasyonuyla üretilen güvenli asitlik düzenleyicidir.",
            commonUsage = "Gazlı içecekler, meyve suları, reçeller, şekerlemeler"
        ),
        EAdditive(
            code = "E407",
            name = "Karragenan (Carrageenan)",
            status = HalalStatus.HELAL,
            origin = "Bitkisel (Kırmızı Deniz Yosunu)",
            description = "Deniz yosunlarından elde edilen bitkisel jelleştiricidir. Hayvansal jelatin yerine kullanılan helal bir alternatiftir.",
            commonUsage = "Bitkisel tatlılar, pudingler, dondurma, bitkisel sütler"
        ),
        EAdditive(
            code = "E412",
            name = "Guar Gam (Guar Gum)",
            status = HalalStatus.HELAL,
            origin = "Bitkisel (Guar tohumu)",
            description = "Guar bitkisi tohumlarından elde edilen doğal kıvam artırıcı lifli maddedir.",
            commonUsage = "Soslar, dondurma, unlu mamuller, yoğurt"
        ),
        EAdditive(
            code = "E415",
            name = "Ksantan Gam (Xanthan Gum)",
            status = HalalStatus.HELAL,
            origin = "Bakteriyel fermantasyon",
            description = "Şekerlerin yararlı bakterilerle fermantasyonundan elde edilen helal kıvam artırıcıdır.",
            commonUsage = "Glutensiz unlar, salata sosları, diş macunları"
        ),
        EAdditive(
            code = "E160a",
            name = "Beta Karoten",
            status = HalalStatus.HELAL,
            origin = "Bitkisel (Havuç, Palmiye yağı)",
            description = "Havuç ve bitkilerden elde edilen güvenli turuncu-sarı doğal provitamin A renklendiricisidir.",
            commonUsage = "Tereyağı, margarin, meyve suları, peynir"
        ),
        EAdditive(
            code = "E162",
            name = "Pancar Kırmızısı (Betanin)",
            status = HalalStatus.HELAL,
            origin = "Bitkisel (Kırmızı Pancar)",
            description = "Kırmızı pancardan elde edilen doğal, helal kırmızı renklendiricidir. Karmin (E120) yerine tercih edilen temiz alternatiftir.",
            commonUsage = "Çilekli dondurma, yoğurt, şekerleme, içecekler"
        )
    )
}
