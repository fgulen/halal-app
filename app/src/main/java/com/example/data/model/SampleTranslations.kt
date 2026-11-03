package com.example.data.model

// Sample products (InitialData) carry hand-written certificate labels and reason texts. They are
// written in English, German or Turkish, so screens in other languages used to show them as-is.
// These maps translate the known sample texts. Any text not listed here is returned unchanged.
// Keys are the exact source strings (English, German or Turkish originals).

private val ARABIC_SAMPLE_TEXT: Map<String, String> = mapOf(
    // Certificate labels
    "Certified OU Kosher Dairy & Vegetarian" to "نباتي وألبان، مُعتمد OU كوشير",
    "Certified OU Kosher Dairy" to "ألبان، مُعتمد OU كوشير",
    "Gelatin-Free Formulation (Vegan-Friendly)" to "تركيبة خالية من الجيلاتين (مناسبة للنباتيين)",
    "Plant-Based & Dairy-Free Formulation" to "تركيبة نباتية وخالية من الألبان",
    "Vegan Formulation (No Animal Fats)" to "تركيبة نباتية (بدون دهون حيوانية)",
    "Vegetarian Verified & Certified Clean" to "نباتي معتمد ونظيف",
    "Vegetarian Verified" to "نباتي معتمد",
    "Vegetarisch / Keine tierischen Fette" to "نباتي / بدون دهون حيوانية",
    "TSE / Helal Uygunluk & Vejetaryen" to "TSE / متوافق مع الحلال ونباتي",
    // Reason texts
    "100% plant-based ingredients using distilled grain/spirit vinegar (non-intoxicating). This is an ingredient screening, not a third-party certification claim." to
        "مكونات نباتية 100% مع خل مقطر من الحبوب (غير مسكر). هذا فحص للمكونات وليس ادعاء شهادة من جهة خارجية.",
    "100% plant-based, verified vegan and Halal. Contains no animal fats, gelatin, or alcohol." to
        "نباتي 100%، ونباتي وحلال موثّق. لا يحتوي على دهون حيوانية أو جيلاتين أو كحول.",
    "Certified 100% Vegan by the European Vegetarian Union. Free from alcohol, animal fats, or non-halal emulsifiers." to
        "معتمد نباتي 100% من الاتحاد الأوروبي للنباتيين. خالٍ من الكحول والدهون الحيوانية والمستحلبات غير الحلال.",
    "Certified Kosher Dairy & Halal-compliant vegetable ingredients. No pork, gelatin or alcohol." to
        "ألبان كوشير معتمدة ومكونات نباتية متوافقة مع الحلال. لا يحتوي على لحم خنزير أو جيلاتين أو كحول.",
    "Certified OU Kosher / Halal-compliant vegetable ingredients. No animal enzymes, gelatin or alcohol." to
        "مُعتمد OU كوشير / مكونات نباتية متوافقة مع الحلال. لا يحتوي على إنزيمات حيوانية أو جيلاتين أو كحول.",
    "Contains no animal fats, gelatin or alcohol. Emulsifier is 100% plant-based soy/sunflower lecithin." to
        "لا يحتوي على دهون حيوانية أو جيلاتين أو كحول. المستحلِب هو ليسيثين نباتي 100% من فول الصويا أو عباد الشمس.",
    "Contains no animal fats, pork derivatives or alcohol. Emulsifiers are plant based." to
        "لا يحتوي على دهون حيوانية أو مشتقات لحم الخنزير أو كحول. المستحلبات نباتية.",
    "Free from alcohol, animal fats or non-halal additives." to
        "خالٍ من الكحول والدهون الحيوانية والإضافات غير الحلال.",
    "EU & German production uses Porcine Gelatin (Schweinegelatine / E441) derived from pig skin and bone collagen. In Islamic law, all pork derivatives are strictly prohibited (Haram)." to
        "يستخدم الإنتاج الأوروبي والألماني جيلاتين الخنزير (E441) المستخرج من جلد الخنزير وعظامه. وفي الفقه الإسلامي، كل مشتقات الخنزير محرّمة تحريماً قاطعاً.",
    "In the USA, Doritos uses cheese flavored with animal-derived enzymes from non-halal slaughtered calves/swine. Classified as Mushbooh / Doubtful unless certified." to
        "في الولايات المتحدة، تستخدم دوريتوس نكهة جبن بإنزيمات حيوانية من عجول أو خنازير غير مذبوحة وفق الحلال. مصنّفة كمشبوه ما لم تكن معتمدة.",
    "No prohibited or doubtful ingredients found in the standard formulation - no alcohol or animal-derived additives. This is an ingredient screening, not a third-party halal certification." to
        "لم يُعثر على مكونات محرّمة أو مشبوهة في التركيبة القياسية، فلا كحول ولا إضافات حيوانية المصدر. هذا فحص للمكونات وليس شهادة حلال من جهة خارجية.",
    "Oreo cookies in Europe and USA are produced with 100% plant-based ingredients and vegetable oils. No gelatin, animal enzymes, or alcohol." to
        "تُنتج بسكويت أوريو في أوروبا وأمريكا بمكونات نباتية 100% وزيوت نباتية. لا يحتوي على جيلاتين أو إنزيمات حيوانية أو كحول.",
    "Plant-based vegetable oils and soy lecithin only. Alcohol and pork-free." to
        "زيوت نباتية وليسيثين صويا فقط. خالٍ من الكحول ولحم الخنزير.",
    "Reformulated in Europe and USA without animal gelatin or carmine (E120). Plant-based starch and natural beetroot red (E162) are used." to
        "أُعيدت صياغته في أوروبا وأمريكا دون جيلاتين حيواني أو كارمين (E120). يُستخدم نشا نباتي وأحمر الشمندر الطبيعي (E162).",
    "Standard US Kraft Jet-Puffed Marshmallows are produced using pork-derived gelatin. Not permissible for Halal diet." to
        "يُنتج مارشميلو Kraft Jet-Puffed الأمريكي القياسي بجيلاتين مشتق من الخنزير. غير جائز في النظام الغذائي الحلال."
)

private val TURKISH_SAMPLE_TEXT: Map<String, String> = mapOf(
    // Certificate labels
    "Certified OU Kosher Dairy & Vegetarian" to "OU koşer süt ürünü ve vejetaryen (sertifikalı)",
    "Certified OU Kosher Dairy" to "OU koşer süt ürünü (sertifikalı)",
    "Gelatin-Free Formulation (Vegan-Friendly)" to "Jelatinsiz formül (veganlara uygun)",
    "Plant-Based & Dairy-Free Formulation" to "Bitkisel ve sütsüz formül",
    "Vegan Formulation (No Animal Fats)" to "Vegan formül (hayvansal yağ yok)",
    "Vegetarian Verified & Certified Clean" to "Vejetaryen onaylı ve temiz (sertifikalı)",
    "Vegetarian Verified" to "Vejetaryen onaylı",
    "Vegetarisch / Keine tierischen Fette" to "Vejetaryen / hayvansal yağ yok",
    // Reason texts
    "100% plant-based ingredients using distilled grain/spirit vinegar (non-intoxicating). This is an ingredient screening, not a third-party certification claim." to
        "%100 bitkisel içerik; damıtılmış tahıl/alkol sirkesi kullanılır (sarhoş edici değil). Bu bir içerik taramasıdır, üçüncü taraf sertifika iddiası değildir.",
    "100% plant-based, verified vegan and Halal. Contains no animal fats, gelatin, or alcohol." to
        "%100 bitkisel, doğrulanmış vegan ve helal. Hayvansal yağ, jelatin veya alkol içermez.",
    "Certified 100% Vegan by the European Vegetarian Union. Free from alcohol, animal fats, or non-halal emulsifiers." to
        "Avrupa Vejetaryen Birliği tarafından %100 vegan olarak onaylı. Alkol, hayvansal yağ veya helal olmayan emülgatör içermez.",
    "Certified Kosher Dairy & Halal-compliant vegetable ingredients. No pork, gelatin or alcohol." to
        "Koşer süt ürünü (sertifikalı) ve helal uyumlu bitkisel içerikler. Domuz, jelatin veya alkol yok.",
    "Certified OU Kosher / Halal-compliant vegetable ingredients. No animal enzymes, gelatin or alcohol." to
        "OU koşer sertifikalı / helal uyumlu bitkisel içerikler. Hayvansal enzim, jelatin veya alkol yok.",
    "Contains no animal fats, gelatin or alcohol. Emulsifier is 100% plant-based soy/sunflower lecithin." to
        "Hayvansal yağ, jelatin veya alkol içermez. Emülgatör %100 bitkisel soya/ayçiçeği lesitinidir.",
    "Contains no animal fats, pork derivatives or alcohol. Emulsifiers are plant based." to
        "Hayvansal yağ, domuz türevi veya alkol içermez. Emülgatörler bitkiseldir.",
    "Free from alcohol, animal fats or non-halal additives." to
        "Alkol, hayvansal yağ veya helal olmayan katkılardan arındırılmış.",
    "EU & German production uses Porcine Gelatin (Schweinegelatine / E441) derived from pig skin and bone collagen. In Islamic law, all pork derivatives are strictly prohibited (Haram)." to
        "AB ve Alman üretiminde domuz derisi ve kemiğinden elde edilen domuz jelatini (E441) kullanılır. İslami hukukta domuz türevleri kesinlikle yasaktır (Haram).",
    "In the USA, Doritos uses cheese flavored with animal-derived enzymes from non-halal slaughtered calves/swine. Classified as Mushbooh / Doubtful unless certified." to
        "ABD'de Doritos, helal olarak kesilmemiş buzağı/domuzlardan elde edilen hayvansal kökenli enzimlerle aromalandırılmış peynir kullanır. Sertifikalı olmadıkça Mushbooh / Şüpheli olarak sınıflandırılır.",
    "No prohibited or doubtful ingredients found in the standard formulation - no alcohol or animal-derived additives. This is an ingredient screening, not a third-party halal certification." to
        "Standart formülde yasaklı veya şüpheli içerik bulunmadı; alkol veya hayvansal kökenli katkı yok. Bu bir içerik taramasıdır, üçüncü taraf helal sertifikası değildir.",
    "Oreo cookies in Europe and USA are produced with 100% plant-based ingredients and vegetable oils. No gelatin, animal enzymes, or alcohol." to
        "Avrupa ve ABD'de üretilen Oreo bisküviler %100 bitkisel içerikler ve bitkisel yağlar ile yapılır. Jelatin, hayvansal enzim veya alkol içermez.",
    "Plant-based vegetable oils and soy lecithin only. Alcohol and pork-free." to
        "Yalnızca bitkisel yağlar ve soya lesitini. Alkol ve domuz içermez.",
    "Reformulated in Europe and USA without animal gelatin or carmine (E120). Plant-based starch and natural beetroot red (E162) are used." to
        "Avrupa ve ABD'de hayvansal jelatin veya karmin (E120) kullanılmadan yeniden formüle edildi. Bitkisel nişasta ve doğal pancar kırmızısı (E162) kullanılır.",
    "Standard US Kraft Jet-Puffed Marshmallows are produced using pork-derived gelatin. Not permissible for Halal diet." to
        "Standart ABD Kraft Jet-Puffed marshmallow'ları domuzdan elde edilen jelatin ile üretilir. Helal beslenmede caiz değildir.",
    // German originals
    "Zertifiziert vegetarisch und helal-konform. Enthält rein pflanzliches Sojalecithin (E322), keine tierischen Fette, keine Schweinegelatine, kein Alkohol." to
        "Vejetaryen sertifikalı ve helal uyumlu. Tamamen bitkisel soya lesitini (E322) içerir; hayvansal yağ, domuz jelatini veya alkol yok.",
    "Zertifiziert vegetarisch. 100% pflanzliches Sojalecithin." to
        "Vejetaryen sertifikalı. %100 bitkisel soya lesitini.",
    "Zertifiziert vegetarisch. Keine tierischen Fette, keine Schweinegelatine, kein Alkohol. 100% pflanzliches Sojalecithin." to
        "Vejetaryen sertifikalı. Hayvansal yağ, domuz jelatini veya alkol yok. %100 bitkisel soya lesitini."
)

private val GERMAN_SAMPLE_TEXT: Map<String, String> = mapOf(
    // Certificate labels
    "Certified OU Kosher Dairy & Vegetarian" to "OU-koscher Milchprodukt & vegetarisch (zertifiziert)",
    "Certified OU Kosher Dairy" to "OU-koscher Milchprodukt (zertifiziert)",
    "Gelatin-Free Formulation (Vegan-Friendly)" to "Gelatinefreie Rezeptur (vegan-geeignet)",
    "Plant-Based & Dairy-Free Formulation" to "Pflanzlich & milchfrei",
    "Vegan Formulation (No Animal Fats)" to "Vegane Rezeptur (keine tierischen Fette)",
    "Vegetarian Verified & Certified Clean" to "Vegetarisch geprüft & zertifiziert sauber",
    "Vegetarian Verified" to "Vegetarisch geprüft",
    "TSE / Helal Uygunluk & Vejetaryen" to "TSE / halal-konform & vegetarisch",
    // Reason texts
    "100% plant-based ingredients using distilled grain/spirit vinegar (non-intoxicating). This is an ingredient screening, not a third-party certification claim." to
        "100 % pflanzliche Zutaten mit destilliertem Getreide-/Spiritusessig (nicht berauschend). Dies ist eine Zutatenprüfung, keine Zertifizierung durch Dritte.",
    "100% plant-based, verified vegan and Halal. Contains no animal fats, gelatin, or alcohol." to
        "100 % pflanzlich, verifiziert vegan und halal. Enthält keine tierischen Fette, keine Gelatine und keinen Alkohol.",
    "Certified 100% Vegan by the European Vegetarian Union. Free from alcohol, animal fats, or non-halal emulsifiers." to
        "Von der Europäischen Vegetarier-Union als 100 % vegan zertifiziert. Frei von Alkohol, tierischen Fetten und nicht-halalen Emulgatoren.",
    "Certified Kosher Dairy & Halal-compliant vegetable ingredients. No pork, gelatin or alcohol." to
        "Koscheres Milchprodukt (zertifiziert) und halal-konforme pflanzliche Zutaten. Kein Schweinefleisch, keine Gelatine, kein Alkohol.",
    "Certified OU Kosher / Halal-compliant vegetable ingredients. No animal enzymes, gelatin or alcohol." to
        "OU-koscher zertifiziert / halal-konforme pflanzliche Zutaten. Keine tierischen Enzyme, keine Gelatine, kein Alkohol.",
    "Contains no animal fats, gelatin or alcohol. Emulsifier is 100% plant-based soy/sunflower lecithin." to
        "Enthält keine tierischen Fette, keine Gelatine und keinen Alkohol. Emulgator ist 100 % pflanzliches Soja-/Sonnenblumenlecithin.",
    "Contains no animal fats, pork derivatives or alcohol. Emulsifiers are plant based." to
        "Enthält keine tierischen Fette, keine Schweinebestandteile und keinen Alkohol. Emulgatoren sind pflanzlich.",
    "Free from alcohol, animal fats or non-halal additives." to
        "Frei von Alkohol, tierischen Fetten und nicht-halalen Zusatzstoffen.",
    "EU & German production uses Porcine Gelatin (Schweinegelatine / E441) derived from pig skin and bone collagen. In Islamic law, all pork derivatives are strictly prohibited (Haram)." to
        "In der EU- und deutschen Produktion wird Schweinegelatine (E441) aus Schweinehaut und -knochen verwendet. Nach islamischem Recht sind alle Schweinebestandteile strikt verboten (Haram).",
    "In the USA, Doritos uses cheese flavored with animal-derived enzymes from non-halal slaughtered calves/swine. Classified as Mushbooh / Doubtful unless certified." to
        "In den USA verwendet Doritos Käse mit tierischen Enzymen von nicht halal geschlachteten Kälbern/Schweinen. Ohne Zertifizierung als Mushbooh / zweifelhaft eingestuft.",
    "No prohibited or doubtful ingredients found in the standard formulation - no alcohol or animal-derived additives. This is an ingredient screening, not a third-party halal certification." to
        "In der Standardrezeptur wurden keine verbotenen oder zweifelhaften Zutaten gefunden: kein Alkohol und keine tierischen Zusatzstoffe. Dies ist eine Zutatenprüfung, keine Halal-Zertifizierung durch Dritte.",
    "Oreo cookies in Europe and USA are produced with 100% plant-based ingredients and vegetable oils. No gelatin, animal enzymes, or alcohol." to
        "Oreo-Kekse in Europa und den USA werden mit 100 % pflanzlichen Zutaten und Pflanzenölen hergestellt. Keine Gelatine, keine tierischen Enzyme, kein Alkohol.",
    "Plant-based vegetable oils and soy lecithin only. Alcohol and pork-free." to
        "Nur pflanzliche Öle und Sojalecithin. Alkohol- und schweinefrei.",
    "Reformulated in Europe and USA without animal gelatin or carmine (E120). Plant-based starch and natural beetroot red (E162) are used." to
        "In Europa und den USA ohne tierische Gelatine oder Karmin (E120) neu formuliert. Verwendet werden pflanzliche Stärke und natürliches Rote-Bete-Rot (E162).",
    "Standard US Kraft Jet-Puffed Marshmallows are produced using pork-derived gelatin. Not permissible for Halal diet." to
        "Die US-Standard-Marshmallows Kraft Jet-Puffed werden mit Gelatine aus Schweinen hergestellt. Nicht für die halal Ernährung geeignet.",
    // Turkish originals
    "Bitkisel içerikli ve süt ürünüdür. Domuz yağı, domuz jelatini ve alkol içermez. Emülgatör olarak bitkisel soya lesitini kullanılmıştır." to
        "Pflanzliche Zutaten und Milchprodukt. Enthält kein Schweineschmalz, keine Schweinegelatine und keinen Alkohol. Als Emulgator wird pflanzliches Sojalecithin verwendet.",
    "Domuz türevi ve alkol içermez. %100 bitkisel soya lesitini." to
        "Keine Schweinebestandteile und kein Alkohol. 100 % pflanzliches Sojalecithin."
)

/** Returns the translation of a known sample text for the app language; otherwise the input unchanged. */
fun localizeSampleText(text: String, language: AppLanguage): String {
    val table = when (language) {
        AppLanguage.AR -> ARABIC_SAMPLE_TEXT
        AppLanguage.TR -> TURKISH_SAMPLE_TEXT
        AppLanguage.DE -> GERMAN_SAMPLE_TEXT
        else -> return text
    }
    return table[text] ?: text
}
