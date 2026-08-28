# Play Store Sürüm Notları

Her yeni sürümde en üste yeni bir bölüm eklenir. Play Console'daki "Sürüm
notları" alanları için kullanılan diller: `en-US`, `ar`, `de-DE`, `fr-FR`,
`tr-TR` (uygulamanın kendi 5 dilinden biraz farklı kodlar — Play Console
bunları böyle bekliyor). Her alan ~500 karakter sınırlıdır.

---

## versionCode 4 / versionName 1.2 (2026-08-28)

Play Console "What's new" alanına yapıştırırken dil etiketlerinin İÇİNE
(açılış/kapanış arasına) sadece o dilin metnini kopyalayın - `<tr-TR>` ve
`</tr-TR>` etiketlerinin kendisini yapıştırmayın. Her blok 500 karakter
sınırının altında tutuldu.

<tr-TR>
Sınıflandırma mantığını iyileştirdik: sakıncalı veya şüpheli madde
içermeyen ürünler artık sırf "helal işareti yok" diye şüpheli
gösterilmiyor, doğrudan helal çıkıyor (et/kümes hayvanı ve kaynağı
belirsiz katkılar yine şüpheli kalıyor). Vejetaryen ürünlerin yanlışlıkla
"vegan" etiketiyle gösterilmesi düzeltildi. Sonuçtaki sebep artık ürüne
özel, genel bir metin değil. Dil değiştirip aynı ürünü tekrar
taradığınızda sonuç artık doğru dilde gösteriliyor.
</tr-TR>

<en-US>
Improved the screening logic: products with no prohibited or doubtful
ingredients no longer default to "doubtful" for lacking a halal label -
they now show Halal directly (meat/poultry and source-unverified
additives still stay doubtful). Fixed vegetarian products being wrongly
shown a "vegan" label. The reason on the result screen is now always
specific to that product, not a generic example. Re-scanning after a
language switch now shows the result in the new language.
</en-US>

<de-DE>
Die Prüflogik wurde verbessert: Produkte ohne verbotene oder
zweifelhafte Zutaten gelten nicht mehr allein wegen fehlendem
Halal-Siegel als zweifelhaft, sondern direkt als Halal (Fleisch/Geflügel
und Zusätze unklarer Herkunft bleiben zweifelhaft). Ein Fehler, bei dem
vegetarische Produkte fälschlich als "vegan" markiert wurden, ist
behoben. Die Begründung bezieht sich jetzt immer auf das Produkt. Ein
erneuter Scan nach Sprachwechsel zeigt das Ergebnis richtig übersetzt.
</de-DE>

<fr-FR>
Logique de contrôle améliorée : les produits sans ingrédient interdit
ou douteux ne sont plus classés "douteux" par simple absence de label
halal - ils apparaissent désormais Halal directement (viande/volaille et
additifs d'origine incertaine restent douteux). Correction d'un bug
étiquetant à tort des produits végétariens comme "végane". La raison
affichée est désormais toujours spécifique au produit. Rescanner après
un changement de langue affiche le résultat dans la bonne langue.
</fr-FR>

<ar>
تحسين منطق الفحص: المنتجات الخالية من مكونات محظورة أو مشبوهة لم تعد
تُصنّف "مشبوهة" لمجرد غياب علامة حلال - تظهر الآن حلال مباشرة (اللحوم
والدواجن والإضافات مجهولة المصدر تبقى مشبوهة). تم إصلاح خطأ كان يعرض
المنتجات النباتية بعلامة "فيغن" خطأً. أصبح السبب المعروض خاصًا بالمنتج
دائمًا. إعادة مسح منتج بعد تغيير اللغة يعرض النتيجة باللغة الصحيحة.
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
