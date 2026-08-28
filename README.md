# Helal Rehberi (Halal Checker)

Barkod okutarak bir gıda ürününün Open Food Facts'teki verisine bakan ve
ürünü **Helal / Şüpheli / Haram / Bilinmiyor** olarak sınıflandıran bir
Android uygulaması (Kotlin, Jetpack Compose).

Barkod → [Open Food Facts API](https://world.openfoodfacts.org) → içindekiler,
katkı maddeleri, etiketler → uygulamanın kendi kural motoru → sonuç.

## Nasıl Karar Veriyoruz?

**Bu bir dini fetva veya resmi sertifika kurumu değildir.** Open Food
Facts, gönüllüler tarafından girilen açık bir veritabanıdır; doğruluk veya
tamlık garantisi yoktur. Uygulama bu veri üzerinde otomatik bir tarama
yapar; nihai karar için ambalajdaki bilgiyi veya resmi bir helal
sertifikasını kontrol etmeniz önerilir.

Karar mantığının tamamı [`HalalAnalyzer.kt`](app/src/main/java/com/example/data/analyzer/HalalAnalyzer.kt)
dosyasında, dört adımda çalışır:

1. **Haram kuralları** (`HARAM_RULES`) — domuz/domuz jelatini (E441), alkol/likör,
   E120 karmin (böcek kökenli), E542 kemik fosfatı gibi kesin olarak
   yasaklanmış maddeler. Bunlardan biri eşleşirse sonuç doğrudan **Haram**'dır.
2. **Şüpheli kurallar** (`SUSPICIOUS_RULES`) — kaynağı ürüne göre değişebilen
   maddeler: E471/E472 emülgatörler, E904 şellak, E920 L-sistein, jelatin
   (kaynağı belirtilmemiş), hayvansal peynir mayası, peynir altı suyu tozu —
   ve **et/kümes hayvanı** (kesim yönteminin ürün verisinden doğrulanamaması
   nedeniyle). Bunlardan biri eşleşirse sonuç **Şüpheli**'dir.
3. **Helal** yalnızca ürün gerçekten bir helal etiketi taşıyorsa (OFF'un
   `labels_tags` alanında) veya açık bir vegan/bitkisel işareti varsa VE
   yukarıdaki hiçbir kural eşleşmemişse verilir. Hiçbir kuralın eşleşmemiş
   olması tek başına "helal" anlamına gelmez — bu yüzden işaretsiz ürünler
   de varsayılan olarak **Şüpheli**'ye düşer, Helal'e değil.
4. **Bilinmiyor** — barkod Open Food Facts'te kayıtlı ama içindekiler
   listesi henüz girilmemişse.

Et içeren bir ürün, üründe zaten bir helal etiketi varsa (kesimin
sertifikalı olduğu varsayımıyla) bu et kontrolünden muaf tutulur; aksi
halde her zaman Şüpheli'ye düşer.

### Bilinçli tasarım kararları

- **Şüphe varsayılan davranıştır, istisna değil.** Bir ürünün doğrulanabilir
  bir helal işareti yoksa, "hiçbir sakıncalı içerik bulunamadı" ifadesi asla
  "bu üründür helal" anlamına gelecek şekilde sunulmaz.
- **Yalnızca yapılandırılmış alanlara (`labels_tags`, `additives_tags`)
  güvenilir**; serbest metindeki "helal" veya "vegan" geçen ifadeler kabul
  edilmez, çünkü "not halal certified" gibi olumsuz ifadeler de aynı
  kelimeyi içerir.
- Şarap sirkesi gibi bazı maddeler fıkhi görüş ayrılığı barındırır (bazı
  mezheplerde istihale yoluyla helal sayılır); bu uygulama ihtiyatlı
  tarafı seçer ve bunu gizlemez.

## Test

```
# Windows
gradlew.bat testDebugUnitTest --tests "com.example.data.analyzer.HalalAnalyzerTest"

# macOS / Linux
./gradlew testDebugUnitTest --tests "com.example.data.analyzer.HalalAnalyzerTest"
```

`HalalAnalyzerTest`, yukarıdaki mantığın regresyon testlerini içerir
(alcohol-free yanlış pozitifi, E120/E1200 karışıklığı, et kontrolü, OFF'un
vegan analiz etiketlerinin yanlış yorumlanması gibi, tarih: 2026-08-28).
