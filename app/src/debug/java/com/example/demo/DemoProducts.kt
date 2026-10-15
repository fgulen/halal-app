package com.example.demo

import com.example.data.analyzer.HalalAnalyzer
import com.example.data.model.AppLanguage
import com.example.data.model.FoodProduct
import com.example.data.model.Madhhab

// Debug-only demo catalogue for store screenshots and the promo video. Every product and the
// "Vellmark" brand are fictional on purpose: a real brand shown as Haram/Doubtful in marketing
// material invites trademark trouble and confuses viewers. Verdicts are NOT hardcoded - each item
// goes through the real HalalAnalyzer, so what the video shows is exactly what the app decides.
// Barcodes use the GS1 "20" prefix (restricted, in-store use), so they can never collide with a
// real product's GTIN in Open Food Facts. Turkish falls back to English: the demo is international.
data class DemoItem(
    val barcode: String,
    val image: String,
    val names: Map<AppLanguage, String>,
    val categories: Map<AppLanguage, String>,
    val ingredients: Map<AppLanguage, String>
) {
    fun analyze(language: AppLanguage, madhhab: Madhhab): FoodProduct {
        val lang = if (language in names) language else AppLanguage.EN
        val product = HalalAnalyzer.analyzeIngredientsText(
            productName = names.getValue(lang),
            ingredientsText = ingredients.getValue(lang),
            barcode = barcode,
            language = language,
            madhhab = madhhab
        )
        return product.copy(
            brand = DemoProducts.BRAND,
            category = categories.getValue(lang),
            imageUrl = "file:///android_asset/demo/$image"
        )
    }
}

object DemoProducts {
    const val BRAND = "Vellmark"

    private val EN = AppLanguage.EN
    private val DE = AppLanguage.DE
    private val FR = AppLanguage.FR
    private val AR = AppLanguage.AR

    val items = listOf(
        DemoItem(
            barcode = "2001000000017",
            image = "crackers.png",
            names = mapOf(EN to "Sunflower Crackers", DE to "Sonnenblumen-Cracker", FR to "Crackers au tournesol", AR to "مقرمشات دوار الشمس"),
            categories = mapOf(EN to "Crackers", DE to "Cracker", FR to "Crackers", AR to "مقرمشات"),
            ingredients = mapOf(
                EN to "Wheat flour, sunflower oil, sunflower seeds (8%), salt, yeast, barley malt extract.",
                DE to "Weizenmehl, Sonnenblumenöl, Sonnenblumenkerne (8%), Salz, Hefe, Gerstenmalzextrakt.",
                FR to "Farine de blé, huile de tournesol, graines de tournesol (8%), sel, levure, extrait de malt d'orge.",
                AR to "دقيق القمح، زيت دوار الشمس، بذور دوار الشمس (8%)، ملح، خميرة، مستخلص مالت الشعير."
            )
        ),
        DemoItem(
            barcode = "2001000000024",
            image = "gummies.png",
            names = mapOf(EN to "Fruit Gummies", DE to "Fruchtgummi", FR to "Bonbons gélifiés aux fruits", AR to "حلوى جيلاتينية بالفواكه"),
            categories = mapOf(EN to "Sweets", DE to "Süßwaren", FR to "Confiserie", AR to "حلويات"),
            ingredients = mapOf(
                EN to "Glucose syrup, sugar, pork gelatine, dextrose, fruit juice from concentrate (5%), citric acid, beeswax.",
                DE to "Glukosesirup, Zucker, Schweinegelatine, Dextrose, Fruchtsaft aus Fruchtsaftkonzentrat (5%), Citronensäure, Bienenwachs.",
                FR to "Sirop de glucose, sucre, gélatine de porc, dextrose, jus de fruits à base de concentré (5%), acide citrique, cire d'abeille.",
                AR to "شراب الجلوكوز، سكر، جيلاتين خنزير، دكستروز، عصير فواكه مركز (5%)، حمض الستريك، شمع العسل."
            )
        ),
        DemoItem(
            barcode = "2001000000031",
            image = "chocolate.png",
            names = mapOf(EN to "Milk Chocolate", DE to "Vollmilchschokolade", FR to "Chocolat au lait", AR to "شوكولاتة بالحليب"),
            categories = mapOf(EN to "Chocolate", DE to "Schokolade", FR to "Chocolat", AR to "شوكولاتة"),
            ingredients = mapOf(
                EN to "Sugar, cocoa butter, whole milk powder, cocoa mass, whey powder, emulsifier (soy lecithin), vanilla extract.",
                DE to "Zucker, Kakaobutter, Vollmilchpulver, Kakaomasse, Süßmolkenpulver, Emulgator (Sojalecithin), Vanilleextrakt.",
                FR to "Sucre, beurre de cacao, lait entier en poudre, pâte de cacao, lactosérum en poudre, émulsifiant (lécithine de soja), extrait de vanille.",
                AR to "سكر، زبدة الكاكاو، حليب كامل الدسم مجفف، عجينة الكاكاو، مسحوق مصل اللبن، مستحلب (ليسيثين الصويا)، مستخلص الفانيليا."
            )
        ),
        DemoItem(
            barcode = "2001000000048",
            image = "soda.png",
            names = mapOf(EN to "Cherry Soda", DE to "Kirsch-Limonade", FR to "Soda à la cerise", AR to "مشروب غازي بالكرز"),
            categories = mapOf(EN to "Soft drinks", DE to "Erfrischungsgetränke", FR to "Boissons gazeuses", AR to "مشروبات غازية"),
            ingredients = mapOf(
                EN to "Carbonated water, sugar, cherry juice from concentrate (6%), citric acid, black carrot concentrate.",
                DE to "Wasser, Zucker, Kohlensäure, Kirschsaft aus Kirschsaftkonzentrat (6%), Citronensäure, Schwarze-Karotten-Konzentrat.",
                FR to "Eau gazéifiée, sucre, jus de cerise à base de concentré (6%), acide citrique, concentré de carotte noire.",
                AR to "ماء مكربن، سكر، عصير كرز مركز (6%)، حمض الستريك، مركز الجزر الأسود."
            )
        ),
        DemoItem(
            barcode = "2001000000055",
            image = "biscuits.png",
            names = mapOf(EN to "Whole Wheat Biscuits", DE to "Vollkornkekse", FR to "Biscuits complets", AR to "بسكويت القمح الكامل"),
            categories = mapOf(EN to "Biscuits", DE to "Kekse", FR to "Biscuits", AR to "بسكويت"),
            ingredients = mapOf(
                EN to "Whole wheat flour (55%), sugar, palm oil, oat flakes, raising agent (sodium bicarbonate), salt.",
                DE to "Vollkornweizenmehl (55%), Zucker, Palmöl, Haferflocken, Backtriebmittel (Natriumhydrogencarbonat), Salz.",
                FR to "Farine de blé complète (55%), sucre, huile de palme, flocons d'avoine, poudre à lever (bicarbonate de sodium), sel.",
                AR to "دقيق القمح الكامل (55%)، سكر، زيت النخيل، رقائق الشوفان، عامل رفع (بيكربونات الصوديوم)، ملح."
            )
        ),
        DemoItem(
            barcode = "2001000000062",
            image = "cake.png",
            names = mapOf(EN to "Rum Truffle Cake", DE to "Rum-Trüffel-Kuchen", FR to "Gâteau truffé au rhum", AR to "كعكة الترافل بالروم"),
            categories = mapOf(EN to "Cakes", DE to "Kuchen", FR to "Gâteaux", AR to "كعك"),
            ingredients = mapOf(
                EN to "Sugar, wheat flour, eggs, cocoa powder, rum (4%), sunflower oil, raising agent.",
                DE to "Zucker, Weizenmehl, Eier, Kakaopulver, Rum (4%), Sonnenblumenöl, Backtriebmittel.",
                FR to "Sucre, farine de blé, œufs, cacao en poudre, rhum (4%), huile de tournesol, poudre à lever.",
                AR to "سكر، دقيق القمح، بيض، مسحوق الكاكاو، روم (4%)، زيت دوار الشمس، عامل رفع."
            )
        )
    )
}
