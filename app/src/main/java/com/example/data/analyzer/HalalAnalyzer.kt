package com.example.data.analyzer

import com.example.data.model.AppLanguage
import com.example.data.model.FlaggedIngredient
import com.example.data.model.FoodProduct
import com.example.data.model.HalalStatus
import com.example.data.model.LocalizedText
import com.example.data.model.Madhhab
import com.example.data.remote.OffProduct
import java.util.Locale

object HalalAnalyzer {

    // id: language-independent rule key (the original English name); logic keys off it, never off
    // the localized display text.
    data class HaramRule(
        val keywords: List<String>,
        val id: String,
        val name: LocalizedText,
        val reason: LocalizedText,
        val eCode: String? = null,
        val origin: LocalizedText
    )

    data class SuspiciousRule(
        val keywords: List<String>,
        val id: String,
        val name: LocalizedText,
        val reason: LocalizedText,
        val eCode: String? = null,
        val origin: LocalizedText,
        // The meat rule below is the only one set to true: a product that already carries an
        // explicit halal claim/label is presumed to source its meat from zabiha slaughter, so
        // flagging the meat again would contradict the certification we're about to display.
        val suppressIfHalalClaim: Boolean = false
    )

    // Multi-language meat/poultry terms. Presence of meat does not make a product haram, but
    // halal validity depends on zabiha (Islamic) slaughter, which packaging/ingredient data
    // essentially never states - so absent a halal claim, meat must default to "unconfirmed"
    // rather than being silently passed through as Helal.
    // Stable identifier for the meat rule (its id), used to detect "only meat was flagged"
    // independent of the language-localized display name.
    private const val MEAT_RULE_ID = "Meat / Poultry (Slaughter Method Unconfirmed)"
    // Stable identifier for the beef/fish-gelatin rule, used below to skip the generic
    // "unspecified source" gelatin rule once the more specific source-stated rule has fired.
    private const val BEEF_FISH_GELATIN_RULE_ID = "Beef/Fish Gelatin (Source Stated)"
    // Stable identifier for the generic "unspecified source" gelatin rule, used below (together
    // with BEEF_FISH_GELATIN_RULE_ID) to identify both gelatin-family Suspicious rules by
    // identity rather than by a fragile rule.keywords.contains("gelatin") literal-list check.
    private const val GELATIN_UNSPECIFIED_RULE_ID = "Gelatin (Unspecified Source)"

    private const val RENNET_RULE_ID = "Animal Rennet / Cheese Enzymes"
    // Unqualified "Lab" (German for rennet, no origin stated). Matched by containsBareLab, not by
    // keyword text, because "lab" alone is also a substring of "lab-grown" / "label" in English.
    private const val BARE_LAB_RULE_ID = "Unlabelled Rennet (Lab, Origin Unknown)"
    private const val BARE_LAB_KEYWORD = "\u0000bare-lab"
    private const val WHEY_RULE_ID = "Whey Powder / Whey Byproduct"
    // Verdict-level suggestions and label lines, shown next to the (localized) flag cards.
    private val ALT_CERTIFIED_EQUIVALENTS = LocalizedText(
        en = "Halal Certified equivalents (IFANCA, GIMDES, HMC)",
        de = "Halal-zertifizierte Alternativen (IFANCA, GIMDES, HMC)",
        fr = "Équivalents certifiés halal (IFANCA, GIMDES, HMC)",
        tr = "Helal sertifikalı muadiller (IFANCA, GİMDES, HMC)",
        ar = "بدائل حاصلة على شهادة حلال (IFANCA، GIMDES، HMC)"
    )
    private val ALT_VEGAN_NO_ALCOHOL = LocalizedText(
        en = "100% Plant-Based / Vegan certified products without alcohol",
        de = "100 % pflanzliche / vegan zertifizierte Produkte ohne Alkohol",
        fr = "Produits 100 % végétaux / certifiés vegan sans alcool",
        tr = "Alkolsüz, %100 bitkisel / vegan sertifikalı ürünler",
        ar = "منتجات نباتية 100% / حاصلة على شهادة نباتية وخالية من الكحول"
    )
    private val ALT_CERT_MARK = LocalizedText(
        en = "Look for a halal certification mark (IFANCA, GIMDES, HMC, JAKIM, MUI)",
        de = "Auf ein Halal-Zertifizierungszeichen achten (IFANCA, GIMDES, HMC, JAKIM, MUI)",
        fr = "Rechercher un label de certification halal (IFANCA, GIMDES, HMC, JAKIM, MUI)",
        tr = "Helal sertifika logosu arayın (IFANCA, GİMDES, HMC, JAKIM, MUI)",
        ar = "ابحث عن علامة شهادة حلال (IFANCA، GIMDES، HMC، JAKIM، MUI)"
    )
    private val ALT_VEGETABLE_EMULSIFIERS = LocalizedText(
        en = "Look for '100% Vegetable Emulsifiers' indication",
        de = "Auf den Hinweis „100 % pflanzliche Emulgatoren“ achten",
        fr = "Rechercher la mention « émulsifiants 100 % végétaux »",
        tr = "\"%100 bitkisel emülgatör\" ibaresini arayın",
        ar = "ابحث عن عبارة \"مستحلبات نباتية 100%\""
    )
    private val ALT_CERTIFIED_ALTERNATIVES = LocalizedText(
        en = "Choose certified Halal alternatives",
        de = "Halal-zertifizierte Alternativen wählen",
        fr = "Choisir des alternatives certifiées halal",
        tr = "Helal sertifikalı alternatifleri tercih edin",
        ar = "اختر بدائل حاصلة على شهادة حلال"
    )
    // "Certified" is deliberately absent: these come from Open Food Facts' crowd-sourced labels.
    private val CERT_HALAL_LABEL = LocalizedText(
        en = "Carries a Halal Label (per product data)",
        de = "Trägt ein Halal-Kennzeichen (laut Produktdaten)",
        fr = "Porte un label halal (selon les données du produit)",
        tr = "Helal etiketi taşıyor (ürün verisine göre)",
        ar = "يحمل علامة حلال (وفق بيانات المنتج)"
    )
    private val CERT_VEGAN_LABEL = LocalizedText(
        en = "100% Plant-Based / Vegan Label (per product data)",
        de = "100 % pflanzlich / Vegan-Kennzeichen (laut Produktdaten)",
        fr = "Label 100 % végétal / vegan (selon les données du produit)",
        tr = "%100 bitkisel / vegan etiketi (ürün verisine göre)",
        ar = "علامة نباتية 100% / نباتي صرف (وفق بيانات المنتج)"
    )

    // The two pork Haram rules; once either fires, gelatin-family doubtful cards are skipped.
    private const val PORK_GELATIN_RULE_ID = "E441 Pork Gelatin"
    private const val PORK_RULE_ID = "Pork / Lard / Swine Derivatives"

    // Each link backs exactly the sentence it is attached to, and each was verified 2026-10-07 by
    // downloading the page and finding the quoted ruling verbatim (source table in
    // docs/superpowers/plans/2026-10-07-madhhab-setting-and-off-5xx-fix.md):
    // - Diyanet DİYK fatwa (15.09.2020): Diyanet's own ruling only - no slaughter condition, pork excluded.
    internal const val DIYANET_RENNET_FATWA_URL =
        "https://kurul.diyanet.gov.tr/tr/fetva/peynir-yapiminda-kullanilan-mayalarin-dini-hukmu-nedir/0193c42d-aa60-7126-92d8-9c538f6b513b"
    // - The Hanafi split: Abu Hanifa (pure) vs. his students Abu Yusuf and Muhammad (consumption haram).
    internal const val HANAFI_RENNET_SOURCE_URL = "https://thehalallife.co.uk/animal-rennet/"
    // - The other schools: "majority of scholars among the Malikis, Shafi'is and Hanbalis... impure (najis)".
    internal const val OTHER_MADHHABS_RENNET_SOURCE_URL = "https://islamqa.info/en/answers/2841"
    // - Arabic UI (user decision): the Arabic edition of the same IslamQA answer, which states both
    //   "قول أبي حنيفة وهو إحدى الروايتين عن أحمد بطهارة الإنفحة من الميتة" and the majority (najis) view.
    internal const val ARABIC_RENNET_SOURCE_URL = "https://islamqa.info/ar/answers/2841"

    /**
     * A sourced, user-approved madhhab difference. It applies only when EVERY keyword of [ruleId]
     * that matched is in [keywords] - so e.g. the rennet override never clears "pepsin" (mostly
     * porcine) or "animal enzymes", which share the rennet rule. Overrides only exist for
     * Suspicious rules, so they can never downgrade a Haram match.
     */
    private data class MadhhabOverride(
        val ruleId: String,
        val madhhab: Madhhab,
        val keywords: List<String>,
        val status: HalalStatus,
        // Shown when this override changed the verdict (madhhab selected).
        val appliedNote: (AppLanguage) -> String,
        // Shown under any other madhhab when a covered keyword matched, so the difference is visible.
        val differenceNote: (AppLanguage) -> String
    )

    private val MADHHAB_OVERRIDES = listOf(
        MadhhabOverride(
            ruleId = RENNET_RULE_ID,
            madhhab = Madhhab.HANAFI,
            keywords = listOf("rennet", "animal rennet", "kälberlab", "kalbslab", "présure",
                "peynir mayası", "hayvansal peynir mayası", "hayvansal maya", "şirden", "إنفحة", "منفحة"),
            status = HalalStatus.HELAL,
            appliedNote = { lang ->
                when (lang) {
                    AppLanguage.EN -> "Madhhab note (Hanafi): the rennet in this product was treated as permissible per Abu Hanifa, who held that rennet from a halal-species animal is pure even if the animal was not Islamically slaughtered; his students Abu Yusuf and Muhammad held that its consumption is not permissible. Pepsin and other enzymes are not covered. Source: $HANAFI_RENNET_SOURCE_URL\nTurkey's Diyanet also sees no objection to animal rennet unless it is known to be from pork. Source: $DIYANET_RENNET_FATWA_URL"
                    AppLanguage.DE -> "Hinweis zur Rechtsschule (hanafitisch): Das Lab in diesem Produkt wurde nach Abu Hanifa als zulässig gewertet - nach ihm ist Lab von einem erlaubten Tier rein, auch wenn das Tier nicht islamisch geschlachtet wurde; seine Schüler Abu Yusuf und Muhammad hielten den Verzehr für unzulässig. Pepsin und andere Enzyme sind nicht erfasst. Quelle: $HANAFI_RENNET_SOURCE_URL\nAuch die türkische Diyanet sieht bei tierischem Lab keinen Einwand, solange nicht bekannt ist, dass es vom Schwein stammt. Quelle: $DIYANET_RENNET_FATWA_URL"
                    AppLanguage.FR -> "Note sur l'école (hanafite) : la présure de ce produit a été considérée comme permise selon Abu Hanifa, pour qui la présure d'un animal d'espèce licite est pure même si l'animal n'a pas été abattu rituellement ; ses disciples Abu Yusuf et Muhammad jugeaient sa consommation non permise. La pepsine et les autres enzymes ne sont pas concernées. Source : $HANAFI_RENNET_SOURCE_URL\nLa Diyanet turque ne voit pas non plus d'objection à la présure animale tant qu'il n'est pas établi qu'elle provient du porc. Source : $DIYANET_RENNET_FATWA_URL"
                    AppLanguage.TR -> "Mezhep notu (Hanefi): Bu üründeki peynir mayası Ebû Hanîfe'nin görüşüne göre caiz sayıldı; ona göre eti yenen türden bir hayvandan alınan maya, hayvan usulüne göre kesilmemiş olsa da temizdir. Talebeleri Ebû Yûsuf ve İmam Muhammed ise yenmesinin caiz olmadığı görüşündedir. Pepsin ve diğer enzimler bu kapsamda değildir. Kaynak: $HANAFI_RENNET_SOURCE_URL\nDiyanet de domuzdan elde edildiği bilinmedikçe hayvansal mayada sakınca görmüyor. Kaynak: $DIYANET_RENNET_FATWA_URL"
                    AppLanguage.AR -> "ملاحظة مذهبية (حنفي): اعتُبرت الإنفحة في هذا المنتج جائزة وفق قول أبي حنيفة، وهو إحدى الروايتين عن أحمد: إنفحة الميتة أو ما ذُكّي ذكاة غير شرعية طاهرة. لا يشمل ذلك البيبسين والإنزيمات الأخرى. المصدر: $ARABIC_RENNET_SOURCE_URL"
                }
            },
            differenceNote = { lang ->
                when (lang) {
                    AppLanguage.EN -> "Madhhab note: per Abu Hanifa, rennet from a halal-species animal is permissible even without Islamic slaughter, so this rennet counts as permissible when 'Hanafi' is selected (not pepsin or other enzymes). Source: $HANAFI_RENNET_SOURCE_URL\nThe majority of Maliki, Shafi'i and Hanbali scholars consider it impure. Source: $OTHER_MADHHABS_RENNET_SOURCE_URL"
                    AppLanguage.DE -> "Hinweis zur Rechtsschule: Nach Abu Hanifa ist Lab von einem erlaubten Tier auch ohne islamische Schlachtung zulässig; bei Auswahl 'Hanafitisch' gilt dieses Lab daher als zulässig (nicht Pepsin oder andere Enzyme). Quelle: $HANAFI_RENNET_SOURCE_URL\nDie Mehrheit der malikitischen, schafiitischen und hanbalitischen Gelehrten hält es für unrein. Quelle: $OTHER_MADHHABS_RENNET_SOURCE_URL"
                    AppLanguage.FR -> "Note sur l'école : selon Abu Hanifa, la présure d'un animal d'espèce licite est permise même sans abattage rituel ; elle est donc considérée comme permise si « Hanafite » est sélectionné (pas la pepsine ni les autres enzymes). Source : $HANAFI_RENNET_SOURCE_URL\nLa majorité des savants malikites, chaféites et hanbalites la jugent impure. Source : $OTHER_MADHHABS_RENNET_SOURCE_URL"
                    AppLanguage.TR -> "Mezhep notu: Ebû Hanîfe'ye göre eti yenen türden bir hayvanın mayası, hayvan usulüne göre kesilmemiş olsa da caizdir; 'Hanefi' seçiliyken bu peynir mayası caiz sayılır (pepsin ve diğer enzimler hariç). Kaynak: $HANAFI_RENNET_SOURCE_URL\nMaliki, Şafii ve Hanbelî âlimlerin çoğunluğu ise necis kabul eder. Kaynak: $OTHER_MADHHABS_RENNET_SOURCE_URL"
                    AppLanguage.AR -> "ملاحظة مذهبية: عند أبي حنيفة، وهو إحدى الروايتين عن أحمد، إنفحة الميتة أو ما ذُكّي ذكاة غير شرعية طاهرة، لذا تُعدّ هذه الإنفحة جائزة عند اختيار \"حنفي\" (دون البيبسين والإنزيمات الأخرى). وذهب الجمهور من المالكية والشافعية والحنابلة إلى أنها نجسة. المصدر: $ARABIC_RENNET_SOURCE_URL"
                }
            }
        ),
        MadhhabOverride(
            ruleId = WHEY_RULE_ID,
            madhhab = Madhhab.HANAFI,
            keywords = listOf("whey", "whey powder", "~molke", "lactosérum", "peynir altı suyu tozu", "مصل اللبن", "مصل الحليب"),
            status = HalalStatus.HELAL,
            // No direct source for whey exists - this is an inference from the rennet ruling and
            // the note says so. Deliberately no fatwa link here: it doesn't cover whey directly.
            appliedNote = { lang ->
                when (lang) {
                    AppLanguage.EN -> "Madhhab note (Hanafi): the whey in this product was treated as permissible because it follows the Hanafi ruling on rennet. This is an inference from that ruling, not a ruling found directly about whey."
                    AppLanguage.DE -> "Hinweis zur Rechtsschule (hanafitisch): Die Molke in diesem Produkt wurde als zulässig gewertet, weil sie der hanafitischen Regel zum Lab folgt. Das ist eine Ableitung aus dieser Regel, keine direkt zu Molke gefundene Regel."
                    AppLanguage.FR -> "Note sur l'école (hanafite) : le lactosérum de ce produit a été considéré comme permis car il suit le statut hanafite de la présure. Il s'agit d'une déduction à partir de ce statut, et non d'un avis trouvé directement sur le lactosérum."
                    AppLanguage.TR -> "Mezhep notu (Hanefi): Bu üründeki peyniraltı suyu, Hanefi mezhebinin peynir mayası hükmünü takip ettiği için caiz sayıldı. Bu, doğrudan peyniraltı suyu hakkında bulunmuş bir hüküm değil, o hükümden yapılmış bir çıkarımdır."
                    AppLanguage.AR -> "ملاحظة مذهبية (حنفي): اعتُبر مصل اللبن في هذا المنتج جائزاً لأنه يتبع الحكم الحنفي في الإنفحة. وهذا استنتاج من ذلك الحكم، وليس حكماً وُجد مباشرة في مصل اللبن."
                }
            },
            differenceNote = { lang ->
                when (lang) {
                    AppLanguage.EN -> "Madhhab note: when 'Hanafi' is selected, whey counts as permissible because it follows the Hanafi ruling on rennet - an inference, not a ruling found directly about whey."
                    AppLanguage.DE -> "Hinweis zur Rechtsschule: Bei Auswahl 'Hanafitisch' gilt Molke als zulässig, weil sie der hanafitischen Regel zum Lab folgt - eine Ableitung, keine direkt zu Molke gefundene Regel."
                    AppLanguage.FR -> "Note sur l'école : si « Hanafite » est sélectionné, le lactosérum est considéré comme permis car il suit le statut hanafite de la présure - une déduction, et non un avis trouvé directement sur le lactosérum."
                    AppLanguage.TR -> "Mezhep notu: 'Hanefi' seçiliyken peyniraltı suyu, peynir mayası hükmünü takip ettiği için caiz sayılır - bu doğrudan bir hüküm değil, bir çıkarımdır."
                    AppLanguage.AR -> "ملاحظة مذهبية: عند اختيار \"حنفي\" يُعدّ مصل اللبن جائزاً لأنه يتبع الحكم الحنفي في الإنفحة - وهذا استنتاج، وليس حكماً وُجد مباشرة في مصل اللبن."
                }
            }
        )
    )

    // Keywords of a rule that actually match, in the text or in OFF's additive tags.
    private fun matchedKeywords(keywords: List<String>, text: String, additiveTags: List<String>): List<String> =
        keywords.filter { kw -> containsKeyword(text, kw) || additiveTags.any { tag -> containsKeyword(tag, kw) } }

    // The override (if any) a madhhab-specific ruling applies to this match. Shared by the product
    // verdict and the chip colouring so the two can never disagree.
    private fun applicableOverride(ruleId: String, matched: List<String>, madhhab: Madhhab): MadhhabOverride? =
        MADHHAB_OVERRIDES.firstOrNull { o ->
            o.ruleId == ruleId && o.madhhab == madhhab && matched.isNotEmpty() && matched.all { it in o.keywords }
        }

    // Overrides for this rule whose keywords matched but which don't apply under [madhhab] - used
    // to tell the user the result would differ under another school.
    private fun differingOverrides(ruleId: String, matched: List<String>, madhhab: Madhhab): List<MadhhabOverride> =
        MADHHAB_OVERRIDES.filter { o ->
            o.ruleId == ruleId && o.madhhab != madhhab && matched.any { it in o.keywords }
        }

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

    internal val HARAM_RULES = listOf(
        HaramRule(
            keywords = listOf("e441", "pork gelatin", "porcine gelatin", "schweinegelatine", "gelatine de porc", "gélatine de porc", "porc gelatine", "varkensgelatine", "domuz jelatini",
                "جيلاتين خنزير", "جيلاتين الخنزير", "جلاتين خنزير"),
            id = PORK_GELATIN_RULE_ID,
            name = LocalizedText(
                en = "E441 Pork Gelatin",
                de = "E441 Schweinegelatine",
                fr = "E441 Gélatine de porc",
                tr = "E441 Domuz Jelatini",
                ar = "E441 جيلاتين الخنزير"
            ),
            reason = LocalizedText(
                en = "Porcine collagen derived from pig skin and bones. Prohibited in Islamic law.",
                de = "Kollagen vom Schwein, gewonnen aus Schweinehaut und -knochen. Im islamischen Recht verboten.",
                fr = "Collagène porcin issu de la peau et des os de porc. Interdit en droit islamique.",
                tr = "Domuz deri ve kemik kolajeninden elde edilen jelleştirici. Dinen haramdır.",
                ar = "كولاجين مستخرج من جلد الخنزير وعظامه. محرّم في الشريعة الإسلامية."
            ),
            eCode = "E441",
            origin = LocalizedText(
                en = "Pork / Swine",
                de = "Schwein",
                fr = "Porc",
                tr = "Domuz",
                ar = "خنزير"
            )
        ),
        HaramRule(
            // "porcine" bare (not just "porcine gelatin" in the E441 rule above) so
            // "porcine collagen", "porcine enzymes", "porcine fat" etc. don't slip through -
            // containsKeyword's word-boundary check means only the E441 rule's exact phrase
            // "porcine gelatin" matched before, not "porcine" followed by any other word.
            keywords = listOf("pork", "pig", "swine", "porc", "porcine", "cochon", "jambon", "~schwein", "schweinefleisch", "speck", "lard", "bacon", "ham", "pork fat", "carne de cerdo", "manteca de cerdo", "domuz", "domuz eti", "domuz yağı",
                // Arabic: the article/conjunction prefix ("الخنزير", "دهن الخنزير") is handled in
                // containsKeyword, so the bare noun covers them.
                "خنزير"),
            id = PORK_RULE_ID,
            name = LocalizedText(
                en = "Pork / Lard / Swine Derivatives",
                de = "Schweinefleisch / Schmalz / Schweineerzeugnisse",
                fr = "Porc / Saindoux / Dérivés du porc",
                tr = "Domuz Eti / Domuz Yağı (Lard)",
                ar = "لحم الخنزير / شحمه / مشتقاته"
            ),
            reason = LocalizedText(
                en = "Direct pig meat or lard fats. Explicitly forbidden in the Quran.",
                de = "Schweinefleisch oder Schweineschmalz. Im Koran ausdrücklich verboten.",
                fr = "Viande de porc ou saindoux. Explicitement interdit dans le Coran.",
                tr = "Domuz eti ve iç yağı. Tüketimi Kur'an-ı Kerim'de açıkça yasaklanmıştır.",
                ar = "لحم الخنزير أو شحمه. محرّم صراحةً في القرآن الكريم."
            ),
            eCode = null,
            origin = LocalizedText(
                en = "Pork / Swine",
                de = "Schwein",
                fr = "Porc",
                tr = "Domuz",
                ar = "خنزير"
            )
        ),
        HaramRule(
            keywords = listOf("e120", "cochineal", "carmines", "karmin", "carmine", "acide carminique", "karminsäure", "cochineal extract", "crimson lake", "natural red 4", "dactylopius coccus",
                "كارمين", "قرمز", "دودة القرمز"),
            id = "E120 Carmine (Cochineal Red)",
            name = LocalizedText(
                en = "E120 Carmine (Cochineal Red)",
                de = "E120 Karmin (Cochenillerot)",
                fr = "E120 Carmin (Rouge cochenille)",
                tr = "E120 Karmin (Koşnil Kırmızısı)",
                ar = "E120 الكارمين (أحمر القرمز)"
            ),
            reason = LocalizedText(
                en = "Red pigment obtained from crushed female scale insects. Prohibited across major Islamic fiqh councils.",
                de = "Roter Farbstoff aus zerstoßenen weiblichen Schildläusen. Von großen islamischen Fiqh-Gremien als unzulässig eingestuft.",
                fr = "Pigment rouge obtenu à partir de cochenilles femelles broyées. Jugé interdit par les principaux conseils de fiqh islamique.",
                tr = "Kurutulmuş kabuklu kalkan bitinin ezilmesiyle elde edilen kırmızı renklendirici. Fıkhen sakıncalıdır.",
                ar = "صبغة حمراء تُستخرج من سحق إناث حشرة القرمز. تعدّها المجامع الفقهية الإسلامية الكبرى غير جائزة."
            ),
            eCode = "E120",
            origin = LocalizedText(
                en = "Insect (Dactylopius coccus)",
                de = "Insekt (Dactylopius coccus)",
                fr = "Insecte (Dactylopius coccus)",
                tr = "Böcek (Dactylopius coccus)",
                ar = "حشرة (Dactylopius coccus)"
            )
        ),
        HaramRule(
            // Note: no bare "bourbon" - "Bourbon-Vanille" (Bourbon vanilla, named after Île
            // Bourbon/Réunion) is a completely alcohol-free, extremely common ingredient term
            // in German product listings, and would collide with a plain "bourbon" keyword.
            // Real bourbon-whiskey mentions are still caught by "whiskey"/"whisky" below.
            // Wine/sherry VINEGAR deliberately excluded here (was previously listed as its own
            // "wine vinegar" trigger phrase) - see the wine-vinegar block in NEGATION_PHRASES
            // below for the fiqh rationale (istihala: the alcohol has fully converted to acetic
            // acid, so majority fiqh treats it as halal, not as wine).
            keywords = listOf("alcohol", "ethanol", "ethyl alcohol", "ethylalkohol", "alkohol", "liqueur", "~likör", "rum", "rhum", "whiskey", "whisky", "vodka", "wine", "wein", "vin", "beer", "bier", "bière", "brandy", "cognac", "champagne", "amaretto", "cooking wine", "sherry", "mirin", "sake",
                // Turkish (added 2026-10-07; before this a Turkish label saying "ALKOL"/"ŞARAP"
                // came out Halal). Suffixes defeat the word-boundary check, so the inflected
                // forms are listed - never prefix matching, or "bira" would hit "biraz".
                // Deliberately no "rom"/"romlu": rum flavouring in cakes is a separate policy call.
                "alkol", "alkollü", "alkolü", "etil alkol", "etanol",
                "şarap", "şarabı", "şaraplı", "bira", "birası", "biralı",
                // German wine compounds, listed in full: "wein" can't be a stem because of
                // Weinsäure (tartaric acid), Weinstein (cream of tartar) and Weinessig.
                "rotwein", "weißwein", "weisswein", "roséwein", "dessertwein", "glühwein", "portwein",
                "schaumwein", "sekt", "weinbrand", "branntwein", "obstbrand",
                // Arabic. "كحولي/كحولية" listed because suffixes defeat the end boundary.
                "كحول", "كحولي", "كحولية", "إيثانول", "نبيذ", "خمر", "بيرة", "روم", "ويسكي", "فودكا",
                "براندي", "كونياك", "ليكيور"),
            id = "Alcohol / Liqueur / Wine Additive",
            name = LocalizedText(
                en = "Alcohol / Liqueur / Wine Additive",
                de = "Alkohol / Likör / Weinbestandteil",
                fr = "Alcool / Liqueur / Ingrédient à base de vin",
                tr = "Alkol / Likör / Şarap Bileşeni",
                ar = "كحول / مشروب كحولي / مكوّن من النبيذ"
            ),
            reason = LocalizedText(
                en = "Intoxicating alcoholic beverages or flavourings. Non-permissible in foods.",
                de = "Berauschende alkoholische Getränke oder Aromen. In Lebensmitteln nicht zulässig.",
                fr = "Boissons ou arômes alcoolisés enivrants. Non permis dans les aliments.",
                tr = "Aroma verici veya çözücü olarak kullanılan alkol / likör bileşeni. Gıdalarda tüketimi helal değildir.",
                ar = "مشروبات أو منكّهات كحولية مسكرة. غير جائزة في الأطعمة."
            ),
            eCode = null,
            origin = LocalizedText(
                en = "Alcoholic Intoxicant",
                de = "Alkoholisches Rauschmittel",
                fr = "Substance alcoolique enivrante",
                tr = "Sarhoş edici alkol",
                ar = "مادة كحولية مسكرة"
            )
        ),
        HaramRule(
            keywords = listOf("e542", "bone phosphate", "schweinefett"),
            id = "E542 Bone Phosphate",
            name = LocalizedText(
                en = "E542 Bone Phosphate",
                de = "E542 Knochenphosphat",
                fr = "E542 Phosphate d'os",
                tr = "E542 Kemik Fosfatı",
                ar = "E542 فوسفات العظام"
            ),
            reason = LocalizedText(
                en = "Derived from defatted bones of non-halal slaughtered animals.",
                de = "Gewonnen aus entfetteten Knochen nicht halal geschlachteter Tiere.",
                fr = "Obtenu à partir d'os dégraissés d'animaux non abattus selon le rite halal.",
                tr = "Helal kesim olmayan hayvan kemiklerinden elde edilen mineral.",
                ar = "يُستخرج من عظام منزوعة الدهن لحيوانات لم تُذبح ذبحاً حلالاً."
            ),
            eCode = "E542",
            origin = LocalizedText(
                en = "Animal Bones",
                de = "Tierknochen",
                fr = "Os d'animaux",
                tr = "Hayvan kemiği",
                ar = "عظام حيوانية"
            )
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
            id = "Blood / Blood Plasma",
            name = LocalizedText(
                en = "Blood / Blood Plasma",
                de = "Blut / Blutplasma",
                fr = "Sang / Plasma sanguin",
                tr = "Kan / Kan Plazması",
                ar = "دم / بلازما الدم"
            ),
            reason = LocalizedText(
                en = "Drained/consumed blood is explicitly forbidden in Islamic law.",
                de = "Der Verzehr von ausgeflossenem Blut ist im islamischen Recht ausdrücklich verboten.",
                fr = "La consommation de sang versé est explicitement interdite en droit islamique.",
                tr = "Akıtılmış kanın tüketimi dinen açıkça yasaklanmıştır.",
                ar = "تناول الدم المسفوح محرّم صراحةً في الشريعة الإسلامية."
            ),
            eCode = null,
            origin = LocalizedText(
                en = "Animal Blood",
                de = "Tierblut",
                fr = "Sang animal",
                tr = "Hayvan kanı",
                ar = "دم حيواني"
            )
        )
    )

    internal val SUSPICIOUS_RULES = listOf(
        SuspiciousRule(
            keywords = listOf("e471", "mono- and diglycerides of fatty acids", "monoglycerides", "diglycerides", "mono- et diglycérides d'acides gras", "speisefettsäuren"),
            id = "E471 Mono- and Diglycerides",
            name = LocalizedText(
                en = "E471 Mono- and Diglycerides",
                de = "E471 Mono- und Diglyceride von Speisefettsäuren",
                fr = "E471 Mono- et diglycérides d'acides gras",
                tr = "E471 Yağ Asitlerinin Mono/Digliseritleri",
                ar = "E471 أحادي وثنائي غليسريد الأحماض الدهنية"
            ),
            reason = LocalizedText(
                en = "Emulsifier from vegetable oil (Halal) OR animal fats (Pork/Beef lard). Source must be verified.",
                de = "Emulgator aus Pflanzenöl (halal) ODER tierischem Fett (Schwein/Rind). Die Herkunft muss geprüft werden.",
                fr = "Émulsifiant issu d'huile végétale (halal) OU de graisses animales (porc/bœuf). L'origine doit être vérifiée.",
                tr = "Bitkisel yağdan üretildiyse helal, hayvansal yağdan (domuz/sığır) üretildiyse sakıncalıdır. Kaynak teyidi gerekir.",
                ar = "مستحلب من زيت نباتي (حلال) أو من دهون حيوانية (خنزير/بقر). يجب التحقق من المصدر."
            ),
            eCode = "E471",
            origin = LocalizedText(
                en = "Plant or Animal",
                de = "Pflanzlich oder tierisch",
                fr = "Végétale ou animale",
                tr = "Bitkisel veya hayvansal",
                ar = "نباتي أو حيواني"
            )
        ),
        SuspiciousRule(
            keywords = listOf("e472", "e472a", "e472b", "e472c", "e472e", "datem", "esters of mono- and diglycerides"),
            id = "E472a-f / DATEM Emulsifiers",
            name = LocalizedText(
                en = "E472a-f / DATEM Emulsifiers",
                de = "E472a-f / DATEM-Emulgatoren",
                fr = "E472a-f / Émulsifiants DATEM",
                tr = "E472a-f / DATEM Yağ Asidi Esterleri",
                ar = "E472a-f / مستحلبات DATEM"
            ),
            reason = LocalizedText(
                en = "Fatty acid derivatives used in bakery. Requires vegetable origin confirmation.",
                de = "Fettsäurederivate für Backwaren. Die pflanzliche Herkunft muss bestätigt sein.",
                fr = "Dérivés d'acides gras utilisés en boulangerie. L'origine végétale doit être confirmée.",
                tr = "Unlu mamul emülgatörleri. Hayvansal yağ riski nedeniyle bitkisel köken teyidi gereklidir.",
                ar = "مشتقات أحماض دهنية تُستخدم في المخبوزات. يلزم تأكيد المصدر النباتي."
            ),
            eCode = "E472",
            origin = LocalizedText(
                en = "Plant or Animal",
                de = "Pflanzlich oder tierisch",
                fr = "Végétale ou animale",
                tr = "Bitkisel veya hayvansal",
                ar = "نباتي أو حيواني"
            )
        ),
        SuspiciousRule(
            keywords = listOf("e422", "glycerin", "glycerine", "glycerol", "gliserin", "gliserol"),
            id = "E422 Glycerin / Glycerol",
            name = LocalizedText(
                en = "E422 Glycerin / Glycerol",
                de = "E422 Glycerin / Glycerol",
                fr = "E422 Glycérine / Glycérol",
                tr = "E422 Gliserin / Gliserol",
                ar = "E422 الغليسرين / الغليسرول"
            ),
            reason = LocalizedText(
                en = "Humectant from vegetable oil (Halal) OR animal fat (Pork/Beef tallow). Source must be verified.",
                de = "Feuchthaltemittel aus Pflanzenöl (halal) ODER tierischem Fett (Schwein/Rindertalg). Die Herkunft muss geprüft werden.",
                fr = "Humectant issu d'huile végétale (halal) OU de graisse animale (porc/suif de bœuf). L'origine doit être vérifiée.",
                tr = "Bitkisel yağdan üretildiyse helal, hayvansal yağdan (domuz/sığır donyağı) üretildiyse sakıncalıdır. Kaynak teyidi gerekir.",
                ar = "مادة مرطّبة من زيت نباتي (حلال) أو من دهن حيواني (خنزير/شحم بقر). يجب التحقق من المصدر."
            ),
            eCode = "E422",
            origin = LocalizedText(
                en = "Plant or Animal",
                de = "Pflanzlich oder tierisch",
                fr = "Végétale ou animale",
                tr = "Bitkisel veya hayvansal",
                ar = "نباتي أو حيواني"
            )
        ),
        SuspiciousRule(
            keywords = listOf("e904", "shellac", "confectioner's glaze", "resinous glaze"),
            id = "E904 Shellac / Confectioner's Glaze",
            name = LocalizedText(
                en = "E904 Shellac / Confectioner's Glaze",
                de = "E904 Schellack / Konditorlack",
                fr = "E904 Gomme laque / Glaçage de confiseur",
                tr = "E904 Şellak / Parlatıcı Sır",
                ar = "E904 الشيلاك / طلاء الحلويات"
            ),
            reason = LocalizedText(
                en = "Glazing agent from lac insect secretion. Deemed doubtful by several Halal standards.",
                de = "Überzugsmittel aus dem Sekret der Lackschildlaus. Mehrere Halal-Standards stufen es als zweifelhaft ein.",
                fr = "Agent d'enrobage issu de la sécrétion de la cochenille à laque. Jugé douteux par plusieurs normes halal.",
                tr = "Lak böceğinin salgısından üretilen parlatıcı sır. Bazı helal mercilerince şüpheli görülür.",
                ar = "مادة تلميع من إفراز حشرة اللك. تعدّها عدة معايير حلال مشبوهة."
            ),
            eCode = "E904",
            origin = LocalizedText(
                en = "Insect Secretion",
                de = "Insektensekret",
                fr = "Sécrétion d'insecte",
                tr = "Böcek salgısı",
                ar = "إفراز حشري"
            )
        ),
        SuspiciousRule(
            keywords = listOf("e920", "l-cysteine", "l-cystein", "cysteine"),
            id = "E920 L-Cysteine (Dough Conditioner)",
            name = LocalizedText(
                en = "E920 L-Cysteine (Dough Conditioner)",
                de = "E920 L-Cystein (Mehlbehandlungsmittel)",
                fr = "E920 L-Cystéine (agent de traitement de la farine)",
                tr = "E920 L-Sistein (Hamur Geliştirici)",
                ar = "E920 إل-سيستين (محسّن للعجين)"
            ),
            reason = LocalizedText(
                en = "Amino acid often sourced from duck feathers or animal hair. Must be synthetic/fermented.",
                de = "Aminosäure, oft aus Entenfedern oder Tierhaaren gewonnen. Muss synthetisch oder durch Fermentation hergestellt sein.",
                fr = "Acide aminé souvent issu de plumes de canard ou de poils d'animaux. Doit être synthétique ou issu de fermentation.",
                tr = "Ördek tüyü veya kıldan elde edilen un geliştirici. Sentetik/fermantasyon kökenli olmalıdır.",
                ar = "حمض أميني يُستخرج غالباً من ريش البط أو شعر الحيوانات. يجب أن يكون مُصنّعاً أو ناتجاً عن التخمير."
            ),
            eCode = "E920",
            origin = LocalizedText(
                en = "Animal feathers/hair or Fermentation",
                de = "Federn/Haare von Tieren oder Fermentation",
                fr = "Plumes/poils d'animaux ou fermentation",
                tr = "Hayvan tüyü/kılı veya fermantasyon",
                ar = "ريش/شعر حيواني أو تخمير"
            )
        ),
        SuspiciousRule(
            keywords = listOf("e631", "disodium inosinate", "sodium inosinate"),
            id = "E631 Disodium Inosinate",
            name = LocalizedText(
                en = "E631 Disodium Inosinate",
                de = "E631 Dinatriuminosinat",
                fr = "E631 Inosinate disodique",
                tr = "E631 Disodyum İnozinat",
                ar = "E631 إينوزينات ثنائي الصوديوم"
            ),
            reason = LocalizedText(
                en = "Flavor enhancer potentially prepared from meat byproducts.",
                de = "Geschmacksverstärker, der aus Fleischnebenprodukten hergestellt sein kann.",
                fr = "Exhausteur de goût pouvant être fabriqué à partir de sous-produits de viande.",
                tr = "Et yan ürünlerinden üretilebilen lezzet artırıcı. Bitkisel/tapiyoka teyidi gerektirir.",
                ar = "مُحسّن نكهة قد يُحضَّر من مخلفات اللحوم."
            ),
            eCode = "E631",
            origin = LocalizedText(
                en = "Meat or Microbial",
                de = "Fleisch oder mikrobiell",
                fr = "Viande ou microbienne",
                tr = "Et veya mikrobiyel",
                ar = "لحم أو مصدر ميكروبي"
            )
        ),
        SuspiciousRule(
            keywords = listOf(
                "beef gelatin", "bovine gelatin", "fish gelatin",
                "rindergelatine", "fischgelatine",
                "gélatine de bœuf", "gélatine de poisson",
                "sığır jelatini", "balık jelatini",
                "gelatina de res", "gelatina de pescado"
            ),
            id = BEEF_FISH_GELATIN_RULE_ID,
            name = LocalizedText(
                en = "Beef/Fish Gelatin (Source Stated)",
                de = "Rinder-/Fischgelatine (Herkunft angegeben)",
                fr = "Gélatine de bœuf/poisson (origine indiquée)",
                tr = "Sığır/Balık Jelatini (Kaynak Belirtilmiş)",
                ar = "جيلاتين بقري/سمكي (المصدر مذكور)"
            ),
            reason = LocalizedText(
                en = "Source is stated as beef or fish, not pork - but halal status still depends on whether the animal was slaughtered according to zabiha (Islamic) method, which cannot be confirmed from packaging alone.",
                de = "Als Herkunft ist Rind oder Fisch angegeben, nicht Schwein - der Halal-Status hängt aber weiterhin davon ab, ob das Tier nach Zabiha-Ritus (islamisch) geschlachtet wurde, was sich anhand der Verpackung allein nicht bestätigen lässt.",
                fr = "L'origine indiquée est le bœuf ou le poisson, pas le porc - mais le statut halal dépend toujours de l'abattage zabiha (rite islamique) de l'animal, ce que l'emballage seul ne permet pas de confirmer.",
                tr = "Kaynağın sığır veya balık olduğu belirtilmiş - domuz değil. Ancak helal olması için hayvanın zebiha usulüne uygun kesilmiş olması gerekir; bu bilgi ambalajdan doğrulanamaz.",
                ar = "المصدر المذكور بقري أو سمكي وليس خنزيراً، لكن الحكم بالحلّ لا يزال يتوقف على ذبح الحيوان ذبحاً شرعياً، وهو ما لا يمكن تأكيده من العبوة وحدها."
            ),
            eCode = "E441",
            origin = LocalizedText(
                en = "Beef / Fish",
                de = "Rind / Fisch",
                fr = "Bœuf / Poisson",
                tr = "Sığır / Balık",
                ar = "بقر / سمك"
            )
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
            id = "Bovine Collagen (Slaughter Method Unconfirmed)",
            name = LocalizedText(
                en = "Bovine Collagen (Slaughter Method Unconfirmed)",
                de = "Rinderkollagen (Schlachtmethode unbestätigt)",
                fr = "Collagène bovin (méthode d'abattage non confirmée)",
                tr = "Sığır Kolajeni (Kesim Yöntemi Teyit Edilmedi)",
                ar = "كولاجين بقري (طريقة الذبح غير مؤكدة)"
            ),
            reason = LocalizedText(
                en = "Collagen/collagen peptides derived from cattle skin, bone, or connective tissue. Halal status depends on zabiha (Islamic) slaughter, which cannot be confirmed from this product's data alone.",
                de = "Kollagen/Kollagenpeptide aus Rinderhaut, -knochen oder Bindegewebe. Der Halal-Status hängt von der Zabiha-Schlachtung (islamisch) ab, die sich anhand der Produktdaten allein nicht bestätigen lässt.",
                fr = "Collagène/peptides de collagène issus de la peau, des os ou du tissu conjonctif de bovins. Le statut halal dépend de l'abattage zabiha (rite islamique), qui ne peut être confirmé à partir des seules données du produit.",
                tr = "Sığır deri, kemik veya bağ dokusundan elde edilen kolajen/kolajen peptit. Helal olması dinen usulüne uygun (zebiha) kesime bağlıdır; bu bilgi ürün verisinden doğrulanamaz.",
                ar = "كولاجين أو ببتيدات كولاجين مستخرجة من جلد الأبقار أو عظامها أو نسيجها الضام. يتوقف الحكم بالحلّ على الذبح الشرعي، وهو ما لا يمكن تأكيده من بيانات المنتج وحدها."
            ),
            eCode = null,
            origin = LocalizedText(
                en = "Beef / Bovine",
                de = "Rind",
                fr = "Bovin",
                tr = "Sığır",
                ar = "بقري"
            ),
            suppressIfHalalClaim = true
        ),
        SuspiciousRule(
            keywords = listOf("gelatin", "gélatine", "~gelatine", "gelatina", "jelatin", "جيلاتين", "جلاتين"),
            id = GELATIN_UNSPECIFIED_RULE_ID,
            name = LocalizedText(
                en = "Gelatin (Unspecified Source)",
                de = "Gelatine (Herkunft nicht angegeben)",
                fr = "Gélatine (origine non précisée)",
                tr = "Jelatin (Kaynağı Belirtilmemiş)",
                ar = "جيلاتين (المصدر غير مذكور)"
            ),
            reason = LocalizedText(
                en = "Packaging does not specify whether fish, halal bovine, or non-halal porcine source is used.",
                de = "Auf der Verpackung steht nicht, ob Fisch, halal geschlachtetes Rind oder (nicht halales) Schwein verwendet wurde.",
                fr = "L'emballage ne précise pas si la source est du poisson, du bœuf halal ou du porc (non halal).",
                tr = "Ambalajda sığır, balık veya domuz olduğu belirtilmemiştir. Üreticiden teyit edilmelidir.",
                ar = "لا توضح العبوة ما إذا كان المصدر سمكياً أو بقرياً حلالاً أو خنزيرياً غير حلال."
            ),
            eCode = "E441",
            origin = LocalizedText(
                en = "Animal",
                de = "Tierisch",
                fr = "Animale",
                tr = "Hayvansal",
                ar = "حيواني"
            )
        ),
        SuspiciousRule(
            // Bare "lab" (German for rennet) dropped: as a standalone word-boundary match it
            // also fires on "lab-grown", "lab-tested", etc. in English ingredient text. Known gap
            // (2026-10-15): an unqualified German "Lab" is therefore NOT flagged - "Milch, Salz,
            // Lab" comes out Halal. (An earlier note here claimed a no-claim fallback makes such
            // products Şüpheli; tests show it doesn't.)
            // Turkish terms added 2026-10-07 - Turkish-labelled cheese wasn't caught at all before.
            // Never bare "maya": on its own that is bread/baker's yeast.
            // "tierisches Lab" (animal rennet) is the wording on Milbona Gouda gerieben, 20692490.
            keywords = listOf("rennet", "pepsin", "animal rennet", "kälberlab", "kalbslab", "tierisches lab", "tierischem lab", "présure", "animal enzymes", "cheese cultures",
                "peynir mayası", "hayvansal peynir mayası", "hayvansal maya", "şirden", "إنفحة", "منفحة"),
            id = RENNET_RULE_ID,
            name = LocalizedText(
                en = "Animal Rennet / Cheese Enzymes",
                de = "Tierisches Lab / Käseenzyme",
                fr = "Présure animale / Enzymes fromagères",
                tr = "Hayvansal Peynir Mayası / Enzim",
                ar = "إنفحة حيوانية / إنزيمات الجبن"
            ),
            reason = LocalizedText(
                en = "Curdling enzyme from calf stomach. Permissible only if from Halal slaughtered animals or microbial.",
                de = "Gerinnungsenzym aus Kälbermagen. Nur zulässig, wenn es von halal geschlachteten Tieren stammt oder mikrobiell ist.",
                fr = "Enzyme de caillage issue de l'estomac de veau. Permise seulement si elle provient d'animaux abattus selon le rite halal ou si elle est microbienne.",
                tr = "Buzağı şirdeninden elde edilen peynir mayası. Helal kesim veya mikrobiyel olmalıdır.",
                ar = "إنزيم تخثير يُستخرج من معدة العجل. لا يجوز إلا إذا كان من حيوان مذبوح ذبحاً حلالاً أو من مصدر ميكروبي."
            ),
            eCode = null,
            origin = LocalizedText(
                en = "Animal (Calf / Swine)",
                de = "Tierisch (Kalb / Schwein)",
                fr = "Animale (veau / porc)",
                tr = "Hayvansal (buzağı / domuz)",
                ar = "حيواني (عجل / خنزير)"
            )
        ),
        SuspiciousRule(
            // Label says only "Lab" (or "Lab" with no source): it may be animal or microbial and
            // the packaging doesn't say which (Oekotest: consumers can't tell). Doubtful, never Haram.
            // No madhhab override: without a stated source, the Hanafi ruling on animal rennet
            // can't be applied, so this stays Doubtful under every school.
            keywords = listOf(BARE_LAB_KEYWORD),
            id = BARE_LAB_RULE_ID,
            name = LocalizedText(
                en = "Unlabelled Rennet ('Lab')",
                de = "Lab ohne Herkunftsangabe",
                fr = "Présure non précisée (« Lab »)",
                tr = "Menşei Belirsiz Peynir Mayası (\"Lab\")",
                ar = "إنفحة غير محددة المصدر (\"Lab\")"
            ),
            reason = LocalizedText(
                en = "'Lab' (rennet) with no origin stated. It can be animal (calf stomach) or microbial, and the label doesn't say which. Not clearly halal until the manufacturer confirms.",
                de = "„Lab“ ohne Angabe der Herkunft. Es kann tierisch (Kälbermagen) oder mikrobiell sein; die Packung sagt nicht, welches. Ohne Auskunft des Herstellers nicht eindeutig halal.",
                fr = "« Présure » sans origine indiquée. Elle peut être animale (estomac de veau) ou microbienne ; l'étiquette ne précise pas laquelle. Pas clairement halal sans confirmation du fabricant.",
                tr = "Menşei belirtilmemiş peynir mayası (\"Lab\"). Hayvansal (buzağı midesi) veya mikrobiyel olabilir; etiket hangisi olduğunu belirtmiyor. Üretici teyit etmeden kesin helal değildir.",
                ar = "إنفحة (\"Lab\") دون بيان مصدرها. قد تكون حيوانية (من معدة العجل) أو ميكروبية، والعبوة لا توضح ذلك. لا تُعدّ حلالاً بوضوح دون تأكيد من المصنّع."
            ),
            eCode = null,
            origin = LocalizedText(
                en = "Unknown (animal or microbial)",
                de = "Unklar (tierisch oder mikrobiell)",
                fr = "Inconnue (animale ou microbienne)",
                tr = "Belirsiz (hayvansal veya mikrobiyel)",
                ar = "غير معروف (حيواني أو ميكروبي)"
            )
        ),
        SuspiciousRule(
            keywords = listOf("whey", "whey powder", "~molke", "lactosérum", "peynir altı suyu tozu", "مصل اللبن", "مصل الحليب"),
            id = WHEY_RULE_ID,
            name = LocalizedText(
                en = "Whey Powder / Whey Byproduct",
                de = "Molkenpulver / Molkenerzeugnis",
                fr = "Lactosérum en poudre / Dérivé du lactosérum",
                tr = "Peynir Altı Suyu Tozu",
                ar = "مصل اللبن المجفف / مشتقاته"
            ),
            reason = LocalizedText(
                en = "Cheese byproduct that may involve animal rennet enzymes during milk curdling.",
                de = "Nebenprodukt der Käseherstellung, bei dem tierisches Lab zum Gerinnen der Milch verwendet worden sein kann.",
                fr = "Sous-produit du fromage pour lequel de la présure animale a pu être utilisée lors du caillage du lait.",
                tr = "Peynir üretiminde kullanılan hayvansal mayanın durumuna göre şüpheli olabilir.",
                ar = "ناتج ثانوي لصناعة الجبن قد تُستخدم فيه إنفحة حيوانية عند تخثير الحليب."
            ),
            eCode = null,
            origin = LocalizedText(
                en = "Dairy Byproduct",
                de = "Milchnebenprodukt",
                fr = "Sous-produit laitier",
                tr = "Süt yan ürünü",
                ar = "ناتج ثانوي للألبان"
            )
        ),
        SuspiciousRule(
            keywords = MEAT_KEYWORDS,
            id = MEAT_RULE_ID,
            name = LocalizedText(
                en = "Meat / Poultry (Slaughter Method Unconfirmed)",
                de = "Fleisch / Geflügel (Schlachtmethode unbestätigt)",
                fr = "Viande / Volaille (méthode d'abattage non confirmée)",
                tr = "Et / Kümes Hayvanı (Kesim Yöntemi Teyit Edilmedi)",
                ar = "لحم / دواجن (طريقة الذبح غير مؤكدة)"
            ),
            reason = LocalizedText(
                en = "Contains meat or poultry. Halal status depends on zabiha (Islamic) slaughter, which cannot be confirmed from this product listing alone.",
                de = "Enthält Fleisch oder Geflügel. Der Halal-Status hängt von der Zabiha-Schlachtung ab, die anhand der Produktdaten nicht bestätigt werden kann.",
                fr = "Contient de la viande ou de la volaille. Son statut halal dépend de l'abattage zabiha, qui ne peut être confirmé à partir des données du produit.",
                tr = "Et veya kümes hayvanı içeriyor. Helal olması dinen usulüne uygun (zebiha) kesime bağlıdır; bu bilgi ürün verisinden doğrulanamaz.",
                ar = "يحتوي على لحم أو دواجن. تعتمد حالته الحلال على الذبح الشرعي (الذبيحة)، والتي لا يمكن تأكيدها من بيانات المنتج."
            ),
            eCode = null,
            origin = LocalizedText(
                en = "Animal Meat",
                de = "Fleisch",
                fr = "Viande",
                tr = "Hayvan eti",
                ar = "لحم حيواني"
            ),
            suppressIfHalalClaim = true
        )
    )

    fun analyzeOpenFoodFactsProduct(
        barcode: String,
        offProduct: OffProduct,
        language: AppLanguage = AppLanguage.EN,
        // Phase 1: threaded through and stamped on the result only - no rule reads it yet, so
        // every madhhab reproduces the UNSPECIFIED verdict (see HalalAnalyzerMadhhabTest).
        madhhab: Madhhab = Madhhab.UNSPECIFIED
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
        val ingredientsLower = stripNegatedPhrases(normalizeForMatching(ingredientsRaw))
        val additiveTags = offProduct.additivesTags?.map { normalizeForMatching(it).replace("en:", "") } ?: emptyList()
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
        // Tracks which SUSPICIOUS_RULES fired, by their language-stable id, independent of
        // suspiciousLabels (which holds the localized display name). Used below to tell "only
        // the meat rule fired" apart from an actual unverified-origin additive, since those two
        // cases need different reason text - "this additive might be animal-derived" makes no
        // sense for a product whose only flag is containing meat outright.
        val suspiciousRuleIds = mutableListOf<String>()
        val haramRuleIds = mutableListOf<String>()

        // 1. Check for Haram rules (Strict)
        for (rule in HARAM_RULES) {
            val matchedInText = rule.keywords.any { kw -> containsKeyword(ingredientsLower, kw) }
            // containsKeyword (not tag.contains) so additive tag "e1200" doesn't false-match
            // keyword "e120" - a plain substring check treated E1200 (polydextrose) as E120 (carmine).
            val matchedInAdditives = rule.keywords.any { kw -> additiveTags.any { tag -> containsKeyword(tag, kw) } }
            if (matchedInText || matchedInAdditives) {
                val displayName = rule.name.get(language)
                val flagged = FlaggedIngredient(
                    name = displayName,
                    eCode = rule.eCode,
                    status = HalalStatus.HARAM,
                    reason = rule.reason.get(language),
                    origin = rule.origin.get(language)
                )
                // Keyed on the rule id, not the localized name, so a translation can't change which
                // cards (and ids) survive.
                if (rule.id !in haramRuleIds) {
                    flaggedItems.add(flagged)
                    harmfulLabels.add(displayName)
                    haramRuleIds.add(rule.id)
                }
            }
        }

        // 2. Check for Suspicious rules (Doubtful / Mushbooh)
        // Madhhab notes, appended after the verdict sentence (history cards truncate the reason, so
        // the main verdict must stay first). Deduplicated: a rule can't add the same note twice.
        val madhhabNotes = mutableListOf<String>()
        for (rule in SUSPICIOUS_RULES) {
            if (rule.suppressIfHalalClaim && hasHalalClaim) continue
            val matched = matchedKeywords(rule.keywords, ingredientsLower, additiveTags)
            if (matched.isNotEmpty()) {
                val override = applicableOverride(rule.id, matched, madhhab)
                // A rennet/whey difference can't change a Haram verdict (e.g. "pork rennet"), and a
                // note calling that same rennet permissible would contradict the Haram banner.
                val notesRelevant = harmfulLabels.isEmpty()
                if (override != null) {
                    if (notesRelevant) override.appliedNote(language).let { if (it !in madhhabNotes) madhhabNotes.add(it) }
                    // Only HELAL overrides exist today: the match simply isn't flagged.
                    if (override.status == HalalStatus.HELAL) continue
                }
                if (notesRelevant) differingOverrides(rule.id, matched, madhhab).forEach { o ->
                    o.differenceNote(language).let { if (it !in madhhabNotes) madhhabNotes.add(it) }
                }
                // Any gelatin-family suspicious rule (generic-unspecified OR the more specific
                // beef/fish rule) must be skipped once a pork/gelatin Haram flag already fired -
                // otherwise a "not pork" Suspicious card renders directly under the Haram
                // "Prohibited Ingredients" header next to "E441 Pork Gelatin", contradicting it.
                val isGelatinFamilyRule = rule.id == GELATIN_UNSPECIFIED_RULE_ID || rule.id == BEEF_FISH_GELATIN_RULE_ID
                if (isGelatinFamilyRule && (PORK_GELATIN_RULE_ID in haramRuleIds || PORK_RULE_ID in haramRuleIds)) {
                    continue
                }
                // The generic rule additionally defers to the more specific beef/fish rule when
                // that one already matched (source IS stated, just not as pork).
                if (rule.id == GELATIN_UNSPECIFIED_RULE_ID && suspiciousRuleIds.contains(BEEF_FISH_GELATIN_RULE_ID)) {
                    continue
                }
                val displayName = rule.name.get(language)
                val flagged = FlaggedIngredient(
                    name = displayName,
                    eCode = rule.eCode,
                    status = HalalStatus.SUPHELI,
                    reason = rule.reason.get(language),
                    origin = rule.origin.get(language)
                )
                if (rule.id !in suspiciousRuleIds) {
                    flaggedItems.add(flagged)
                    suspiciousLabels.add(displayName)
                    suspiciousRuleIds.add(rule.id)
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
                alternatives = listOf(ALT_CERTIFIED_EQUIVALENTS.get(language), ALT_VEGAN_NO_ALCOHOL.get(language))
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
                    alternatives = listOf(ALT_CERT_MARK.get(language))
                } else {
                    reason = when (language) {
                        AppLanguage.EN -> "Contains additives of unverified origin (${suspiciousLabels.joinToString(", ")}). Must verify whether animal or plant-derived."
                        AppLanguage.DE -> "Enthält Zusätze mit unklarer Herkunft (${suspiciousLabels.joinToString(", ")}). Bitte prüfen, ob pflanzlich oder tierisch."
                        AppLanguage.FR -> "Contient des additifs d'origine non spécifiée (${suspiciousLabels.joinToString(", ")}). Vérifiez si végétal."
                        AppLanguage.TR -> "İçeriğindeki bazı katkı maddeleri (${suspiciousLabels.joinToString(", ")}) bitkisel veya hayvansal kökenli olabilir. Teyit gerektirir."
                        AppLanguage.AR -> "يحتوي على إضافات غير مؤكدة المصدر (${suspiciousLabels.joinToString(", ")}). يرجى التأكد من المصدر النباتي."
                    }
                    alternatives = listOf(ALT_VEGETABLE_EMULSIFIERS.get(language), ALT_CERTIFIED_ALTERNATIVES.get(language))
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
                certificate = (if (hasHalalClaim) CERT_HALAL_LABEL else CERT_VEGAN_LABEL).get(language)
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
            reasonOrDetails = (listOf(reason) + madhhabNotes).joinToString("\n\n"),
            alternatives = alternatives,
            imageUrl = imageUrl,
            language = language,
            madhhab = madhhab,
            nutriScore = offProduct.nutriScore,
            novaGroup = offProduct.novaGroup
        )
    }

    // Negated compounds that must not trip a keyword match. "alcohol-free" is safe to match
    // literally because '-' breaks the word boundary the same way a space does, but that is
    // exactly the bug: "alcohol-free" and "alcohol" share a boundary at the hyphen, so the plain
    // keyword rule fires on a product explicitly declaring the opposite. Scrubbed out before
    // matching rather than special-cased in containsKeyword, since new negated phrases are far
    // easier to add to this list than to a boundary algorithm.
    private val NEGATION_PHRASES = listOf(
        // Compound "modifier + alcoholic-beverage noun" phrases MUST be listed (and stripped)
        // before the bare modifier phrases further down: "beer"/"bier"/"bière"/"wine"/"wein"/
        // "vin" are themselves bare Haram keywords, so a declared "non-alcoholic beer" or
        // "alkoholfreies Bier" previously had only its modifier ("non-alcoholic"/"alkoholfrei")
        // stripped, leaving the bare noun ("beer"/"bier") behind to still trip the alcohol rule -
        // i.e. a 0.0% product was flagged Haram for containing "beer". Stripping the whole
        // compound phrase first removes the noun along with its modifier.
        "alcohol-free beer", "alcohol free beer", "non-alcoholic beer", "non alcoholic beer",
        "alcohol-free wine", "alcohol free wine", "non-alcoholic wine", "non alcoholic wine",
        "alkoholfreies bier", "alkoholfreie bier", "alkoholfreier wein",
        "alkoholfreier rotwein", "alkoholfreier weißwein", "alkoholfreier sekt", "alkoholfreier glühwein",
        "بيرة خالية من الكحول", "بيرة بدون كحول", "نبيذ خال من الكحول",
        "bière sans alcool", "vin sans alcool",
        "cerveza sin alcohol", "vino sin alcohol",
        "alkolsüz bira", "alkolsüz şarap",
        // Manufacturer disclaimers (user chose exact claim phrases, 2026-10-07 - deliberately not
        // sentence-level negation, which could hide a real pork ingredient when OFF text has no
        // sentence breaks). Phrasings taken from real Turkish OFF products. The "alkol, ..." form
        // is listed before the shorter one it contains, or its "alkol," would be left behind.
        "alkol, domuz yağı ve katkıları yoktur", "domuz yağı ve katkıları yoktur",
        "domuz yağı ve katkılarını içermez", "domuz yağı ve katkıları içermez",
        "domuz yağı içermez", "domuz yağı içermemektedir",
        "domuz ürünü içermez", "domuz ürünleri içermez", "domuz eti içermez",
        "domuz türevi içermez", "domuz türevleri içermez",
        "domuzdan elde edilen katkı maddesi içermez", "domuz kaynaklı katkı maddesi içermez",
        "contains no alcohol", "contains no pork", "pork-free", "pork free", "free from pork", "free from alcohol",
        "alcohol-free", "alcohol free", "non-alcoholic", "non alcoholic",
        // Non-beverage "alcohols" and Turkish "contains no alcohol" phrasings. Without these the
        // alcohol keywords above would flag a synthetic polymer, a preservative or a polyol
        // sweetener as Haram. "polivinil"/"benzil" can't stand alone as an ingredient, so those are
        // stems ("polivinil alkol" also strips "polivinil alkolü"). "şeker" and "bira" CAN stand
        // alone, so those phrases are written in full: a bare "şeker alkol" stem would swallow
        // "şeker, alkol (%3)" when OCR drops the comma and turn a Haram product green.
        "polyvinyl alcohol", "benzyl alcohol", "sugar alcohol",
        "polivinil alkol", "benzil alkol",
        "şeker alkolü", "şeker alkolleri", "bira mayası", "bira mayalı",
        "alkol içermez", "alkol içermemektedir", "alkol ihtiva etmez",
        "alkoholfrei", "alkoholfreie", "alkoholfreies", "alkolsüz",
        "ohne alkohol", "sans alcool", "sin alcohol",
        // Wine/sherry VINEGAR is excluded from the bare "wine"/"wein"/"vin"/"sherry" keywords.
        // Fiqh basis (istihala - complete substance transformation): once wine ferments into
        // vinegar its intoxicating alcohol has fully converted to acetic acid, and the majority
        // position across all four Sunni madhabs treats the resulting vinegar as halal, citing
        // the Prophet's saying "What a good food vinegar is" (Sahih Muslim). The narrower fiqh
        // dispute (Shafi'i/Hanbali) concerns deliberately converting wine into vinegar oneself,
        // not consuming vinegar already sold as such - and per IslamQA 2283, a consumer buying
        // commercial vinegar without knowledge of its production method is not obligated to
        // treat it as impermissible. So this is a deliberate exclusion, not an oversight.
        "wine vinegar", "red wine vinegar", "white wine vinegar", "rice wine vinegar", "sherry vinegar",
        "weinessig", "rotweinessig", "weißweinessig",
        "vinaigre de vin", "vinaigre de vin rouge", "vinaigre de vin blanc",
        "vinagre de vino", "vinagre de vino tinto", "vinagre de vino blanco",
        "şarap sirkesi",
        // Explicit plant-based gelatin claims - stripping the whole phrase (not just "gelatin")
        // means the bare "gelatin" keyword below no longer matches this specific mention, while
        // a separate, unrelated "gelatin" elsewhere in the same ingredient list still does.
        "vegetable gelatin", "gélatine végétale", "pflanzliche gelatine", "bitkisel jelatin",
        "gelatina vegetal",
        // Microbial / vegetable rennet: non-animal, permissible per Diyanet's rennet fatwa ("Bitkisel
        // ve mikrobiyal mayaların ... kullanılmasında ... sakınca bulunmamaktadır"). Stripped as a
        // whole so the bare "rennet"/"peynir mayası" keywords don't fire on them, while a separate
        // animal rennet elsewhere in the same list still does.
        "microbial rennet", "vegetable rennet", "non-animal rennet", "vegetarian rennet",
        "présure microbienne", "présure végétale", "mikrobielles lab", "cuajo microbiano", "cuajo vegetal",
        // OFF wording seen on real products: "microbiological rennet" (Frico Gouda, 8710912497127),
        // "mikrobieller Labaustauschstoff" (Milsani, Hofburger, Milbona), "microbiological lab".
        "microbiological rennet", "microbiological lab",
        "mikrobieller labaustauschstoff", "mikrobieller lab",
        "mikrobiyal peynir mayası", "bitkisel peynir mayası", "mikrobiyal maya", "bitkisel maya",
        // ...and the same written with the qualifier in brackets after the noun.
        "rennet (microbial)", "rennet (vegetable)", "rennet (non-animal)",
        "peynir mayası (mikrobiyal)", "peynir mayası (bitkisel)", "maya (mikrobiyal)", "maya (bitkisel)",
        // German compounds that contain a stem (~molke, ~schwein) but not the ingredient:
        // "Molkereibutter"/"Molkereiprodukte" (dairy), "Schweineohren" (puff-pastry palmiers),
        // marzipan/lucky pigs. Ingredient texts sometimes repeat the product name.
        "molkerei", "schweineohr", "schweinsohr", "marzipanschwein", "glücksschwein", "glücksschweinchen",
        // Arabic: alcohol-free / non-alcoholic claims, wine vinegar, pork-free disclaimers,
        // plant gelatine and microbial rennet - same reasoning as the blocks above.
        "خالي من الكحول", "خالية من الكحول", "خال من الكحول", "بدون كحول", "بدون الكحول",
        "لا يحتوي على الكحول", "لا يحتوي على كحول", "غير كحولي", "غير كحولية",
        "خل النبيذ", "خل العنب",
        "خالي من لحم الخنزير", "خالي من الخنزير", "خال من الخنزير", "خالية من الخنزير",
        "لا يحتوي على لحم الخنزير", "لا يحتوي على الخنزير", "لا يحتوي على مشتقات الخنزير",
        "جيلاتين نباتي", "إنفحة ميكروبية", "إنفحة نباتية", "منفحة ميكروبية"
    )

    // Rule keywords and phrases are static, but classifyIngredientToken runs per chip on every
    // recomposition, so their normalized forms are computed once. Thread-safe: the analyzer runs on
    // IO while chips render on the main thread. Raw keywords stay the identities used elsewhere.
    private val normalizedKeywordCache = java.util.concurrent.ConcurrentHashMap<String, String>()
    private fun normalizedKeyword(keyword: String): String =
        normalizedKeywordCache.getOrPut(keyword) { normalizeForMatching(keyword) }
    private val NORMALIZED_NEGATION_PHRASES by lazy { NEGATION_PHRASES.map(::normalizeForMatching) }
    private val NORMALIZED_NUTRITION_MARKERS by lazy { NUTRITION_TABLE_MARKERS.map(::normalizeForMatching) }
    private val NORMALIZED_INGREDIENT_MARKERS by lazy { INGREDIENT_SECTION_MARKERS.map(::normalizeForMatching) }

    // [text] must already be normalizeForMatching()'d; phrases are normalized the same way, or
    // e.g. "mikrobiyal peynir mayası" would stop being stripped from folded text.
    private fun stripNegatedPhrases(text: String): String {
        var result = text
        for (phrase in NORMALIZED_NEGATION_PHRASES) {
            result = result.replace(phrase, " ")
        }
        return result
    }

    /**
     * The one text normalization every matching path uses (rule keywords, negation phrases,
     * ingredient text, chip tokens, section markers). lowercase(Locale.ROOT) alone maps Turkish
     * "İ" to "i" + U+0307 and "I" to "i" (never "ı"), so capitalised Turkish labels - very common
     * on packaging - matched no Turkish keyword at all ("JELATİN" came out Halal). Dropping the
     * combining dot and folding "ı" to "i" makes "PEYNİR MAYASI", "peynir mayası" and the keyword
     * all compare equal. Not Locale("tr"): that lowercases "I" to "ı" and breaks caps English.
     */
    internal fun normalizeForMatching(text: String): String =
        text.lowercase(Locale.ROOT).replace("\u0307", "").replace('ı', 'i')
            // Arabic: labels mix hamza-alif spellings ("إنفحة"/"انفحة"), alif maqsura/ya ("خالى"/
            // "خالي") and optional vowel marks/tatweel, all of which are the same word.
            .replace(ARABIC_MARKS, "")
            .replace('أ', 'ا').replace('إ', 'ا').replace('آ', 'ا').replace('ى', 'ي')

    private val ARABIC_MARKS = Regex("[\u064B-\u0652\u0640]")

    // German writes ingredients as compounds ("Süßmolkenpulver", "Schweineschmalz"), so a few
    // unambiguous stems are marked with this prefix and also match INSIDE a word. Never used for
    // short or ambiguous stems ("wein" -> Weinsäure, "lab" -> lab-grown).
    private const val STEM_MARKER = "~"

    // Arabic attaches the article and some conjunctions/prepositions to the noun ("الخنزير",
    // "والكحول", "بالجيلاتين"). "ك"/"ف" are left out on purpose: "كروم" (vines) would read as rum.
    private val ARABIC_CLITIC_PREFIXES = setOf("ال", "و", "ب", "ل", "وال", "بال", "لل", "ولل")

    // Word-boundary match instead of plain substring: a short keyword like "rum" or "vin"
    // must not fire on "sérum" (whey) or "vinegar" just because it appears inside the word.
    // Checked manually via Char.isLetterOrDigit (Unicode-aware on both JVM and Android) rather
    // than a regex "(?U)\b" flag, which Android's ICU-based regex engine doesn't support and
    // throws PatternSyntaxException on at runtime.
    //
    // [text] must already be normalizeForMatching()'d; the keyword is normalized here so every
    // rule list can stay written in natural spelling (e.g. "peynir mayası", "şirden").
    private fun containsKeyword(text: String, keyword: String): Boolean {
        if (keyword == BARE_LAB_KEYWORD) return containsBareLab(text)
        if (keyword.startsWith(STEM_MARKER)) return containsStem(text, normalizedKeyword(keyword).removePrefix(STEM_MARKER))
        val kw = normalizedKeyword(keyword)
        var fromIndex = 0
        while (true) {
            val idx = text.indexOf(kw, fromIndex)
            if (idx == -1) return false
            val beforeIsBoundary = idx == 0 || !text[idx - 1].isLetterOrDigit() || hasArabicCliticPrefix(text, idx)
            val afterIndex = idx + kw.length
            val afterIsBoundary = afterIndex >= text.length || !text[afterIndex].isLetterOrDigit()
            if (beforeIsBoundary && afterIsBoundary) return true
            fromIndex = idx + 1
        }
    }

    // A stem anywhere inside a word, unless the rest of that word declares its absence
    // ("gelatinefrei", "schweinefleischfreie Wurst").
    private fun containsStem(text: String, stem: String): Boolean {
        var fromIndex = 0
        while (true) {
            val idx = text.indexOf(stem, fromIndex)
            if (idx == -1) return false
            var wordEnd = idx + stem.length
            while (wordEnd < text.length && text[wordEnd].isLetterOrDigit()) wordEnd++
            if (!text.substring(idx + stem.length, wordEnd).contains("frei")) return true
            fromIndex = idx + 1
        }
    }

    // A standalone "lab" (German rennet) whose origin isn't stated. [text] is normalized (lowercase).
    // Skipped when the product names microbial/vegetable rennet anywhere (its "Lab" then has a
    // known origin), and when "lab" directly follows an animal word ("tierisches Lab" is already
    // flagged by the animal-rennet rule, so this rule doesn't duplicate that chip).
    private fun containsBareLab(text: String): Boolean {
        if (BARE_LAB_EXCLUDING_TERMS.any { text.contains(it) }) return false
        var fromIndex = 0
        while (true) {
            val idx = text.indexOf("lab", fromIndex)
            if (idx == -1) return false
            val end = idx + 3
            val beforeOk = idx == 0 || (!text[idx - 1].isLetterOrDigit() && text[idx - 1] != '-')
            val afterOk = end >= text.length || (!text[end].isLetterOrDigit() && text[end] != '-')
            if (beforeOk && afterOk && !followsAnimalWord(text, idx)) return true
            fromIndex = idx + 1
        }
    }

    private val BARE_LAB_EXCLUDING_TERMS = listOf("mikrob", "microb", "vegetab", "pflanz")

    private fun followsAnimalWord(text: String, idx: Int): Boolean {
        var p = idx - 1
        while (p >= 0 && text[p] == ' ') p--
        var q = p
        while (q >= 0 && text[q].isLetter()) q--
        val prev = text.substring(q + 1, p + 1)
        return prev.startsWith("tierisch") || prev.startsWith("kälber") || prev.startsWith("kalb") ||
                prev == "animal" || prev == "animale"
    }

    private fun hasArabicCliticPrefix(text: String, idx: Int): Boolean {
        if (text[idx] !in '\u0600'..'\u06FF') return false
        var wordStart = idx
        while (wordStart > 0 && text[wordStart - 1].isLetterOrDigit()) wordStart--
        return text.substring(wordStart, idx) in ARABIC_CLITIC_PREFIXES
    }

    // Chip colour for one ingredient token. Applies the same madhhab overrides as the product verdict
    // (via applicableOverride) so a chip never contradicts its banner.
    fun classifyIngredientToken(token: String, madhhab: Madhhab = Madhhab.UNSPECIFIED): HalalStatus {
        val lower = stripNegatedPhrases(normalizeForMatching(token))
        if (HARAM_RULES.any { rule -> rule.keywords.any { kw -> containsKeyword(lower, kw) } }) {
            return HalalStatus.HARAM
        }
        val stillSuspicious = SUSPICIOUS_RULES.any { rule ->
            val matched = matchedKeywords(rule.keywords, lower, emptyList())
            matched.isNotEmpty() && applicableOverride(rule.id, matched, madhhab)?.status != HalalStatus.HELAL
        }
        if (stillSuspicious) {
            return HalalStatus.SUPHELI
        }
        return HalalStatus.HELAL
    }

    fun analyzeIngredientsText(
        productName: String,
        ingredientsText: String,
        barcode: String = "",
        language: AppLanguage = AppLanguage.EN,
        madhhab: Madhhab = Madhhab.UNSPECIFIED
    ): FoodProduct {
        val dummyOff = OffProduct(
            productName = productName,
            ingredientsText = ingredientsText,
            categories = "Analyzed Ingredients"
        )
        val result = analyzeOpenFoodFactsProduct(barcode, dummyOff, language, madhhab)

        // A photo of the nutrition-facts panel (Energie/Eiweiß/Kohlenhydrate/kcal/...) contains
        // no E-numbers or ingredient names at all, so the rule engine above finds nothing to flag
        // and falls through to the "no prohibited ingredients found" Helal verdict - which then
        // reads as a confident halal claim about a product whose real ingredients were never
        // actually looked at (caught via a barcode re-scan of "Yogurette": photographing its
        // nutrition table instead of its ingredients list produced Helal here, while the barcode
        // scan correctly flags it Şüpheli for E471). Downgrade only that specific
        // false-confidence case to Doubtful with an explicit retake prompt; a verdict that
        // already found something Haram/Doubtful despite the wrong panel is left as is.
        if (result.status == HalalStatus.HELAL && looksLikeNutritionTableText(ingredientsText)) {
            return result.copy(
                status = HalalStatus.SUPHELI,
                reasonOrDetails = when (language) {
                    AppLanguage.EN -> "This photo looks like the nutrition facts panel (calories, protein, fat...), not the ingredients list, so no additives could be checked. Please retake a photo of the 'Ingredients' section on the packaging."
                    AppLanguage.DE -> "Dieses Foto sieht nach der Nährwerttabelle aus (Kalorien, Eiweiß, Fett...), nicht nach der Zutatenliste - Zusatzstoffe konnten daher nicht geprüft werden. Bitte fotografieren Sie den Abschnitt 'Zutaten' auf der Verpackung."
                    AppLanguage.FR -> "Cette photo ressemble au tableau nutritionnel (calories, protéines, lipides...), pas à la liste des ingrédients - les additifs n'ont donc pas pu être vérifiés. Merci de reprendre une photo de la section « Ingrédients » sur l'emballage."
                    AppLanguage.TR -> "Bu fotoğraf içindekiler listesine değil, besin değerleri tablosuna (kalori, protein, yağ vb.) benziyor; bu yüzden katkı maddeleri kontrol edilemedi. Lütfen ambalajdaki 'İçindekiler' bölümünün fotoğrafını tekrar çekin."
                    AppLanguage.AR -> "تبدو هذه الصورة وكأنها جدول القيم الغذائية (السعرات، البروتين، الدهون...) وليست قائمة المكونات، لذا تعذّر التحقق من الإضافات. يرجى إعادة تصوير قسم 'المكونات' على العبوة."
                },
                halalCertificate = null,
                allIngredients = emptyList()
            )
        }
        return result
    }

    // Nutrition-table words in the languages this app ships (EN/DE/FR/TR/AR); ">= 2 hits" avoids
    // a false positive from one stray word (e.g. a genuine ingredient list that happens to
    // mention "sugar"/"salt"). Suppressed entirely if the text names an actual ingredients
    // section, since some labels print both panels close together in one photo.
    // internal (not private): PhotoIngredientCapture.kt reuses these same word lists to locate
    // the ingredients block among a photo's OCR text blocks, so both layers agree on what
    // "looks like an ingredients section" vs "looks like a nutrition table" means.
    internal val NUTRITION_TABLE_MARKERS = listOf(
        "nährwerte", "brennwert", "energie", "eiweiß", "kohlenhydrate", "gesättigte fettsäuren",
        "nutrition facts", "energy value", "calories", "protein", "carbohydrate", "saturated fat", "sodium",
        "valeur nutritionnelle", "valeur énergétique", "glucides", "protéines", "matières grasses", "lipides",
        "besin değerleri", "enerji", "karbonhidrat", "kkal", "doymuş yağ",
        "القيم الغذائية", "السعرات", "البروتين", "الدهون المشبعة"
    )
    internal val INGREDIENT_SECTION_MARKERS = listOf(
        "zutaten", "ingredients", "içindekiler", "ingrédients", "ingredientes", "المكونات"
    )

    fun looksLikeNutritionTableText(text: String): Boolean {
        val lower = normalizeForMatching(text)
        if (NORMALIZED_INGREDIENT_MARKERS.any { lower.contains(it) }) return false
        return NORMALIZED_NUTRITION_MARKERS.count { lower.contains(it) } >= 2
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
