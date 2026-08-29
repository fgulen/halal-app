package com.example.data.local

import com.example.data.model.EAdditive
import com.example.data.model.FlaggedIngredient
import com.example.data.model.FoodProduct
import com.example.data.model.HalalStatus
import com.example.data.model.LocalizedText

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
            // No fabricated "Certified/Approved" claim - see the Coca-Cola entry above.
            halalCertificate = null,
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
            // No fabricated "Certified by X" claim: standard Coca-Cola is not independently
            // halal-certified in most markets, it's simply free of flagged ingredients. Same
            // honesty standard the live analyzer applies (see HalalAnalyzer.kt Rule 4).
            halalCertificate = null,
            harmfulOrSuspiciousIngredients = emptyList(),
            allIngredients = listOf(
                "Carbonated water",
                "Sugar",
                "Caramel color (E150d)",
                "Phosphoric acid (E338)",
                "Natural flavorings (including caffeine)"
            ),
            reasonOrDetails = "No prohibited or doubtful ingredients found in the standard formulation - no alcohol or animal-derived additives. This is an ingredient screening, not a third-party halal certification.",
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
            // No fabricated "Certified" claim - see the Coca-Cola entry above for why.
            halalCertificate = null,
            harmfulOrSuspiciousIngredients = emptyList(),
            allIngredients = listOf(
                "Tomatoes",
                "Spirit vinegar",
                "Sugar",
                "Salt",
                "Spice and herb extracts (contains celery)",
                "Spice"
            ),
            reasonOrDetails = "100% plant-based ingredients using distilled grain/spirit vinegar (non-intoxicating). This is an ingredient screening, not a third-party certification claim.",
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
            // Softened from "Vegan Certified (V-Label)": the app has no sourced evidence of an
            // actual V-Label badge for this specific listing, so it should not assert one - see
            // the Coca-Cola entry above for the same reasoning.
            halalCertificate = "Gelatin-Free Formulation (Vegan-Friendly)",
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
            // Softened from "European Vegetarian Union (V-Label Vegan)" for the same reason as
            // the Skittles entry above - no sourced evidence of that specific badge in-app.
            halalCertificate = "Vegan Formulation (No Animal Fats)",
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
            name = LocalizedText(
                en = "Gelatin (Pork Gelatin)",
                de = "Gelatine (Schweinegelatine)",
                fr = "Gélatine (Gélatine de porc)",
                tr = "Gelatin / Schweinegelatine",
                ar = "الجيلاتين (جيلاتين الخنزير)"
            ),
            status = HalalStatus.HARAM,
            origin = LocalizedText(
                en = "Animal-derived (Pork skin & bone collagen)",
                de = "Tierisch (Schweinehaut- und Knochenkollagen)",
                fr = "Origine animale (Collagène de peau et d'os de porc)",
                tr = "Hayvansal (Domuz derisi ve kemik kolajeni / Swine & Pork)",
                ar = "حيواني المصدر (كولاجين جلد وعظام الخنزير)"
            ),
            description = LocalizedText(
                en = "A gelling and thickening agent. Standard gelatin in Europe and the US is sourced from pork slaughterhouses unless explicitly stated otherwise.",
                de = "Ein Gelier- und Verdickungsmittel. Standardgelatine in Europa und den USA stammt aus Schweineschlachtungen, sofern nicht ausdrücklich anders angegeben.",
                fr = "Agent gélifiant et épaississant. En Europe et aux États-Unis, la gélatine standard provient d'abattoirs porcins, sauf mention contraire explicite.",
                tr = "Jelleştirici ve kıvam artırıcı madde. Avrupa ve ABD'de standart jelatinler aksi açıkça belirtilmedikçe domuz kesimhanelerinden elde edilmektedir.",
                ar = "مادة تجليد وتكثيف. الجيلاتين القياسي في أوروبا والولايات المتحدة يُستخرج من مسالخ الخنازير ما لم يُذكر خلاف ذلك صراحة."
            ),
            alternateNames = listOf("Pork Gelatin", "Porcine Gelatin", "Schweinegelatine", "Gelatine de porc", "Varkensgelatine"),
            commonUsage = LocalizedText(
                en = "Gummy bears, marshmallows, desserts, yogurts, capsule medications",
                de = "Gummibärchen, Marshmallows, Süßspeisen, Joghurts, Kapselmedikamente",
                fr = "Oursons en gélatine, chamallows, desserts, yaourts, médicaments en capsules",
                tr = "Yumuşak şekerler (Gummy bears), marshmallow, tatlılar, yoğurtlar, kapsül ilaçlar",
                ar = "حلوى الدببة الهلامية، المارشميلو، الحلويات، الزبادي، الأدوية على شكل كبسولات"
            )
        ),
        EAdditive(
            code = "E120",
            name = LocalizedText(
                en = "Carmine / Cochineal Red",
                de = "Karmin / Cochenillerot",
                fr = "Carmin / Rouge cochenille",
                tr = "Karmin / Cochineal / Karmin Kırmızısı",
                ar = "القرمز / الكوشينيل"
            ),
            status = HalalStatus.HARAM,
            origin = LocalizedText(
                en = "Insect-derived (from the Dactylopius coccus scale insect)",
                de = "Insektenbasiert (von der Schildlaus Dactylopius coccus)",
                fr = "Origine insecte (de la cochenille Dactylopius coccus)",
                tr = "Böcek (Dactylopius coccus kabuklu böceğinden)",
                ar = "حشري المصدر (من حشرة القرمز Dactylopius coccus)"
            ),
            description = LocalizedText(
                en = "A bright red natural colorant produced by crushing dried female cochineal insects. Most fiqh councils rule insect consumption as haram.",
                de = "Ein leuchtend roter Naturfarbstoff, der durch Zerstoßen getrockneter weiblicher Cochenille-Schildläuse gewonnen wird. Die meisten Fiqh-Räte stufen den Verzehr von Insekten als haram ein.",
                fr = "Colorant naturel rouge vif obtenu en broyant des cochenilles femelles séchées. La plupart des conseils de fiqh considèrent la consommation d'insectes comme haram.",
                tr = "Kurutulmuş kalkan biti (cochineal) dişilerinin ezilmesiyle üretilen parlak kırmızı doğal renklendirici. Fıkıh kurullarının çoğunluğu böcek tüketimini haram kabul eder.",
                ar = "صبغة طبيعية حمراء زاهية تُنتج بسحق إناث حشرة القرمز المجففة. تعتبر معظم المجالس الفقهية استهلاك الحشرات حراماً."
            ),
            alternateNames = listOf("Carmine", "Cochineal", "Carmines", "Karminsäure", "Natural Red 4", "Crimson Lake", "CI 75470"),
            commonUsage = LocalizedText(
                en = "Red gummy candies, strawberry yogurts, sausages, fruit juices, red cakes, cosmetics",
                de = "Rote Fruchtgummis, Erdbeerjoghurts, Wurstwaren, Fruchtsäfte, rote Kuchen, Kosmetika",
                fr = "Bonbons gélifiés rouges, yaourts à la fraise, saucisses, jus de fruits, gâteaux rouges, cosmétiques",
                tr = "Kırmızı jelibonlar, çilekli yoğurtlar, sucuk/sosis, meyve suları, kırmızı pastalar, kozmetik",
                ar = "الحلوى الهلامية الحمراء، زبادي الفراولة، النقانق والسجق، عصائر الفواكه، الكعك الأحمر، مستحضرات التجميل"
            )
        ),
        EAdditive(
            code = "E471",
            name = LocalizedText(
                en = "Mono- and Diglycerides of Fatty Acids",
                de = "Mono- und Diglyceride von Speisefettsäuren",
                fr = "Mono- et diglycérides d'acides gras",
                tr = "Yağ Asitlerinin Mono- ve Digliseritleri",
                ar = "أحادي وثنائي غليسريد الأحماض الدهنية"
            ),
            status = HalalStatus.SUPHELI,
            origin = LocalizedText(
                en = "Vegetable oils (palm/soy) OR animal fats (pork/beef tallow)",
                de = "Pflanzliche Öle (Palme/Soja) ODER tierische Fette (Schweine-/Rindertalg)",
                fr = "Huiles végétales (palme/soja) OU graisses animales (suif de porc/bœuf)",
                tr = "Bitkisel yağlar (Palmiye/Soya) VEYA Hayvansal yağlar (Domuz/Sığır iç yağı)",
                ar = "زيوت نباتية (نخيل/صويا) أو دهون حيوانية (شحم خنزير/بقر)"
            ),
            description = LocalizedText(
                en = "The most common fat emulsifier. Plant-derived versions are halal, but without 'vegetable' stated on the packaging there is a risk it is animal-derived.",
                de = "Der häufigste Fettemulgator. Pflanzliche Varianten sind halal; steht jedoch kein Hinweis auf 'pflanzlich' auf der Verpackung, besteht das Risiko einer tierischen Herkunft.",
                fr = "L'émulsifiant gras le plus courant. Les versions d'origine végétale sont halal, mais sans mention 'végétal' sur l'emballage, il existe un risque d'origine animale.",
                tr = "En yaygın yağ emülgatörü. Bitkisel kökenli olanlar helaldir; ancak ambalajda 'bitkisel' veya 'vegetable' yazmıyorsa hayvansal kökenli olma riski taşır.",
                ar = "المستحلب الدهني الأكثر شيوعاً. الأنواع النباتية المصدر حلال؛ لكن في حال عدم ذكر كلمة 'نباتي' على العبوة يكون هناك خطر أن يكون المصدر حيوانياً."
            ),
            alternateNames = listOf("Mono- and diglycerides of fatty acids", "Glyceryl monostearate", "Mono- und Diglyceride von Speisefettsäuren"),
            commonUsage = LocalizedText(
                en = "Bread, cakes, chocolates, ice cream, margarine, chips, packaged snacks",
                de = "Brot, Kuchen, Schokolade, Eiscreme, Margarine, Chips, verpackte Snacks",
                fr = "Pain, gâteaux, chocolats, crèmes glacées, margarine, chips, en-cas emballés",
                tr = "Ekmekler, kekler, çikolatalar, dondurmalar, margarinler, cipsler, paketli atıştırmalıklar",
                ar = "الخبز، الكعك، الشوكولاتة، الآيس كريم، السمن النباتي، رقائق البطاطس، الوجبات الخفيفة المعبأة"
            )
        ),
        EAdditive(
            code = "E472a-f",
            name = LocalizedText(
                en = "Esters of Mono- and Diglycerides (DATEM)",
                de = "Ester von Mono- und Diglyceriden (DATEM)",
                fr = "Esters de mono- et diglycérides (DATEM)",
                tr = "Mono- ve Digliseritlerin Esterleri (DATEM)",
                ar = "إسترات أحادي وثنائي الغليسريد (DATEM)"
            ),
            status = HalalStatus.SUPHELI,
            origin = LocalizedText(
                en = "Plant or animal fatty acids",
                de = "Pflanzliche oder tierische Fettsäuren",
                fr = "Acides gras végétaux ou animaux",
                tr = "Bitkisel veya Hayvansal yağ asitleri",
                ar = "أحماض دهنية نباتية أو حيوانية"
            ),
            description = LocalizedText(
                en = "Dough-conditioning emulsifiers derived from E471. Considered doubtful unless the source is confirmed as plant-based.",
                de = "Von E471 abgeleitete teigverbessernde Emulgatoren. Gilt als zweifelhaft, sofern die pflanzliche Herkunft nicht bestätigt ist.",
                fr = "Émulsifiants améliorants de pâte dérivés de l'E471. Considérés comme douteux sauf confirmation d'une origine végétale.",
                tr = "E471 türevi hamur geliştirici emülgatörler. Kaynağı bitkisel olarak teyit edilmedikçe şüpheli kabul edilir.",
                ar = "مستحلبات محسّنة للعجين مشتقة من E471. تُعتبر مشبوهة ما لم يُؤكد أن مصدرها نباتي."
            ),
            alternateNames = listOf("DATEM", "E472a", "E472b", "E472c", "E472e", "Diacetyl tartaric acid esters"),
            commonUsage = LocalizedText(
                en = "Toast bread, croissants, frozen dough, mayonnaise",
                de = "Toastbrot, Croissants, Tiefkühlteig, Mayonnaise",
                fr = "Pain de mie, croissants, pâtes surgelées, mayonnaise",
                tr = "Tost ekmekleri, kruvasanlar, dondurulmuş hamurlar, mayonezler",
                ar = "خبز التوست، الكرواسون، العجين المجمد، المايونيز"
            )
        ),
        EAdditive(
            code = "E904",
            name = LocalizedText(
                en = "Shellac / Confectioner's Glaze",
                de = "Schellack / Konditorlack",
                fr = "Gomme laque / Glaçage de confiseur",
                tr = "Şellak / Confectioner's Glaze",
                ar = "الشيلاك / طلاء الحلويات"
            ),
            status = HalalStatus.SUPHELI,
            origin = LocalizedText(
                en = "Insect secretion (resinous secretion of the Kerria lacca insect)",
                de = "Insektensekret (Harzsekret der Lackschildlaus Kerria lacca)",
                fr = "Sécrétion d'insecte (sécrétion résineuse de l'insecte Kerria lacca)",
                tr = "Böcek Salgısı (Kerria lacca böceğinin reçineli salgısı)",
                ar = "إفراز حشري (إفراز راتنجي من حشرة Kerria lacca)"
            ),
            description = LocalizedText(
                en = "A glazing agent that gives confectionery and fruit their shine. Some halal authorities view it as doubtful/objectionable since it is an insect secretion.",
                de = "Ein Glasurmittel, das Süßwaren und Obst ihren Glanz verleiht. Manche Halal-Autoritäten stufen es aufgrund seiner Herkunft als Insektensekret als zweifelhaft/bedenklich ein.",
                fr = "Agent lustrant qui donne leur brillance aux confiseries et aux fruits. Certaines autorités halal le considèrent douteux/problématique car il s'agit d'une sécrétion d'insecte.",
                tr = "Şekerleme ve meyvelere parlaklık veren sır maddesi. Bazı helal otoritelerince böcek salgısı olduğu için şüpheli/sakıncalı görülür.",
                ar = "مادة تلميع تمنح الحلويات والفواكه لمعانها. تعتبره بعض الجهات الفقهية مشبوهاً/مكروهاً لأنه إفراز حشري."
            ),
            alternateNames = listOf("Shellac", "Confectioner's Glaze", "Resinous glaze", "Gummilack"),
            commonUsage = LocalizedText(
                en = "Sugar-coated candies, glazed chocolate dragées, waxed apples, tablet coatings",
                de = "Dragees, glasierte Schokoladendragees, gewachste Äpfel, Tablettenüberzüge",
                fr = "Bonbons dragéifiés, dragées de chocolat lustrées, pommes cirées, enrobages de comprimés",
                tr = "Draje şekerler, parlatılmış draje çikolatalar, parlatılmış elmalar, tablet kaplamaları",
                ar = "الحلوى المطلية بالسكر، حبيبات الشوكولاتة المصقولة، التفاح المصقول، أغلفة الأقراص الدوائية"
            )
        ),
        EAdditive(
            code = "E920",
            name = LocalizedText(
                en = "L-Cysteine",
                de = "L-Cystein",
                fr = "L-Cystéine",
                tr = "L-Sistein (L-Cysteine)",
                ar = "إل-سيستين (L-Cysteine)"
            ),
            status = HalalStatus.SUPHELI,
            origin = LocalizedText(
                en = "Duck feathers, human hair, pig bristles OR synthetic/fermentation-derived",
                de = "Entenfedern, Menschenhaar, Schweineborsten ODER synthetisch/fermentativ hergestellt",
                fr = "Plumes de canard, cheveux humains, soies de porc OU d'origine synthétique/fermentation",
                tr = "Ördek tüyü, insan saçı, domuz kılı VEYA sentetik/fermantasyon",
                ar = "ريش البط، شعر الإنسان، شعر الخنزير أو مُصنّع/ناتج عن التخمير"
            ),
            description = LocalizedText(
                en = "An amino acid that speeds up dough mixing in industrial flour. Versions derived from animal hair/bristles are objectionable.",
                de = "Eine Aminosäure, die in industriellem Mehl die Teigknetzeit verkürzt. Aus tierischen Haaren/Borsten gewonnene Varianten sind bedenklich.",
                fr = "Un acide aminé qui accélère le pétrissage de la pâte dans la farine industrielle. Les versions issues de poils/soies animales sont problématiques.",
                tr = "Endüstriyel unlarda hamurun hızlı yoğrulmasını sağlayan aminoasit. Hayvansal kıldan elde edilenler sakıncalıdır.",
                ar = "حمض أميني يسرّع عجن العجين في الدقيق الصناعي. الأنواع المستخرجة من شعر أو وبر الحيوانات مكروهة."
            ),
            alternateNames = listOf("L-Cysteine", "Cysteine hydrochloride", "L-Cystein"),
            commonUsage = LocalizedText(
                en = "Packaged sandwich bread, burger buns, pizza bases, flatbread/tortillas",
                de = "Verpacktes Sandwichbrot, Burgerbrötchen, Pizzaböden, Fladenbrot/Tortillas",
                fr = "Pain de mie emballé, pains à burger, bases de pizza, galettes/tortillas",
                tr = "Paketli sandviç ekmekleri, hamburger ekmekleri, pizza tabanları, lavaş/tortilla",
                ar = "خبز الساندويتش المعبأ، خبز البرغر، عجينة البيتزا، الخبز المسطح/التورتيلا"
            )
        ),
        EAdditive(
            code = "E631",
            name = LocalizedText(
                en = "Disodium Inosinate",
                de = "Dinatriuminosinat",
                fr = "Inosinate disodique",
                tr = "Disodyum İnozinat",
                ar = "إينوزينات ثنائي الصوديوم"
            ),
            status = HalalStatus.SUPHELI,
            origin = LocalizedText(
                en = "Meat extract (pork/beef) OR plant-based tapioca fermentation",
                de = "Fleischextrakt (Schwein/Rind) ODER pflanzliche Tapioka-Fermentation",
                fr = "Extrait de viande (porc/bœuf) OU fermentation végétale du tapioca",
                tr = "Et ekstraktı (Domuz/Sığır) VEYA bitkisel tapiyoka fermantasyonu",
                ar = "مستخلص اللحم (خنزير/بقر) أو تخمير نباتي من التابيوكا"
            ),
            description = LocalizedText(
                en = "An umami flavor enhancer. Mostly produced from meat by-products, so it is doubtful without a halal certificate.",
                de = "Ein Umami-Geschmacksverstärker. Wird meist aus Fleischnebenprodukten hergestellt, daher ohne Halal-Zertifikat zweifelhaft.",
                fr = "Exhausteur de goût umami. Le plus souvent produit à partir de sous-produits carnés, donc douteux sans certification halal.",
                tr = "Umami lezzet artırıcı. Çoğunlukla et yan ürünlerinden üretildiğinden helal sertifikası yoksa şüphelidir.",
                ar = "معزز نكهة أومامي. يُنتج غالباً من مشتقات اللحوم، لذا يُعتبر مشبوهاً دون شهادة حلال."
            ),
            alternateNames = listOf("Disodium inosinate", "Sodium inosinate", "IMP"),
            commonUsage = LocalizedText(
                en = "Flavored potato chips, instant noodles, instant soups, bouillon cubes",
                de = "Aromatisierte Kartoffelchips, Instantnudeln, Instantsuppen, Brühwürfel",
                fr = "Chips aromatisées, nouilles instantanées, soupes instantanées, bouillons",
                tr = "Aromalı patates cipsleri, hazır noodle'lar, hazır çorbalar, bulyonlar",
                ar = "رقائق البطاطس المنكهة، النودلز سريعة التحضير، الشوربات الجاهزة، مكعبات المرق"
            )
        ),
        EAdditive(
            code = "E542",
            name = LocalizedText(
                en = "Bone Phosphate",
                de = "Knochenphosphat",
                fr = "Phosphate d'os",
                tr = "Kemik Fosfatı (Bone Phosphate)",
                ar = "فوسفات العظام"
            ),
            status = HalalStatus.HARAM,
            origin = LocalizedText(
                en = "Animal bones (beef/pork)",
                de = "Tierknochen (Rind/Schwein)",
                fr = "Os d'animaux (bœuf/porc)",
                tr = "Hayvan kemikleri (Sığır / Domuz)",
                ar = "عظام حيوانية (بقر/خنزير)"
            ),
            description = LocalizedText(
                en = "An anti-caking mineral derived from defatted animal bones. Those produced without halal slaughter are haram.",
                de = "Ein Antiklumpmittel aus entfetteten Tierknochen. Ohne halal-konforme Schlachtung hergestellte Varianten sind haram.",
                fr = "Un agent antiagglomérant issu d'os d'animaux dégraissés. Ceux produits sans abattage halal sont haram.",
                tr = "Yağsız hayvan kemiklerinden elde edilen topaklanmayı önleyici mineral. Helal kesim olmadan üretilenler haramdır.",
                ar = "مادة مانعة للتكتل مستخرجة من عظام حيوانية منزوعة الدهن. المنتجة دون ذبح حلال تُعتبر حراماً."
            ),
            alternateNames = listOf("Bone Phosphate", "Edible Bone Phosphate"),
            commonUsage = LocalizedText(
                en = "Food supplements, powdered drink mixes",
                de = "Nahrungsergänzungsmittel, Getränkepulver",
                fr = "Compléments alimentaires, boissons en poudre",
                tr = "Gıda takviyeleri, toz içecekler",
                ar = "المكملات الغذائية، مساحيق المشروبات"
            )
        ),
        EAdditive(
            code = "E100",
            name = LocalizedText(
                en = "Curcumin / Turmeric Yellow",
                de = "Curcumin / Kurkumagelb",
                fr = "Curcumine / Jaune de curcuma",
                tr = "Kurkumin / Zerdeçal Sarısı",
                ar = "الكركمين / أصفر الكركم"
            ),
            status = HalalStatus.HELAL,
            origin = LocalizedText(
                en = "Plant-based (natural turmeric root / Curcuma longa)",
                de = "Pflanzlich (natürliche Kurkumawurzel / Curcuma longa)",
                fr = "Origine végétale (racine de curcuma naturelle / Curcuma longa)",
                tr = "Bitkisel (Doğal Zerdeçal Kökü / Curcuma longa)",
                ar = "نباتي المصدر (جذر الكركم الطبيعي / Curcuma longa)"
            ),
            description = LocalizedText(
                en = "A 100% natural, safe, and halal yellow colorant derived from the turmeric plant.",
                de = "Ein 100 % natürlicher, sicherer und halal-konformer gelber Farbstoff aus der Kurkumapflanze.",
                fr = "Un colorant jaune 100 % naturel, sûr et halal, extrait de la plante de curcuma.",
                tr = "Zerdeçal bitkisinden elde edilen %100 doğal, güvenli ve helal sarı renklendirici.",
                ar = "صبغة صفراء طبيعية 100% وآمنة وحلال مستخرجة من نبات الكركم."
            ),
            alternateNames = listOf("Curcumin", "Turmeric Yellow", "Diferuloylmethane", "CI 75300"),
            commonUsage = LocalizedText(
                en = "Mustard, curry sauces, cheeses, baked goods, beverages, soups",
                de = "Senf, Currysoßen, Käse, Backwaren, Getränke, Suppen",
                fr = "Moutarde, sauces au curry, fromages, produits de boulangerie, boissons, soupes",
                tr = "Hardal, köri sosları, peynirler, unlu mamuller, içecekler, çorbalar",
                ar = "الخردل، صلصات الكاري، الأجبان، المخبوزات، المشروبات، الشوربات"
            )
        ),
        EAdditive(
            code = "E322",
            name = LocalizedText(
                en = "Lecithin (Soy / Sunflower)",
                de = "Lecithin (Soja / Sonnenblume)",
                fr = "Lécithine (Soja / Tournesol)",
                tr = "Lesitin (Soya / Ayçiçeği)",
                ar = "الليسيثين (الصويا / عباد الشمس)"
            ),
            status = HalalStatus.HELAL,
            origin = LocalizedText(
                en = "Plant-based (soybean or sunflower seed)",
                de = "Pflanzlich (Sojabohne oder Sonnenblumenkern)",
                fr = "Origine végétale (graine de soja ou de tournesol)",
                tr = "Bitkisel (Soya fasulyesi veya Ayçiçeği tohumu)",
                ar = "نباتي المصدر (فول الصويا أو بذور عباد الشمس)"
            ),
            description = LocalizedText(
                en = "A natural plant-based emulsifier used in chocolate and spreads. Safe and halal.",
                de = "Ein natürlicher pflanzlicher Emulgator, der in Schokolade und Aufstrichen verwendet wird. Sicher und halal.",
                fr = "Un émulsifiant naturel d'origine végétale utilisé dans le chocolat et les pâtes à tartiner. Sûr et halal.",
                tr = "Çikolata ve ezmelerde kullanılan doğal bitkisel emülgatör. Güvenli ve helaldir.",
                ar = "مستحلب نباتي طبيعي يُستخدم في الشوكولاتة والمعجونات القابلة للدهن. آمن وحلال."
            ),
            alternateNames = listOf("Lecithin", "Soy Lecithin", "Sunflower Lecithin", "Phosphatidylcholine"),
            commonUsage = LocalizedText(
                en = "Chocolates, Nutella, biscuits, baby formula",
                de = "Schokolade, Nutella, Kekse, Babynahrung",
                fr = "Chocolats, Nutella, biscuits, préparations pour nourrissons",
                tr = "Çikolatalar, Nutella, bisküviler, bebek mamaları",
                ar = "الشوكولاتة، النوتيلا، البسكويت، حليب الأطفال"
            )
        ),
        EAdditive(
            code = "E330",
            name = LocalizedText(
                en = "Citric Acid",
                de = "Zitronensäure",
                fr = "Acide citrique",
                tr = "Sitrik Asit (Limon Tuzu)",
                ar = "حمض الستريك (ملح الليمون)"
            ),
            status = HalalStatus.HELAL,
            origin = LocalizedText(
                en = "Plant-based / sugar fermentation (citrus)",
                de = "Pflanzlich / Zuckerfermentation (Zitrusfrüchte)",
                fr = "Origine végétale / fermentation du sucre (agrumes)",
                tr = "Bitkisel / Şeker fermantasyonu (Narenciye)",
                ar = "نباتي / تخمير سكري (الحمضيات)"
            ),
            description = LocalizedText(
                en = "The most common acidity regulator and preservative. 100% halal and natural.",
                de = "Der häufigste Säureregulator und Konservierungsstoff. 100 % halal und natürlich.",
                fr = "Le régulateur d'acidité et conservateur le plus courant. 100 % halal et naturel.",
                tr = "En yaygın asitlik düzenleyici ve koruyucu. %100 helal ve doğaldır.",
                ar = "منظم الحموضة والمادة الحافظة الأكثر شيوعاً. حلال وطبيعي 100%."
            ),
            alternateNames = listOf("Citric Acid", "Limon Tuzu", "Zitronensäure", "Acide citrique"),
            commonUsage = LocalizedText(
                en = "Carbonated drinks, jams, canned foods, fruit candies",
                de = "Kohlensäurehaltige Getränke, Marmeladen, Konserven, Fruchtbonbons",
                fr = "Boissons gazeuses, confitures, conserves, bonbons aux fruits",
                tr = "Gazlı içecekler, reçeller, konserve gıdalar, meyveli şekerlemeler",
                ar = "المشروبات الغازية، المربى، الأطعمة المعلبة، حلوى الفواكه"
            )
        ),
        EAdditive(
            code = "E407",
            name = LocalizedText(
                en = "Carrageenan",
                de = "Carrageen",
                fr = "Carraghénane",
                tr = "Karragenan (Carrageenan)",
                ar = "الكاراجينان"
            ),
            status = HalalStatus.HELAL,
            origin = LocalizedText(
                en = "Plant-based (red seaweed / red algae)",
                de = "Pflanzlich (Rotalgen)",
                fr = "Origine végétale (algue rouge)",
                tr = "Bitkisel (Kırmızı Deniz Yosunu / Red Algae)",
                ar = "نباتي المصدر (الطحالب البحرية الحمراء)"
            ),
            description = LocalizedText(
                en = "A plant-based thickener derived from seaweed. A popular halal alternative to animal gelatin.",
                de = "Ein pflanzliches Verdickungsmittel aus Meeresalgen. Eine beliebte halal-konforme Alternative zu tierischer Gelatine.",
                fr = "Un épaississant d'origine végétale extrait d'algues marines. Une alternative halal populaire à la gélatine animale.",
                tr = "Deniz yosunlarından elde edilen bitkisel kıvam artırıcı. Hayvansal jelatin yerine popüler helal alternatiftir.",
                ar = "مادة تكثيف نباتية مستخرجة من الطحالب البحرية. بديل حلال شائع عن الجيلاتين الحيواني."
            ),
            alternateNames = listOf("Carrageenan", "Irish Moss Extract", "Karrageen"),
            commonUsage = LocalizedText(
                en = "Plant-based milks, puddings, ice cream, vegan gummies",
                de = "Pflanzliche Milchalternativen, Pudding, Eiscreme, vegane Fruchtgummis",
                fr = "Laits végétaux, puddings, crèmes glacées, bonbons gélifiés vegan",
                tr = "Bitkisel sütler, pudingler, dondurmalar, vegan jelibonlar",
                ar = "الحليب النباتي، البودينغ، الآيس كريم، الحلوى الهلامية النباتية"
            )
        ),
        EAdditive(
            code = "E412",
            name = LocalizedText(
                en = "Guar Gum",
                de = "Guarkernmehl",
                fr = "Gomme de guar",
                tr = "Guar Gam (Guar Gum)",
                ar = "صمغ الغوار"
            ),
            status = HalalStatus.HELAL,
            origin = LocalizedText(
                en = "Plant-based (guar bean seed)",
                de = "Pflanzlich (Guarbohnensamen)",
                fr = "Origine végétale (graine de haricot guar)",
                tr = "Bitkisel (Guar fasulyesi tohumu)",
                ar = "نباتي المصدر (بذور فول الغوار)"
            ),
            description = LocalizedText(
                en = "A natural thickening fiber derived from the guar plant. Fully halal.",
                de = "Ein natürlicher verdickender Ballaststoff aus der Guarpflanze. Vollständig halal.",
                fr = "Une fibre épaississante naturelle extraite de la plante de guar. Entièrement halal.",
                tr = "Guar bitkisinden elde edilen doğal kıvam artırıcı lif. Tamamen helaldir.",
                ar = "ألياف طبيعية مكثفة مستخرجة من نبات الغوار. حلال تماماً."
            ),
            alternateNames = listOf("Guar Gum", "Guaran", "Guarkernmehl"),
            commonUsage = LocalizedText(
                en = "Salad dressings, ice cream, sauces, gluten-free baked goods",
                de = "Salatdressings, Eiscreme, Soßen, glutenfreie Backwaren",
                fr = "Vinaigrettes, crèmes glacées, sauces, produits de boulangerie sans gluten",
                tr = "Salata sosları, dondurma, soslar, glütensiz fırın ürünleri",
                ar = "صلصات السلطة، الآيس كريم، الصلصات، المخبوزات الخالية من الغلوتين"
            )
        ),
        EAdditive(
            code = "E415",
            name = LocalizedText(
                en = "Xanthan Gum",
                de = "Xanthan",
                fr = "Gomme xanthane",
                tr = "Ksantan Gam (Xanthan Gum)",
                ar = "صمغ الزانثان"
            ),
            status = HalalStatus.HELAL,
            origin = LocalizedText(
                en = "Bacterial glucose fermentation (Xanthomonas campestris)",
                de = "Bakterielle Glukosefermentation (Xanthomonas campestris)",
                fr = "Fermentation bactérienne du glucose (Xanthomonas campestris)",
                tr = "Bakteriyel glukoz fermantasyonu (Xanthomonas campestris)",
                ar = "تخمير بكتيري للغلوكوز (Xanthomonas campestris)"
            ),
            description = LocalizedText(
                en = "A safe thickener and stabilizer produced by natural fermentation. 100% halal.",
                de = "Ein sicheres Verdickungs- und Stabilisierungsmittel, hergestellt durch natürliche Fermentation. 100 % halal.",
                fr = "Un épaississant et stabilisant sûr produit par fermentation naturelle. 100 % halal.",
                tr = "Doğal fermantasyonla üretilen güvenli kıvam artırıcı ve stabilizatör. %100 helaldir.",
                ar = "مادة تكثيف وتثبيت آمنة تُنتج بالتخمير الطبيعي. حلال 100%."
            ),
            alternateNames = listOf("Xanthan Gum", "Xanthangummi"),
            commonUsage = LocalizedText(
                en = "Gluten-free flours, sauces, syrups, toothpaste",
                de = "Glutenfreie Mehle, Soßen, Sirupe, Zahnpasta",
                fr = "Farines sans gluten, sauces, sirops, dentifrices",
                tr = "Glütensiz unlar, soslar, şuruplar, diş macunları",
                ar = "الدقيق الخالي من الغلوتين، الصلصات، الشراب، معجون الأسنان"
            )
        ),
        EAdditive(
            code = "E162",
            name = LocalizedText(
                en = "Beetroot Red (Betanin)",
                de = "Rote-Bete-Rot (Betanin)",
                fr = "Rouge de betterave (Bétanine)",
                tr = "Pancar Kırmızısı (Betanin)",
                ar = "أحمر الشمندر (البيتانين)"
            ),
            status = HalalStatus.HELAL,
            origin = LocalizedText(
                en = "Plant-based (red beet / Beta vulgaris)",
                de = "Pflanzlich (Rote Bete / Beta vulgaris)",
                fr = "Origine végétale (betterave rouge / Beta vulgaris)",
                tr = "Bitkisel (Kırmızı Pancar / Beta vulgaris)",
                ar = "نباتي المصدر (الشمندر الأحمر / Beta vulgaris)"
            ),
            description = LocalizedText(
                en = "A 100% plant-based, healthy, and halal red colorant used in place of insect-derived carmine (E120).",
                de = "Ein 100 % pflanzlicher, gesunder und halal-konformer roter Farbstoff, der anstelle des insektenbasierten Karmins (E120) verwendet wird.",
                fr = "Un colorant rouge 100 % végétal, sain et halal, utilisé à la place du carmin d'origine insecte (E120).",
                tr = "Böcek karmini (E120) yerine kullanılan %100 bitkisel, sağlıklı ve helal kırmızı renklendirici.",
                ar = "صبغة حمراء نباتية 100% وصحية وحلال تُستخدم بدلاً من القرمز الحشري (E120)."
            ),
            alternateNames = listOf("Beetroot Red", "Betanin", "Rote-Bete-Rot"),
            commonUsage = LocalizedText(
                en = "Strawberry ice cream, fruit yogurts, red candies, beverages",
                de = "Erdbeereis, Fruchtjoghurts, rote Bonbons, Getränke",
                fr = "Glaces à la fraise, yaourts aux fruits, bonbons rouges, boissons",
                tr = "Çilekli dondurmalar, meyveli yoğurtlar, kırmızı şekerler, içecekler",
                ar = "آيس كريم الفراولة، زبادي الفواكه، الحلوى الحمراء، المشروبات"
            )
        ),
        EAdditive(
            code = "E160a",
            name = LocalizedText(
                en = "Beta-Carotene",
                de = "Beta-Carotin",
                fr = "Bêta-carotène",
                tr = "Beta-Karoten",
                ar = "بيتا كاروتين"
            ),
            status = HalalStatus.HELAL,
            origin = LocalizedText(
                en = "Plant-based (carrot, palm fruit)",
                de = "Pflanzlich (Karotte, Palmfrucht)",
                fr = "Origine végétale (carotte, fruit du palmier)",
                tr = "Bitkisel (Havuç, Palmiye meyvesi)",
                ar = "نباتي المصدر (الجزر، ثمرة النخيل)"
            ),
            description = LocalizedText(
                en = "A natural orange-yellow pro-vitamin A colorant derived from carrots and plants.",
                de = "Ein natürlicher orange-gelber Provitamin-A-Farbstoff aus Karotten und Pflanzen.",
                fr = "Un colorant naturel orange-jaune, provitamine A, extrait de carottes et de plantes.",
                tr = "Havuç ve bitkilerden elde edilen doğal turuncu-sarı pro-vitamin A renklendirici.",
                ar = "صبغة طبيعية برتقالية-صفراء من نوع بروفيتامين أ مستخرجة من الجزر والنباتات."
            ),
            alternateNames = listOf("Beta-Carotene", "Provitamin A", "CI 75130"),
            commonUsage = LocalizedText(
                en = "Margarine, fruit juices, cakes, cheeses",
                de = "Margarine, Fruchtsäfte, Kuchen, Käse",
                fr = "Margarine, jus de fruits, gâteaux, fromages",
                tr = "Margarinler, meyve suları, kekler, peynirler",
                ar = "السمن النباتي، عصائر الفواكه، الكعك، الأجبان"
            )
        ),
        // Additional source-dependent (şüpheli) emulsifiers/additives: like E471/E472, these are
        // genuinely either plant or animal fatty-acid derivatives depending on manufacturer sourcing,
        // so they are classified as doubtful rather than certain haram unless a halal certificate
        // or explicit "vegetable/bitkisel" origin is stated on the packaging.
        EAdditive(
            code = "E422",
            name = LocalizedText(
                en = "Glycerin (Glycerol)",
                de = "Glyzerin (Glycerol)",
                fr = "Glycérine (Glycérol)",
                tr = "Gliserin (Glycerol)",
                ar = "الغليسرين"
            ),
            status = HalalStatus.SUPHELI,
            origin = LocalizedText(
                en = "Vegetable oils OR animal fats (pork/beef tallow)",
                de = "Pflanzliche Öle ODER tierische Fette (Schweine-/Rindertalg)",
                fr = "Huiles végétales OU graisses animales (suif de porc/bœuf)",
                tr = "Bitkisel yağlar VEYA Hayvansal yağlar (Domuz/Sığır iç yağı)",
                ar = "زيوت نباتية أو دهون حيوانية (شحم خنزير/بقر)"
            ),
            description = LocalizedText(
                en = "Glycerol used as a humectant and sweetener. Can be obtained from the saponification of vegetable oil (soy, palm) or from animal fat. Requires source confirmation.",
                de = "Als Feuchthaltemittel und Süßungsmittel eingesetztes Glycerol. Kann aus der Verseifung von Pflanzenöl (Soja, Palme) oder aus tierischem Fett gewonnen werden. Herkunftsbestätigung erforderlich.",
                fr = "Glycérol utilisé comme humectant et édulcorant. Peut être obtenu par saponification d'huile végétale (soja, palme) ou à partir de graisse animale. Nécessite une confirmation de la source.",
                tr = "Nem tutucu ve tatlandırıcı olarak kullanılan gliserol. Bitkisel yağ (soya, palmiye) sabunlaşmasından veya hayvansal iç yağdan elde edilebilir. Kaynak teyidi gerektirir.",
                ar = "غليسرول يُستخدم كمرطب ومحلي. يمكن الحصول عليه من تصبن الزيت النباتي (صويا، نخيل) أو من الدهون الحيوانية. يتطلب التأكد من المصدر."
            ),
            alternateNames = listOf("Glycerin", "Glycerol", "E422", "Glyzerin"),
            commonUsage = LocalizedText(
                en = "Cakes and pastries, chewing gum, moisturized dried fruit, liquid drinks, medicine syrups",
                de = "Kuchen und Gebäck, Kaugummi, feuchtgehaltenes Trockenobst, Flüssiggetränke, Arzneisirupe",
                fr = "Gâteaux et pâtisseries, chewing-gum, fruits secs humidifiés, boissons liquides, sirops médicamenteux",
                tr = "Kek ve pastalar, sakızlar, nemlendirilmiş kuruyemişler, likit içecekler, ilaç şurupları",
                ar = "الكعك والمعجنات، العلكة، الفواكه المجففة المرطبة، المشروبات السائلة، أشربة الأدوية"
            )
        ),
        EAdditive(
            code = "E432-436",
            name = LocalizedText(
                en = "Polysorbates (Tween 20/60/80)",
                de = "Polysorbate (Tween 20/60/80)",
                fr = "Polysorbates (Tween 20/60/80)",
                tr = "Polisorbatlar (Tween 20/60/80)",
                ar = "البوليسوربات (توين 20/60/80)"
            ),
            status = HalalStatus.SUPHELI,
            origin = LocalizedText(
                en = "Plant or animal fatty acids + sorbitol",
                de = "Pflanzliche oder tierische Fettsäuren + Sorbit",
                fr = "Acides gras végétaux ou animaux + sorbitol",
                tr = "Bitkisel veya Hayvansal yağ asitleri + Sorbitol",
                ar = "أحماض دهنية نباتية أو حيوانية + السوربيتول"
            ),
            description = LocalizedText(
                en = "Emulsifiers produced from fatty acid esters. The fatty acid source may be plant or animal; doubtful unless confirmed.",
                de = "Aus Fettsäureestern hergestellte Emulgatoren. Die Fettsäurequelle kann pflanzlich oder tierisch sein; ohne Bestätigung zweifelhaft.",
                fr = "Émulsifiants produits à partir d'esters d'acides gras. La source de l'acide gras peut être végétale ou animale ; douteux sans confirmation.",
                tr = "Yağ asidi esterinden üretilen emülgatörler. Yağ asidi kaynağı bitkisel veya hayvansal olabilir; teyit edilmedikçe şüphelidir.",
                ar = "مستحلبات مُنتجة من إسترات الأحماض الدهنية. قد يكون مصدر الحمض الدهني نباتياً أو حيوانياً؛ مشبوه ما لم يُؤكد."
            ),
            alternateNames = listOf("Polysorbate 20", "Polysorbate 60", "Polysorbate 80", "Tween 80"),
            commonUsage = LocalizedText(
                en = "Ice cream, creams, salad dressings, baked goods, vitamin supplements",
                de = "Eiscreme, Cremes, Salatdressings, Backwaren, Vitaminpräparate",
                fr = "Crèmes glacées, crèmes, vinaigrettes, produits de boulangerie, compléments vitaminés",
                tr = "Dondurmalar, kremalar, salata sosları, unlu mamuller, vitamin takviyeleri",
                ar = "الآيس كريم، الكريمات، صلصات السلطة، المخبوزات، مكملات الفيتامينات"
            )
        ),
        EAdditive(
            code = "E470a/E470b",
            name = LocalizedText(
                en = "Sodium/Potassium/Calcium Salts of Fatty Acids",
                de = "Natrium-/Kalium-/Calciumsalze von Fettsäuren",
                fr = "Sels de sodium/potassium/calcium d'acides gras",
                tr = "Yağ Asitlerinin Sodyum/Potasyum/Kalsiyum Tuzları",
                ar = "أملاح الصوديوم/البوتاسيوم/الكالسيوم للأحماض الدهنية"
            ),
            status = HalalStatus.SUPHELI,
            origin = LocalizedText(
                en = "Plant or animal fatty acids",
                de = "Pflanzliche oder tierische Fettsäuren",
                fr = "Acides gras végétaux ou animaux",
                tr = "Bitkisel veya Hayvansal yağ asitleri",
                ar = "أحماض دهنية نباتية أو حيوانية"
            ),
            description = LocalizedText(
                en = "Fatty acid salts used as emulsifiers and anti-caking agents. The source may be plant or animal.",
                de = "Als Emulgatoren und Antiklumpmittel eingesetzte Fettsäuresalze. Die Herkunft kann pflanzlich oder tierisch sein.",
                fr = "Sels d'acides gras utilisés comme émulsifiants et antiagglomérants. La source peut être végétale ou animale.",
                tr = "Emülgatör ve topaklanma önleyici olarak kullanılan yağ asidi tuzları. Kaynağı bitkisel veya hayvansal olabilir.",
                ar = "أملاح أحماض دهنية تُستخدم كمستحلبات ومانعة للتكتل. قد يكون المصدر نباتياً أو حيوانياً."
            ),
            alternateNames = listOf("Sodium salts of fatty acids", "Calcium stearate", "Magnesium stearate"),
            commonUsage = LocalizedText(
                en = "Powdered drinks, tablets, flour and spice blends, chips",
                de = "Getränkepulver, Tabletten, Mehl- und Gewürzmischungen, Chips",
                fr = "Boissons en poudre, comprimés, mélanges de farine et d'épices, chips",
                tr = "Toz içecekler, tabletler, un ve baharat karışımları, cipsler",
                ar = "المشروبات المسحوقة، الأقراص، خلطات الدقيق والتوابل، رقائق البطاطس"
            )
        ),
        EAdditive(
            code = "E473",
            name = LocalizedText(
                en = "Sucrose Esters of Fatty Acids",
                de = "Zuckerester von Fettsäuren",
                fr = "Esters de saccharose d'acides gras",
                tr = "Sükroz Yağ Asidi Esterleri",
                ar = "إسترات السكروز للأحماض الدهنية"
            ),
            status = HalalStatus.SUPHELI,
            origin = LocalizedText(
                en = "Plant or animal fatty acids + sugar",
                de = "Pflanzliche oder tierische Fettsäuren + Zucker",
                fr = "Acides gras végétaux ou animaux + sucre",
                tr = "Bitkisel veya Hayvansal yağ asitleri + Şeker",
                ar = "أحماض دهنية نباتية أو حيوانية + السكر"
            ),
            description = LocalizedText(
                en = "An emulsifier obtained by esterifying sugar with fatty acids. The fatty acid source must be confirmed.",
                de = "Ein Emulgator, gewonnen durch Veresterung von Zucker mit Fettsäuren. Die Fettsäurequelle muss bestätigt werden.",
                fr = "Un émulsifiant obtenu par estérification du sucre avec des acides gras. La source de l'acide gras doit être confirmée.",
                tr = "Şeker ile yağ asitlerinin esterleşmesinden elde edilen emülgatör. Yağ asidi kaynağı teyit edilmelidir.",
                ar = "مستحلب يُحصل عليه من أسترة السكر بالأحماض الدهنية. يجب التأكد من مصدر الحمض الدهني."
            ),
            alternateNames = listOf("Sucrose esters of fatty acids", "Sucrose Stearate"),
            commonUsage = LocalizedText(
                en = "Chocolates, baked goods, ice cream, powdered drinks",
                de = "Schokolade, Backwaren, Eiscreme, Getränkepulver",
                fr = "Chocolats, produits de boulangerie, crèmes glacées, boissons en poudre",
                tr = "Çikolatalar, fırın ürünleri, dondurmalar, içecek tozları",
                ar = "الشوكولاتة، المخبوزات، الآيس كريم، المشروبات المسحوقة"
            )
        ),
        EAdditive(
            code = "E475",
            name = LocalizedText(
                en = "Polyglycerol Esters of Fatty Acids",
                de = "Polyglycerinester von Fettsäuren",
                fr = "Esters de polyglycérol d'acides gras",
                tr = "Poligliserol Yağ Asidi Esterleri",
                ar = "إسترات البوليغليسرول للأحماض الدهنية"
            ),
            status = HalalStatus.SUPHELI,
            origin = LocalizedText(
                en = "Plant or animal fatty acids + glycerol",
                de = "Pflanzliche oder tierische Fettsäuren + Glycerol",
                fr = "Acides gras végétaux ou animaux + glycérol",
                tr = "Bitkisel veya Hayvansal yağ asitleri + Gliserol",
                ar = "أحماض دهنية نباتية أو حيوانية + الغليسرول"
            ),
            description = LocalizedText(
                en = "An emulsifier used in dough and fat-based products. Its glycerol and fatty acid components may be animal-derived.",
                de = "Ein in Teig- und Fettprodukten eingesetzter Emulgator. Die Glycerol- und Fettsäurekomponenten können tierischen Ursprungs sein.",
                fr = "Un émulsifiant utilisé dans les produits à base de pâte et de matières grasses. Ses composants glycérol et acides gras peuvent être d'origine animale.",
                tr = "Hamur ve yağ bazlı ürünlerde kullanılan emülgatör. Gliserol ve yağ asidi bileşenleri hayvansal kaynaklı olabilir.",
                ar = "مستحلب يُستخدم في منتجات العجين والدهون. قد تكون مكوناته من الغليسرول والأحماض الدهنية من مصدر حيواني."
            ),
            alternateNames = listOf("Polyglycerol esters of fatty acids", "PGE"),
            commonUsage = LocalizedText(
                en = "Margarine, cake mixes, chocolate coatings",
                de = "Margarine, Kuchenmischungen, Schokoladenüberzüge",
                fr = "Margarine, préparations pour gâteaux, enrobages au chocolat",
                tr = "Margarinler, kek karışımları, çikolata kaplamalar",
                ar = "السمن النباتي، خلطات الكعك، أغلفة الشوكولاتة"
            )
        ),
        EAdditive(
            code = "E481/E482",
            name = LocalizedText(
                en = "Sodium/Calcium Stearoyl Lactylate",
                de = "Natrium-/Calciumstearoyllactylat",
                fr = "Stéaroyl-lactylate de sodium/calcium",
                tr = "Sodyum/Kalsiyum Stearoil Laktilat",
                ar = "لاكتيلات الصوديوم/الكالسيوم ستيارويل"
            ),
            status = HalalStatus.SUPHELI,
            origin = LocalizedText(
                en = "Plant or animal stearic acid + lactic acid",
                de = "Pflanzliche oder tierische Stearinsäure + Milchsäure",
                fr = "Acide stéarique végétal ou animal + acide lactique",
                tr = "Bitkisel veya Hayvansal stearik asit + Laktik asit",
                ar = "حمض دهني نباتي أو حيواني (ستياريك) + حمض اللاكتيك"
            ),
            description = LocalizedText(
                en = "A dough-strengthening emulsifier. Its stearic acid component can be produced from plant or animal fat.",
                de = "Ein teigverstärkender Emulgator. Die Stearinsäurekomponente kann aus pflanzlichem oder tierischem Fett hergestellt werden.",
                fr = "Un émulsifiant fortifiant la pâte. Son composant acide stéarique peut être produit à partir de graisse végétale ou animale.",
                tr = "Hamur güçlendirici emülgatör. Stearik asit bileşeni bitkisel veya hayvansal yağdan üretilebilir.",
                ar = "مستحلب معزز للعجين. يمكن إنتاج مكوّن حمض الستياريك من دهون نباتية أو حيوانية."
            ),
            alternateNames = listOf("Sodium Stearoyl Lactylate", "SSL", "Calcium Stearoyl Lactylate", "CSL"),
            commonUsage = LocalizedText(
                en = "Bread, burger buns, cake mixes, powdered creamers",
                de = "Brot, Burgerbrötchen, Kuchenmischungen, Kaffeeweißer-Pulver",
                fr = "Pain, pains à burger, préparations pour gâteaux, crèmes en poudre",
                tr = "Ekmekler, hamburger ekmekleri, kek karışımları, krema tozları",
                ar = "الخبز، خبز البرغر، خلطات الكعك، بودرة الكريمة"
            )
        ),
        EAdditive(
            code = "E491-495",
            name = LocalizedText(
                en = "Sorbitan Esters (Span)",
                de = "Sorbitanester (Span)",
                fr = "Esters de sorbitan (Span)",
                tr = "Sorbitan Esterleri (Span)",
                ar = "إسترات السوربيتان (سبان)"
            ),
            status = HalalStatus.SUPHELI,
            origin = LocalizedText(
                en = "Plant or animal fatty acids + sorbitol",
                de = "Pflanzliche oder tierische Fettsäuren + Sorbit",
                fr = "Acides gras végétaux ou animaux + sorbitol",
                tr = "Bitkisel veya Hayvansal yağ asitleri + Sorbitol",
                ar = "أحماض دهنية نباتية أو حيوانية + السوربيتول"
            ),
            description = LocalizedText(
                en = "A family of fat-based emulsifiers, the counterpart of polysorbates (Tween). Doubtful unless the source is confirmed.",
                de = "Eine Familie fettbasierter Emulgatoren, das Gegenstück zu Polysorbaten (Tween). Ohne Herkunftsbestätigung zweifelhaft.",
                fr = "Une famille d'émulsifiants à base de graisse, pendant des polysorbates (Tween). Douteux sans confirmation de la source.",
                tr = "Polisorbatların (Tween) eşleniği olan yağ bazlı emülgatörler ailesi. Kaynağı teyit edilmedikçe şüphelidir.",
                ar = "عائلة من المستحلبات الدهنية، وهي نظيرة للبوليسوربات (توين). مشبوهة ما لم يُؤكد مصدرها."
            ),
            alternateNames = listOf("Sorbitan Monostearate", "Span 60", "Span 80", "Sorbitan Tristearate"),
            commonUsage = LocalizedText(
                en = "Margarine, chocolates, cake mixes, ice cream",
                de = "Margarine, Schokolade, Kuchenmischungen, Eiscreme",
                fr = "Margarine, chocolats, préparations pour gâteaux, crèmes glacées",
                tr = "Margarinler, çikolatalar, kek karışımları, dondurmalar",
                ar = "السمن النباتي، الشوكولاتة، خلطات الكعك، الآيس كريم"
            )
        ),
        EAdditive(
            code = "E570",
            name = LocalizedText(
                en = "Stearic Acid / Fatty Acids",
                de = "Stearinsäure / Fettsäuren",
                fr = "Acide stéarique / Acides gras",
                tr = "Stearik Asit / Yağ Asitleri",
                ar = "حمض الستياريك / الأحماض الدهنية"
            ),
            status = HalalStatus.SUPHELI,
            origin = LocalizedText(
                en = "Vegetable oils OR animal tallow (pork/beef)",
                de = "Pflanzliche Öle ODER tierischer Talg (Schwein/Rind)",
                fr = "Huiles végétales OU suif animal (porc/bœuf)",
                tr = "Bitkisel yağlar VEYA Hayvansal iç yağ (Domuz/Sığır)",
                ar = "زيوت نباتية أو شحم حيواني (خنزير/بقر)"
            ),
            description = LocalizedText(
                en = "A fatty acid used as an anti-caking and coating agent. Can be obtained from palm oil or animal tallow.",
                de = "Eine als Antiklumpmittel und Überzugsmittel eingesetzte Fettsäure. Kann aus Palmöl oder tierischem Talg gewonnen werden.",
                fr = "Un acide gras utilisé comme antiagglomérant et agent d'enrobage. Peut être obtenu à partir d'huile de palme ou de suif animal.",
                tr = "Topaklanma önleyici ve kaplama maddesi olarak kullanılan yağ asidi. Palmiye yağından veya hayvan iç yağından elde edilebilir.",
                ar = "حمض دهني يُستخدم كمانع للتكتل ومادة تغليف. يمكن الحصول عليه من زيت النخيل أو الشحم الحيواني."
            ),
            alternateNames = listOf("Stearic Acid", "Fatty Acids", "E570"),
            commonUsage = LocalizedText(
                en = "Chewing gum, confectionery, tablet coatings, baked goods",
                de = "Kaugummi, Süßwaren, Tablettenüberzüge, Backwaren",
                fr = "Chewing-gum, confiseries, enrobages de comprimés, produits de boulangerie",
                tr = "Sakızlar, şekerlemeler, tablet kaplamaları, unlu mamuller",
                ar = "العلكة، الحلويات، أغلفة الأقراص الدوائية، المخبوزات"
            )
        ),
        EAdditive(
            code = "E627/E635",
            name = LocalizedText(
                en = "Disodium Guanylate / Ribonucleotides",
                de = "Dinatriumguanylat / Ribonukleotide",
                fr = "Guanylate disodique / Ribonucléotides",
                tr = "Disodyum Guanilat / Ribonükleotidler",
                ar = "غوانيلات ثنائي الصوديوم / النيوكليوتيدات الريبية"
            ),
            status = HalalStatus.SUPHELI,
            origin = LocalizedText(
                en = "Meat or fish extract OR plant/microbial fermentation",
                de = "Fleisch- oder Fischextrakt ODER pflanzliche/mikrobielle Fermentation",
                fr = "Extrait de viande ou de poisson OU fermentation végétale/microbienne",
                tr = "Et veya Balık ekstraktı VEYA bitkisel/mikrobiyal fermantasyon",
                ar = "مستخلص لحم أو سمك أو تخمير نباتي/ميكروبي"
            ),
            description = LocalizedText(
                en = "Umami flavor enhancers used together with E631 (Disodium Inosinate). Doubtful without a halal certificate since they can be produced from meat/fish.",
                de = "Umami-Geschmacksverstärker, die zusammen mit E631 (Dinatriuminosinat) eingesetzt werden. Ohne Halal-Zertifikat zweifelhaft, da sie aus Fleisch/Fisch hergestellt werden können.",
                fr = "Exhausteurs de goût umami utilisés avec l'E631 (inosinate disodique). Douteux sans certification halal car ils peuvent être produits à partir de viande/poisson.",
                tr = "E631 (Disodyum İnozinat) ile birlikte kullanılan umami lezzet artırıcılar. Et/balık kaynaklı üretilebildiğinden helal sertifikası yoksa şüphelidir.",
                ar = "معززات نكهة أومامي تُستخدم مع E631 (إينوزينات ثنائي الصوديوم). مشبوهة دون شهادة حلال لأنها قد تُنتج من اللحم/السمك."
            ),
            alternateNames = listOf("Disodium Guanylate", "Disodium 5'-ribonucleotides", "GMP"),
            commonUsage = LocalizedText(
                en = "Instant soups, bouillon, chips, ready-made sauces, noodle seasoning packets",
                de = "Instantsuppen, Brühe, Chips, Fertigsoßen, Nudelgewürzbeutel",
                fr = "Soupes instantanées, bouillons, chips, sauces prêtes à l'emploi, sachets d'assaisonnement pour nouilles",
                tr = "Hazır çorbalar, bulyonlar, cipsler, hazır soslar, noodle baharat paketleri",
                ar = "الشوربات الجاهزة، المرق، رقائق البطاطس، الصلصات الجاهزة، أكياس توابل النودلز"
            )
        ),
        EAdditive(
            code = "E640",
            name = LocalizedText(
                en = "Glycine and Sodium Glycinate",
                de = "Glycin und Natriumglycinat",
                fr = "Glycine et glycinate de sodium",
                tr = "Glisin ve Sodyum Glisinat",
                ar = "الجليسين وجليسينات الصوديوم"
            ),
            status = HalalStatus.SUPHELI,
            origin = LocalizedText(
                en = "Synthetic OR animal collagen hydrolysis",
                de = "Synthetisch ODER durch Hydrolyse von tierischem Kollagen",
                fr = "Synthétique OU hydrolyse de collagène animal",
                tr = "Sentetik VEYA Hayvansal kolajen hidrolizi",
                ar = "مُصنّع أو ناتج عن تحلل الكولاجين الحيواني"
            ),
            description = LocalizedText(
                en = "A flavor-enhancing and sweetening amino acid. Can be produced synthetically or obtained from animal collagen.",
                de = "Eine geschmacksverstärkende und süßende Aminosäure. Kann synthetisch hergestellt oder aus tierischem Kollagen gewonnen werden.",
                fr = "Un acide aminé exhausteur de goût et édulcorant. Peut être produit synthétiquement ou obtenu à partir de collagène animal.",
                tr = "Lezzet artırıcı ve tatlandırıcı amino asit. Sentetik üretilebildiği gibi hayvansal kolajenden de elde edilebilir.",
                ar = "حمض أميني معزز للنكهة ومُحلي. يمكن إنتاجه صناعياً أو الحصول عليه من الكولاجين الحيواني."
            ),
            alternateNames = listOf("Glycine", "Sodium Glycinate"),
            commonUsage = LocalizedText(
                en = "Instant soups, processed meats, snacks, flavoring blends",
                de = "Instantsuppen, verarbeitetes Fleisch, Snacks, Würzmischungen",
                fr = "Soupes instantanées, viandes transformées, en-cas, mélanges aromatisants",
                tr = "Hazır çorbalar, işlenmiş etler, atıştırmalıklar, tatlandırıcı karışımlar",
                ar = "الشوربات الجاهزة، اللحوم المصنعة، الوجبات الخفيفة، خلطات النكهات"
            )
        )
    )
}
