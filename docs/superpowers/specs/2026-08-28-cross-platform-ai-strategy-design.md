# Helal Rehberi: Çapraz Platform, AI ve Büyüme Stratejisi

**Tarih:** 2026-08-28
**Durum:** Onaylandı — uygulama fazı writing-plans ile başlayacak

## 1. Temel Felsefe

- **Fayda odaklı, kâr odaklı değil.** Amaç kullanıcıya değer sunmak; gelir modeli yalnızca altyapı (sunucu, opsiyonel AI çağrıları, geliştirici hesapları) maliyetini karşılamayı hedefler.
- **Sıfır sürtünme.** Hesap yok, bulut senkronizasyonu yok — veriler cihazda kalır. Bu, hem gizlilik hem de mevcut mağaza metinlerinde zaten verilen bir sözdür ("Tüm veriler cihazınızda kalır, hesap oluşturmanıza gerek yoktur").
- **Önce güven, sonra gelir.** İlk 6 ayda 10 özellik yerine, "barkodu okut → 2 saniyede güvenilir Helal/Şüpheli/Haram sonucu" deneyimini kusursuzlaştırmak önceliklidir.

## 2. Platform Stratejisi

- **Android öncelikli, iOS'a taşınabilir mimari.** Kullanıcının 2013 model bir Mac'i var ama şu an iOS derlemesi hedeflenmiyor; hedef ileride ihtiyaç olursa kolayca taşınabilmek.
- **Doğrulanmış teknik zemin:** `analyzer/` ve `model/` paketlerinde (`HalalAnalyzer.kt`, `EAdditive.kt`, `FoodProduct.kt`, `HalalStatus.kt`, `AppLanguage.kt`, `AppStrings.kt`) sıfır Android importu var (grep ile doğrulandı). Yalnızca `local/` (Room, Context) ve `repository/remote` (Log) Android'e bağımlı, bunlar da kolay soyutlanabilir.
- **Karar:** Şimdi tam bir Kotlin Multiplatform (KMP) modül taşıması yapılmayacak — erken optimizasyon olur (YAGNI). Bunun yerine bir **disiplin kuralı** benimsenir: `analyzer/` ve `model/` paketlerine yeni Android-özel bağımlılık eklenmez. Gerçek KMP/iOS çalışması, iOS işine fiilen başlanınca (Faz sonrası, ayrı bir proje olarak) ele alınır.
- **Reddedilen alternatif:** Flutter/React Native ile baştan yeniden yazım — kural motorunun testleriyle birlikte (bourbon-vanilla, E120/E1200, et kontrolü regresyonları) yeniden sertifikalanması gerekirdi; KMP yolu bu testleri olduğu gibi taşır.

## 3. Mimari İlke: Sıfır Marjinal Maliyet

Sağlık skoru, alerjenler, işlenmişlik seviyesi gibi görünüşte "akıllı" özelliklerin çoğu aslında **deterministik kural motoruyla** çözülebilir çünkü gerekli veri (OFF `nutriments` alanı) zaten çekiliyor:

- Helal/Haram/Şüpheli kararı, E-kod analizi, şeker/tuz/yağ/protein/lif/kalori değerlendirmesi, işlenmişlik seviyesi, alerjenler → **kural motoru, cihaz üzerinde, sıfır AI maliyeti.**
- OCR (barkodsuz ürün) → **ML Kit on-device text recognition, sıfır API maliyeti**, çıktısı mevcut `analyzeIngredientsText`'e (`ProductRepository.kt`) beslenir.
- Katkı maddesi açıklamaları, mezhep bazlı notlar → **tek seferlik üretilip** (AI yardımıyla yazılıp gözden geçirilip) statik, 5 dilde çevrilmiş içerik olarak paketlenir. Runtime'da AI çağrısı yok.
- **Gerçek, tekrarlanan AI maliyeti yalnızca** kullanıcının serbest metinle "açıkla" istediği, önceden şablonlanamayan durumlarda oluşur — ve bu, ürün barkodu/metni hash'lenerek **cache'lenir**: aynı ürün için ikinci kullanıcı sıfır maliyetle yararlanır.

Sonuç: uygulamanın büyük çoğunluğu ücretsiz ve yerel kalır; yalnızca dar, opsiyonel bir dilim gerçek işletme maliyeti taşır.

## 4. Roadmap

### Faz 0 — Çekirdek Güvenilirlik (şimdi, en yüksek öncelik)
- Helal karar motorunun kaynak-bazlı akıl yürütmesini (örn. jelatin: E441/domuz→Haram, kaynağı belirsiz→Şüpheli — bu mantık kısmen zaten var) kullanıcıya daha görünür şekilde sun ("neden" açıklaması).
- Mevcut doğruluk düzeltme çalışmasına devam (git geçmişindeki pattern: bourbon-vanilla, E120/E1200, et kontrolü gibi regresyon düzeltmeleri).
- **Topluluk "Hata Bildir / Doğrusunu Öner" butonu** — ücretsiz, veritabanı kalitesini organik ve sürekli tutar.

### Faz 1 — Kapsam Genişletme + Viral Çekirdek
- **OCR entegrasyonu:** ML Kit on-device text recognition → mevcut `analyzeIngredientsText` fonksiyonu. "Kayıtsız Ürün" boşluğunu kapatır.
- **Dinamik paylaşım motoru:** Tarama sonucunu WhatsApp/Instagram'a paylaşırken, sonuca göre (🔴 Haram/Şüpheli uyarı damgası veya 🟢 Temiz/Helal tavsiye damgası) uygulama logolu görsel üretir. Organik büyüme için en yüksek kaldıraçlı ucuz özellik.

*Not: OCR bilinçli olarak Faz 3'e değil Faz 1'e alındı — yeni bir sütun değil, mevcut "güvenilir helal kontrolü" değer önermesinin kapsam genişlemesi olarak değerlendirildi, maliyeti sıfır (cihaz üzerinde).*

### Faz 2 — Sağlık Skoru (deterministik, yeni sütun)
- OFF `nutriments` verisi + WHO eşik değerleri: şeker, tuz/sodyum, doymuş yağ, trans yağ, kalori, protein, lif.
- 0-100 arası **"sağlık değerlendirmesi"** (bilinçli olarak "sağlıklı/sağlıksız" değil) + "Bu değerlendirme besin değerlerine dayanır, tıbbi tavsiye değildir" notu.
- İşlenmişlik seviyesi (NOVA benzeri), alerjenler — tamamı kural/veri bazlı.

### Faz 3 — Büyüme Özellikleri
- Endonezce/Malayca/Urduca **arayüz** desteği (şu an yalnızca mağaza listelemesinde var, uygulama içi çeviri yok — `PLAY_STORE_LISTING.md`'de zaten tespit edilmiş bir fırsat).
- Favoriler, son taramalar, deep link, ASO ince ayarı.

### Faz 4 — Kişiselleştirme ve Talep Doğrulama
- Hedef profilleri (kilo verme/protein/kalp sağlığı/çocuk/genel) — cihazda saklanır, şablon metinlerle yorumlanır (AI değil).
- **On-device haftalık analiz paneli** (örn. "Sağlık Skoru Ortalaman", "İncelediğin ürün sayısı") — gamification/retention.
- **Hard gate:** "Neden şüpheli / açıkla" butonuna tıklama oranı ölçülür. Faz 5'e geçiş yalnızca bu veri gerçek talep gösterirse yapılır — AI altyapısına, talep doğrulanmadan yatırım yapılmaz.

### Faz 5 — Opsiyonel AI + Premium (yalnızca Faz 4 talebi doğrularsa)
- **Free:** sınırsız tarama, helal analiz, şüpheli madde tespiti, sağlık skoru, OCR, reklamsız — sonsuza kadar.
- **Plus:** AI açıklamaları, kişisel hedef analizi, alternatif ürün önerisi. Yıllık ücretlendirme ($4.99–9.99) — mağaza kesintisi küçük aylık tutarları eritiyor, yıllık daha az sürtünmeli ve cache sayesinde kullanıcı başı marjinal maliyet zamanla sıfıra yakınsıyor.
- Reklam yok — "güvenilir, bağımsız analiz" konumlandırmasıyla çelişir; zaten reklam gelirine muhtaç olmayan bir maliyet yapısı var.

## 5. Büyüme Stratejisi

- **Faz A — Kapalı test → yayın (şimdi, $0):** Play'in test gereksinimini Müslüman topluluk kanalları (Telegram/WhatsApp grupları, r/islam vb.) üzerinden organik doldur, erken geri bildirimle listelemeyi cilalandır.
- **Faz B — Organik büyüme ($0, zaman-yoğun):** ASO + çoklu dil arayüz (bkz. Faz 3), kısa video içerik ("bu ürünü taradım, sonuç..." formatı), helal sertifika kuruluşları / helal yaşam içerik üreticileriyle karşılıklı tanıtım.
- **Faz C — Gelir geldikçe yeniden yatırım:** Bütçe kullanıcı sayısına göre kademelendirilir (0–1k kullanıcı: €0–20/ay, 1k–5k: €20–50/ay, 5k+: €50–100/ay). Harcama önceliği: **1) Çeviri → 2) ASO → 3) Tasarım/UX → 4) AI → 5) Reklam.** Reklam en son çünkü en düşük güven/en yüksek maliyet oranına sahip.

## 6. Değerlendirilip Reddedilen / Ertelenen Fikirler

- **Flutter/React Native ile tam yeniden yazım** — reddedildi; mevcut kural motoru ve testleri Android'e bağımlı değil, KMP yolu bunları korur, yeniden yazım gereksiz risk ve efor.
- **Şimdi tam KMP modül taşıması** — ertelendi; iOS işi fiilen başlayana kadar erken optimizasyon (YAGNI).
- **OCR'ı Faz 3'e erteleme** (bir dış görüşün önerisi) — reddedildi; OCR mevcut çekirdek değer önermesinin doğal kapsam genişlemesi ve maliyeti sıfır, geciktirmenin gerekçesi yok.
- **"AI Plus"ı erken satmak** — reddedildi; Faz 4'te talep ölçülmeden AI altyapısına yatırım yapılmayacak (bkz. hard gate).
- **Belirsiz "gelişmiş raporlar" premium maddesi** — kapsam netliği için Plus tanımına dahil edilmedi; Plus yalnızca (AI açıklama + kişisel analiz + alternatif ürün) ile sınırlı.
- **Reklam geliri** — reddedildi; maliyet yapısı zaten düşük, reklam güven konumlandırmasına zarar verir.

## 7. Sonraki Adım

Bu doküman onaylandı. Uygulamaya geçiş, **Faz 0**'dan başlayarak `writing-plans` süreciyle ayrı bir implementasyon planına dökülecek.
