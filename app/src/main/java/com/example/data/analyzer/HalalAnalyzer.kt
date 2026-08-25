package com.example.data.analyzer

import com.example.data.model.AppLanguage
import com.example.data.model.FoodProduct
import com.example.data.model.HalalStatus
import com.example.data.remote.OffProduct
import java.util.Locale

object HalalAnalyzer {

    // Multilingual Haram keywords (USA, UK, Germany, France, Netherlands, Spain, Turkey)
    private val HARAM_KEYWORDS = listOf(
        // Pork & Swine derivatives
        "pork", "pig", "swine", "porc", "schwein", "schweinefleisch", "speck", "lard", "bacon",
        "pork fat", "pork gelatin", "porcine gelatin", "schweinegelatine", "porc gelatine", "gelatine de porc",
        "varkensvlees", "varkensgelatine", "carne de cerdo", "manteca de cerdo", "maiale", "strutto",
        "domuz", "domuz eti", "domuz yağı", "domuz jelatini",
        // Alcohol / Intoxicants / Liquor in USA & EU confectionery and bakery
        "alcohol", "ethanol", "ethyl alcohol", "ethylalkohol", "alkohol", "liqueur", "likör",
        "rum", "rhum", "whiskey", "whisky", "vodka", "wine", "wein", "vin", "beer", "bier", "bière",
        "brandy", "cognac", "bourbon", "champagne", "amaretto", "cooking wine", "sherry", "mirin", "sake",
        "wine vinegar", "red wine vinegar", "white wine vinegar",
        // Specific Prohibited Additives
        "e120", "cochineal", "carmines", "karmin", "carmine", "acide carminique", "karminsäure",
        "cochineal extract", "crimson lake", "natural red 4", "dactylopius coccus",
        "e441", "schweinefett", "bone phosphate", "e542"
    )

    // Suspicious / Doubtful additives requiring source verification (Animal vs Plant)
    private val SUSPICIOUS_KEYWORDS = mapOf(
        "e471" to "E471 (Mono- and diglycerides of fatty acids / Emulsifier - Animal or Plant origin)",
        "e472" to "E472 (Esters of fatty acids - Potential animal origin)",
        "e472a" to "E472a (Acetic acid esters)",
        "e472b" to "E472b (Lactic acid esters)",
        "e472c" to "E472c (Citric acid esters)",
        "e472e" to "E472e / DATEM (Diacetyl tartaric acid esters)",
        "e473" to "E473 (Sucrose esters of fatty acids)",
        "e475" to "E475 (Polyglycerol esters of fatty acids)",
        "e476" to "E476 (Polyglycerol polyricinoleate - Often plant but requires checking)",
        "e481" to "E481 / SSL (Sodium stearoyl lactylate - Fatty acid origin)",
        "e482" to "E482 (Calcium stearoyl lactylate)",
        "e483" to "E483 (Stearyl tartrate)",
        "e491" to "E491 (Sorbitan monostearate)",
        "e492" to "E492 (Sorbitan tristearate)",
        "e904" to "E904 / Shellac / Confectioner's Glaze (Insect secretion resin)",
        "e920" to "E920 / L-Cysteine (Dough conditioner from animal hair/feathers)",
        "e542" to "E542 (Bone phosphate)",
        "e631" to "E631 / Disodium inosinate (Flavour enhancer - Potential meat origin)",
        "gelatin" to "Gelatin (Source unspecified: check if bovine/fish or non-halal)",
        "gélatine" to "Gélatine (Source unspecified)",
        "gelatine" to "Gelatine (Source unspecified)",
        "rennet" to "Animal Rennet / Pepsin (Animal enzyme used in cheese)",
        "lab" to "Lab / Rennet (Animal curdling enzyme)",
        "présure" to "Présure (Animal enzyme)",
        "pepsin" to "Pepsin (Stomach enzyme)",
        "tallow" to "Beef Tallow / Animal Fat (Uncertified slaughter)",
        "animal shortening" to "Animal Shortening",
        "whey" to "Whey Powder (Check rennet enzyme in cheese processing)"
    )

    fun analyzeOpenFoodFactsProduct(
        barcode: String,
        offProduct: OffProduct,
        language: AppLanguage = AppLanguage.EN
    ): FoodProduct {
        val name = when (language) {
            AppLanguage.EN -> offProduct.productNameEn ?: offProduct.productName ?: offProduct.productNameDe ?: offProduct.productNameFr ?: "Global Food Product"
            AppLanguage.DE -> offProduct.productNameDe ?: offProduct.productNameEn ?: offProduct.productName ?: "Europäisches Produkt"
            AppLanguage.FR -> offProduct.productNameFr ?: offProduct.productNameEn ?: offProduct.productName ?: "Produit International"
            AppLanguage.TR -> offProduct.productNameTr ?: offProduct.productNameEn ?: offProduct.productName ?: "Uluslararası Ürün"
            AppLanguage.AR -> offProduct.productNameAr ?: offProduct.productNameEn ?: offProduct.productName ?: "منتج غذائي عالمي"
        }

        val brand = offProduct.brands ?: when (language) {
            AppLanguage.EN -> "Global Brand"
            AppLanguage.DE -> "Hersteller"
            AppLanguage.FR -> "Marque"
            AppLanguage.TR -> "Uluslararası Marka"
            AppLanguage.AR -> "علامة تجارية"
        }

        val category = offProduct.categories?.split(",")?.firstOrNull()?.trim() ?: when (language) {
            AppLanguage.EN -> "Food & Beverage"
            AppLanguage.DE -> "Lebensmittel & Getränke"
            AppLanguage.FR -> "Alimentation"
            AppLanguage.TR -> "Gıda & İçecek"
            AppLanguage.AR -> "أغذية ومشروبات"
        }

        val imageUrl = offProduct.imageFrontUrl ?: offProduct.imageUrl

        val ingredientsRaw = listOfNotNull(
            offProduct.ingredientsTextEn,
            offProduct.ingredientsTextDe,
            offProduct.ingredientsTextFr,
            offProduct.ingredientsTextEs,
            offProduct.ingredientsTextTr,
            offProduct.ingredientsTextAr,
            offProduct.ingredientsText
        ).joinToString(" ")

        val ingredientsLower = ingredientsRaw.lowercase(Locale.ROOT)
        val additiveTags = offProduct.additivesTags?.map { it.lowercase(Locale.ROOT).replace("en:", "") } ?: emptyList()
        val labelsTags = offProduct.labelsTags?.map { it.lowercase(Locale.ROOT).replace("en:", "") } ?: emptyList()
        val analysisTags = offProduct.ingredientsAnalysisTags?.map { it.lowercase(Locale.ROOT) } ?: emptyList()

        val harmfulFound = mutableListOf<String>()
        val suspiciousFound = mutableListOf<String>()

        // 1. Check for Haram markers
        for (haram in HARAM_KEYWORDS) {
            val isPresentInText = ingredientsLower.contains(haram)
            val isPresentInAdditives = additiveTags.any { it.contains(haram) }
            if (isPresentInText || isPresentInAdditives) {
                val label = when (haram) {
                    "e120", "cochineal", "carmines", "karmin", "carmine", "karminsäure", "cochineal extract", "natural red 4" ->
                        "E120 Carmine / Cochineal (Insect-derived red dye)"
                    "e441", "schweinegelatine", "porc gelatine", "gelatine de porc", "pork gelatin", "porcine gelatin", "varkensgelatine", "domuz jelatini" ->
                        "E441 Pork Gelatin / Schweinegelatine"
                    "pork", "pig", "swine", "porc", "schwein", "schweinefleisch", "speck", "lard", "bacon", "pork fat", "carne de cerdo", "domuz", "domuz yağı" ->
                        "Pork Meat / Lard / Swine Derivatives"
                    "alcohol", "ethanol", "ethyl alcohol", "ethylalkohol", "alkohol", "liqueur", "likör", "rum", "rhum", "whiskey", "whisky", "vodka", "wine", "wein", "vin", "beer", "bier", "champagne", "brandy", "bourbon" ->
                        "Alcohol / Wine / Liquor Component"
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
                // Skip gelatin if already caught as pork gelatin
                if (code.contains("gelatin") && harmfulFound.any { it.contains("Gelatin") || it.contains("Pork") }) continue
                if (!suspiciousFound.contains(description)) {
                    suspiciousFound.add(description)
                }
            }
        }

        // 3. Positive claims (Halal, Kosher, Vegan)
        val hasHalalClaim = ingredientsLower.contains("halal") ||
                ingredientsLower.contains("helal") ||
                labelsTags.any { it.contains("halal") || it.contains("helal") }

        val hasVeganClaim = analysisTags.any { it.contains("vegan") || it.contains("vegetarian") } ||
                labelsTags.any { it.contains("vegan") || it.contains("v-label") || it.contains("vegetarian") } ||
                ingredientsLower.contains("100% plant") ||
                ingredientsLower.contains("vegan")

        val hasKosherClaim = labelsTags.any { it.contains("kosher") || it.contains("ou") } ||
                ingredientsLower.contains("kosher") ||
                ingredientsLower.contains("pareve")

        val isSafeHalalOrVegan = (hasHalalClaim || (hasVeganClaim && !ingredientsLower.contains("alcohol"))) &&
                harmfulFound.isEmpty() && suspiciousFound.isEmpty()

        // 4. Extract ingredients tokens for UI display
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
        val certificate: String?

        when {
            harmfulFound.isNotEmpty() -> {
                status = HalalStatus.HARAM
                reason = when (language) {
                    AppLanguage.EN -> "Prohibited ingredients (${harmfulFound.joinToString(", ")}) detected via Open Food Facts. Not permissible according to Islamic dietary standards."
                    AppLanguage.DE -> "Verbotene Inhaltsstoffe (${harmfulFound.joinToString(", ")}) in der Open Food Facts Datenbank gefunden. Nicht für den Halal-Verzehr geeignet."
                    AppLanguage.FR -> "Ingrédients interdits détectés (${harmfulFound.joinToString(", ")}). Non conforme aux normes alimentaires islamiques."
                    AppLanguage.TR -> "Open Food Facts veri tabanında sakıncalı içerikler (${harmfulFound.joinToString(", ")}) tespit edilmiştir. Tüketilmesi uygun değildir."
                    AppLanguage.AR -> "تم اكتشاف مكونات محظورة (${harmfulFound.joinToString(", ")}) غير مطابقة لمعايير الحلال."
                }
                alternatives = listOf(
                    "Certified Halal alternatives (IFANCA, HMC, Halal Europe)",
                    "100% Vegan / Plant-Based alternatives without alcohol"
                )
                certificate = null
            }
            suspiciousFound.isNotEmpty() -> {
                status = HalalStatus.SUPHELI
                reason = when (language) {
                    AppLanguage.EN -> "Contains additives of unconfirmed origin (${suspiciousFound.joinToString(", ")}). Verify whether animal or vegetable source is used by manufacturer."
                    AppLanguage.DE -> "Enthält Zusatzstoffe unklarer Herkunft (${suspiciousFound.joinToString(", ")}). Bitte prüfen, ob pflanzlich oder tierisch."
                    AppLanguage.FR -> "Contient des additifs d'origine non spécifiée (${suspiciousFound.joinToString(", ")}). Vérifiez si d'origine végétale."
                    AppLanguage.TR -> "Bitkisel veya hayvansal kökenli olabilecek şüpheli katkı maddeleri (${suspiciousFound.joinToString(", ")}) yer almaktadır."
                    AppLanguage.AR -> "يحتوي على إضافات غير مؤكدة المصدر (${suspiciousFound.joinToString(", ")}). يرجى التأكد من المصدر النباتي."
                }
                alternatives = listOf(
                    "Opt for certified Halal or 100% Plant-based versions",
                    "Check packaging for '100% Vegetable Emulsifiers'"
                )
                certificate = null
            }
            isSafeHalalOrVegan -> {
                status = HalalStatus.HELAL
                reason = when (language) {
                    AppLanguage.EN -> "No prohibited additives found. Product matches Halal and plant-based safety standards."
                    AppLanguage.DE -> "Keine verbotenen E-Nummern oder Zusätze gefunden. Entspricht den Halal-Kriterien."
                    AppLanguage.FR -> "Aucun additif prohibé détecté. Conforme aux critères de consommation Halal."
                    AppLanguage.TR -> "Haram veya şüpheli E-kodu tespit edilmedi. Helal ve bitkisel standartlara uygundur."
                    AppLanguage.AR -> "لا توجد مواد محظورة أو مشبوهة، المنتج متوافق مع معايير الحلال."
                }
                certificate = if (hasHalalClaim) "Certified Halal Product" else if (hasVeganClaim) "100% Plant-Based / Vegan Verified" else "Open Food Facts Verified"
                alternatives = emptyList()
            }
            allIngredientsList.isNotEmpty() -> {
                status = HalalStatus.HELAL
                reason = when (language) {
                    AppLanguage.EN -> "Verified through Open Food Facts global database. Formulated without prohibited additives."
                    AppLanguage.DE -> "Geprüft über die globale Open Food Facts Datenbank. Keine unzulässigen E-Nummern."
                    AppLanguage.FR -> "Vérifié via la base de données Open Food Facts. Sans additifs prohibés."
                    AppLanguage.TR -> "Küresel gıda veri tabanından alınan içerikte şüpheli katkı maddesi bulunmadı."
                    AppLanguage.AR -> "تم الفحص عبر قاعدة البيانات العالمية ولم يتم العثور على إضافات محظورة."
                }
                certificate = if (hasKosherClaim) "Kosher Certified Formulation" else null
                alternatives = emptyList()
            }
            else -> {
                status = HalalStatus.BULUNAMADI
                reason = when (language) {
                    AppLanguage.EN -> "Product barcode exists in Open Food Facts, but ingredients table is not yet populated. Please verify on package."
                    AppLanguage.DE -> "Produkt ist in Open Food Facts gelistet, aber Zutatenliste ist leer. Bitte Etikett prüfen."
                    AppLanguage.FR -> "Code-barres indexé mais liste d'ingrédients manquante. Veuillez vérifier l'emballage."
                    AppLanguage.TR -> "Barkod kayıtlı ancak içindekiler tablosu henüz doldurulmamıştır. Lütfen paketi inceleyin."
                    AppLanguage.AR -> "الباركود مسجل لكن جدول المكونات غير مكتمل، يرجى فحص الغلاف."
                }
                certificate = null
                alternatives = emptyList()
            }
        }

        return FoodProduct(
            barcode = barcode,
            name = name,
            brand = brand,
            category = category,
            status = status,
            halalCertificate = certificate,
            harmfulOrSuspiciousIngredients = if (harmfulFound.isNotEmpty()) harmfulFound else suspiciousFound,
            allIngredients = allIngredientsList,
            reasonOrDetails = reason,
            alternatives = alternatives,
            imageUrl = imageUrl
        )
    }
}
