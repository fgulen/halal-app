package com.example.data.analyzer

import com.example.data.model.AppLanguage
import com.example.data.model.FlaggedIngredient
import com.example.data.model.FoodProduct
import com.example.data.model.HalalStatus
import com.example.data.remote.OffProduct
import java.util.Locale

object HalalAnalyzer {

    data class HaramRule(
        val keywords: List<String>,
        val nameEn: String,
        val nameTr: String,
        val reasonEn: String,
        val reasonTr: String,
        val eCode: String? = null,
        val origin: String = "Animal / Alcohol / Insect"
    )

    data class SuspiciousRule(
        val keywords: List<String>,
        val nameEn: String,
        val nameTr: String,
        val reasonEn: String,
        val reasonTr: String,
        val eCode: String? = null,
        val origin: String = "Animal or Plant"
    )

    private val HARAM_RULES = listOf(
        HaramRule(
            keywords = listOf("e441", "pork gelatin", "porcine gelatin", "schweinegelatine", "gelatine de porc", "porc gelatine", "varkensgelatine", "domuz jelatini"),
            nameEn = "E441 Pork Gelatin",
            nameTr = "E441 Domuz Jelatini",
            reasonEn = "Porcine collagen derived from pig skin and bones. Prohibited in Islamic law.",
            reasonTr = "Domuz deri ve kemik kolajeninden elde edilen jelleştirici. Dinen haramdır.",
            eCode = "E441",
            origin = "Pork / Swine"
        ),
        HaramRule(
            keywords = listOf("pork", "pig", "swine", "porc", "schwein", "schweinefleisch", "speck", "lard", "bacon", "pork fat", "carne de cerdo", "manteca de cerdo", "domuz", "domuz eti", "domuz yağı"),
            nameEn = "Pork / Lard / Swine Derivatives",
            nameTr = "Domuz Eti / Domuz Yağı (Lard)",
            reasonEn = "Direct pig meat or lard fats. Explicitly forbidden in the Quran.",
            reasonTr = "Domuz eti ve iç yağı. Tüketimi Kur'an-ı Kerim'de açıkça yasaklanmıştır.",
            eCode = null,
            origin = "Pork / Swine"
        ),
        HaramRule(
            keywords = listOf("e120", "cochineal", "carmines", "karmin", "carmine", "acide carminique", "karminsäure", "cochineal extract", "crimson lake", "natural red 4", "dactylopius coccus"),
            nameEn = "E120 Carmine (Cochineal Red)",
            nameTr = "E120 Karmin (Koşnil Kırmızısı)",
            reasonEn = "Red pigment obtained from crushed female scale insects. Prohibited across major Islamic fiqh councils.",
            reasonTr = "Kurutulmuş kabuklu kalkan bitinin ezilmesiyle elde edilen kırmızı renklendirici. Fıkhen sakıncalıdır.",
            eCode = "E120",
            origin = "Insect (Dactylopius coccus)"
        ),
        HaramRule(
            // Note: no bare "bourbon" - "Bourbon-Vanille" (Bourbon vanilla, named after Île
            // Bourbon/Réunion) is a completely alcohol-free, extremely common ingredient term
            // in German product listings, and would collide with a plain "bourbon" keyword.
            // Real bourbon-whiskey mentions are still caught by "whiskey"/"whisky" below.
            keywords = listOf("alcohol", "ethanol", "ethyl alcohol", "ethylalkohol", "alkohol", "liqueur", "likör", "rum", "rhum", "whiskey", "whisky", "vodka", "wine", "wein", "vin", "beer", "bier", "bière", "brandy", "cognac", "champagne", "amaretto", "cooking wine", "sherry", "mirin", "sake", "wine vinegar", "red wine vinegar", "white wine vinegar"),
            nameEn = "Alcohol / Liqueur / Wine Additive",
            nameTr = "Alkol / Likör / Şarap Bileşeni",
            reasonEn = "Intoxicating alcoholic beverages or flavourings. Non-permissible in foods.",
            reasonTr = "Aroma verici veya çözücü olarak kullanılan alkol / likör bileşeni. Gıdalarda tüketimi helal değildir.",
            eCode = null,
            origin = "Alcoholic Intoxicant"
        ),
        HaramRule(
            keywords = listOf("e542", "bone phosphate", "schweinefett"),
            nameEn = "E542 Bone Phosphate",
            nameTr = "E542 Kemik Fosfatı",
            reasonEn = "Derived from defatted bones of non-halal slaughtered animals.",
            reasonTr = "Helal kesim olmayan hayvan kemiklerinden elde edilen mineral.",
            eCode = "E542",
            origin = "Animal Bones"
        )
    )

    private val SUSPICIOUS_RULES = listOf(
        SuspiciousRule(
            keywords = listOf("e471", "mono- and diglycerides of fatty acids", "monoglycerides", "diglycerides", "mono- et diglycérides d'acides gras", "speisefettsäuren"),
            nameEn = "E471 Mono- and Diglycerides",
            nameTr = "E471 Yağ Asitlerinin Mono/Digliseritleri",
            reasonEn = "Emulsifier from vegetable oil (Halal) OR animal fats (Pork/Beef lard). Source must be verified.",
            reasonTr = "Bitkisel yağdan üretildiyse helal, hayvansal yağdan (domuz/sığır) üretildiyse sakıncalıdır. Kaynak teyidi gerekir.",
            eCode = "E471",
            origin = "Plant or Animal"
        ),
        SuspiciousRule(
            keywords = listOf("e472", "e472a", "e472b", "e472c", "e472e", "datem", "esters of mono- and diglycerides"),
            nameEn = "E472a-f / DATEM Emulsifiers",
            nameTr = "E472a-f / DATEM Yağ Asidi Esterleri",
            reasonEn = "Fatty acid derivatives used in bakery. Requires vegetable origin confirmation.",
            reasonTr = "Unlu mamul emülgatörleri. Hayvansal yağ riski nedeniyle bitkisel köken teyidi gereklidir.",
            eCode = "E472",
            origin = "Plant or Animal"
        ),
        SuspiciousRule(
            keywords = listOf("e904", "shellac", "confectioner's glaze", "resinous glaze"),
            nameEn = "E904 Shellac / Confectioner's Glaze",
            nameTr = "E904 Şellak / Parlatıcı Sır",
            reasonEn = "Glazing agent from lac insect secretion. Deemed doubtful by several Halal standards.",
            reasonTr = "Lak böceğinin salgısından üretilen parlatıcı sır. Bazı helal mercilerince şüpheli görülür.",
            eCode = "E904",
            origin = "Insect Secretion"
        ),
        SuspiciousRule(
            keywords = listOf("e920", "l-cysteine", "l-cystein", "cysteine"),
            nameEn = "E920 L-Cysteine (Dough Conditioner)",
            nameTr = "E920 L-Sistein (Hamur Geliştirici)",
            reasonEn = "Amino acid often sourced from duck feathers or animal hair. Must be synthetic/fermented.",
            reasonTr = "Ördek tüyü veya kıldan elde edilen un geliştirici. Sentetik/fermantasyon kökenli olmalıdır.",
            eCode = "E920",
            origin = "Animal feathers/hair or Fermentation"
        ),
        SuspiciousRule(
            keywords = listOf("e631", "disodium inosinate", "sodium inosinate"),
            nameEn = "E631 Disodium Inosinate",
            nameTr = "E631 Disodyum İnozinat",
            reasonEn = "Flavor enhancer potentially prepared from meat byproducts.",
            reasonTr = "Et yan ürünlerinden üretilebilen lezzet artırıcı. Bitkisel/tapiyoka teyidi gerektirir.",
            eCode = "E631",
            origin = "Meat or Microbial"
        ),
        SuspiciousRule(
            keywords = listOf("gelatin", "gélatine", "gelatine", "gelatina"),
            nameEn = "Gelatin (Unspecified Source)",
            nameTr = "Jelatin (Kaynağı Belirtilmemiş)",
            reasonEn = "Packaging does not specify whether fish, halal bovine, or non-halal porcine source is used.",
            reasonTr = "Ambalajda sığır, balık veya domuz olduğu belirtilmemiştir. Üreticiden teyit edilmelidir.",
            eCode = "E441",
            origin = "Animal"
        ),
        SuspiciousRule(
            keywords = listOf("rennet", "pepsin", "animal rennet", "lab", "présure", "animal enzymes", "cheese cultures"),
            nameEn = "Animal Rennet / Cheese Enzymes",
            nameTr = "Hayvansal Peynir Mayası / Enzim",
            reasonEn = "Curdling enzyme from calf stomach. Permissible only if from Halal slaughtered animals or microbial.",
            reasonTr = "Buzağı şirdeninden elde edilen peynir mayası. Helal kesim veya mikrobiyel olmalıdır.",
            eCode = null,
            origin = "Animal (Calf / Swine)"
        ),
        SuspiciousRule(
            keywords = listOf("whey", "whey powder", "molke", "lactosérum", "peynir altı suyu tozu"),
            nameEn = "Whey Powder / Whey Byproduct",
            nameTr = "Peynir Altı Suyu Tozu",
            reasonEn = "Cheese byproduct that may involve animal rennet enzymes during milk curdling.",
            reasonTr = "Peynir üretiminde kullanılan hayvansal mayanın durumuna göre şüpheli olabilir.",
            eCode = null,
            origin = "Dairy Byproduct"
        )
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

        val imageUrl = resolveProductImageUrl(offProduct, barcode)

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

        val flaggedItems = mutableListOf<FlaggedIngredient>()
        val harmfulLabels = mutableListOf<String>()
        val suspiciousLabels = mutableListOf<String>()

        // 1. Check for Haram rules (Strict)
        for (rule in HARAM_RULES) {
            val matchedInText = rule.keywords.any { kw -> containsKeyword(ingredientsLower, kw) }
            val matchedInAdditives = rule.keywords.any { kw -> additiveTags.any { tag -> tag.contains(kw) } }
            if (matchedInText || matchedInAdditives) {
                val displayName = if (language == AppLanguage.TR) rule.nameTr else rule.nameEn
                val displayReason = if (language == AppLanguage.TR) rule.reasonTr else rule.reasonEn
                val flagged = FlaggedIngredient(
                    name = displayName,
                    eCode = rule.eCode,
                    status = HalalStatus.HARAM,
                    reason = displayReason,
                    origin = rule.origin
                )
                if (flaggedItems.none { it.name == displayName }) {
                    flaggedItems.add(flagged)
                    harmfulLabels.add(displayName)
                }
            }
        }

        // 2. Check for Suspicious rules (Doubtful / Mushbooh)
        for (rule in SUSPICIOUS_RULES) {
            val matchedInText = rule.keywords.any { kw -> containsKeyword(ingredientsLower, kw) }
            val matchedInAdditives = rule.keywords.any { kw -> additiveTags.any { tag -> tag == kw || tag.contains(kw) } }
            if (matchedInText || matchedInAdditives) {
                // If gelatin is already identified as pork gelatin, skip general gelatin
                if (rule.keywords.contains("gelatin") && harmfulLabels.any { it.contains("Gelatin") || it.contains("Pork") }) {
                    continue
                }
                val displayName = if (language == AppLanguage.TR) rule.nameTr else rule.nameEn
                val displayReason = if (language == AppLanguage.TR) rule.reasonTr else rule.reasonEn
                val flagged = FlaggedIngredient(
                    name = displayName,
                    eCode = rule.eCode,
                    status = HalalStatus.SUPHELI,
                    reason = displayReason,
                    origin = rule.origin
                )
                if (flaggedItems.none { it.name == displayName }) {
                    flaggedItems.add(flagged)
                    suspiciousLabels.add(displayName)
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
                harmfulLabels.isEmpty() && suspiciousLabels.isEmpty()

        // 4. Extract tokenized ingredients list
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
            // Rule 1: Kesin haram içerik varsa -> HARAM
            harmfulLabels.isNotEmpty() -> {
                status = HalalStatus.HARAM
                reason = when (language) {
                    AppLanguage.EN -> "Prohibited ingredients (${harmfulLabels.joinToString(", ")}) detected. Not permissible for consumption according to Islamic dietary standards."
                    AppLanguage.DE -> "Verbotene Inhaltsstoffe (${harmfulLabels.joinToString(", ")}) gefunden. Nicht für den Verzehr geeignet."
                    AppLanguage.FR -> "Ingrédients interdits détectés (${harmfulLabels.joinToString(", ")}). Non conforme aux normes alimentaires islamiques."
                    AppLanguage.TR -> "İçeriğinde dinen yasaklanmış maddeler (${harmfulLabels.joinToString(", ")}) tespit edilmiştir. Tüketilmesi uygun değildir."
                    AppLanguage.AR -> "تم اكتشاف مكونات محظورة (${harmfulLabels.joinToString(", ")}). غير مباح للاستهلاك."
                }
                alternatives = listOf(
                    "Halal Certified equivalents (IFANCA, GIMDES, HMC)",
                    "100% Plant-Based / Vegan certified products without alcohol"
                )
                certificate = null
            }
            // Rule 2: Haram yok ama şüpheli içerik varsa -> ŞÜPHELİ
            suspiciousLabels.isNotEmpty() -> {
                status = HalalStatus.SUPHELI
                reason = when (language) {
                    AppLanguage.EN -> "Contains additives of unverified origin (${suspiciousLabels.joinToString(", ")}). Must verify whether animal or plant-derived."
                    AppLanguage.DE -> "Enthält Zusätze mit unklarer Herkunft (${suspiciousLabels.joinToString(", ")}). Bitte prüfen, ob pflanzlich oder tierisch."
                    AppLanguage.FR -> "Contient des additifs d'origine non spécifiée (${suspiciousLabels.joinToString(", ")}). Vérifiez si végétal."
                    AppLanguage.TR -> "İçeriğindeki bazı katkı maddeleri (${suspiciousLabels.joinToString(", ")}) bitkisel veya hayvansal kökenli olabilir. Teyit gerektirir."
                    AppLanguage.AR -> "يحتوي على إضافات غير مؤكدة المصدر (${suspiciousLabels.joinToString(", ")}). يرجى التأكد من المصدر النباتي."
                }
                alternatives = listOf(
                    "Look for '100% Vegetable Emulsifiers' indication",
                    "Choose certified Halal alternatives"
                )
                certificate = null
            }
            // Rule 3: Bilinen tüm içerikler helalse -> HELAL
            isSafeHalalOrVegan || (allIngredientsList.isNotEmpty() && harmfulLabels.isEmpty() && suspiciousLabels.isEmpty()) -> {
                status = HalalStatus.HELAL
                reason = when (language) {
                    AppLanguage.EN -> "No prohibited or doubtful additives found. Ingredients comply with Halal & plant-based standards."
                    AppLanguage.DE -> "Keine verbotenen oder zweifelhaften Zusätze gefunden. Entspricht den Halal-Kriterien."
                    AppLanguage.FR -> "Aucun additif prohibé ou douteux détecté. Conforme aux critères Halal."
                    AppLanguage.TR -> "Sakıncalı veya şüpheli katkı maddesi tespit edilmedi. İçerik helal standartlarına uygundur."
                    AppLanguage.AR -> "لا توجد مواد محظورة أو مشبوهة. المنتج مطابق للمعايir الحلال."
                }
                // Only claim a certificate when the product actually carries a halal/vegan
                // label or explicit claim in its data - fabricating "Open Food Facts Verified"
                // for every product that simply had no flagged ingredients overstated what
                // this screening actually checked.
                certificate = if (hasHalalClaim) "Certified Halal Product" else if (hasVeganClaim) "100% Plant-Based / Vegan Verified" else null
                alternatives = emptyList()
            }
            // Rule 4: Yeterli bilgi yoksa -> BİLİNMİYOR
            else -> {
                status = HalalStatus.BULUNAMADI
                reason = when (language) {
                    AppLanguage.EN -> "Barcode found in Open Food Facts, but ingredients are not listed yet. Please inspect packaging manually."
                    AppLanguage.DE -> "Produkt ist erfasst, aber Zutatenliste fehlt noch. Bitte Etikett manuell prüfen."
                    AppLanguage.FR -> "Code-barres indexé mais liste d'ingrédients manquante. Veuillez vérifier l'emballage."
                    AppLanguage.TR -> "Ürün barkodu sistemde kayıtlı fakat içerik tablosu henüz doldurulmamış. Lütfen ambalajı kontrol edin."
                    AppLanguage.AR -> "المنتج مسجل ولكن قائمة المكونات غير مكتملة، يرجى فحص الغلاف يدوياً."
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
            harmfulOrSuspiciousIngredients = if (harmfulLabels.isNotEmpty()) harmfulLabels else suspiciousLabels,
            flaggedDetails = flaggedItems,
            allIngredients = allIngredientsList,
            reasonOrDetails = reason,
            alternatives = alternatives,
            imageUrl = imageUrl
        )
    }

    // Word-boundary match instead of plain substring: a short keyword like "rum" or "vin"
    // must not fire on "sérum" (whey) or "vinegar" just because it appears inside the word.
    // Checked manually via Char.isLetterOrDigit (Unicode-aware on both JVM and Android) rather
    // than a regex "(?U)\b" flag, which Android's ICU-based regex engine doesn't support and
    // throws PatternSyntaxException on at runtime.
    private fun containsKeyword(text: String, keyword: String): Boolean {
        var fromIndex = 0
        while (true) {
            val idx = text.indexOf(keyword, fromIndex)
            if (idx == -1) return false
            val beforeIsBoundary = idx == 0 || !text[idx - 1].isLetterOrDigit()
            val afterIndex = idx + keyword.length
            val afterIsBoundary = afterIndex >= text.length || !text[afterIndex].isLetterOrDigit()
            if (beforeIsBoundary && afterIsBoundary) return true
            fromIndex = idx + 1
        }
    }

    fun classifyIngredientToken(token: String): HalalStatus {
        val lower = token.lowercase(Locale.ROOT)
        if (HARAM_RULES.any { rule -> rule.keywords.any { kw -> containsKeyword(lower, kw) } }) {
            return HalalStatus.HARAM
        }
        if (SUSPICIOUS_RULES.any { rule -> rule.keywords.any { kw -> containsKeyword(lower, kw) } }) {
            return HalalStatus.SUPHELI
        }
        return HalalStatus.HELAL
    }

    fun analyzeIngredientsText(
        productName: String,
        ingredientsText: String,
        barcode: String = "",
        language: AppLanguage = AppLanguage.EN
    ): FoodProduct {
        val dummyOff = OffProduct(
            productName = productName,
            ingredientsText = ingredientsText,
            categories = "Analyzed Ingredients"
        )
        return analyzeOpenFoodFactsProduct(barcode, dummyOff, language)
    }

    fun resolveProductImageUrl(offProduct: OffProduct, barcode: String): String? {
        val candidate = offProduct.imageFrontUrl
            ?: offProduct.imageUrl
            ?: offProduct.imageFrontSmallUrl
            ?: offProduct.imageSmallUrl
            ?: offProduct.imageFrontThumbUrl
            ?: offProduct.imageThumbUrl

        if (!candidate.isNullOrBlank()) {
            val trimmed = candidate.trim()
            return when {
                trimmed.startsWith("//") -> "https:$trimmed"
                trimmed.startsWith("http://") -> trimmed.replace("http://", "https://")
                else -> trimmed
            }
        }

        return null
    }
}
