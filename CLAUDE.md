# Halal Checker (com.fatihgulen.helalrehberi)

Android (Kotlin + Jetpack Compose) barkod tarayıcı: Open Food Facts verisinden içindekileri okuyup helal / şüpheli / haram sonucu ve nedenini gösterir. 5 dil (tr, en, de, fr, ar), mezhep ayarı, E-kodları rehberi, cihazda tarama geçmişi. Play Store'da "Halal Checker: Barcode Scanner" adıyla yayında (managed publishing açık).

Kullanıcıya Türkçe, kısa cevap ver. Push, Play'e gönderim ve ücretli API çağrısı onaysız yapılmaz.

## Derleme ve test

- JDK 21 ve SDK: `export JAVA_HOME="C:/Android/jdk-21" ANDROID_HOME="C:/Android/Sdk"` (JDK 17 ile Robolectric testleri düşer).
- Test: `./gradlew testDebugUnitTest` (2026-10-22: 116 test).
- Release AAB: `./gradlew bundleRelease`; imza değerlerini `RELEASE_KEYSTORE_INFO.txt`'ten komuta satır içi ver (`KEYSTORE_PATH`, `STORE_PASSWORD`, `KEY_ALIAS`, `KEY_PASSWORD`), asla yazdırma. Doğrula: `jarsigner -verify app/build/outputs/bundle/release/app-release.aab`.
- Her yeni versionCode'da `RELEASE_NOTES.md`'nin en üstüne 5 dilde not ekle. Kullanılmış en yüksek versionCode'u hafızadaki release toolchain notundan al, build.gradle.kts'ten değil.
- R8 açık. Compose BOM, compileSdk 37 kurulana kadar <= 2026.06.01 kalmalı.

## Play Console

- Mağaza girişini `store-listings/default/edit` adresinden düzenle (`main-store-listing` uygulama listesine yönlendirir).
- Tarayıcıdan dosya yükleme sınırı 10 MB. AAB'yi (~38 MB) kullanıcı yükler, sürüme "Kitaplıktan ekle" ile eklenir.
- Özel mağaza girişi varsayılanı o ülkelerde ezer. Varsayılandaki her görsel değişikliği oraya da yapılmalı. 2026-10-22 itibarıyla özel giriş yok.
- Mağaza görselleri: `store_assets/screenshots_v2` (render.sh + capture_lang.py), fragman: `store_assets/video/promo-v2/src/build.py` (HyperFrames, dil başına seslendirme `store_assets/video/vo_langs`).

## Kapat

| Ayar | Değer |
|---|---|
| Plan / günlük | Hafızadaki proje notları (bu dosyada uzun günlük tutma) |
| Repo | Kök dizin tek repo, mevcut branch |
| Test komutu | `./gradlew testDebugUnitTest` (JDK 21 ile) |
| Ek adımlar | versionCode değiştiyse: `RELEASE_NOTES.md` 5 dil + hafızada en yüksek versionCode'u güncelle |
| Deploy | Play Console'a gönderim; her zaman kullanıcı onayıyla, "İncelemeye gönder"den önce sor |
