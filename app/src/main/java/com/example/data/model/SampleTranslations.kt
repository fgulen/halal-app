package com.example.data.model

// Sample products (InitialData) carry hand-written certificate labels and reason texts in English
// (some in German). Those are shown as-is, so an Arabic screen used to show English sentences.
// This maps the known English originals to Arabic. Any text not listed here is returned unchanged.
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

/** Returns the Arabic version of a known sample text when the app language is Arabic; otherwise the input. */
fun localizeSampleText(text: String, language: AppLanguage): String =
    if (language == AppLanguage.AR) ARABIC_SAMPLE_TEXT[text] ?: text else text
