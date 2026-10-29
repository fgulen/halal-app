# Play Store Sürüm Notları

Her yeni sürümde en üste yeni bir bölüm eklenir. Play Console'daki "Sürüm
notları" alanları için kullanılan diller: `en-US`, `ar`, `de-DE`, `fr-FR`,
`tr-TR` (uygulamanın kendi 5 dilinden biraz farklı kodlar — Play Console
bunları böyle bekliyor). Her alan ~500 karakter sınırlıdır.

---

## versionCode 18 / versionName 2.4 (2026-10-29)

Sonuç ekranına sağlık göstergesi eklendi: ürünün Nutri-Score (A–E) ve NOVA (1–4) değeri, kırmızıdan yeşile bir ibre ve sade cümlelerle gösterilir. Bu bilgi helal kararını değiştirmez; veri Open Food Facts'ten gelir ve değeri olmayan ürünlerde kart görünmez. Tarama geçmişi ve ürün önbelleği korunur (DB v7); eski önbellekteki ürünler internet varken bir kez yeniden alınarak gösterge eklenir. Bu sürümden önceki taramalarda gösterge yoktur.

<tr-TR>
Sonuç ekranına sağlık göstergesi eklendi. Ürünün Nutri-Score (A–E) ve NOVA (1–4) değeri ibre üzerinde, sade cümlelerle gösterilir. Helal kararı değişmez. Eski taramalarda göstergeyi görmek için ürünü yeniden tarayın.
</tr-TR>

<en-US>
The result screen now has a health gauge. It shows the product's Nutri-Score (A–E) and NOVA group (1–4) on a red-to-green needle, with plain-language explanations. The halal verdict does not change. To see the gauge on older scans, scan the product again.
</en-US>

<de-DE>
Der Ergebnisbildschirm zeigt jetzt eine Gesundheitsanzeige. Nutri-Score (A–E) und NOVA-Gruppe (1–4) des Produkts werden auf einer Nadel von Rot bis Grün mit einfachen Erklärungen angezeigt. Das Halal-Urteil ändert sich nicht. Um die Anzeige bei älteren Scans zu sehen, scannen Sie das Produkt erneut.
</de-DE>

<fr-FR>
L'écran de résultat affiche désormais une jauge santé. Le Nutri-Score (A–E) et le groupe NOVA (1–4) du produit apparaissent sur une aiguille du rouge au vert, avec des explications simples. Le verdict halal ne change pas. Pour voir la jauge sur les anciens scans, rescannez le produit.
</fr-FR>

<ar>
تعرض شاشة النتيجة الآن مؤشراً صحياً. يظهر Nutri-Score (من A إلى E) ومجموعة NOVA (من 1 إلى 4) للمنتج على إبرة تتدرج من الأحمر إلى الأخضر مع شرح بسيط. لا يتغير حكم الحلال. لرؤية المؤشر في عمليات المسح السابقة، امسح المنتج مرة أخرى.
</ar>

---

## versionCode 16 / versionName 2.3 (2026-10-22)

Sonuç kartlarındaki madde adı, açıklama ve kaynak metinleri artık Almanca, Fransızca ve Arapçada kendi dilinde (önceden İngilizce kalıyordu); kaynak bilgisi Türkçede de çevrildi. Türkçede domuz jelatini kartının yanına çelişkili "sığır/balık jelatini" şüpheli kartı eklenmesi düzeltildi. Fransızca "Total Scans" çevrildi, arama kutusundaki marka örnekleri kaldırıldı. Ürün önbelleği bir kez temizlenir (DB v6), tarama geçmişi korunur.

<tr-TR>
Sonuç ekranındaki içerik kartları artık tamamen seçtiğiniz dilde. Jelatinli bazı ürünlerde görünen çelişkili bir uyarı kaldırıldı ve küçük çeviri hataları düzeltildi.
</tr-TR>

<en-US>
Ingredient cards on the result screen are now fully translated into German, French and Arabic. A contradictory gelatin warning on some products has been removed, and small translation issues have been fixed.
</en-US>

<de-DE>
Die Zutatenkarten im Ergebnis werden jetzt vollständig auf Deutsch angezeigt (zuvor teilweise Englisch). Ein widersprüchlicher Gelatine-Hinweis bei einigen Produkten wurde entfernt und kleine Übersetzungsfehler wurden behoben.
</de-DE>

<fr-FR>
Les fiches d'ingrédients du résultat sont désormais entièrement en français (auparavant en partie en anglais). Un avertissement contradictoire sur la gélatine a été supprimé et de petites erreurs de traduction ont été corrigées.
</fr-FR>

<ar>
أصبحت بطاقات المكونات في شاشة النتيجة معروضة بالكامل باللغة العربية بعد أن كانت تظهر جزئياً بالإنجليزية. تمت إزالة تنبيه متناقض حول الجيلاتين في بعض المنتجات، وإصلاح أخطاء ترجمة بسيطة.
</ar>

---

## versionCode 15 / versionName 2.2 (2026-10-22)

Uygulama içi değerlendirme isteği (3. ve 20. başarılı taramadan sonra, sonuç kapatılınca; Google kotası belirler). Uygulama içi güncelleme akışının iki kez başlatılmasından doğan çökme giderildi (Play vitals, 2.0). Fransızca tarama geçmişi başlığında "Effacer" düğmesinin harf harf alt alta dizilmesi düzeltildi.

<tr-TR>
Birkaç taramadan sonra, isterseniz uygulamayı Google Play'de doğrudan uygulama içinden değerlendirebilirsiniz. Güncelleme sırasında nadiren oluşan bir çökme giderildi ve tarama geçmişi başlığı uzun dillerde artık düzgün görünüyor.
</tr-TR>

<en-US>
After a few scans you can now rate the app on Google Play right from inside the app, if you like. A rare crash during app updates has been fixed, and the scan history header now displays correctly in longer languages.
</en-US>

<de-DE>
Nach einigen Scans können Sie die App jetzt, wenn Sie möchten, direkt in der App bei Google Play bewerten. Ein seltener Absturz beim App-Update wurde behoben, und die Überschrift des Scan-Verlaufs wird jetzt auch in längeren Sprachen korrekt angezeigt.
</de-DE>

<fr-FR>
Après quelques scans, vous pouvez désormais noter l'application sur Google Play directement depuis l'application, si vous le souhaitez. Un plantage rare lors des mises à jour a été corrigé, et l'en-tête de l'historique des scans s'affiche désormais correctement en français.
</fr-FR>

<ar>
بعد بضع عمليات مسح، يمكنك الآن تقييم التطبيق على Google Play مباشرةً من داخل التطبيق إن رغبت. تم إصلاح عطل نادر كان يحدث أثناء تحديث التطبيق، وأصبح عنوان سجل المسح يظهر بشكل صحيح.
</ar>

---

## versionCode 14 / versionName 2.1 (2026-10-15)

versionCode 13 incelemeye gönderildi ama YAYINLANMADI: yerine 14 yüklendi (v13'te Almanca bileşik kelimelerdeki peynir altı suyu yanlış HELAL çıkıyordu; v13 yayınlansaydı bu sonuçlar önbellekte kalırdı). Notlar bu yüzden v13'ün içeriğini de kapsar. Ek olarak: Almanca bileşik kelimeler (Süßmolkenpulver, Schweineschmalz, Rindergelatine, Eierlikör) ve Arapça içerik metni artık eşleşiyor.

<tr-TR>
Yeni: Ana ekrandaki "Mezhep" düğmesinden mezhebinizi seçin; Hanefi seçildiğinde peynir mayası veya peynir altı suyu içeren ürünler kaynaklı açıklamayla değerlendirilir. Almanca ve Arapça içerik listeleri artık daha doğru okunuyor. "Domuz yağı içermez" gibi üretici uyarıları artık yanlışlıkla Haram sonucu vermiyor. Büyük harfli Türkçe etiketler doğru tanınıyor; açılır pencereler daha belirgin, yazarken oluşabilen bir çökme giderildi.
</tr-TR>

<en-US>
New: choose your madhhab with the "Madhhab" button on the home screen; with Hanafi selected, products with rennet or whey get a sourced explanation. German and Arabic ingredient lists are now read more accurately (e.g. whey in German compound words). Manufacturer statements like "contains no pork fat" no longer cause a false Haram. Clearer pop-ups, and a crash that could occur while typing has been fixed.
</en-US>

<de-DE>
Neu: Wählen Sie Ihre Rechtsschule über die Schaltfläche "Madhhab" auf dem Startbildschirm; mit Hanafi erhalten Produkte mit Lab oder Molke eine belegte Erklärung. Deutsche und arabische Zutatenlisten werden jetzt genauer erkannt (z. B. Molke in Wörtern wie Süßmolkenpulver). Herstellerhinweise wie "enthält kein Schweinefett" führen nicht mehr fälschlich zu Haram. Klarere Pop-ups, und ein Absturz beim Tippen wurde behoben.
</de-DE>

<fr-FR>
Nouveau : choisissez votre madhhab via le bouton « Madhhab » de l'écran d'accueil ; avec Hanafi, les produits contenant de la présure ou du lactosérum reçoivent une explication sourcée. Les listes d'ingrédients en allemand et en arabe sont mieux reconnues. Les mentions comme « sans graisse de porc » ne donnent plus un faux Haram. Fenêtres plus lisibles et un plantage lors de la saisie corrigé.
</fr-FR>

<ar>
جديد: اختر مذهبك من زر "المذهب" في الشاشة الرئيسية؛ عند اختيار المذهب الحنفي تحصل المنتجات التي تحتوي على الإنفحة أو مصل اللبن على شرح موثّق. أصبحت قوائم المكونات بالألمانية والعربية تُقرأ بدقة أكبر. لم تعد عبارات مثل "لا يحتوي على دهن الخنزير" تؤدي إلى حكم حرام خاطئ. نوافذ منبثقة أوضح، وتم إصلاح عطل كان قد يحدث أثناء الكتابة.
</ar>

---

## versionCode 13 / versionName 2.1 (2026-10-15)

Mezhep seçimi (Hanefi'de peynir mayası/peynir altı suyu kaynaklı notla helal), Türk ambalajlarındaki "domuz yağı içermez" uyarılarının yanlış Haram vermesi düzeltildi, büyük harfli Türkçe etiket eşleşmesi düzeltildi, Open Food Facts sunucu hatası artık "ürün bulunamadı" gibi gösterilmiyor. Teknik: Compose BOM 2026.06.01 (Samsung/Android 15'te yazı alanı çökmesi giderildi), R8 küçültme açıldı (Play vitals DEX uyarısı), açılır pencerelere kenarlık eklendi.

<tr-TR>
Yeni: Mezhep seçimi! Ana ekrandaki "Mezhep" düğmesinden mezhebinizi seçebilirsiniz; Hanefi seçildiğinde peynir mayası ve peynir altı suyu içeren ürünler kaynaklı açıklamayla değerlendirilir. "Ürünlerimizde domuz yağı yoktur" gibi üretici uyarıları artık ürünü yanlışlıkla Haram yapmıyor. Büyük harfli Türkçe etiketler (ör. JELATİN) artık doğru tanınıyor. Açılır pencereler daha belirgin, yazı yazarken oluşabilen bir çökme giderildi.
</tr-TR>

<en-US>
New: choose your madhhab! Use the "Madhhab" button on the home screen; with Hanafi selected, products with rennet or whey are assessed with a sourced explanation. Manufacturer statements such as "contains no pork fat" no longer make a product Haram by mistake. All-caps Turkish labels are now recognized correctly. Pop-up windows stand out more clearly, and a crash that could occur while typing has been fixed.
</en-US>

<de-DE>
Neu: Wählen Sie Ihre Rechtsschule (Madhhab)! Über die Schaltfläche auf dem Startbildschirm; mit Hanafi werden Produkte mit Lab oder Molke mit belegter Erklärung bewertet. Herstellerhinweise wie "enthält kein Schweinefett" machen ein Produkt nicht mehr fälschlich Haram. Türkische Etiketten in Großbuchstaben werden jetzt korrekt erkannt. Pop-up-Fenster heben sich deutlicher ab, und ein Absturz, der beim Tippen auftreten konnte, wurde behoben.
</de-DE>

<fr-FR>
Nouveau : choisissez votre madhhab ! Via le bouton sur l'écran d'accueil ; avec Hanafi, les produits contenant de la présure ou du lactosérum sont évalués avec une explication sourcée. Les mentions du fabricant comme « ne contient pas de graisse de porc » ne rendent plus un produit Haram par erreur. Les étiquettes turques en majuscules sont désormais bien reconnues. Les fenêtres contextuelles sont plus visibles et un plantage pouvant survenir lors de la saisie a été corrigé.
</fr-FR>

<ar>
جديد: اختر مذهبك! من زر "المذهب" في الشاشة الرئيسية؛ عند اختيار المذهب الحنفي تُقيَّم المنتجات التي تحتوي على الإنفحة أو مصل اللبن مع شرح موثَّق بالمصادر. لم تعد عبارات الشركات المصنِّعة مثل "لا يحتوي على دهن الخنزير" تجعل المنتج حرامًا عن طريق الخطأ. أصبحت الملصقات التركية المكتوبة بأحرف كبيرة تُقرأ بشكل صحيح. النوافذ المنبثقة أصبحت أوضح، وتم إصلاح عطل كان قد يحدث أثناء الكتابة.
</ar>

---

## versionCode 12 / versionName 2.0 (2026-10-06)

İlk üretim (Production) sürümü. Uygulama ikonu Play Store ikonuyla aynı hale getirildi (önceden telefonda eski kalkan ikonu görünüyordu); alkolsüz bira/şarap ve şarap sirkesi artık yanlışlıkla Haram işaretlenmiyor.

<tr-TR>
Bu sürümde: Uygulama ikonu yenilendi - telefonunuzda artık Play Store'daki ikonun aynısı görünüyor. Alkolsüz bira ve alkolsüz şarap gibi %0,0 alkollü ürünler artık yanlışlıkla Haram olarak işaretlenmiyor. Şarap sirkesi, alkolün tamamen sirkeye dönüşmesi (istihale) nedeniyle mezheplerin çoğunluk görüşüne göre artık Haram sayılmıyor; içindekilerde ayrıca şarap geçiyorsa yine uyarı verilir.
</tr-TR>

<en-US>
This update: a refreshed app icon - your home screen now shows the same icon as the Play Store. Products labeled alcohol-free, such as non-alcoholic beer or wine, are no longer wrongly marked Haram. Wine vinegar is no longer flagged as Haram, following the majority scholarly view that the alcohol fully turns into vinegar (istihala); a separate mention of wine in the ingredients is still flagged.
</en-US>

<de-DE>
Update: Neues App-Symbol - auf Ihrem Startbildschirm erscheint jetzt dasselbe Symbol wie im Play Store. Als alkoholfrei gekennzeichnete Produkte wie alkoholfreies Bier oder alkoholfreier Wein werden nicht mehr fälschlich als Haram markiert. Weinessig gilt nach der Mehrheitsmeinung der Gelehrten (Istihala: der Alkohol wird vollständig zu Essig) nicht mehr als Haram; eine separate Erwähnung von Wein in den Zutaten wird weiterhin markiert.
</de-DE>

<fr-FR>
Mise à jour : nouvelle icône - votre écran d'accueil affiche désormais la même icône que le Play Store. Les produits sans alcool, comme la bière ou le vin sans alcool, ne sont plus marqués Haram par erreur. Le vinaigre de vin n'est plus signalé comme Haram, selon l'avis majoritaire des savants (istihala : l'alcool est entièrement transformé en vinaigre) ; une mention distincte de vin dans les ingrédients reste signalée.
</fr-FR>

<ar>
في هذا الإصدار: أيقونة جديدة للتطبيق - تظهر الآن على شاشتك نفس أيقونة متجر Play. لم تعد المنتجات الخالية من الكحول، مثل البيرة أو النبيذ الخالي من الكحول، تُصنَّف حرامًا عن طريق الخطأ. ولم يعد خل النبيذ يُصنَّف حرامًا وفق رأي جمهور العلماء (الاستحالة: تحوّل الكحول بالكامل إلى خل)؛ ويظل ذكر النبيذ بشكل منفصل في المكونات مُنبَّهًا عليه.
</ar>

---

## versionCode 11 / versionName 1.9 (2026-09-20)

Barkod taramasında, içindekiler bulunmadan erken pes edilmesi düzeltildi (tüm veri kaynakları denenmeden "içerik bulunamadı" gösterilmiyor artık); ana ekrandaki "Toplam Tarama" sayacı artık Helal rengiyle karıştırılmayacak şekilde nötr renkte.

<tr-TR>
Bu sürümde: Barkod taraması artık bir ürünün içindekiler bilgisini bulmadan önce tüm veri kaynaklarını deniyor - önceden ilk bulunan eksik kayıt (sadece isim/fotoğraf, içindekiler yok) erken kabul edilip "içerik bulunamadı" gösterilebiliyordu. Ayrıca ana ekrandaki "Toplam Tarama" sayacı, Helal ürün sayısıyla aynı yeşil renkte gösterildiği için kafa karıştırıyordu - artık nötr bir renkte.
</tr-TR>

<en-US>
This update: barcode scanning now checks every available data source before giving up on a product's ingredients - previously the first incomplete match (name/photo only, no ingredients) could be accepted too early, showing "ingredients not found." Also fixed: the "Total Scans" counter on the home screen used the same green as the Halal count, which was misleading - it's now a neutral color.
</en-US>

<de-DE>
Update: Die Barcode-Suche prüft jetzt alle verfügbaren Datenquellen, bevor sie bei den Zutaten eines Produkts aufgibt - zuvor konnte ein unvollständiger erster Treffer (nur Name/Foto, keine Zutaten) zu früh akzeptiert werden und "Zutaten nicht gefunden" anzeigen. Außerdem behoben: Der Zähler "Gesamt gescannt" auf dem Startbildschirm hatte dasselbe Grün wie die Halal-Anzahl, was irreführend war - jetzt in einer neutralen Farbe.
</de-DE>

<fr-FR>
Mise à jour : la recherche par code-barres vérifie désormais toutes les sources de données disponibles avant d'abandonner la recherche des ingrédients - auparavant, une première correspondance incomplète (nom/photo seulement, sans ingrédients) pouvait être acceptée trop tôt, affichant « ingrédients non trouvés ». Également corrigé : le compteur « Total des scans » de l'écran d'accueil utilisait le même vert que le nombre de produits Halal, ce qui prêtait à confusion - il est désormais d'une couleur neutre.
</fr-FR>

<ar>
في هذا الإصدار: يتحقق البحث بالباركود الآن من جميع مصادر البيانات المتاحة قبل التوقف عن البحث عن مكونات المنتج - سابقاً كان يمكن قبول أول نتيجة غير مكتملة (اسم/صورة فقط، بلا مكونات) مبكراً جداً وعرض "المكونات غير موجودة". كما تم إصلاح: عداد "إجمالي المسح" في الشاشة الرئيسية كان يستخدم نفس اللون الأخضر لعدد المنتجات الحلال، مما كان مضللاً - أصبح الآن بلون محايد.
</ar>

---

## versionCode 10 / versionName 1.8 (2026-09-13)

Barkodu bulunamayan ürünler için fotoğrafla içindekiler analizi, ağ hatalarında ayrı "tekrar dene" ekranı, ve uygulamayı paylaşma butonu.

<tr-TR>
Bu sürümde: Barkodu veritabanında bulunamayan bir ürün için artık içindekiler etiketinin fotoğrafını çekip analiz ettirebilirsiniz — metin otomatik okunur, analiz etmeden önce gözden geçirip düzeltebilirsiniz. İnternet bağlantısı sorununda artık ürün "bulunamadı" değil, ayrı bir "tekrar dene" ekranı gösterilir. Ayrıca uygulamayı arkadaşlarınızla paylaşma butonu eklendi.
</tr-TR>

<en-US>
This update: for products not found by barcode, you can now photograph the ingredients label and get it analyzed — text is read automatically, and you can review and fix it before analysis runs. A connectivity problem now shows a dedicated retry screen instead of a false "not found" result. Also added: a button to share the app with friends.
</en-US>

<de-DE>
Update: Für Produkte, die per Barcode nicht gefunden werden, können Sie jetzt die Zutatenliste fotografieren und analysieren lassen - der Text wird automatisch erkannt und kann vor der Analyse überprüft und korrigiert werden. Bei Verbindungsproblemen erscheint jetzt ein eigener "Erneut versuchen"-Bildschirm statt eines falschen "nicht gefunden"-Ergebnisses. Außerdem neu: ein Button zum Teilen der App.
</de-DE>

<fr-FR>
Mise à jour : pour les produits non trouvés par code-barres, vous pouvez désormais photographier la liste des ingrédients pour l'analyser - le texte est lu automatiquement et peut être vérifié et corrigé avant l'analyse. Un problème de connexion affiche désormais un écran « réessayer » dédié au lieu d'un faux résultat « non trouvé ». Nouveau aussi : un bouton pour partager l'application.
</fr-FR>

<ar>
في هذا الإصدار: بالنسبة للمنتجات التي لا يُعثر عليها بالباركود، يمكنك الآن تصوير ملصق المكونات لتحليله - يتم قراءة النص تلقائياً، ويمكنك مراجعته وتصحيحه قبل التحليل. عند وجود مشكلة في الاتصال، تظهر الآن شاشة "إعادة المحاولة" مخصصة بدلاً من نتيجة "غير موجود" خاطئة. كما تمت إضافة زر لمشاركة التطبيق.
</ar>

---

## versionCode 9 / versionName 1.7 (2026-08-30)

Bazı ürünlerde taranan barkodun eşleştiği fotoğraf/bilgi farklı bir ürüne aitti; bu artık düzeltildi ve barkod eşleşmesinde canlı veri tabanı her zaman önceliklidir.

<tr-TR>
Bu sürümde: Bazı ürünlerde tarama sonucunda gösterilen fotoğraf veya bilgiler farklı bir ürüne ait olabiliyordu (ör. yanlış ürün fotoğrafı). Barkod eşleştirme mantığı, uygulama içindeki örnek verilerin gerçek ürün veri tabanının önüne geçmeyecek şekilde düzeltildi. Cihazınızda önceden hatalı önbelleğe alınmış sonuçlar bu güncellemeyle temizlenir (tarama geçmişiniz etkilenmez).
</tr-TR>

<en-US>
This update: some products were showing a photo or details that belonged to a different product after scanning. The barcode-matching logic was fixed so the app's built-in sample data can never override the real product database. Any incorrect results previously cached on your device are cleared by this update (your scan history is not affected).
</en-US>

<de-DE>
Update: Bei manchen Produkten wurden nach dem Scannen ein Foto oder Angaben eines anderen Produkts angezeigt. Die Barcode-Zuordnung wurde korrigiert, sodass die eingebauten Beispieldaten der App die echte Produktdatenbank nie mehr überschreiben können. Zuvor fehlerhaft zwischengespeicherte Ergebnisse werden mit diesem Update entfernt (Ihr Scan-Verlauf bleibt erhalten).
</de-DE>

<fr-FR>
Mise à jour : certains produits affichaient après un scan une photo ou des informations appartenant à un autre produit. La logique de correspondance des codes-barres a été corrigée afin que les données d'exemple intégrées à l'application ne puissent plus jamais remplacer la base de données réelle des produits. Les résultats erronés précédemment mis en cache sur votre appareil sont supprimés par cette mise à jour (votre historique de scan n'est pas affecté).
</fr-FR>

<ar>
في هذا الإصدار: كانت بعض المنتجات تعرض بعد المسح صورة أو معلومات تخص منتجاً مختلفاً. تم إصلاح منطق مطابقة الباركود بحيث لا يمكن للبيانات النموذجية المدمجة في التطبيق أن تحل محل قاعدة بيانات المنتجات الحقيقية بعد الآن. يتم مسح أي نتائج مخزّنة مؤقتاً بشكل خاطئ على جهازك مع هذا التحديث (سجل المسح الخاص بك لا يتأثر).
</ar>

---

## versionCode 8 / versionName 1.6 (2026-08-30)

Barkod sonuçlarında gerçek sebep gösterimi, genişletilmiş içerik tespiti (sığır kolajeni, kan ürünleri, gliserin), tekrarlanan içerik metni düzeltmesi, uzun içerik listelerinin artık kırpılmaması, ve yeni "Nasıl Çalışıyor?" sayfası.

<tr-TR>
Bu sürümde: Eşleşmeyen barkod sonuçlarında artık gerçek sebep gösteriliyor (ağ hatası, içerik eksik vb.), genel bir mesaj yerine. İçerik tarayıcı artık kaynağı belirsiz sığır kolajenini, jelatin dışı domuz türevlerini, kan ürünlerini ve gliserini de yakalıyor. Bazı içeriklerin iki kez gösterilmesine neden olan hata düzeltildi. Uzun içerik listeleri artık kırpılmıyor. Sonuç ekranına, Helal/Şüpheli/Haram mantığını açıklayan yeni bir "Nasıl Çalışıyor?" sayfası eklendi.
</tr-TR>

<en-US>
This update: barcode results that couldn't be matched now explain the real reason (network issue, missing ingredient data, etc.) instead of a generic message. The ingredient checker now also catches unverified bovine collagen, pork derivatives beyond gelatin, blood products, and glycerin. Fixed a bug that sometimes showed an ingredient twice. Long ingredient lists are no longer cut short. Added a new "How does this work?" page explaining the Halal/Doubtful/Haram logic.
</en-US>

<de-DE>
Update: Nicht gefundene Barcode-Ergebnisse zeigen jetzt den tatsächlichen Grund (Netzwerkfehler, fehlende Zutatenliste usw.) statt einer allgemeinen Meldung. Die Zutatenprüfung erkennt jetzt auch nicht bestätigtes Rinderkollagen, Schweinederivate über Gelatine hinaus, Blutprodukte und Glycerin. Ein Fehler, der manche Zutaten doppelt anzeigte, wurde behoben. Lange Zutatenlisten werden nicht mehr abgeschnitten. Neue Seite "Wie funktioniert das?" mit Erklärung der Halal/Zweifelhaft/Haram-Logik.
</de-DE>

<fr-FR>
Mise à jour : les résultats de codes-barres non trouvés affichent désormais la raison réelle (erreur réseau, ingrédients manquants, etc.) au lieu d'un message générique. Le contrôle des ingrédients détecte maintenant aussi le collagène bovin non vérifié, les dérivés porcins au-delà de la gélatine, les produits sanguins et la glycérine. Correction d'un bug affichant certains ingrédients en double. Les longues listes d'ingrédients ne sont plus tronquées. Nouvelle page « Comment ça marche ? » expliquant la logique Halal/Douteux/Haram.
</fr-FR>

<ar>
في هذا الإصدار: نتائج الباركود غير الموجودة تعرض الآن السبب الحقيقي (خطأ في الشبكة، مكونات غير مدرجة، إلخ) بدلاً من رسالة عامة. يكتشف فحص المكونات الآن أيضاً الكولاجين البقري غير المؤكد المصدر، مشتقات الخنزير غير الجيلاتين، منتجات الدم، والجليسرين. تم إصلاح خطأ كان يعرض بعض المكونات مرتين. لم تعد قوائم المكونات الطويلة تُقتطع. تمت إضافة صفحة جديدة "كيف يعمل هذا؟" تشرح منطق حلال/مشبوه/حرام.
</ar>

---

## versionCode 7 / versionName 1.5 (2026-08-29)

Ana sayfa, geçmiş ve E-Kodları ekranlarındaki arama kutuları tek bir
görsel standarda kavuşturuldu.

<tr-TR>
Bu sürümde: Ana sayfa, Geçmiş ve E-Kodları ekranlarındaki arama
kutuları artık aynı görünüme sahip. Ana sayfadaki arama kutusuna
dokunduğunuzda artık odaklandığını gösteren bir çerçeve görünüyor
(önceden görünmüyordu). E-Kodları aramasına, yazdığınızı tek
dokunuşla temizleyebileceğiniz bir "temizle" düğmesi eklendi.
</tr-TR>

<en-US>
This update: the search boxes on the Home, History, and E-Codes
screens now share a consistent look. The Home search box now shows a
visible focus outline when tapped (it didn't before). Added a clear
button to the E-Codes search so you can clear your query with one
tap.
</en-US>

<de-DE>
Update: Die Suchfelder auf den Bildschirmen "Start", "Verlauf" und
"E-Nummern" sehen jetzt einheitlich aus. Das Suchfeld auf der
Startseite zeigt beim Antippen nun einen sichtbaren Fokusrahmen
(vorher nicht). Der E-Nummern-Suche wurde eine Löschen-Schaltfläche
hinzugefügt, um die Eingabe mit einem Tipp zu leeren.
</de-DE>

<fr-FR>
Mise à jour : les champs de recherche des écrans Accueil, Historique
et Codes E ont désormais le même aspect. Le champ de recherche de
l'accueil affiche maintenant un contour visible lorsqu'il est
sélectionné (ce n'était pas le cas avant). Un bouton d'effacement a
été ajouté à la recherche des codes E pour effacer votre saisie en un
tap.
</fr-FR>

<ar>
في هذا الإصدار: أصبحت مربعات البحث في شاشات الرئيسية والسجل ودليل
أكواد E متشابهة الشكل الآن. يعرض مربع البحث في الشاشة الرئيسية الآن
إطاراً واضحاً عند النقر عليه (لم يكن يظهر سابقاً). تمت إضافة زر مسح
إلى بحث أكواد E لمسح ما كتبته بنقرة واحدة.
</ar>

---

## versionCode 6 / versionName 1.4 (2026-08-29)

Tablet odaklı düzeltmeler + E-Kodları rehberinin tam yerelleştirmesi.

<tr-TR>
Bu sürümde: Tabletlerde alt menü artık sistem çubuğuyla çakışmıyor.
E-Kodları rehberindeki tüm maddeler artık seçtiğiniz dilde (İngilizce,
Almanca, Fransızca, Türkçe, Arapça) tam gösteriliyor - bazı açıklamalar
önceden sadece Türkçeydi. Tabletlerde yazı boyutu okunabilirlik için
büyütüldü. Uygulama simgesi düzeltildi. Yeni güncelleme çıktığında
artık bildirim alacaksınız.
</tr-TR>

<en-US>
This update: fixed the bottom navigation bar overlapping the tablet's
system navigation bar. The E-Codes & Additives guide is now fully
translated in all 5 languages (English, German, French, Turkish,
Arabic) - some entries were previously Turkish-only. Increased text
size on tablets for readability. Fixed the app icon. You'll now be
notified when a new update is available.
</en-US>

<de-DE>
Update: Die untere Navigationsleiste überschneidet sich auf Tablets
nicht mehr mit der System-Navigationsleiste. Der E-Nummern-Leitfaden
ist jetzt vollständig in allen 5 Sprachen übersetzt (Englisch,
Deutsch, Französisch, Türkisch, Arabisch) - manche Einträge waren
zuvor nur auf Türkisch. Schriftgröße auf Tablets für bessere
Lesbarkeit vergrößert. App-Symbol korrigiert. Sie werden nun
benachrichtigt, wenn ein Update verfügbar ist.
</de-DE>

<fr-FR>
Mise à jour : la barre de navigation inférieure ne chevauche plus la
barre système sur tablette. Le guide des codes E est désormais
entièrement traduit dans les 5 langues (anglais, allemand, français,
turc, arabe) - certaines fiches n'étaient qu'en turc auparavant.
Taille du texte augmentée sur tablette pour une meilleure lisibilité.
Icône de l'application corrigée. Vous serez désormais averti lorsqu'une
mise à jour est disponible.
</fr-FR>

<ar>
في هذا الإصدار: لم يعد شريط التنقل السفلي يتداخل مع شريط النظام على
الأجهزة اللوحية. أصبح دليل أكواد E مترجماً بالكامل إلى جميع اللغات
الخمس (الإنجليزية، الألمانية، الفرنسية، التركية، العربية) - كانت بعض
الإدخالات باللغة التركية فقط سابقاً. تم تكبير حجم الخط على الأجهزة
اللوحية لتحسين القراءة. تم إصلاح أيقونة التطبيق. ستصلك الآن إشعارات
عند توفر تحديث جديد.
</ar>

---

## versionCode 5 / versionName 1.3 (2026-08-28)

Play Console versionCode 4'ü de "daha önce kullanıldı" diyerek reddetti
(muhtemelen geçmişte manuel olarak yüklenmiş). İçerik versionCode 4 ile
birebir aynı, sadece numara değişti. Metinler aşağıdaki versionCode 4
bölümüyle aynı - o bloklardaki dil etiketli metni kullanın.

---

## versionCode 4 / versionName 1.2 (2026-08-28) — SUPERSEDED, Play Console rejected as already used, never shipped

Play Console "What's new" alanına yapıştırırken dil etiketlerinin İÇİNE
(açılış/kapanış arasına) sadece o dilin metnini kopyalayın - `<tr-TR>` ve
`</tr-TR>` etiketlerinin kendisini yapıştırmayın. Her blok 500 karakter
sınırının altında tutuldu. (Bu bölüm, versionCode 4 hiç yayınlanmadığı
için üzerine yazılarak güncellendi - Faz 0 ve arayüz düzeltmeleri de
aynı sürüme eklendi.)

<tr-TR>
Sınıflandırma mantığı iyileştirildi: sakıncalı/şüpheli madde içermeyen
ürünler artık sadece "helal işareti yok" diye şüpheli gösterilmiyor,
doğrudan helal çıkıyor. Jelatin gibi maddelerin kaynağı (sığır/balık/
domuz) artık ayrı değerlendiriliyor, daha doğru sonuç veriyor. Sonuç
ekranına "Hata Bildir" butonu eklendi. Ayrıca buton metni kırpılması,
durum çubuğu görünürlüğü ve filtre etiketi taşması gibi arayüz
sorunları düzeltildi.
</tr-TR>

<en-US>
Improved the screening logic: products with no prohibited or doubtful
ingredients no longer default to "doubtful" for lacking a halal label -
they now show Halal directly. Gelatin's stated source (beef/fish/pork)
is now evaluated separately for more accurate results. Added a "Report
Error" button on the result screen - one tap to flag an incorrect
result. Also fixed several interface issues: clipped button text,
status bar visibility, and filter labels wrapping awkwardly.
</en-US>

<de-DE>
Die Prüflogik wurde verbessert: Produkte ohne verbotene oder
zweifelhafte Zutaten gelten nicht mehr allein wegen fehlendem
Halal-Siegel als zweifelhaft, sondern direkt als Halal. Die angegebene
Gelatinequelle (Rind/Fisch/Schwein) wird jetzt separat bewertet für
genauere Ergebnisse. Ein "Fehler melden"-Button wurde hinzugefügt. Zudem
wurden mehrere Anzeigefehler behoben: abgeschnittener Button-Text,
Sichtbarkeit der Statusleiste und umbrechende Filter-Beschriftungen.
</de-DE>

<fr-FR>
Logique de contrôle améliorée : les produits sans ingrédient interdit
ou douteux ne sont plus classés "douteux" par simple absence de label
halal - ils apparaissent désormais Halal directement. La source
indiquée de la gélatine (bœuf/poisson/porc) est désormais évaluée
séparément pour plus de précision. Ajout d'un bouton "Signaler une
erreur". Plusieurs problèmes d'interface ont aussi été corrigés : texte
de bouton tronqué, visibilité de la barre d'état, étiquettes de filtre
mal ajustées.
</fr-FR>

<ar>
تحسين منطق الفحص: المنتجات الخالية من مكونات محظورة أو مشبوهة لم تعد
تُصنّف "مشبوهة" لمجرد غياب علامة حلال - تظهر الآن حلال مباشرة. أصبح
مصدر الجيلاتين المذكور (بقري/سمك/خنزير) يُقيَّم بشكل منفصل لنتائج أدق.
تمت إضافة زر "الإبلاغ عن خطأ" في شاشة النتيجة. كما تم إصلاح عدة مشاكل
في الواجهة: اقتصاص نص الأزرار، ظهور شريط الحالة، والتفاف تسميات الفلتر.
</ar>

---

## versionCode 3 / versionName 1.1 (2026-08-28)

(Play Console rejected versionCode 2 as already used - see note below. Same
changes, renumbered; release notes text is unchanged from that section.)

### tr-TR
```
Bu sürümde tarama doğruluğunu artırdık: et/kümes hayvanı içeren ürünler,
kesim yöntemi belirtilmediği sürece artık "şüpheli" olarak gösteriliyor.
Birkaç yanlış sonuç veren durum (örn. "alkolsüz" ifadesinin yanlışlıkla
işaretlenmesi) düzeltildi. "Helal" sonucu artık yalnızca üründe gerçek bir
helal veya bitkisel işaret varsa gösteriliyor.
```

### en-US
```
This update improves screening accuracy: meat and poultry ingredients are
now marked "doubtful" unless the slaughter method is stated, and we fixed
a few false-flag bugs (e.g. "alcohol-free" being wrongly flagged). A
"Halal" result now only appears when the product itself carries an
explicit halal or plant-based claim.
```

### de-DE
```
Dieses Update verbessert die Prüfgenauigkeit: Fleisch- und Geflügelprodukte
werden jetzt als "zweifelhaft" markiert, sofern die Schlachtmethode nicht
angegeben ist. Einige falsche Markierungen (z. B. bei "alkoholfrei") wurden
behoben. Ein "Halal"-Ergebnis erscheint nur noch bei einer ausdrücklichen
Halal- oder pflanzlichen Kennzeichnung.
```

### fr-FR
```
Cette mise à jour améliore la précision du contrôle : la viande et la
volaille sont désormais marquées "douteuses" sauf mention du mode
d'abattage. Plusieurs faux signalements (ex. "sans alcool") ont été
corrigés. Un résultat "Halal" n'apparaît désormais que si le produit porte
une mention halal ou végétale explicite.
```

### ar
```
يحسّن هذا التحديث دقة الفحص: يتم الآن تصنيف اللحوم والدواجن كـ"مشبوهة" ما لم
تُذكر طريقة الذبح. تم إصلاح عدة حالات إشارة خاطئة (مثل عبارة "خالٍ من
الكحول"). تظهر نتيجة "حلال" الآن فقط عند وجود إشارة حلال أو نباتية صريحة
على المنتج.
```

---

## versionCode 2 / versionName 1.0 (2026-08-28) — SUPERSEDED, Play Console rejected as already used, never shipped

### tr-TR
```
Bu sürümde tarama doğruluğunu artırdık: et/kümes hayvanı içeren ürünler,
kesim yöntemi belirtilmediği sürece artık "şüpheli" olarak gösteriliyor.
Birkaç yanlış sonuç veren durum (örn. "alkolsüz" ifadesinin yanlışlıkla
işaretlenmesi) düzeltildi. "Helal" sonucu artık yalnızca üründe gerçek bir
helal veya bitkisel işaret varsa gösteriliyor.
```

### en-US
```
This update improves screening accuracy: meat and poultry ingredients are
now marked "doubtful" unless the slaughter method is stated, and we fixed
a few false-flag bugs (e.g. "alcohol-free" being wrongly flagged). A
"Halal" result now only appears when the product itself carries an
explicit halal or plant-based claim.
```

### de-DE
```
Dieses Update verbessert die Prüfgenauigkeit: Fleisch- und Geflügelprodukte
werden jetzt als "zweifelhaft" markiert, sofern die Schlachtmethode nicht
angegeben ist. Einige falsche Markierungen (z. B. bei "alkoholfrei") wurden
behoben. Ein "Halal"-Ergebnis erscheint nur noch bei einer ausdrücklichen
Halal- oder pflanzlichen Kennzeichnung.
```

### fr-FR
```
Cette mise à jour améliore la précision du contrôle : la viande et la
volaille sont désormais marquées "douteuses" sauf mention du mode
d'abattage. Plusieurs faux signalements (ex. "sans alcool") ont été
corrigés. Un résultat "Halal" n'apparaît désormais que si le produit porte
une mention halal ou végétale explicite.
```

### ar
```
يحسّن هذا التحديث دقة الفحص: يتم الآن تصنيف اللحوم والدواجن كـ"مشبوهة" ما لم
تُذكر طريقة الذبح. تم إصلاح عدة حالات إشارة خاطئة (مثل عبارة "خالٍ من
الكحول"). تظهر نتيجة "حلال" الآن فقط عند وجود إشارة حلال أو نباتية صريحة
على المنتج.
```
