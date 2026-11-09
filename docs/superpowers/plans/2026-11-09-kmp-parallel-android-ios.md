# KMP paralel geliştirme planı (Android + iOS)

**Tarih:** 2026-11-09
**Hedef:** Android (canlı) ve iOS aynı kod tabanından paralel geliştirilsin. Şimdilik Play/App Store yayını yok. iPhone'a test kurulumu yapılabilsin.
**Yön:** Kotlin Multiplatform + Compose Multiplatform. Flutter ve React Native elendi (Kotlin bilgisi ve mevcut ~13k satırlık kod korunuyor).
**Kural:** Her faz sonunda Android release build alınır ve `./gradlew testDebugUnitTest` (116 test) yeşil olur. Canlı Android sürümü bozulmaz.

## 0. Kararlar ve açık noktalar

1. **Mac yok.** iOS derlemesi yalnız bulut CI'da (macOS runner) yapılabilir. Yerel iOS hata ayıklaması olmaz.
2. **Apple hesabı: ücretsiz Apple ID.** Kurulum Sideloadly ile yapılır, uygulama 7 günde bir yeniden imzalanır. Ücretli hesap yayın zamanına bırakıldı.
3. **CI onayı: kullanıcı henüz karar vermedi.** Macbook olmadığı için P0 için CI gerekir. Push ve repo ayarı kullanıcı onayı olmadan yapılmaz.
4. **Açık soru: Expo mu, KMP mi?** Kullanıcı "Expo ile telefonuma kurmak" dedi. Expo, React Native demektir ve bu planın dışındadır. Expo'ya geçilirse Android ile iOS iki ayrı kod tabanı olur. Bu plan KMP'de kalır. Karar kullanıcıya bağlı.

## 1. Envanter (kodda doğrulandı, 2026-11-09)

**Ortak koda taşınacak (`commonMain`):**
- `data/analyzer` (HalalAnalyzer.kt 1493 satır dahil) ve `data/model`. Android import'u yok.
- Çoğu Compose UI (`ui/screens`, `ui/components`, `ui/theme`). Material3 ve foundation ortak API.
- Metinler (5 dil) Kotlin içinde, aynen taşınır.

**Kütüphane değişimleri:**
- Room 2.7.0: KMP destekli, şema aynı kalır (DB v10).
- Retrofit + Moshi + OkHttp → Ktor + kotlinx.serialization (`data/remote/OpenFoodFactsApi.kt`).
- Coil 2.7 → Coil 3 (KMP).
- DataStore Preferences → DataStore KMP.
- Navigation Compose, Lifecycle ViewModel → JetBrains KMP sürümleri.
- Accompanist Permissions → KMP izin kütüphanesi veya `expect`/`actual`.
- `android.util.Log` → Kermit veya `println` tabanlı küçük bir wrapper.

**Platforma özel (`expect`/`actual`):**
- Kamera ve barkod: Android'de CameraX + ML Kit barcode; iOS'ta AVFoundation metadata output (ML Kit iOS SDK da var, ama AVFoundation daha az bağımlılık getirir).
- Fotoğraftan OCR (`PhotoIngredientCapture`): Android'de ML Kit text recognition; iOS'ta Apple Vision.
- Paylaş / mailto / Intent: Android'de Intent, iOS'ta UIActivityViewController ve mailto.
- Play In-App Update ve Play Review (`MainActivity.kt`): sadece Android. iOS'ta no-op.

**Dikkat:** `build.gradle.kts` içinde `firebase-ai` ve `firebase-appcheck-recaptcha` tanımlı, ama uygulama kodunda Firebase import'u yok. Migration sırasında kaldırılması önerilir. Bu, Gradle değişikliği olduğu için kullanıcı onayıyla yapılır.

## 2. Fazlar

**P0 — iOS hattı spike'ı (önce bu, en büyük risk).**
- JetBrains KMP şablonu (`iosApp` ile) oluştur.
- CI'da macOS runner ile imzasız IPA üret.
- iPhone'a kur (Developer Mode açık olmalı). Yöntem, 0. maddedeki cevaba göre seçilir.
- Ölçüt: şablon uygulama iPhone'da açılıyor. Bu olmadan P2+ anlamsız.

**P1 — Analyzer ortak modüle (Windows'ta hemen başlanabilir, P0 ile paralel).**
- `shared` modülü: `commonMain` (`data/analyzer`, `data/model`), `commonTest` (ilgili testler).
- Testler JVM'de Windows'ta çalışır. Android uygulaması ve test sayısı değişmez.
- Ölçüt: Android `testDebugUnitTest` yeşil, `shared` JVM testleri yeşil.

**P2 — Veri katmanı.**
- Room KMP (aynı şema, `AppDatabaseMigrationTest` geçmeli, mevcut kullanıcı verisi korunur).
- Ktor + kotlinx.serialization ile `OpenFoodFactsApi` ve `ProductRepository`.
- DataStore KMP (`UserPreferences`).
- Ölçüt: Android'de geçmiş ve tercihler eski sürümle aynı çalışır.

**P3 — Arayüz Compose Multiplatform'a.**
- Ekranlar ve bileşenler `commonMain`'e taşınır.
- CMP sürümü seçilirken Android Compose BOM sınırı (`2026.06.01`, CLAUDE.md) kontrol edilir. Çakışma varsa önce versiyon hizalanır.
- Ölçüt: Android ekranları görsel olarak aynı, Roborazzi ekran görüntüleri değişmiyor (ya da bilinçli güncelleniyor).

**P4 — iOS platform katmanı ve CI.**
- Kamera/barkod, OCR, paylaş/mailto için iOS `actual`'ları.
- `Info.plist` kamera izin metni, ikon, bundle ID.
- CI: her PR'da Android testleri + iOS derlemesi; main'e merge sonrası iPhone için IPA.
- Ölçüt: iPhone'da tarama, geçmiş, analiz çalışıyor.

## 3. Tuzaklar

- `kotlin.native.ignoreDisabledTargets=true` ekle. Windows'ta iOS hedefi derlenmediği için build düşmez.
- `iosMain` kodu Windows'ta derlenmez. Mümkün olduğunca ince tut, iş mantığı `commonMain`'de kalsın.
- Room KMP için iOS hedeflerinde KSP gerekir. Bu yalnız CI'da doğrulanır.
- Windows'ta iOS hata ayıklaması yok. Hatalar CI logları üzerinden takip edilir.
- Mevcut kirli dosyalar (`RELEASE_NOTES.md`, `store_assets/video/...`) KMP commit'lerine karışmamalı.

## 4. Git

- Dal: mevcut HEAD'den (`fix/off-server-error-and-madhhab-plan`), `main`'den değil. Bu dal henüz main'e merge olmadı.
- Her faz ayrı commit serisi. Her commit, Android testleri yeşil olduğunda atılır.
- Push ve CI değişiklikleri kullanıcı onayıyla yapılır.

## 5. Hafıza güncellemesi

- `project_faz0_roadmap.md`: "Android-first, KMP sonraya" kararı değişti. Paralel KMP geliştirme, yayın yok. Strateji dokümanı (`docs/superpowers/specs/2026-08-28-cross-platform-ai-strategy-design.md`) bu kararla güncel değil.
