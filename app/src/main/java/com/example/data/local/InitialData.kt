package com.example.data.local

import com.example.data.model.EAdditive
import com.example.data.model.FlaggedIngredient
import com.example.data.model.FoodProduct
import com.example.data.model.HalalStatus

object InitialData {
    val sampleProducts = listOf(
        // 1. HARAM - EU: Haribo Goldbären (Pork Gelatin)
        ProductEntity(
            barcode = "4001686301265",
            name = "Haribo Goldbären (Germany / EU)",
            brand = "Haribo",
            category = "Confectionery & Gummy Candy",
            status = HalalStatus.HARAM,
            halalCertificate = null,
            harmfulOrSuspiciousIngredients = listOf("Pork Gelatin (Schweinegelatine / E441)"),
            allIngredients = listOf(
                "Glucose syrup",
                "Sugar",
                "Pork Gelatine (E441)",
                "Dextrose",
                "Fruit juice from concentrate",
                "Citric acid (E330)",
                "Beeswax (E901)",
                "Carnauba wax (E903)"
            ),
            reasonOrDetails = "EU & German production uses Porcine Gelatin (Schweinegelatine / E441) derived from pig skin and bone collagen. In Islamic law, all pork derivatives are strictly prohibited (Haram).",
            alternatives = listOf(
                "Haribo Turkey / Halal certified edition (Bovine Gelatin)",
                "Bebeto 100% Halal Certified Gummy Candy",
                "Katjes Plant-Based / Gelatin-Free Fruit Candies (V-Label)"
            ),
            imageUrl = "https://images.openfoodfacts.org/images/products/400/168/630/1265/1.400.jpg"
        ),

        // 2. HARAM - USA: Kraft Jet-Puffed Marshmallows
        ProductEntity(
            barcode = "0021000612803",
            name = "Kraft Jet-Puffed Marshmallows (USA)",
            brand = "Kraft Heinz",
            category = "Marshmallows & Sweets",
            status = HalalStatus.HARAM,
            halalCertificate = null,
            harmfulOrSuspiciousIngredients = listOf("Pork Gelatin (Porcine collagen)"),
            allIngredients = listOf(
                "Corn syrup",
                "Sugar",
                "Dextrose",
                "Modified cornstarch",
                "Water",
                "Gelatin (Pork origin)",
                "Tetrasodium pyrophosphate",
                "Artificial flavor"
            ),
            reasonOrDetails = "Standard US Kraft Jet-Puffed Marshmallows are produced using pork-derived gelatin. Not permissible for Halal diet.",
            alternatives = listOf(
                "Campfire Halal Marshmallows (Fish / Bovine Gelatin)",
                "Dandies Vegan Marshmallows (Plant-based / Tapioca)",
                "Ziyad Halal Certified Marshmallows"
            ),
            imageUrl = "https://images.openfoodfacts.org/images/products/400/168/630/1265/1.400.jpg"
        ),

        // 3. HARAM - EU: Milka Daim (Alcohol Flavoring)
        ProductEntity(
            barcode = "7622210449283",
            name = "Milka Daim Chocolate (EU Edition)",
            brand = "Mondelez / Milka",
            category = "Chocolate & Confectionery",
            status = HalalStatus.HARAM,
            halalCertificate = null,
            harmfulOrSuspiciousIngredients = listOf("Ethyl Alcohol / Liqueur Extract", "E476 (Polyglycerol Polyricinoleate)"),
            allIngredients = listOf(
                "Sugar",
                "Cocoa butter",
                "Skimmed milk powder",
                "Cocoa mass",
                "Butterfat",
                "Ethyl alcohol / Liqueur flavoring",
                "Soya lecithin (E322)",
                "E476"
            ),
            reasonOrDetails = "Contains alcohol / liqueur extract in the flavor blend. All intoxicants and alcohol additives are non-compliant with Islamic dietary standards.",
            alternatives = listOf(
                "Ritter Sport Vegan & Halal editions",
                "Lindt Excellence Dark Chocolate (Alcohol-free)",
                "Godiva Pure Milk Chocolate"
            ),
            imageUrl = "https://images.openfoodfacts.org/images/products/762/221/044/9283/1.400.jpg"
        ),

        // 4. SUSPICIOUS - USA: Doritos Nacho Cheese (Animal Enzymes)
        ProductEntity(
            barcode = "0028400090896",
            name = "Doritos Nacho Cheese (USA)",
            brand = "Frito-Lay / PepsiCo",
            category = "Snacks & Chips",
            status = HalalStatus.SUPHELI,
            halalCertificate = null,
            harmfulOrSuspiciousIngredients = listOf(
                "Animal Enzymes (Cheese Rennet - Non-Halal Slaughter)",
                "Whey Powder (Enzymatic cheese byproduct)",
                "E631 (Disodium Inosinate)"
            ),
            allIngredients = listOf(
                "Corn",
                "Vegetable oil (Corn, Canola, Sunflower)",
                "Maltodextrin",
                "Cheddar cheese (Milk, Cheese cultures, Salt, Animal enzymes)",
                "Whey",
                "Monosodium glutamate (E621)",
                "Disodium inosinate (E631)",
                "Artificial color (Yellow 6, Yellow 5)"
            ),
            reasonOrDetails = "In the USA, Doritos uses cheese flavored with animal-derived enzymes from non-halal slaughtered calves/swine. Classified as Mushbooh / Doubtful unless certified.",
            alternatives = listOf(
                "Doritos UK / EU Halal-certified or vegetarian editions",
                "Late July Organic Tortilla Chips",
                "Siete Grain-Free Nacho Chips"
            ),
            imageUrl = "https://images.openfoodfacts.org/images/products/002/840/009/0896/1.400.jpg"
        ),

        // 5. HELAL - USA/Global: Oreo Original Cookies
        ProductEntity(
            barcode = "7622210700544",
            name = "Oreo Original Sandwich Cookies",
            brand = "Nabisco / Mondelez",
            category = "Cookies & Biscuits",
            status = HalalStatus.HELAL,
            halalCertificate = "Plant-Based & Dairy-Free Formulation",
            harmfulOrSuspiciousIngredients = emptyList(),
            allIngredients = listOf(
                "Wheat flour",
                "Sugar",
                "Palm oil",
                "Fat reduced cocoa powder",
                "Wheat starch",
                "Glucose-fructose syrup",
                "Raising agents (Potassium carbonates, Ammonium carbonates, Sodium carbonates)",
                "Salt",
                "Emulsifiers (Soya lecithin, Sunflower lecithin)",
                "Flavouring (Vanillin)"
            ),
            reasonOrDetails = "Oreo cookies in Europe and USA are produced with 100% plant-based ingredients and vegetable oils. No gelatin, animal enzymes, or alcohol.",
            alternatives = emptyList(),
            imageUrl = "https://images.openfoodfacts.org/images/products/541/012/600/6957/front_en.161.400.jpg"
        ),

        // 6. HELAL - Global: Lotus Biscoff Spread
        ProductEntity(
            barcode = "5410126006957",
            name = "Lotus Biscoff Spread / Caramelized Biscuit",
            brand = "Lotus Bakeries",
            category = "Spreads & Biscuits",
            status = HalalStatus.HELAL,
            halalCertificate = "Halal Certified & Vegan Approved",
            harmfulOrSuspiciousIngredients = emptyList(),
            allIngredients = listOf(
                "Original Caramelised Biscuits (Wheat flour, Sugar, Vegetable oils, Candy sugar syrup, Sodium hydrogen carbonate, Soya flour, Salt, Cinnamon)",
                "Rapeseed oil",
                "Sugar",
                "Emulsifier (Soya lecithin)",
                "Citric acid"
            ),
            reasonOrDetails = "100% plant-based, verified vegan and Halal. Contains no animal fats, gelatin, or alcohol.",
            alternatives = emptyList(),
            imageUrl = "https://images.openfoodfacts.org/images/products/541/012/600/6957/front_en.161.400.jpg"
        ),

        // 7. HELAL - Global: Coca-Cola Original
        ProductEntity(
            barcode = "5449000000996",
            name = "Coca-Cola Original Taste (Can / Bottle)",
            brand = "The Coca-Cola Company",
            category = "Beverages & Soft Drinks",
            status = HalalStatus.HELAL,
            halalCertificate = "Halal Authority Verified Formulation",
            harmfulOrSuspiciousIngredients = emptyList(),
            allIngredients = listOf(
                "Carbonated water",
                "Sugar",
                "Caramel color (E150d)",
                "Phosphoric acid (E338)",
                "Natural flavorings (including caffeine)"
            ),
            reasonOrDetails = "Standard Coca-Cola contains no alcohol or animal-derived ingredients. Certified Halal by major international standards.",
            alternatives = emptyList(),
            imageUrl = "https://images.openfoodfacts.org/images/products/544/900/000/0996/front_en.1129.400.jpg"
        ),

        // 8. HELAL - Global: Heinz Tomato Ketchup
        ProductEntity(
            barcode = "8715700421384",
            name = "Heinz Tomato Ketchup 500ml",
            brand = "Kraft Heinz",
            category = "Condiments & Sauces",
            status = HalalStatus.HELAL,
            halalCertificate = "Halal Certified & Kosher Certified",
            harmfulOrSuspiciousIngredients = emptyList(),
            allIngredients = listOf(
                "Tomatoes",
                "Spirit vinegar",
                "Sugar",
                "Salt",
                "Spice and herb extracts (contains celery)",
                "Spice"
            ),
            reasonOrDetails = "100% plant-based ingredients using distilled grain/spirit vinegar (non-intoxicating). Certified Halal.",
            alternatives = emptyList(),
            imageUrl = "https://images.openfoodfacts.org/images/products/871/570/042/1384/front_fr.4.400.jpg"
        ),

        // 9. HELAL - EU/Global: Skittles Fruits
        ProductEntity(
            barcode = "5000159461122",
            name = "Skittles Fruits (Gelatin-Free)",
            brand = "Mars Wrigley",
            category = "Candy & Sweets",
            status = HalalStatus.HELAL,
            halalCertificate = "Gelatin-Free & Vegan Certified (V-Label)",
            harmfulOrSuspiciousIngredients = emptyList(),
            allIngredients = listOf(
                "Sugar",
                "Glucose syrup",
                "Palm fat",
                "Citric acid",
                "Malic acid",
                "Dextrin",
                "Maltodextrin",
                "Flavorings",
                "Modified starch",
                "Colorings (E162 Beetroot red, E163, E160a, E170, E100, E132, E133)",
                "Carnauba wax (E903)"
            ),
            reasonOrDetails = "Reformulated in Europe and USA without animal gelatin or carmine (E120). Plant-based starch and natural beetroot red (E162) are used.",
            alternatives = emptyList(),
            imageUrl = "https://images.openfoodfacts.org/images/products/500/015/946/1122/1.400.jpg"
        ),

        // 7. HELAL - EU/USA: Nutella Hazelnut Spread (Standard 400g/750g EU)
        ProductEntity(
            barcode = "3017620422003",
            name = "Nutella Nuss-Nugat-Creme (400g/750g)",
            brand = "Ferrero",
            category = "Spreads & Breakfast",
            status = HalalStatus.HELAL,
            halalCertificate = "Vegetarian Verified & Certified Clean",
            harmfulOrSuspiciousIngredients = emptyList(),
            allIngredients = listOf(
                "Sugar",
                "Palm oil",
                "Hazelnuts (13%)",
                "Skimmed milk powder (8.7%)",
                "Fat-reduced cocoa (7.4%)",
                "Emulsifier: Lecithins (Soya / Sunflower E322)",
                "Vanillin"
            ),
            reasonOrDetails = "Contains no animal fats, gelatin or alcohol. Emulsifier is 100% plant-based soy/sunflower lecithin.",
            alternatives = emptyList(),
            imageUrl = "https://images.openfoodfacts.org/images/products/301/762/042/2003/1.400.jpg"
        ),

        // 7b. HELAL - Germany: Nutella 750g / 450g Glass Jar
        ProductEntity(
            barcode = "4008400404127",
            name = "Nutella Nuss-Nugat-Creme 750g (Germany)",
            brand = "Ferrero",
            category = "Spreads & Breakfast",
            status = HalalStatus.HELAL,
            halalCertificate = "Vegetarisch / Keine tierischen Fette",
            harmfulOrSuspiciousIngredients = emptyList(),
            allIngredients = listOf(
                "Zucker",
                "Palmöl",
                "Haselnüsse (13%)",
                "Magermilchpulver (8,7%)",
                "Fettarmer Kakao (7,4%)",
                "Emulgator: Lecithine (Soja E322)",
                "Vanillin"
            ),
            reasonOrDetails = "Zertifiziert vegetarisch. Keine tierischen Fette, keine Schweinegelatine, kein Alkohol. 100% pflanzliches Sojalecithin.",
            alternatives = emptyList(),
            imageUrl = "https://images.openfoodfacts.org/images/products/400/840/040/4127/1.400.jpg"
        ),

        // 7bb. HELAL - Germany: Nutella Ferrero (4008400402222)
        ProductEntity(
            barcode = "4008400402222",
            name = "Nutella Nuss-Nugat-Creme (Ferrero Germany)",
            brand = "Ferrero",
            category = "Spreads & Breakfast",
            status = HalalStatus.HELAL,
            halalCertificate = "Vegetarisch / Keine tierischen Fette",
            harmfulOrSuspiciousIngredients = emptyList(),
            allIngredients = listOf(
                "Zucker",
                "Palmöl",
                "Haselnüsse (13%)",
                "Magermilchpulver (8,7%)",
                "Fettarmer Kakao (7,4%)",
                "Emulgator: Lecithine (Soja E322)",
                "Vanillin"
            ),
            reasonOrDetails = "Zertifiziert vegetarisch und helal-konform. Enthält rein pflanzliches Sojalecithin (E322), keine tierischen Fette, keine Schweinegelatine, kein Alkohol.",
            alternatives = emptyList(),
            imageUrl = "https://images.openfoodfacts.org/images/products/400/840/040/2222/1.400.jpg"
        ),

        // 7c. HELAL - Germany: Nutella 450g Glass Jar
        ProductEntity(
            barcode = "4008400401126",
            name = "Nutella Nuss-Nugat-Creme 450g (Germany)",
            brand = "Ferrero",
            category = "Spreads & Breakfast",
            status = HalalStatus.HELAL,
            halalCertificate = "Vegetarisch / Keine tierischen Fette",
            harmfulOrSuspiciousIngredients = emptyList(),
            allIngredients = listOf(
                "Zucker",
                "Palmöl",
                "Haselnüsse (13%)",
                "Magermilchpulver (8,7%)",
                "Fettarmer Kakao (7,4%)",
                "Emulgator: Lecithine (Soja E322)",
                "Vanillin"
            ),
            reasonOrDetails = "Zertifiziert vegetarisch. 100% pflanzliches Sojalecithin.",
            alternatives = emptyList(),
            imageUrl = "https://images.openfoodfacts.org/images/products/400/840/040/2222/1.400.jpg"
        ),

        // 7d. HELAL - Nutella B-ready
        ProductEntity(
            barcode = "8000500179864",
            name = "Nutella B-ready (Ferrero)",
            brand = "Ferrero",
            category = "Snacks & Biscuits",
            status = HalalStatus.HELAL,
            halalCertificate = "Vegetarian Verified",
            harmfulOrSuspiciousIngredients = emptyList(),
            allIngredients = listOf(
                "Nutella 81.5% (Sugar, Palm oil, Hazelnuts, Skimmed milk powder, Cocoa, Soy lecithin, Vanillin)",
                "Wheat flour (16%)",
                "Baker's yeast",
                "Barley malt extract",
                "Salt",
                "Skimmed milk powder"
            ),
            reasonOrDetails = "Free from alcohol, animal fats or non-halal additives.",
            alternatives = emptyList(),
            imageUrl = "https://images.openfoodfacts.org/images/products/800/050/017/9864/1.400.jpg"
        ),

        // 7e. HELAL - Nutella & GO!
        ProductEntity(
            barcode = "4008400401829",
            name = "Nutella & GO! (Ferrero)",
            brand = "Ferrero",
            category = "Snacks & Dips",
            status = HalalStatus.HELAL,
            halalCertificate = "Vegetarian Verified",
            harmfulOrSuspiciousIngredients = emptyList(),
            allIngredients = listOf(
                "Nutella spread",
                "Breadsticks (Wheat flour, Palm oil, Salt, Baker's yeast, Barley malt extract)"
            ),
            reasonOrDetails = "Plant-based vegetable oils and soy lecithin only. Alcohol and pork-free.",
            alternatives = emptyList(),
            imageUrl = "https://images.openfoodfacts.org/images/products/400/840/040/1829/1.400.jpg"
        ),

        // 7f. HELAL - Nutella 13 oz / 26 oz USA
        ProductEntity(
            barcode = "009800895007",
            name = "Nutella Hazelnut Spread (USA 13 oz)",
            brand = "Ferrero",
            category = "Spreads & Breakfast",
            status = HalalStatus.HELAL,
            halalCertificate = "Certified OU Kosher Dairy & Vegetarian",
            harmfulOrSuspiciousIngredients = emptyList(),
            allIngredients = listOf(
                "Sugar",
                "Palm oil",
                "Hazelnuts",
                "Skim milk",
                "Cocoa",
                "Soy lecithin as emulsifier",
                "Vanillin: an artificial flavor"
            ),
            reasonOrDetails = "Certified OU Kosher / Halal-compliant vegetable ingredients. No animal enzymes, gelatin or alcohol.",
            alternatives = emptyList(),
            imageUrl = "https://images.openfoodfacts.org/images/products/000/980/089/5007/front_en.106.400.jpg"
        ),

        // 7g. HELAL - Nutella Ferrero 750g France / EU
        ProductEntity(
            barcode = "3017624010701",
            name = "Nutella Pâte à Tartiner 750g (Ferrero France/EU)",
            brand = "Ferrero",
            category = "Spreads & Breakfast",
            status = HalalStatus.HELAL,
            halalCertificate = "Vegetarian Verified",
            harmfulOrSuspiciousIngredients = emptyList(),
            allIngredients = listOf(
                "Sucre",
                "Huile de palme",
                "Noisettes (13%)",
                "Lait écrémé en poudre (8,7%)",
                "Cacao maigre (7,4%)",
                "Émulsifiants: Lécithines [soja] (E322)",
                "Vanilline"
            ),
            reasonOrDetails = "100% végétal et lait. Sans graisse animale, sans gélatine, sans alcool.",
            alternatives = emptyList(),
            imageUrl = "https://images.openfoodfacts.org/images/products/301/762/401/0701/1.400.jpg"
        ),

        // 7h. HELAL - Nutella Kakaolu Fındık Kreması 750g Turkey
        ProductEntity(
            barcode = "8690504033845",
            name = "Nutella Kakaolu Fındık Kreması 750g (Ferrero)",
            brand = "Ferrero",
            category = "Krema & Kahvaltılık",
            status = HalalStatus.HELAL,
            halalCertificate = "TSE / Helal Uygunluk & Vejetaryen",
            harmfulOrSuspiciousIngredients = emptyList(),
            allIngredients = listOf(
                "Şeker",
                "Bitkisel Yağ (Palm)",
                "Fındık (%13)",
                "Yağsız Süt Tozu (%8,7)",
                "Yağı Azaltılmış Kakao Tozu (%7,4)",
                "Emülgatör: Lesitin (Soya E322)",
                "Aroma Verici (Vanilin)"
            ),
            reasonOrDetails = "Bitkisel içerikli ve süt ürünüdür. Domuz yağı, domuz jelatini ve alkol içermez. Emülgatör olarak bitkisel soya lesitini kullanılmıştır.",
            alternatives = emptyList(),
            imageUrl = "https://images.openfoodfacts.org/images/products/400/840/040/2222/1.400.jpg"
        ),

        // 7i. HELAL - Nutella Kakaolu Fındık Kreması 400g Turkey
        ProductEntity(
            barcode = "8690504033838",
            name = "Nutella Kakaolu Fındık Kreması 400g (Ferrero)",
            brand = "Ferrero",
            category = "Krema & Kahvaltılık",
            status = HalalStatus.HELAL,
            halalCertificate = "TSE / Helal Uygunluk & Vejetaryen",
            harmfulOrSuspiciousIngredients = emptyList(),
            allIngredients = listOf(
                "Şeker",
                "Bitkisel Yağ (Palm)",
                "Fındık (%13)",
                "Yağsız Süt Tozu (%8,7)",
                "Yağı Azaltılmış Kakao Tozu (%7,4)",
                "Emülgatör: Lesitin (Soya E322)",
                "Aroma Verici (Vanilin)"
            ),
            reasonOrDetails = "Domuz türevi ve alkol içermez. %100 bitkisel soya lesitini.",
            alternatives = emptyList(),
            imageUrl = "https://images.openfoodfacts.org/images/products/400/840/040/2222/1.400.jpg"
        ),

        // 7j. HELAL - Nutella Biscuits 304g Italy / EU
        ProductEntity(
            barcode = "8000500310427",
            name = "Nutella Biscuits Tube 166g / 304g (Ferrero)",
            brand = "Ferrero",
            category = "Snacks & Biscuits",
            status = HalalStatus.HELAL,
            halalCertificate = "Vegetarian Verified",
            harmfulOrSuspiciousIngredients = emptyList(),
            allIngredients = listOf(
                "Nutella 40% (Sugar, Palm oil, Hazelnuts 13%, Skimmed milk powder, Cocoa, Soy lecithin, Vanillin)",
                "Wheat flour (32.5%)",
                "Vegetable fats",
                "Cane sugar",
                "Lactose",
                "Wheat bran",
                "Whole milk powder",
                "Barley and corn malt extract",
                "Honey",
                "Baking powders (E500ii, E503ii)"
            ),
            reasonOrDetails = "Contains no animal fats, pork derivatives or alcohol. Emulsifiers are plant based.",
            alternatives = emptyList(),
            imageUrl = "https://images.openfoodfacts.org/images/products/800/050/031/0427/1.400.jpg"
        ),

        // 7k. HELAL - Nutella 1kg Italy / Glass
        ProductEntity(
            barcode = "8000500037560",
            name = "Nutella Crema Spalmabile 1000g (Ferrero Italy)",
            brand = "Ferrero",
            category = "Spreads & Breakfast",
            status = HalalStatus.HELAL,
            halalCertificate = "Vegetarian Verified",
            harmfulOrSuspiciousIngredients = emptyList(),
            allIngredients = listOf(
                "Zucchero",
                "Olio di palma",
                "Nocciole (13%)",
                "Latte scremato in polvere (8,7%)",
                "Cacao magro (7,4%)",
                "Emulsionanti: Lecitine (soia E322)",
                "Vanillina"
            ),
            reasonOrDetails = "Senza ingredienti animali o alcol. 100% lecitina vegetale.",
            alternatives = emptyList(),
            imageUrl = "https://images.openfoodfacts.org/images/products/800/050/003/7560/1.400.jpg"
        ),

        // 7l. HELAL - Nutella US UPC variations
        ProductEntity(
            barcode = "09800895007",
            name = "Nutella Hazelnut Spread (USA)",
            brand = "Ferrero",
            category = "Spreads & Breakfast",
            status = HalalStatus.HELAL,
            halalCertificate = "Certified OU Kosher Dairy",
            harmfulOrSuspiciousIngredients = emptyList(),
            allIngredients = listOf("Sugar", "Palm oil", "Hazelnuts", "Skim milk", "Cocoa", "Soy lecithin", "Vanillin"),
            reasonOrDetails = "Certified Kosher Dairy & Halal-compliant vegetable ingredients. No pork, gelatin or alcohol.",
            alternatives = emptyList(),
            imageUrl = "https://images.openfoodfacts.org/images/products/000/980/089/5007/front_en.106.400.jpg"
        ),

        // 8. HELAL - Germany: Ritter Sport Marzipan
        ProductEntity(
            barcode = "4000417025005",
            name = "Ritter Sport Marzipan (Germany)",
            brand = "Ritter Sport",
            category = "Dark Chocolate",
            status = HalalStatus.HELAL,
            halalCertificate = "European Vegetarian Union (V-Label Vegan)",
            harmfulOrSuspiciousIngredients = emptyList(),
            allIngredients = listOf(
                "Sugar",
                "Cocoa mass",
                "Almonds (16%)",
                "Cocoa butter",
                "Invert sugar syrup",
                "Emulsifier: Lecithins (Soya E322)",
                "Humectant: Invertase"
            ),
            reasonOrDetails = "Certified 100% Vegan by the European Vegetarian Union. Free from alcohol, animal fats, or non-halal emulsifiers.",
            alternatives = emptyList(),
            imageUrl = "https://images.openfoodfacts.org/images/products/400/041/702/5005/1.400.jpg"
        )
    )

    val eAdditivesDirectory = listOf(
        EAdditive(
            code = "E441",
            name = "Gelatin / Schweinegelatine",
            status = HalalStatus.HARAM,
            origin = "Hayvansal (Domuz derisi ve kemik kolajeni / Swine & Pork)",
            description = "Jelleştirici ve kıvam artırıcı madde. Avrupa ve ABD'de standart jelatinler aksi açıkça belirtilmedikçe domuz kesimhanelerinden elde edilmektedir.",
            alternateNames = listOf("Pork Gelatin", "Porcine Gelatin", "Schweinegelatine", "Gelatine de porc", "Varkensgelatine"),
            commonUsage = "Yumuşak şekerler (Gummy bears), marshmallow, tatlılar, yoğurtlar, kapsül ilaçlar"
        ),
        EAdditive(
            code = "E120",
            name = "Karmin / Cochineal / Karmin Kırmızısı",
            status = HalalStatus.HARAM,
            origin = "Böcek (Dactylopius coccus kabuklu böceğinden)",
            description = "Kurutulmuş kalkan biti (cochineal) dişilerinin ezilmesiyle üretilen parlak kırmızı doğal renklendirici. Fıkıh kurullarının çoğunluğu böcek tüketimini haram kabul eder.",
            alternateNames = listOf("Carmine", "Cochineal", "Carmines", "Karminsäure", "Natural Red 4", "Crimson Lake", "CI 75470"),
            commonUsage = "Kırmızı jelibonlar, çilekli yoğurtlar, sucuk/sosis, meyve suları, kırmızı pastalar, kozmetik"
        ),
        EAdditive(
            code = "E471",
            name = "Yağ Asitlerinin Mono- ve Digliseritleri",
            status = HalalStatus.SUPHELI,
            origin = "Bitkisel yağlar (Palmiye/Soya) VEYA Hayvansal yağlar (Domuz/Sığır iç yağı)",
            description = "En yaygın yağ emülgatörü. Bitkisel kökenli olanlar helaldir; ancak ambalajda 'bitkisel' veya 'vegetable' yazmıyorsa hayvansal kökenli olma riski taşır.",
            alternateNames = listOf("Mono- and diglycerides of fatty acids", "Glyceryl monostearate", "Mono- und Diglyceride von Speisefettsäuren"),
            commonUsage = "Ekmekler, kekler, çikolatalar, dondurmalar, margarinler, cipsler, paketli atıştırmalıklar"
        ),
        EAdditive(
            code = "E472a-f",
            name = "Mono- ve Digliseritlerin Esterleri (DATEM)",
            status = HalalStatus.SUPHELI,
            origin = "Bitkisel veya Hayvansal yağ asitleri",
            description = "E471 türevi hamur geliştirici emülgatörler. Kaynağı bitkisel olarak teyit edilmedikçe şüpheli kabul edilir.",
            alternateNames = listOf("DATEM", "E472a", "E472b", "E472c", "E472e", "Diacetyl tartaric acid esters"),
            commonUsage = "Tost ekmekleri, kruvasanlar, dondurulmuş hamurlar, mayonezler"
        ),
        EAdditive(
            code = "E904",
            name = "Şellak / Confectioner's Glaze",
            status = HalalStatus.SUPHELI,
            origin = "Böcek Salgısı (Kerria lacca böceğinin reçineli salgısı)",
            description = "Şekerleme ve meyvelere parlaklık veren sır maddesi. Bazı helal otoritelerince böcek salgısı olduğu için şüpheli/sakıncalı görülür.",
            alternateNames = listOf("Shellac", "Confectioner's Glaze", "Resinous glaze", "Gummilack"),
            commonUsage = "Draje şekerler, parlatılmış draje çikolatalar, parlatılmış elmalar, tablet kaplamaları"
        ),
        EAdditive(
            code = "E920",
            name = "L-Sistein (L-Cysteine)",
            status = HalalStatus.SUPHELI,
            origin = "Ördek tüyü, insan saçı, domuz kılı VEYA sentetik/fermantasyon",
            description = "Endüstriyel unlarda hamurun hızlı yoğrulmasını sağlayan aminoasit. Hayvansal kıldan elde edilenler sakıncalıdır.",
            alternateNames = listOf("L-Cysteine", "Cysteine hydrochloride", "L-Cystein"),
            commonUsage = "Paketli sandviç ekmekleri, hamburger ekmekleri, pizza tabanları, lavaş/tortilla"
        ),
        EAdditive(
            code = "E631",
            name = "Disodyum İnozinat",
            status = HalalStatus.SUPHELI,
            origin = "Et ekstraktı (Domuz/Sığır) VEYA bitkisel tapiyoka fermantasyonu",
            description = "Umami lezzet artırıcı. Çoğunlukla et yan ürünlerinden üretildiğinden helal sertifikası yoksa şüphelidir.",
            alternateNames = listOf("Disodium inosinate", "Sodium inosinate", "IMP"),
            commonUsage = "Aromalı patates cipsleri, hazır noodle'lar, hazır çorbalar, bulyonlar"
        ),
        EAdditive(
            code = "E542",
            name = "Kemik Fosfatı (Bone Phosphate)",
            status = HalalStatus.HARAM,
            origin = "Hayvan kemikleri (Sığır / Domuz)",
            description = "Yağsız hayvan kemiklerinden elde edilen topaklanmayı önleyici mineral. Helal kesim olmadan üretilenler haramdır.",
            alternateNames = listOf("Bone Phosphate", "Edible Bone Phosphate"),
            commonUsage = "Gıda takviyeleri, toz içecekler"
        ),
        EAdditive(
            code = "E100",
            name = "Kurkumin / Zerdeçal Sarısı",
            status = HalalStatus.HELAL,
            origin = "Bitkisel (Doğal Zerdeçal Kökü / Curcuma longa)",
            description = "Zerdeçal bitkisinden elde edilen %100 doğal, güvenli ve helal sarı renklendirici.",
            alternateNames = listOf("Curcumin", "Turmeric Yellow", "Diferuloylmethane", "CI 75300"),
            commonUsage = "Hardal, köri sosları, peynirler, unlu mamuller, içecekler, çorbalar"
        ),
        EAdditive(
            code = "E322",
            name = "Lesitin (Soya / Ayçiçeği)",
            status = HalalStatus.HELAL,
            origin = "Bitkisel (Soya fasulyesi veya Ayçiçeği tohumu)",
            description = "Çikolata ve ezmelerde kullanılan doğal bitkisel emülgatör. Güvenli ve helaldir.",
            alternateNames = listOf("Lecithin", "Soy Lecithin", "Sunflower Lecithin", "Phosphatidylcholine"),
            commonUsage = "Çikolatalar, Nutella, bisküviler, bebek mamaları"
        ),
        EAdditive(
            code = "E330",
            name = "Sitrik Asit (Limon Tuzu)",
            status = HalalStatus.HELAL,
            origin = "Bitkisel / Şeker fermantasyonu (Narenciye)",
            description = "En yaygın asitlik düzenleyici ve koruyucu. %100 helal ve doğaldır.",
            alternateNames = listOf("Citric Acid", "Limon Tuzu", "Zitronensäure", "Acide citrique"),
            commonUsage = "Gazlı içecekler, reçeller, konserve gıdalar, meyveli şekerlemeler"
        ),
        EAdditive(
            code = "E407",
            name = "Karragenan (Carrageenan)",
            status = HalalStatus.HELAL,
            origin = "Bitkisel (Kırmızı Deniz Yosunu / Red Algae)",
            description = "Deniz yosunlarından elde edilen bitkisel kıvam artırıcı. Hayvansal jelatin yerine popüler helal alternatiftir.",
            alternateNames = listOf("Carrageenan", "Irish Moss Extract", "Karrageen"),
            commonUsage = "Bitkisel sütler, pudingler, dondurmalar, vegan jelibonlar"
        ),
        EAdditive(
            code = "E412",
            name = "Guar Gam (Guar Gum)",
            status = HalalStatus.HELAL,
            origin = "Bitkisel (Guar fasulyesi tohumu)",
            description = "Guar bitkisinden elde edilen doğal kıvam artırıcı lif. Tamamen helaldir.",
            alternateNames = listOf("Guar Gum", "Guaran", "Guarkernmehl"),
            commonUsage = "Salata sosları, dondurma, soslar, glütensiz fırın ürünleri"
        ),
        EAdditive(
            code = "E415",
            name = "Ksantan Gam (Xanthan Gum)",
            status = HalalStatus.HELAL,
            origin = "Bakteriyel glukoz fermantasyonu (Xanthomonas campestris)",
            description = "Doğal fermantasyonla üretilen güvenli kıvam artırıcı ve stabilizatör. %100 helaldir.",
            alternateNames = listOf("Xanthan Gum", "Xanthangummi"),
            commonUsage = "Glütensiz unlar, soslar, şuruplar, diş macunları"
        ),
        EAdditive(
            code = "E162",
            name = "Pancar Kırmızısı (Betanin)",
            status = HalalStatus.HELAL,
            origin = "Bitkisel (Kırmızı Pancar / Beta vulgaris)",
            description = "Böcek karmini (E120) yerine kullanılan %100 bitkisel, sağlıklı ve helal kırmızı renklendirici.",
            alternateNames = listOf("Beetroot Red", "Betanin", "Rote-Bete-Rot"),
            commonUsage = "Çilekli dondurmalar, meyveli yoğurtlar, kırmızı şekerler, içecekler"
        ),
        EAdditive(
            code = "E160a",
            name = "Beta-Karoten",
            status = HalalStatus.HELAL,
            origin = "Bitkisel (Havuç, Palmiye meyvesi)",
            description = "Havuç ve bitkilerden elde edilen doğal turuncu-sarı pro-vitamin A renklendirici.",
            alternateNames = listOf("Beta-Carotene", "Provitamin A", "CI 75130"),
            commonUsage = "Margarinler, meyve suları, kekler, peynirler"
        )
    )
}
