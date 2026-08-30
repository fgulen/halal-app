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
        val origin: String = "Animal or Plant",
        // The meat rule below is the only one set to true: a product that already carries an
        // explicit halal claim/label is presumed to source its meat from zabiha slaughter, so
        // flagging the meat again would contradict the certification we're about to display.
        val suppressIfHalalClaim: Boolean = false
    )

    // Multi-language meat/poultry terms. Presence of meat does not make a product haram, but
    // halal validity depends on zabiha (Islamic) slaughter, which packaging/ingredient data
    // essentially never states - so absent a halal claim, meat must default to "unconfirmed"
    // rather than being silently passed through as Helal.
    // Stable identifier for the meat rule (its nameEn), used to detect "only meat was flagged"
    // independent of the language-localized display name.
    private const val MEAT_RULE_ID = "Meat / Poultry (Slaughter Method Unconfirmed)"
    // Stable identifier for the beef/fish-gelatin rule, used below to skip the generic
    // "unspecified source" gelatin rule once the more specific source-stated rule has fired.
    private const val BEEF_FISH_GELATIN_RULE_ID = "Beef/Fish Gelatin (Source Stated)"
    // Stable identifier for the generic "unspecified source" gelatin rule, used below (together
    // with BEEF_FISH_GELATIN_RULE_ID) to identify both gelatin-family Suspicious rules by
    // identity rather than by a fragile rule.keywords.contains("gelatin") literal-list check.
    private const val GELATIN_UNSPECIFIED_RULE_ID = "Gelatin (Unspecified Source)"

    private val MEAT_KEYWORDS = listOf(
        "beef", "veal", "lamb", "mutton", "goat meat", "chicken", "poultry", "turkey meat",
        "duck meat", "goose meat", "chicken broth", "beef broth", "chicken stock", "beef stock",
        "chicken fat", "beef fat", "meat extract", "bouillon de boeuf", "bouillon de poulet",
        "rindfleisch", "kalbfleisch", "lammfleisch", "hühnerfleisch", "haehnchenfleisch",
        "putenfleisch", "entenfleisch", "fleischextrakt", "hühnerbrühe", "rinderbrühe",
        "boeuf", "veau", "agneau", "poulet", "volaille", "dinde", "canard", "extrait de viande",
        "carne de res", "carne de vaca", "ternera", "cordero", "pollo", "pavo",
        "sığır eti", "dana eti", "kuzu eti", "tavuk eti", "hindi eti", "ördek eti", "et suyu", "et özütü"
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
            // "porcine" bare (not just "porcine gelatin" in the E441 rule above) so
            // "porcine collagen", "porcine enzymes", "porcine fat" etc. don't slip through -
            // containsKeyword's word-boundary check means only the E441 rule's exact phrase
            // "porcine gelatin" matched before, not "porcine" followed by any other word.
            keywords = listOf("pork", "pig", "swine", "porc", "porcine", "cochon", "jambon", "schwein", "schweinefleisch", "speck", "lard", "bacon", "ham", "pork fat", "carne de cerdo", "manteca de cerdo", "domuz", "domuz eti", "domuz yağı"),
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
        ),
        HaramRule(
            // Specific multi-word phrases only, never bare "blood" - "blood orange"/"blutorange"/
            // "orange sanguine" is a common, entirely blood-free juice/flavoring ingredient that
            // a bare "blood" keyword would false-positive on (same class of bug "vin" bare would
            // cause inside "vinegar" - see containsKeyword's word-boundary comment above).
            keywords = listOf(
                "blood plasma", "dried blood", "blood sausage", "black pudding",
                "blutplasma", "blutwurst", "boudin noir", "kan plazması"
            ),
            nameEn = "Blood / Blood Plasma",
            nameTr = "Kan / Kan Plazması",
            reasonEn = "Drained/consumed blood is explicitly forbidden in Islamic law.",
            reasonTr = "Akıtılmış kanın tüketimi dinen açıkça yasaklanmıştır.",
            eCode = null,
            origin = "Animal Blood"
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
            keywords = listOf("e422", "glycerin", "glycerine", "glycerol", "gliserin", "gliserol"),
            nameEn = "E422 Glycerin / Glycerol",
            nameTr = "E422 Gliserin / Gliserol",
            reasonEn = "Humectant from vegetable oil (Halal) OR animal fat (Pork/Beef tallow). Source must be verified.",
            reasonTr = "Bitkisel yağdan üretildiyse helal, hayvansal yağdan (domuz/sığır donyağı) üretildiyse sakıncalıdır. Kaynak teyidi gerekir.",
            eCode = "E422",
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
            keywords = listOf(
                "beef gelatin", "bovine gelatin", "fish gelatin",
                "rindergelatine", "fischgelatine",
                "gélatine de bœuf", "gélatine de poisson",
                "sığır jelatini", "balık jelatini",
                "gelatina de res", "gelatina de pescado"
            ),
            nameEn = BEEF_FISH_GELATIN_RULE_ID,
            nameTr = "Sığır/Balık Jelatini (Kaynak Belirtilmiş)",
            reasonEn = "Source is stated as beef or fish, not pork - but halal status still depends on whether the animal was slaughtered according to zabiha (Islamic) method, which cannot be confirmed from packaging alone.",
            reasonTr = "Kaynağın sığır veya balık olduğu belirtilmiş - domuz değil. Ancak helal olması için hayvanın zebiha usulüne uygun kesilmiş olması gerekir; bu bilgi ambalajdan doğrulanamaz.",
            eCode = "E441",
            origin = "Beef / Fish"
        ),
        SuspiciousRule(
            // Collagen/collagen peptides (e.g. "Bovine collagen peptides" on protein/beauty
            // supplements) are chemically a precursor of gelatin - same cattle skin/bone/
            // connective-tissue origin, same zabiha-slaughter question - but the bare word
            // "gelatin" never appears on these labels, so the gelatin rules above never catch
            // them. A product carrying an explicit halal claim already implies zabiha sourcing,
            // same as the meat rule, hence suppressIfHalalClaim.
            keywords = listOf(
                "bovine collagen", "beef collagen", "collagen peptides", "hydrolyzed collagen",
                "hydrolysed collagen", "collagen hydrolysate",
                "rinderkollagen", "kollagenpeptide", "hydrolysiertes kollagen",
                "collagène bovin", "peptides de collagène", "collagène hydrolysé",
                "colágeno bovino", "colágeno de res", "péptidos de colágeno",
                "sığır kolajeni", "kolajen peptit", "hidrolize kolajen"
            ),
            nameEn = "Bovine Collagen (Slaughter Method Unconfirmed)",
            nameTr = "Sığır Kolajeni (Kesim Yöntemi Teyit Edilmedi)",
            reasonEn = "Collagen/collagen peptides derived from cattle skin, bone, or connective tissue. Halal status depends on zabiha (Islamic) slaughter, which cannot be confirmed from this product's data alone.",
            reasonTr = "Sığır deri, kemik veya bağ dokusundan elde edilen kolajen/kolajen peptit. Helal olması dinen usulüne uygun (zebiha) kesime bağlıdır; bu bilgi ürün verisinden doğrulanamaz.",
            eCode = null,
            origin = "Beef / Bovine",
            suppressIfHalalClaim = true
        ),
        SuspiciousRule(
            keywords = listOf("gelatin", "gélatine", "gelatine", "gelatina", "jelatin"),
            nameEn = GELATIN_UNSPECIFIED_RULE_ID,
            nameTr = "Jelatin (Kaynağı Belirtilmemiş)",
            reasonEn = "Packaging does not specify whether fish, halal bovine, or non-halal porcine source is used.",
            reasonTr = "Ambalajda sığır, balık veya domuz olduğu belirtilmemiştir. Üreticiden teyit edilmelidir.",
            eCode = "E441",
            origin = "Animal"
        ),
        SuspiciousRule(
            // Bare "lab" (German for rennet) dropped: as a standalone word-boundary match it
            // also fires on "lab-grown", "lab-tested", etc. in English ingredient text. Products
            // without a halal claim already default to Şüpheli via the generic no-claim branch,
            // so this rule missing an unqualified German "Lab" mention isn't a coverage gap.
            keywords = listOf("rennet", "pepsin", "animal rennet", "kälberlab", "kalbslab", "présure", "animal enzymes", "cheese cultures"),
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
        ),
        SuspiciousRule(
            keywords = MEAT_KEYWORDS,
            nameEn = MEAT_RULE_ID,
            nameTr = "Et / Kümes Hayvanı (Kesim Yöntemi Teyit Edilmedi)",
            reasonEn = "Contains meat or poultry. Halal status depends on zabiha (Islamic) slaughter, which cannot be confirmed from this product listing alone.",
            reasonTr = "Et veya kümes hayvanı içeriyor. Helal olması dinen usulüne uygun (zebiha) kesime bağlıdır; bu bilgi ürün verisinden doğrulanamaz.",
            eCode = null,
            origin = "Animal Meat",
            suppressIfHalalClaim = true
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

        // OFF frequently populates ingredients_text and ingredients_text_en with the identical
        // string (no distinct per-language text was ever entered) - joining every field
        // unconditionally then duplicated that one sentence, which both doubled every chip in
        // the visible ingredients list and (harmlessly, since matching is idempotent) doubled
        // it in the text rule-matching runs against.
        val ingredientsRaw = listOfNotNull(
            offProduct.ingredientsTextEn,
            offProduct.ingredientsTextDe,
            offProduct.ingredientsTextFr,
            offProduct.ingredientsTextEs,
            offProduct.ingredientsTextTr,
            offProduct.ingredientsTextAr,
            offProduct.ingredientsText
        ).distinctBy { it.trim().lowercase(Locale.ROOT) }
            .joinToString(" ")

        // Strip negated compounds ("alcohol-free", "non-alcoholic") before matching, so a
        // hyphen boundary doesn't let e.g. "alcohol-free" trip the "alcohol" keyword - the
        // word-boundary check in containsKeyword treats '-' as a boundary, not as glue.
        val ingredientsLower = stripNegatedPhrases(ingredientsRaw.lowercase(Locale.ROOT))
        val additiveTags = offProduct.additivesTags?.map { it.lowercase(Locale.ROOT).replace("en:", "") } ?: emptyList()
        val labelsTags = offProduct.labelsTags?.map { it.lowercase(Locale.ROOT).replace("en:", "") } ?: emptyList()
        val analysisTags = offProduct.ingredientsAnalysisTags?.map { it.lowercase(Locale.ROOT) } ?: emptyList()

        // Computed early (moved ahead of rule matching) so the meat rule below can suppress
        // itself on products that already carry an explicit halal claim/label.
        //
        // Deliberately trusts labelsTags ONLY, not free ingredient text: a plain .contains
        // on ingredientsLower also matched "not halal", "non-halal", "helal değildir" - i.e.
        // the exact opposite claim - which both disabled the meat gate and routed straight to
        // a green Helal verdict. labelsTags is OFF's structured field where "halal" actually
        // means the halal label is present; it can't be negated the same way. The asymmetric
        // risk (false positive here -> green verdict on a haram product) outweighs catching the
        // rarer product whose only halal mention is in free text.
        val hasHalalClaim = labelsTags.any { it == "halal" || it == "helal" || it.contains("halal") || it.contains("helal") }

        val flaggedItems = mutableListOf<FlaggedIngredient>()
        val harmfulLabels = mutableListOf<String>()
        val suspiciousLabels = mutableListOf<String>()
        // Tracks which SUSPICIOUS_RULES fired, by their language-stable nameEn, independent of
        // suspiciousLabels (which holds the localized display name). Used below to tell "only
        // the meat rule fired" apart from an actual unverified-origin additive, since those two
        // cases need different reason text - "this additive might be animal-derived" makes no
        // sense for a product whose only flag is containing meat outright.
        val suspiciousRuleIds = mutableListOf<String>()

        // 1. Check for Haram rules (Strict)
        for (rule in HARAM_RULES) {
            val matchedInText = rule.keywords.any { kw -> containsKeyword(ingredientsLower, kw) }
            // containsKeyword (not tag.contains) so additive tag "e1200" doesn't false-match
            // keyword "e120" - a plain substring check treated E1200 (polydextrose) as E120 (carmine).
            val matchedInAdditives = rule.keywords.any { kw -> additiveTags.any { tag -> containsKeyword(tag, kw) } }
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
            if (rule.suppressIfHalalClaim && hasHalalClaim) continue
            val matchedInText = rule.keywords.any { kw -> containsKeyword(ingredientsLower, kw) }
            val matchedInAdditives = rule.keywords.any { kw -> additiveTags.any { tag -> containsKeyword(tag, kw) } }
            if (matchedInText || matchedInAdditives) {
                // Any gelatin-family suspicious rule (generic-unspecified OR the more specific
                // beef/fish rule) must be skipped once a pork/gelatin Haram flag already fired -
                // otherwise a "not pork" Suspicious card renders directly under the Haram
                // "Prohibited Ingredients" header next to "E441 Pork Gelatin", contradicting it.
                val isGelatinFamilyRule = rule.nameEn == GELATIN_UNSPECIFIED_RULE_ID || rule.nameEn == BEEF_FISH_GELATIN_RULE_ID
                if (isGelatinFamilyRule && harmfulLabels.any { it.contains("Gelatin") || it.contains("Pork") }) {
                    continue
                }
                // The generic rule additionally defers to the more specific beef/fish rule when
                // that one already matched (source IS stated, just not as pork).
                if (rule.nameEn == GELATIN_UNSPECIFIED_RULE_ID && suspiciousRuleIds.contains(BEEF_FISH_GELATIN_RULE_ID)) {
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
                    suspiciousRuleIds.add(rule.nameEn)
                }
            }
        }

        // 3. Positive claims (Halal, Vegan). hasHalalClaim was already computed above, ahead
        // of rule matching, so the meat rule can consult it.
        //
        // Exact match on analysisTags, not .contains: OFF's ingredients_analysis_tags puts one
        // of en:vegan / en:non-vegan / en:maybe-vegan / en:vegan-status-unknown (and the
        // -vegetarian equivalents) on nearly every product it has parsed - all four contain the
        // substring "vegan", so a .contains check was true for almost every product regardless
        // of its actual vegan status, which silently neutralized the Rule 3 tightening below.
        // Free-text ingredientsLower.contains("vegan") is dropped for the same reason
        // hasHalalClaim dropped free text: "not vegan" also contains "vegan".
        //
        // "en:vegetarian" / labelsTags "vegetarian" deliberately excluded: vegetarian permits
        // dairy, eggs, and animal rennet (the very enzyme SUSPICIOUS_RULES flags above), so a
        // vegetarian tag is not a plant-based claim. Counting it as one previously let a dairy
        // product like butter (vegetarian, but OFF-tagged en:non-vegan) reach the isSafeHalalOrVegan
        // branch and get shown a "100% Plant-Based / Vegan Label" certificate it has no basis for.
        val hasVeganClaim = analysisTags.any { it == "en:vegan" } ||
                labelsTags.any { it == "vegan" || it == "v-label" } ||
                ingredientsLower.contains("100% plant")

        val isSafeHalalOrVegan = (hasHalalClaim || (hasVeganClaim && !ingredientsLower.contains("alcohol"))) &&
                harmfulLabels.isEmpty() && suspiciousLabels.isEmpty()

        // 4. Extract tokenized ingredients list. Cap raised from 15 to 40: a long real label
        // (e.g. Dave's Killer Bread's 21-grain mix) was silently truncating before ingredients
        // like "vinegar", "yeast", or "enzymes" ever reached the visible list - the user has no
        // way to verify what was actually screened if the display cuts off before showing it.
        val allIngredientsList = if (ingredientsRaw.isNotBlank()) {
            ingredientsRaw.split(Regex("[,;•]"))
                .map { it.trim().trim('.', '(', ')') }
                .filter { it.isNotBlank() && it.length > 1 }
                .take(40)
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
                // Meat present with no other flag needs its own sentence: "this additive might
                // be animal-derived" makes no sense when the product is, say, a can of beef stew.
                val onlyMeatFlag = suspiciousRuleIds.isNotEmpty() && suspiciousRuleIds.all { it == MEAT_RULE_ID }
                if (onlyMeatFlag) {
                    reason = when (language) {
                        AppLanguage.EN -> "Contains meat or poultry. Its halal status depends on zabiha (Islamic) slaughter, which cannot be confirmed from this product's data."
                        AppLanguage.DE -> "Enthält Fleisch oder Geflügel. Der Halal-Status hängt von der Zabiha-Schlachtung ab, die anhand der Produktdaten nicht bestätigt werden kann."
                        AppLanguage.FR -> "Contient de la viande ou de la volaille. Son statut halal dépend de l'abattage zabiha, qui ne peut être confirmé à partir des données du produit."
                        AppLanguage.TR -> "Et veya kümes hayvanı içeriyor. Helal olması dinen usulüne uygun (zebiha) kesime bağlıdır; bu bilgi ürün verisinden doğrulanamaz."
                        AppLanguage.AR -> "يحتوي على لحم أو دواجن. تعتمد حالته الحلال على الذبح الشرعي (الذبيحة)، والتي لا يمكن تأكيدها من بيانات المنتج."
                    }
                    alternatives = listOf("Look for a halal certification mark (IFANCA, GIMDES, HMC, JAKIM, MUI)")
                } else {
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
                }
                certificate = null
            }
            // Rule 3: Only give a positive Helal verdict when the product carries an actual
            // halal or vegan claim/label AND nothing was flagged. Absence of a rule match is
            // not evidence of permissibility - it must not be presented as one.
            isSafeHalalOrVegan -> {
                status = HalalStatus.HELAL
                reason = when (language) {
                    AppLanguage.EN -> "No prohibited or doubtful additives found, and the product carries an explicit halal/plant-based claim."
                    AppLanguage.DE -> "Keine verbotenen oder zweifelhaften Zusätze gefunden, und das Produkt trägt eine Halal-/pflanzliche Kennzeichnung."
                    AppLanguage.FR -> "Aucun additif prohibé ou douteux détecté, et le produit porte une mention halal/végétale explicite."
                    AppLanguage.TR -> "Sakıncalı veya şüpheli katkı maddesi tespit edilmedi ve üründe açık bir helal/bitkisel işaret bulunuyor."
                    AppLanguage.AR -> "لا توجد مواد محظورة أو مشبوهة، والمنتج يحمل إشارة حلال/نباتية صريحة."
                }
                // "Certified" is dropped deliberately: this is a claim/label read from Open Food
                // Facts' crowd-sourced data, not a certification this app has verified itself.
                certificate = if (hasHalalClaim) "Carries a Halal Label (per product data)" else "100% Plant-Based / Vegan Label (per product data)"
                alternatives = emptyList()
            }
            // Rule 4: No rule matched and no positive claim exists either. Absence of a halal/
            // vegan label is not itself a red flag - the ingredient list was actually screened
            // and nothing prohibited or doubtful turned up, so this is a Helal screening result.
            // It is presented as automated screening, not certification (see reason text and the
            // null certificate below), the same way the explicit-claim path already labels itself
            // "(per product data)" rather than "Certified".
            allIngredientsList.isNotEmpty() -> {
                status = HalalStatus.HELAL
                reason = when (language) {
                    AppLanguage.EN -> "No prohibited or doubtful ingredients were found in this product's ingredient list. Note: this is an automated screening, not a halal certification - the product carries no explicit halal/vegan label."
                    AppLanguage.DE -> "In der Zutatenliste dieses Produkts wurden keine verbotenen oder zweifelhaften Inhaltsstoffe gefunden. Hinweis: Dies ist eine automatische Prüfung, keine Halal-Zertifizierung - das Produkt trägt kein ausdrückliches Halal-/Vegan-Siegel."
                    AppLanguage.FR -> "Aucun ingrédient interdit ou douteux n'a été détecté dans la liste des ingrédients de ce produit. Remarque : ceci est un contrôle automatique, pas une certification halal - le produit ne porte aucun label halal/végane explicite."
                    AppLanguage.TR -> "Bu ürünün içindekiler listesinde yasaklı veya şüpheli bir madde tespit edilmedi. Not: Bu otomatik bir tarama sonucudur, helal sertifikası değildir - üründe açık bir helal/vegan işareti bulunmuyor."
                    AppLanguage.AR -> "لم يتم العثور على أي مكون محظور أو مشبوه في قائمة مكونات هذا المنتج. ملاحظة: هذا فحص آلي وليس شهادة حلال - لا يحمل المنتج علامة حلال/نباتية صريحة."
                }
                certificate = null
                alternatives = emptyList()
            }
            // Rule 5: Yeterli bilgi yoksa -> BİLİNMİYOR
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
            imageUrl = imageUrl,
            language = language
        )
    }

    // Negated compounds that must not trip a keyword match. "alcohol-free" is safe to match
    // literally because '-' breaks the word boundary the same way a space does, but that is
    // exactly the bug: "alcohol-free" and "alcohol" share a boundary at the hyphen, so the plain
    // keyword rule fires on a product explicitly declaring the opposite. Scrubbed out before
    // matching rather than special-cased in containsKeyword, since new negated phrases are far
    // easier to add to this list than to a boundary algorithm.
    private val NEGATION_PHRASES = listOf(
        "alcohol-free", "alcohol free", "non-alcoholic", "alkoholfrei", "alkoholfreie", "alkoholfreies",
        "ohne alkohol", "sans alcool", "sin alcohol",
        // Explicit plant-based gelatin claims - stripping the whole phrase (not just "gelatin")
        // means the bare "gelatin" keyword below no longer matches this specific mention, while
        // a separate, unrelated "gelatin" elsewhere in the same ingredient list still does.
        "vegetable gelatin", "gélatine végétale", "pflanzliche gelatine", "bitkisel jelatin",
        "gelatina vegetal"
    )

    private fun stripNegatedPhrases(text: String): String {
        var result = text
        for (phrase in NEGATION_PHRASES) {
            result = result.replace(phrase, " ")
        }
        return result
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
        val lower = stripNegatedPhrases(token.lowercase(Locale.ROOT))
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
