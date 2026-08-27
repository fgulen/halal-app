# Gizlilik Politikası — Helal Rehberi

**Yürürlük tarihi:** 27 Ağustos 2026

Bu gizlilik politikası, "Helal Rehberi" (Halal Rehberi / Helal Kontrol) mobil uygulamasının
("Uygulama") kişisel verilerinizi nasıl işlediğini açıklar. Bu belge, Uygulamanın gerçek kod
davranışına göre hazırlanmıştır.

## 1. Hangi izinleri istiyoruz ve neden

| İzin | Amaç |
|---|---|
| Kamera (`CAMERA`) | Barkod taramak için. Kamera görüntüsü cihazınızdan **hiçbir yere gönderilmez** — barkod tanıma tamamen cihazınızda (Google ML Kit ile, çevrimdışı) yapılır. |
| İnternet (`INTERNET`) | Taradığınız barkodun ürün bilgisini ve görselini Open Food Facts'ten (bkz. Bölüm 3) çekmek için. |
| Titreşim (`VIBRATE`) | Barkod başarıyla okunduğunda kısa bir geri bildirim titreşimi vermek için. |

Uygulama konum, kişi listesi, mikrofon, depolama veya diğer hassas izinleri istemez.

## 2. Cihazınızda tutulan veriler

Taradığınız ürünler ve sonuçları (tarama geçmişi), performans için önbelleğe alınan ürün
verileriyle birlikte **yalnızca cihazınızdaki yerel veritabanında** saklanır. Bu veriler
geliştiriciye veya üçüncü bir tarafa **gönderilmez, satılmaz veya paylaşılmaz**. Uygulama
içinden "Geçmişi Temizle" ile bu verileri istediğiniz zaman silebilir, uygulamayı kaldırarak
tamamını cihazınızdan kaldırabilirsiniz.

## 3. Üçüncü taraf hizmet: Open Food Facts

Bir barkod taradığınızda veya ürün aradığınızda, yalnızca **barkod numarası veya arama
metni**, ürün bilgisi ve görseli almak amacıyla [Open Food Facts](https://world.openfoodfacts.org)
adlı bağımsız, kâr amacı gütmeyen açık veri projesinin sunucularına gönderilir. Bu istekler
sırasında kimliğinizi belirleyen herhangi bir bilgi (ad, e-posta, cihaz kimliği vb.)
gönderilmez. Open Food Facts'in kendi gizlilik politikası için:
https://world.openfoodfacts.org/privacy

## 4. Reklam ve analitik

Uygulama şu anda reklam ağı veya kullanıcı davranışı analitiği (analytics) **kullanmamaktadır**.
Bu politika, ileride böyle bir hizmet eklenirse güncellenecektir.

## 5. Çocukların gizliliği

Uygulama genel bir gıda-bilgi aracıdır, çocuklara özel olarak yönlendirilmemiştir ve
bilerek çocuklardan veri toplamaz.

## 6. Veri güvenliği

Cihazınızdan ayrılan tek veri, Open Food Facts'e gönderilen barkod/arama sorgusudur ve bu
iletişim şifreli bağlantı (HTTPS) üzerinden yapılır.

## 7. Haklarınız

Tüm verileriniz cihazınızda tutulduğu için, verilerinizi görüntülemek, silmek veya taşımak
tamamen sizin kontrolünüzdedir (uygulama içi geçmiş temizleme veya uygulamayı kaldırma yoluyla).

## 8. Bu politikadaki değişiklikler

Bu politika güncellenirse, yeni sürüm bu sayfada yayınlanır ve yürürlük tarihi güncellenir.

## 9. İletişim

Sorularınız için: **fatihgulen@gmail.com**

---

# Privacy Policy — Helal Rehberi (English)

**Effective date:** August 27, 2026

This policy explains how the "Helal Rehberi" app handles data, based on its actual code
behavior.

**Permissions:** Camera (on-device barcode scanning only — no image ever leaves your device),
Internet (to fetch product data/images), Vibration (scan feedback).

**Local storage:** Your scan history and cached product data are stored only in a local
database on your device. Nothing is sent to the developer. Clear it anytime via "Clear
History" in the app, or by uninstalling the app.

**Third-party service:** When you scan or search a product, only the barcode number or
search text is sent to [Open Food Facts](https://world.openfoodfacts.org), an independent
non-profit open database, to retrieve product information and images. No personally
identifying information is sent. See Open Food Facts' own privacy policy at
https://world.openfoodfacts.org/privacy.

**No ads or analytics** are currently used in this app. **Not directed at children.** All
network requests use HTTPS.

**Contact:** **fatihgulen@gmail.com**
