package com.example.data.local

import com.example.data.model.EAdditive
import com.example.data.model.HalalStatus
import com.example.data.model.LocalizedText

/**
 * Shared texts for the generated E-code entries ([EAdditivesGenerated]). Only the hand-written
 * entries in [InitialData.eAdditivesDirectory] carry per-additive explanations; the long tail
 * gets an origin/verdict text by source kind and a usage text by E-number group.
 */
internal object EAdditiveTemplates {

    enum class Kind(val origin: LocalizedText, val description: LocalizedText) {
        // No known animal-slaughter or alcohol source
        SAFE(
            origin = LocalizedText(
                en = "Plant, mineral, microbial or synthetic",
                de = "Pflanzlich, mineralisch, mikrobiell oder synthetisch",
                fr = "Végétale, minérale, microbienne ou synthétique",
                tr = "Bitkisel, mineral, mikrobiyal veya sentetik",
                ar = "نباتي أو معدني أو ميكروبي أو صناعي"
            ),
            description = LocalizedText(
                en = "No animal or alcohol source is known for this additive, so it is generally considered halal.",
                de = "Für diesen Zusatzstoff ist keine tierische oder alkoholische Quelle bekannt; er gilt daher allgemein als halal.",
                fr = "Aucune source animale ou alcoolique n'est connue pour cet additif ; il est donc généralement considéré comme halal.",
                tr = "Bu katkı maddesinin bilinen bir hayvansal ya da alkol kaynağı yok; genellikle helal kabul edilir.",
                ar = "لا يُعرف لهذا المضاف مصدر حيواني أو كحولي، لذا يُعتبر حلالاً بشكل عام."
            )
        ),
        // Milk, egg, honey bee or wool: taken from a living animal, no slaughter involved
        LIVING_ANIMAL(
            origin = LocalizedText(
                en = "From a living animal (milk, egg, bees or wool)",
                de = "Von einem lebenden Tier (Milch, Ei, Bienen oder Wolle)",
                fr = "D'un animal vivant (lait, œuf, abeilles ou laine)",
                tr = "Canlı hayvandan (süt, yumurta, arı veya yün)",
                ar = "من حيوان حي (حليب أو بيض أو نحل أو صوف)"
            ),
            description = LocalizedText(
                en = "Obtained from a living animal without slaughter, so it is generally considered halal.",
                de = "Wird ohne Schlachtung von einem lebenden Tier gewonnen und gilt daher allgemein als halal.",
                fr = "Obtenu à partir d'un animal vivant sans abattage ; il est donc généralement considéré comme halal.",
                tr = "Kesim olmadan canlı hayvandan elde edilir; genellikle helal kabul edilir.",
                ar = "يُستخرج من حيوان حي دون ذبح، لذا يُعتبر حلالاً بشكل عام."
            )
        ),
        // Tartaric acid and its salts: same istihala reasoning as vinegar
        TARTARIC(
            origin = LocalizedText(
                en = "Grapes or wine-making residue (tartar)",
                de = "Trauben oder Weinstein aus der Weinherstellung",
                fr = "Raisin ou tartre issu de la vinification",
                tr = "Üzüm veya şarap yapımı tortusu (tartar)",
                ar = "العنب أو رواسب صناعة النبيذ (الطرطير)"
            ),
            description = LocalizedText(
                en = "Can be made from wine-making residue, but it is chemically transformed and contains no alcohol (istihala), so most scholars consider it halal.",
                de = "Kann aus Rückständen der Weinherstellung stammen, ist aber chemisch umgewandelt und enthält keinen Alkohol (Istihala); die meisten Gelehrten halten es für halal.",
                fr = "Peut provenir de résidus de vinification, mais il est chimiquement transformé et ne contient pas d'alcool (istihala) ; la plupart des savants le considèrent halal.",
                tr = "Şarap tortusundan üretilebilir, ancak kimyasal olarak dönüşmüştür ve alkol içermez (istihale); âlimlerin çoğu helal kabul eder.",
                ar = "قد يُصنع من رواسب النبيذ، لكنه يتحول كيميائياً ولا يحتوي على كحول (استحالة)، لذا يعتبره أغلب العلماء حلالاً."
            )
        ),
        // Fatty-acid based: vegetable oil or animal fat (possibly pork)
        MIXED(
            origin = LocalizedText(
                en = "Plant or animal (possibly pork) fats",
                de = "Pflanzliche oder tierische (evtl. Schweine-) Fette",
                fr = "Graisses végétales ou animales (éventuellement de porc)",
                tr = "Bitkisel veya hayvansal (domuz olabilir) yağlar",
                ar = "دهون نباتية أو حيوانية (قد تكون من الخنزير)"
            ),
            description = LocalizedText(
                en = "Can be made from plant or animal sources, including pork. Unless the label says plant-based or the product is halal certified, it is doubtful.",
                de = "Kann aus pflanzlichen oder tierischen Quellen, auch vom Schwein, hergestellt werden. Ohne Hinweis „pflanzlich“ oder Halal-Zertifikat ist es zweifelhaft.",
                fr = "Peut être fabriqué à partir de sources végétales ou animales, y compris le porc. Sans mention « végétal » ou certification halal, il est douteux.",
                tr = "Bitkisel ya da domuz dahil hayvansal kaynaktan üretilebilir. Etikette bitkisel yazmıyorsa veya ürün helal sertifikalı değilse şüphelidir.",
                ar = "قد يُصنع من مصادر نباتية أو حيوانية بما فيها الخنزير. ما لم يُذكر أنه نباتي أو يحمل شهادة حلال فهو مشبوه."
            )
        ),
        // Glycerol-based: same verdict as E422
        GLYCEROL(
            origin = LocalizedText(
                en = "Contains glycerol (plant or animal fat)",
                de = "Enthält Glycerin (pflanzliches oder tierisches Fett)",
                fr = "Contient du glycérol (graisse végétale ou animale)",
                tr = "Gliserol içerir (bitkisel veya hayvansal yağ)",
                ar = "يحتوي على الغليسرول (دهن نباتي أو حيواني)"
            ),
            description = LocalizedText(
                en = "Made with glycerol, which can come from vegetable oils or animal fats (possibly pork). Doubtful unless the source is stated.",
                de = "Wird mit Glycerin hergestellt, das aus Pflanzenölen oder tierischen Fetten (evtl. Schwein) stammen kann. Zweifelhaft, wenn die Quelle nicht angegeben ist.",
                fr = "Fabriqué avec du glycérol, qui peut provenir d'huiles végétales ou de graisses animales (éventuellement de porc). Douteux si la source n'est pas indiquée.",
                tr = "Bitkisel yağlardan ya da hayvansal (domuz olabilir) yağlardan elde edilebilen gliserolle üretilir. Kaynak belirtilmemişse şüphelidir.",
                ar = "يُصنع باستخدام الغليسرول الذي قد يأتي من زيوت نباتية أو دهون حيوانية (قد تكون من الخنزير). مشبوه ما لم يُذكر المصدر."
            )
        ),
        // Nucleotides and enzymes: yeast/microbial, or meat, fish or pancreas
        UNSPECIFIED(
            origin = LocalizedText(
                en = "Microbial, plant or animal",
                de = "Mikrobiell, pflanzlich oder tierisch",
                fr = "Microbienne, végétale ou animale",
                tr = "Mikrobiyal, bitkisel veya hayvansal",
                ar = "ميكروبي أو نباتي أو حيواني"
            ),
            description = LocalizedText(
                en = "Can be made by fermentation or from animal sources such as meat, fish or pork. Doubtful unless the source is stated.",
                de = "Kann durch Fermentation oder aus tierischen Quellen wie Fleisch, Fisch oder Schwein hergestellt werden. Zweifelhaft, wenn die Quelle nicht angegeben ist.",
                fr = "Peut être obtenu par fermentation ou à partir de sources animales comme la viande, le poisson ou le porc. Douteux si la source n'est pas indiquée.",
                tr = "Fermantasyonla ya da et, balık veya domuz gibi hayvansal kaynaklardan üretilebilir. Kaynak belirtilmemişse şüphelidir.",
                ar = "قد يُصنع بالتخمير أو من مصادر حيوانية كاللحم أو السمك أو الخنزير. مشبوه ما لم يُذكر المصدر."
            )
        ),
        // Colour that is itself plant/synthetic but often sold on a gelatin carrier
        CARRIER(
            origin = LocalizedText(
                en = "Plant or synthetic; carrier may be gelatin",
                de = "Pflanzlich oder synthetisch; Träger kann Gelatine sein",
                fr = "Végétale ou synthétique ; le support peut être de la gélatine",
                tr = "Bitkisel veya sentetik; taşıyıcısı jelatin olabilir",
                ar = "نباتي أو صناعي؛ قد تكون المادة الحاملة جيلاتين"
            ),
            description = LocalizedText(
                en = "The colour itself is not animal-derived, but it is often stabilised with gelatin. Doubtful unless the product is vegan or halal certified.",
                de = "Der Farbstoff selbst ist nicht tierisch, wird aber oft mit Gelatine stabilisiert. Zweifelhaft, wenn das Produkt nicht vegan oder halal-zertifiziert ist.",
                fr = "Le colorant lui-même n'est pas d'origine animale, mais il est souvent stabilisé avec de la gélatine. Douteux si le produit n'est pas vegan ou certifié halal.",
                tr = "Renk maddesinin kendisi hayvansal değildir ama çoğu zaman jelatinle stabilize edilir. Ürün vegan veya helal sertifikalı değilse şüphelidir.",
                ar = "الملون نفسه ليس حيواني المصدر، لكنه يُثبَّت غالباً بالجيلاتين. مشبوه ما لم يكن المنتج نباتياً أو يحمل شهادة حلال."
            )
        ),
        // Usually from meat, fish, bone, hair/feathers or bile
        ANIMAL(
            origin = LocalizedText(
                en = "Mostly animal-derived",
                de = "Überwiegend tierischen Ursprungs",
                fr = "Principalement d'origine animale",
                tr = "Çoğunlukla hayvansal",
                ar = "حيواني المصدر في الغالب"
            ),
            description = LocalizedText(
                en = "Usually obtained from animal sources (meat, fish, bone, hair or bile). The animal and its slaughter can't be verified, so it is doubtful.",
                de = "Wird meist aus tierischen Quellen (Fleisch, Fisch, Knochen, Haare oder Galle) gewonnen. Tierart und Schlachtung sind nicht überprüfbar, daher zweifelhaft.",
                fr = "Généralement obtenu à partir de sources animales (viande, poisson, os, poils ou bile). L'animal et son abattage ne sont pas vérifiables ; il est donc douteux.",
                tr = "Genellikle hayvansal kaynaklardan (et, balık, kemik, kıl veya safra) elde edilir. Hayvan ve kesimi doğrulanamadığı için şüphelidir.",
                ar = "يُستخرج عادةً من مصادر حيوانية (لحم أو سمك أو عظم أو شعر أو صفراء). لا يمكن التحقق من الحيوان وذبحه، لذا فهو مشبوه."
            )
        ),
        // Added ethyl alcohol: same verdict as the analyzer's alcohol rule
        ALCOHOL(
            origin = LocalizedText(
                en = "Fermentation or chemical synthesis (alcohol)",
                de = "Gärung oder chemische Synthese (Alkohol)",
                fr = "Fermentation ou synthèse chimique (alcool)",
                tr = "Fermantasyon veya kimyasal sentez (alkol)",
                ar = "التخمير أو التصنيع الكيميائي (كحول)"
            ),
            description = LocalizedText(
                en = "Ethyl alcohol added as an ingredient or solvent. Added alcohol is not permissible in food.",
                de = "Ethylalkohol, als Zutat oder Lösungsmittel zugesetzt. Zugesetzter Alkohol ist in Lebensmitteln nicht erlaubt.",
                fr = "Alcool éthylique ajouté comme ingrédient ou solvant. L'alcool ajouté n'est pas permis dans les aliments.",
                tr = "İçerik veya çözücü olarak eklenen etil alkol. Gıdaya eklenen alkol caiz değildir.",
                ar = "كحول إيثيلي مضاف كمكوّن أو مذيب. الكحول المضاف غير جائز في الطعام."
            )
        )
    }

    enum class Group(val usage: LocalizedText) {
        COLOUR(LocalizedText(en = "Food colouring", de = "Lebensmittelfarbstoff", fr = "Colorant alimentaire", tr = "Gıda renklendirici", ar = "ملوّن غذائي")),
        PRESERVATIVE(LocalizedText(en = "Preservative", de = "Konservierungsstoff", fr = "Conservateur", tr = "Koruyucu", ar = "مادة حافظة")),
        ANTIOXIDANT(LocalizedText(en = "Antioxidant, acid or acidity regulator", de = "Antioxidationsmittel, Säure oder Säureregulator", fr = "Antioxydant, acide ou correcteur d'acidité", tr = "Antioksidan, asit veya asitlik düzenleyici", ar = "مضاد أكسدة أو حمض أو منظم حموضة")),
        THICKENER(LocalizedText(en = "Thickener, stabiliser or emulsifier", de = "Verdickungsmittel, Stabilisator oder Emulgator", fr = "Épaississant, stabilisant ou émulsifiant", tr = "Kıvam artırıcı, stabilizör veya emülgatör", ar = "مكثف أو مثبت أو مستحلب")),
        MINERAL(LocalizedText(en = "Acidity regulator, anti-caking agent or mineral salt", de = "Säureregulator, Trennmittel oder Mineralsalz", fr = "Correcteur d'acidité, antiagglomérant ou sel minéral", tr = "Asitlik düzenleyici, topaklanmayı önleyici veya mineral tuz", ar = "منظم حموضة أو مانع تكتل أو ملح معدني")),
        FLAVOUR(LocalizedText(en = "Flavour enhancer", de = "Geschmacksverstärker", fr = "Exhausteur de goût", tr = "Lezzet artırıcı", ar = "معزز نكهة")),
        ANTIBIOTIC(LocalizedText(en = "Antibiotic (not permitted in EU food)", de = "Antibiotikum (in EU-Lebensmitteln nicht zugelassen)", fr = "Antibiotique (non autorisé dans les aliments de l'UE)", tr = "Antibiyotik (AB'de gıdada izinli değil)", ar = "مضاد حيوي (غير مسموح به في أغذية الاتحاد الأوروبي)")),
        GLAZING(LocalizedText(en = "Glazing agent, wax or anti-foaming agent", de = "Überzugsmittel, Wachs oder Schaumverhüter", fr = "Agent d'enrobage, cire ou antimoussant", tr = "Parlatıcı, mum veya köpük önleyici", ar = "عامل تلميع أو شمع أو مانع رغوة")),
        FLOUR(LocalizedText(en = "Flour treatment agent", de = "Mehlbehandlungsmittel", fr = "Agent de traitement de la farine", tr = "Un işleme maddesi", ar = "عامل معالجة الدقيق")),
        GAS(LocalizedText(en = "Packaging or propellant gas", de = "Packgas oder Treibgas", fr = "Gaz d'emballage ou propulseur", tr = "Ambalaj veya itici gaz", ar = "غاز تعبئة أو دافع")),
        SWEETENER(LocalizedText(en = "Sweetener", de = "Süßungsmittel", fr = "Édulcorant", tr = "Tatlandırıcı", ar = "مُحلٍّ")),
        ENZYME(LocalizedText(en = "Enzyme", de = "Enzym", fr = "Enzyme", tr = "Enzim", ar = "إنزيم")),
        BULKING(LocalizedText(en = "Bulking agent, coating or carrier", de = "Füllstoff, Überzug oder Trägerstoff", fr = "Agent de charge, enrobage ou support", tr = "Dolgu maddesi, kaplama veya taşıyıcı", ar = "مادة مالئة أو تغليف أو مادة حاملة")),
        STARCH(LocalizedText(en = "Modified starch (thickener)", de = "Modifizierte Stärke (Verdickungsmittel)", fr = "Amidon modifié (épaississant)", tr = "Modifiye nişasta (kıvam artırıcı)", ar = "نشا معدّل (مكثف)")),
        SOLVENT(LocalizedText(en = "Carrier solvent or humectant", de = "Trägerlösungsmittel oder Feuchthaltemittel", fr = "Solvant de support ou humectant", tr = "Taşıyıcı çözücü veya nem tutucu", ar = "مذيب حامل أو مرطب")),
        OTHER(LocalizedText(en = "Various food uses", de = "Verschiedene Verwendungen", fr = "Usages divers", tr = "Çeşitli gıda kullanımları", ar = "استخدامات غذائية متنوعة"))
    }

    fun entry(
        code: String,
        name: LocalizedText,
        status: HalalStatus,
        kind: Kind,
        group: Group,
        alternateNames: List<String>
    ) = EAdditive(
        code = code,
        name = name,
        status = status,
        origin = kind.origin,
        description = kind.description,
        alternateNames = alternateNames,
        commonUsage = group.usage
    )
}
