package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.AppStrings
import com.example.ui.theme.NaturalTextDark
import com.example.ui.theme.NaturalTextMuted
import com.example.ui.theme.NaturalWarmBg
import com.example.ui.theme.NaturalWarmBorder
import com.example.ui.theme.NaturalWarmSurface

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HowItWorksDialog(language: AppLanguage, onDismiss: () -> Unit) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = NaturalWarmBg,
        dragHandle = null,
        modifier = Modifier.testTag("how_it_works_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 36.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Text(
                    text = when (language) {
                        AppLanguage.EN -> "How Does This Work?"
                        AppLanguage.DE -> "Wie funktioniert das?"
                        AppLanguage.FR -> "Comment ça marche ?"
                        AppLanguage.TR -> "Nasıl Çalışıyor?"
                        AppLanguage.AR -> "كيف يعمل هذا؟"
                    },
                    color = NaturalTextDark,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.align(Alignment.CenterStart)
                )
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .testTag("close_how_it_works_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = AppStrings.getClose(language),
                        tint = NaturalTextDark
                    )
                }
            }

            Column(
                modifier = Modifier.padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                HowItWorksSection(
                    title = when (language) {
                        AppLanguage.EN -> "Where the data comes from"
                        AppLanguage.DE -> "Datenquelle"
                        AppLanguage.FR -> "Source des données"
                        AppLanguage.TR -> "Veri Kaynağı"
                        AppLanguage.AR -> "مصدر البيانات"
                    },
                    body = when (language) {
                        AppLanguage.EN -> "Every scan looks up the barcode in Open Food Facts, a free, community-maintained global food database. The result is only as complete as the data that community has entered for that specific product."
                        AppLanguage.DE -> "Jeder Scan sucht den Barcode in Open Food Facts - einer kostenlosen, von der Community gepflegten globalen Lebensmitteldatenbank. Das Ergebnis ist nur so vollständig wie die Daten, die die Community für dieses Produkt eingetragen hat."
                        AppLanguage.FR -> "Chaque scan recherche le code-barres dans Open Food Facts, une base de données alimentaire mondiale gratuite et alimentée par la communauté. Le résultat n'est complet que dans la mesure des données saisies par cette communauté pour ce produit."
                        AppLanguage.TR -> "Her tarama, barkodu Open Food Facts'te arar — dünya çapında topluluk tarafından güncellenen, ücretsiz bir gıda veritabanı. Sonuç, o ürün için bu topluluğun girdiği veri kadar eksiksizdir."
                        AppLanguage.AR -> "يبحث كل مسح عن الباركود في Open Food Facts، وهي قاعدة بيانات غذائية عالمية مجانية يديرها المجتمع. النتيجة لا تكون كاملة إلا بقدر البيانات التي أدخلها المجتمع لهذا المنتج."
                    }
                )

                HowItWorksSection(
                    title = when (language) {
                        AppLanguage.EN -> "Three-tier verdict"
                        AppLanguage.DE -> "Dreistufiges Ergebnis"
                        AppLanguage.FR -> "Résultat à trois niveaux"
                        AppLanguage.TR -> "Üç Seviyeli Sonuç"
                        AppLanguage.AR -> "نتيجة ثلاثية المستويات"
                    },
                    body = when (language) {
                        AppLanguage.EN -> "Haram (Prohibited): the ingredient list contains a substance considered definitively forbidden - pork/lard, alcohol, E120 carmine (insect), E441 pork gelatin, blood products, etc.\n\nDoubtful / Mushbooh: an ingredient's source cannot be confirmed - gelatin, collagen, E471/E472 emulsifiers, animal rennet, whey, or meat/poultry with no halal claim on file. These are not automatically forbidden, but require verification.\n\nHalal: if the product carries an explicit halal or vegan/plant-based label in Open Food Facts's data, the result shows \"Carries a Halal Label (per product data)\". If no prohibited or doubtful ingredient was found AND no such label exists, the result is still shown as Halal, but labeled \"automated screening, not a halal certification\" - the absence of a red flag is not the same as a certification."
                        AppLanguage.DE -> "Haram (Verboten): Die Zutatenliste enthält einen eindeutig verbotenen Stoff - Schweinefleisch/-fett, Alkohol, E120 Karmin (Insekt), E441 Schweinegelatine, Blutprodukte usw.\n\nZweifelhaft / Mushbooh: Die Herkunft eines Inhaltsstoffs ist nicht bestätigt - Gelatine, Kollagen, E471/E472-Emulgatoren, tierisches Lab, Molkepulver oder Fleisch/Geflügel ohne Halal-Angabe. Diese sind nicht automatisch verboten, erfordern aber eine Prüfung.\n\nHalal: Trägt das Produkt laut Open Food Facts ein ausdrückliches Halal- oder Vegan-/pflanzliches Siegel, zeigt das Ergebnis \"Trägt ein Halal-Siegel (laut Produktdaten)\". Wurde nichts Verbotenes/Zweifelhaftes gefunden UND existiert kein solches Siegel, wird das Ergebnis dennoch als Halal angezeigt, aber mit dem Hinweis \"automatische Prüfung, keine Halal-Zertifizierung\" - das Fehlen einer roten Flagge ist keine Zertifizierung."
                        AppLanguage.FR -> "Haram (Interdit) : la liste des ingrédients contient une substance considérée comme formellement interdite - porc/lard, alcool, E120 carmin (insecte), E441 gélatine de porc, produits sanguins, etc.\n\nDouteux / Mushbooh : la source d'un ingrédient ne peut être confirmée - gélatine, collagène, émulsifiants E471/E472, présure animale, poudre de lactosérum, ou viande/volaille sans mention halal. Ceux-ci ne sont pas automatiquement interdits, mais nécessitent une vérification.\n\nHalal : si le produit porte une mention halal ou végane/végétale explicite dans les données Open Food Facts, le résultat affiche \"Porte un label halal (selon les données du produit)\". Si aucun ingrédient interdit ou douteux n'a été trouvé ET qu'aucune mention de ce type n'existe, le résultat reste Halal, mais avec la mention \"contrôle automatique, pas une certification halal\" - l'absence de signal rouge n'équivaut pas à une certification."
                        AppLanguage.TR -> "Haram (Kesin Yasak): İçindekiler listesinde dinen kesin yasak kabul edilen bir madde varsa — domuz/domuz yağı, alkol, E120 karmin (böcek), E441 domuz jelatini, kan ürünleri vb.\n\nŞüpheli / Mushbooh: Bir maddenin kaynağı doğrulanamıyorsa — jelatin, kolajen, E471/E472 emülgatörler, hayvansal maya (rennet), peynir altı suyu tozu ya da helal kesim bilgisi olmayan et/kümes hayvanı. Bunlar otomatik olarak yasak değildir, ama teyit gerektirir.\n\nHelal: Üründe Open Food Facts verisinde açık bir helal veya vegan/bitkisel etiketi varsa, sonuç \"Helal İşareti Taşıyor (ürün verisine göre)\" şeklinde gösterilir. Hiçbir yasak/şüpheli madde bulunamadıysa VE böyle bir etiket de yoksa, sonuç yine Helal gösterilir ama \"otomatik tarama, helal sertifikası değil\" notuyla — kırmızı bir işaretin olmaması, sertifika anlamına gelmez."
                        AppLanguage.AR -> "حرام (محظور): تحتوي قائمة المكونات على مادة تُعتبر محظورة قطعياً - لحم/دهن الخنزير، الكحول، E120 كارمين (حشرة)، E441 جيلاتين الخنزير، منتجات الدم، إلخ.\n\nمشبوه: لا يمكن تأكيد مصدر أحد المكونات - الجيلاتين، الكولاجين، مستحلبات E471/E472، منفحة حيوانية، مسحوق مصل اللبن، أو اللحم/الدواجن دون إشارة حلال. هذه ليست محظورة تلقائياً، لكنها تتطلب التحقق.\n\nحلال: إذا كان المنتج يحمل علامة حلال أو نباتية صريحة في بيانات Open Food Facts، تُعرض النتيجة كـ \"يحمل علامة حلال (وفق بيانات المنتج)\". إذا لم يُعثر على أي مكون محظور أو مشبوه ولم توجد علامة كهذه، تظل النتيجة حلال لكن مع ملاحظة \"فحص آلي، وليس شهادة حلال\" - عدم وجود إشارة حمراء لا يعني وجود شهادة."
                    }
                )

                HowItWorksSection(
                    title = when (language) {
                        AppLanguage.EN -> "Known limitations"
                        AppLanguage.DE -> "Bekannte Einschränkungen"
                        AppLanguage.FR -> "Limites connues"
                        AppLanguage.TR -> "Bilinen Sınırlamalar"
                        AppLanguage.AR -> "القيود المعروفة"
                    },
                    body = when (language) {
                        AppLanguage.EN -> "This is not a religious ruling (fatwa) or an official halal certification - it is an automated first pass.\n\nDetection relies on a fixed set of known ingredient names and phrasings, in several languages. A rare term, a language not covered, or missing/incorrect data in Open Food Facts can be missed.\n\nWhen in doubt, check the packaging yourself or contact the manufacturer."
                        AppLanguage.DE -> "Dies ist kein religiöses Urteil (Fatwa) und keine offizielle Halal-Zertifizierung - es ist eine automatisierte Erstprüfung.\n\nDie Erkennung basiert auf einer festen Liste bekannter Zutatennamen in mehreren Sprachen. Ein seltener Begriff, eine nicht abgedeckte Sprache oder fehlende/falsche Daten bei Open Food Facts können übersehen werden.\n\nIm Zweifelsfall prüfen Sie die Verpackung selbst oder wenden Sie sich an den Hersteller."
                        AppLanguage.FR -> "Ceci n'est ni une fatwa ni une certification halal officielle - il s'agit d'un premier contrôle automatisé.\n\nLa détection repose sur une liste fixe de noms d'ingrédients connus, en plusieurs langues. Un terme rare, une langue non couverte, ou des données manquantes/erronées sur Open Food Facts peuvent être manqués.\n\nEn cas de doute, vérifiez l'emballage vous-même ou contactez le fabricant."
                        AppLanguage.TR -> "Bu bir fetva ya da resmi helal sertifikası değildir — otomatik bir ön taramadır.\n\nTespit, birkaç dilde önceden tanımlanmış bilinen madde isimlerine/ifadelerine dayanır. Nadir bir terim, kapsanmayan bir dil, ya da Open Food Facts'teki eksik/hatalı veri kaçabilir.\n\nŞüpheli durumlarda ambalajı kendiniz kontrol edin ya da üreticiyle iletişime geçin."
                        AppLanguage.AR -> "هذا ليس فتوى دينية ولا شهادة حلال رسمية - إنه فحص أولي آلي.\n\nيعتمد الكشف على قائمة ثابتة من أسماء المكونات المعروفة بعدة لغات. قد يفوتنا مصطلح نادر، أو لغة غير مغطاة، أو بيانات ناقصة/خاطئة في Open Food Facts.\n\nعند الشك، يرجى فحص الغلاف بنفسك أو التواصل مع الشركة المصنعة."
                    }
                )
            }
        }
    }
}

@Composable
private fun HowItWorksSection(title: String, body: String) {
    Surface(
        color = NaturalWarmSurface,
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, NaturalWarmBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = NaturalTextDark
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = body,
                fontSize = 13.sp,
                color = NaturalTextMuted,
                lineHeight = 19.sp
            )
        }
    }
}
