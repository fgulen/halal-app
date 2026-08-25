package com.example.data.local

import com.example.data.model.EAdditive
import com.example.data.model.HalalStatus

object InitialData {
    val sampleProducts = listOf(
        // 1. HARAM - EU: Haribo Goldbären (Pork Gelatin)
        ProductEntity(
            barcode = "4001686301265",
            name = "Haribo Goldbären (Original Germany)",
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
            imageUrl = null
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
            imageUrl = null
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
            imageUrl = null
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
            imageUrl = null
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
                "Raising agents (Potassium hydrogen carbonate, Ammonium hydrogen carbonate, Sodium hydrogen carbonate)",
                "Salt",
                "Emulsifier (Soya lecithin E322)",
                "Flavoring (Vanillin)"
            ),
            reasonOrDetails = "Oreo cookies use 100% plant-based vegetable fats (Palm oil) and plant soya lecithin. Free of animal fats, lard, pork gelatin, or alcohol.",
            alternatives = emptyList(),
            imageUrl = null
        ),

        // 6. HELAL - EU/Global: Skittles Fruits
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
            imageUrl = null
        ),

        // 7. HELAL - EU/USA: Nutella Hazelnut Spread
        ProductEntity(
            barcode = "3017620422003",
            name = "Nutella Hazelnut Cocoa Spread",
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
            imageUrl = null
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
            imageUrl = null
        )
    )

    val eAdditivesDirectory = listOf(
        EAdditive(
            code = "E441",
            name = "Gelatin / Schweinegelatine",
            status = HalalStatus.HARAM,
            origin = "Animal (Commonly Pork skin/bones in EU & USA)",
            description = "Gelling agent widely used in gummy bears, marshmallows, desserts, and medicine capsules. In Europe and the Americas, standard gelatin is predominantly derived from pig slaughter unless explicitly certified Bovine/Fish Halal.",
            commonUsage = "Gummy bears, marshmallows, jellies, yogurts, capsule medications"
        ),
        EAdditive(
            code = "E120",
            name = "Carmine / Cochineal / Karmin",
            status = HalalStatus.HARAM,
            origin = "Insect (Crushed female scale insects - Dactylopius coccus)",
            description = "Natural bright red pigment extracted from dried cochineal bugs. Prohibited in Islamic dietary law across major fiqh councils due to insect consumption prohibition.",
            commonUsage = "Red candies, strawberry yogurt, sausages, fruit juices, red icing, cosmetics"
        ),
        EAdditive(
            code = "E471",
            name = "Mono- and Diglycerides of Fatty Acids",
            status = HalalStatus.SUPHELI,
            origin = "Plant oils (Palm/Soy) OR Animal fats (Pork/Beef lard)",
            description = "Common emulsifier binding fat and water. Permissible if 100% plant-based (Vegetable origin), but doubtful if origin is unspecified on EU/US packaging.",
            commonUsage = "Breads, cakes, chocolate, ice cream, margarine, chips, packaged snacks"
        ),
        EAdditive(
            code = "E472a-f",
            name = "Esters of Mono- and Diglycerides / DATEM",
            status = HalalStatus.SUPHELI,
            origin = "Plant or Animal fatty acids",
            description = "Derivative of E471 used in industrial baking and bakery doughs. Unverified sources pose risk of animal fat derivation.",
            commonUsage = "Toast bread, croissants, frozen doughs, mayonnaise, dessert mixes"
        ),
        EAdditive(
            code = "E904",
            name = "Shellac / Confectioner's Glaze",
            status = HalalStatus.SUPHELI,
            origin = "Insect Secretion (Lac bug - Kerria lacca)",
            description = "Resinous glaze providing shine to confectionery and fruit coating. Regarded as doubtful or restricted under several global Halal standards.",
            commonUsage = "Glazed pills, shiny jelly beans, chocolate-coated dragees, fruit wax"
        ),
        EAdditive(
            code = "E920",
            name = "L-Cysteine",
            status = HalalStatus.SUPHELI,
            origin = "Duck feathers, human hair, pig bristles, or microbial synthesis",
            description = "Dough conditioner used to accelerate industrial bread kneading. Must be verified as synthetic or microbial fermentation.",
            commonUsage = "Commercial sandwich breads, burger buns, pizza crusts, tortillas"
        ),
        EAdditive(
            code = "E631",
            name = "Disodium Inosinate",
            status = HalalStatus.SUPHELI,
            origin = "Meat extract (Pork/Beef) or Microbial tapioca fermentation",
            description = "Savory flavor enhancer (Umami). Often prepared from non-halal meat tissues unless certified plant/microbial source.",
            commonUsage = "Flavored potato chips, instant noodles, dry soup mixes, bouillon cubes"
        ),
        EAdditive(
            code = "E542",
            name = "Bone Phosphate",
            status = HalalStatus.HARAM,
            origin = "Animal bones (Cattle / Swine)",
            description = "Anti-caking agent derived from defatted animal bones. Prohibited without verified halal slaughter.",
            commonUsage = "Dietary supplements, anti-caking food powders"
        ),
        EAdditive(
            code = "E100",
            name = "Curcumin (Turmeric Yellow)",
            status = HalalStatus.HELAL,
            origin = "Plant (Natural Turmeric root / Curcuma longa)",
            description = "Safe and 100% natural yellow food coloring extracted from turmeric root.",
            commonUsage = "Mustard, curry, cheeses, pastries, beverages, soups"
        ),
        EAdditive(
            code = "E322",
            name = "Lecithin (Soy / Sunflower)",
            status = HalalStatus.HELAL,
            origin = "Plant (Soybeans or Sunflower seeds)",
            description = "Natural plant emulsifier widely used in chocolates and spreads. Safe and Halal.",
            commonUsage = "Chocolate bars, Nutella, cookies, infant formula"
        ),
        EAdditive(
            code = "E330",
            name = "Citric Acid",
            status = HalalStatus.HELAL,
            origin = "Plant / Sugar fermentation (Citrus fruits)",
            description = "Safe and widely used acidity regulator and preservative. 100% Halal.",
            commonUsage = "Soft drinks, jams, canned foods, gummy sweets"
        ),
        EAdditive(
            code = "E407",
            name = "Carrageenan",
            status = HalalStatus.HELAL,
            origin = "Plant (Red Seaweed / Algae)",
            description = "Seaweed-derived gelling agent. Popular 100% plant-based alternative to animal gelatin.",
            commonUsage = "Plant milks, puddings, vegan jellies, ice creams"
        ),
        EAdditive(
            code = "E412",
            name = "Guar Gum",
            status = HalalStatus.HELAL,
            origin = "Plant (Guar bean seeds)",
            description = "Natural thickening agent from guar plant seeds. Completely Halal.",
            commonUsage = "Salad dressings, ice cream, sauces, gluten-free bakery"
        ),
        EAdditive(
            code = "E415",
            name = "Xanthan Gum",
            status = HalalStatus.HELAL,
            origin = "Bacterial fermentation of glucose",
            description = "Safe stabilizer and thickener produced by fermentation. 100% Halal.",
            commonUsage = "Gluten-free baking, sauces, syrups, toothpaste"
        ),
        EAdditive(
            code = "E162",
            name = "Beetroot Red (Betanin)",
            status = HalalStatus.HELAL,
            origin = "Plant (Red Beetroot / Beta vulgaris)",
            description = "Natural and Halal red vegetable coloring used as a clean alternative to insect Carmine (E120).",
            commonUsage = "Strawberry ice cream, fruit yogurts, red candies, beverages"
        ),
        EAdditive(
            code = "E160a",
            name = "Beta-Carotene",
            status = HalalStatus.HELAL,
            origin = "Plant (Carrots, Palm fruit)",
            description = "Natural orange-yellow provitamin A coloring derived from carrots and plant sources.",
            commonUsage = "Margarine, fruit drinks, bakery, cheese"
        )
    )
}
