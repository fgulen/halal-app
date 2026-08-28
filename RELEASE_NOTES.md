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
