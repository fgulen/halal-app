package com.example.data.analyzer

import com.example.data.model.FoodProduct
import com.example.data.model.HalalStatus
import com.example.data.remote.OffProduct
import java.util.Locale

object HalalAnalyzer {

    // Haram keywords across European languages (German, French, English, Dutch, Turkish)
    private val HARAM_KEYWORDS = listOf(
        // Pork derivatives
        "pork", "pig", "swine", "porc", "schwein", "schweinefleisch", "speck", "lard", "bacon",
        "schweinegelatine", "porc gelatine", "gelatine de porc", "domuz", "domuz yağı",
        // Alcohol / Intoxicants
        "alcohol", "ethanol", "ethyl alcohol", "ethylalkohol", "alkohol", "liqueur", "likör",
        "rum", "rhum", "whiskey", "whisky", "vodka", "wine", "wein", "vin", "beer", "bier", "bière",
        // Specific haram additives
        "e120", "cochineal", "carmines", "karmin", "carmine", "acide carminique", "karminsäure",
        "e441", "schweinefett"
    )

    // Suspicious keywords requiring source verification (animal vs plant)
    private val SUSPICIOUS_KEYWORDS = mapOf(
        "e471" to "E471 (Mono- ve digliseritler - Hayvansal veya bitkisel yağ kökenli olabilir)",
        "e472" to "E472 (Yağ asitleri esterleri - Hayvansal köken şüphesi)",
        "e472a" to "E472a (Asetik asit esterleri - Yağ kökeni şüpheli)",
        "e472b" to "E472b (Laktik asit esterleri - Yağ kökeni şüpheli)",
        "e472c" to "E472c (Sitrik asit esterleri - Yağ kökeni şüpheli)",
        "e472e" to "E472e (Tartarik asit esterleri - Yağ kökeni şüpheli)",
        "e473" to "E473 (Sükroz esterleri)",
        "e475" to "E475 (Poligliserol esterleri)",
        "e476" to "E476 (Poligliserol polirisinoleat - Genellikle bitkisel ancak şüpheli kontrol edilmeli)",
        "e481" to "E481 (Sodyum stearol-2-laktilat - Yağ asidi şüpheli)",
        "e482" to "E482 (Kalsiyum stearol-2-laktilat)",
        "e483" to "E483 (Stearil tartarat)",
        "e491" to "E491 (Sorbitan monostearat)",
        "e492" to "E492 (Sorbitan tristearat)",
        "e904" to "E904 (Şellak / Shellac - Böcek salgısı parlatıcı)",
        "e920" to "E920 (L-Sistein - Hayvan kılı veya tüyü kökenli olabilir)",
        "e542" to "E542 (Kemik fosfatı)",
        "gelatin" to "Jelatin (Kaynağı sığır/domuz belirtilmemiş genel jelatin)",
        "gélatine" to "Jelatin (Kaynağı belirtilmemiş)",
        "gelatine" to "Jelatin (Kaynağı belirtilmemiş)",
        "rennet" to "Peynir mayası (Mikrobiyal veya hayvansal şüphesi)",
        "lab" to "Peynir mayası (Lab / Rennet)",
        "présure" to "Peynir mayası (Présure)",
        "pepsin" to "Pepsin (Domuz midesinden elde edilmiş olabilir)"
    )

    fun analyzeOpenFoodFactsProduct(barcode: String, offProduct: OffProduct): FoodProduct {
        val name = offProduct.productNameTr
            ?: offProduct.productName
            ?: offProduct.productNameEn
            ?: offProduct.productNameDe
            ?: offProduct.productNameFr
            ?: "Avrupa Ürünü"

        val brand = offProduct.brands ?: "Bilinmeyen Marka"
        val category = offProduct.categories?.split(",")?.firstOrNull()?.trim() ?: "Gıda & İçecek"
        val imageUrl = offProduct.imageFrontUrl ?: offProduct.imageUrl

        val ingredientsRaw = listOfNotNull(
            offProduct.ingredientsTextTr,
            offProduct.ingredientsText,
            offProduct.ingredientsTextEn,
            offProduct.ingredientsTextDe,
            offProduct.ingredientsTextFr
        ).joinToString(" ")

        val ingredientsLower = ingredientsRaw.lowercase(Locale.ROOT)
        val additiveTags = offProduct.additivesTags?.map { it.lowercase(Locale.ROOT).replace("en:", "") } ?: emptyList()

        val harmfulFound = mutableListOf<String>()
        val suspiciousFound = mutableListOf<String>()

        // 1. Check for Haram markers
        for (haram in HARAM_KEYWORDS) {
            val isPresentInText = ingredientsLower.contains(haram)
            val isPresentInAdditives = additiveTags.any { it.contains(haram) }
            if (isPresentInText || isPresentInAdditives) {
                val label = when (haram) {
                    "e120", "cochineal", "carmines", "karmin", "carmine", "karminsäure" -> "E120 Karmin (Böcek kökenli kırmızı renklendirici)"
                    "e441", "schweinegelatine", "porc gelatine", "gelatine de porc" -> "E441 Domuz Jelatini"
                    "pork", "swine", "porc", "schwein", "schweinefleisch", "speck", "lard", "bacon", "domuz", "domuz yağı" -> "Domuz Eti / Yağı / Türevi"
                    "alcohol", "ethanol", "ethyl alcohol", "ethylalkohol", "alkohol", "liqueur", "likör", "rum", "rhum", "whiskey", "whisky", "vodka", "wine", "wein", "vin", "beer", "bier" -> "Alkol / Likör / Şarap Bileşeni"
                    else -> haram.uppercase(Locale.ROOT)
                }
                if (!harmfulFound.contains(label)) {
                    harmfulFound.add(label)
                }
            }
        }

        // 2. Check for Suspicious markers
        for ((code, description) in SUSPICIOUS_KEYWORDS) {
            val isPresentInText = ingredientsLower.contains(code)
            val isPresentInAdditives = additiveTags.any { it == code || it.contains(code) }
            if (isPresentInText || isPresentInAdditives) {
                // If gelatin was already identified as pork, don't double count as generic suspicious
                if (code.contains("gelatin") && harmfulFound.any { it.contains("Jelatin") }) continue
                if (!suspiciousFound.contains(description)) {
                    suspiciousFound.add(description)
                }
            }
        }

        // 3. Check for Halal claims or certificates
        val isExplicitlyHalal = ingredientsLower.contains("halal") ||
                ingredientsLower.contains("helal") ||
                ingredientsLower.contains("tse helal") ||
                ingredientsLower.contains("gimdes") ||
                offProduct.ingredientsAnalysisTags?.any { it.contains("vegan") } == true && harmfulFound.isEmpty() && suspiciousFound.isEmpty()

        // 4. Split all ingredients for display
        val allIngredientsList = if (ingredientsRaw.isNotBlank()) {
            ingredientsRaw.split(Regex("[,;•]"))
                .map { it.trim().trim('.', '(', ')') }
                .filter { it.isNotBlank() && it.length > 1 }
                .take(15)
        } else {
            additiveTags.map { it.uppercase(Locale.ROOT) }
        }

        val status: HalalStatus
        val reason: String
        val alternatives: List<String>

        when {
            harmfulFound.isNotEmpty() -> {
                status = HalalStatus.HARAM
                reason = "Open Food Facts Avrupa veri tabanında yapılan incelemede sakıncalı içerikler (${harmfulFound.joinToString(", ")}) tespit edilmiştir. İslami fıkıh kriterlerine göre tüketilmesi uygun değildir."
                alternatives = listOf(
                    "TSE veya GİMDES Helal sertifikalı muadil ürünleri tercih edebilirsiniz.",
                    "Bitkisel veya %100 Vegan etiketli alternatifleri seçebilirsiniz."
                )
            }
            suspiciousFound.isNotEmpty() -> {
                status = HalalStatus.SUPHELI
                reason = "Ürün içeriğinde kaynağı bitkisel ya da hayvansal olabilecek katkı maddeleri (${suspiciousFound.joinToString(", ")}) yer almaktadır. Üreticiye veya ambalaj üzerindeki 'Bitkisel' ibaresine dikkat edilmesi tavsiye edilir."
                alternatives = listOf(
                    "Sertifikalı helal ürünleri tercih ediniz.",
                    "İçeriğinde sadece bitkisel emülgatör belirtilen ürünleri inceleyiniz."
                )
            }
            isExplicitlyHalal || offProduct.ingredientsAnalysisTags?.any { it.contains("vegan") } == true -> {
                status = HalalStatus.HELAL
                reason = "Ürün analizinde şüpheli veya haram katkı maddesine rastlanmamıştır. Katkı ve bileşenler helallik standartlarına uygundur."
                alternatives = emptyList()
            }
            allIngredientsList.isNotEmpty() -> {
                status = HalalStatus.HELAL
                reason = "Avrupa gıda veri tabanından alınan içindekiler listesinde haram veya şüpheli E-kodu tespit edilmedi."
                alternatives = emptyList()
            }
            else -> {
                status = HalalStatus.BULUNAMADI
                reason = "Ürünün barkodu Avrupa veri tabanında kayıtlıdır ancak ayrıntılı içerik tablosu henüz doldurulmamıştır."
                alternatives = emptyList()
            }
        }

        return FoodProduct(
            barcode = barcode,
            name = name,
            brand = brand,
            category = category,
            status = status,
            halalCertificate = if (isExplicitlyHalal) "Open Food Facts Analizi (Helal / Vegan)" else null,
            harmfulOrSuspiciousIngredients = if (harmfulFound.isNotEmpty()) harmfulFound else suspiciousFound,
            allIngredients = allIngredientsList,
            reasonOrDetails = reason,
            alternatives = alternatives,
            imageUrl = imageUrl
        )
    }
}
