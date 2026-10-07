# Madhhab setting + OFF 5xx fix — plan

**Date:** 2026-10-07
**Status:** Draft. Waiting for user approval. No code has been written.
**Background:** The TagHalal competitor analysis (see memory `project_competitor_taghalal`). The user's request: "keep it simple, only halal/haram checking, show madhhab-level differences."

---

## Phase 0 — Tuc bug: an OFF 5xx shows as "not found" ✅ IMPLEMENTED (2026-10-07, not committed)

> Built test-first. `OpenFoodFactsApiServerErrorTest` (4 tests) failed for the expected reason, then the fix made it pass. Full suite: 48/48. The optional one-time v2 retry was **not** added. Verified by unit tests only; not tested on a device.

**Root cause (confirmed):**
- Logcat at 12:14:02 shows OFF `v2` and `v0` both returned HTTP 502 and `openbeautyfacts` returned 404.
- In `OpenFoodFactsApi.getProductByBarcode`, `reachedServerAtLeastOnce` turns true for every HTTP response. Five-hundred responses therefore come back as `"product not found"`.
- The repository then treats it as a definite not-found. The user sees an "Unregistered Product" screen, and the wrong result is written to scan history.
- A re-scan after OFF recovered got HTTP 200 and showed the correct verdict ("no flagged ingredients").

**Change:**
1. Only two outcomes count as "not found", and only from an **openfoodfacts.org food endpoint**:
   - 2xx with `status = 0`
   - HTTP 404
2. 5xx or 429 from OFF food means a network or server error. The result is `statusVerbose = "network error"`.
3. Open Beauty Facts responses never count toward "not found". Otherwise its 404 alone would hide the 502.
4. Optional: if v2 returns 5xx, retry once and only once.
5. The `getNetworkErrorTitle` title "No Connection" / "Bağlantı yok" is wrong in this case, because the user is online. Change it to something like "Couldn't check right now" / "Şu an kontrol edilemedi". The message text is already correct ("this doesn't mean the product is unregistered").

**Test:** inject an `OkHttpClient` interceptor that returns 502 for openfoodfacts.org and 404 for openbeautyfacts. Assert:
- `statusVerbose == "network error"`
- `ProductRepository.checkBarcode` throws `NetworkUnavailableException`
- no row is added to scan history

**Separate small bug (not part of this fix):** for `BULUNAMADI`, the banner (`getStatusCardSubtitle`) always says "Barcode found in OFF but ingredients empty". The detail card underneath says "not found in OFF". The same text is used for two different situations.

---

## Madhhab setting — design principles

- **Simple:** one choice in Settings: Hanafi / Shafi'i / Maliki / Hanbali / **Not specified (cautious — default)**.
- **The default does not change verdicts:** under "Not specified", the **status and flagged set** of every product stay exactly as they are today. The reason text may gain one "madhhab difference" line (Phase 3). The existing 44-test suite (counted 2026-10-07) must pass unchanged. Its `reasonOrDetails` checks use `contains`, so an extra line doesn't break them.
- **Store the madhhab with each scan (DB v4→v5):**
  - The selection itself lives in `UserPreferences` (DataStore), like `language`.
  - Add `madhhab TEXT NOT NULL DEFAULT 'UNSPECIFIED'` to both `products` and `scan_history`, through an **explicit** `MIGRATION_4_5` (`ALTER TABLE … ADD COLUMN`).
  - Why: the ingredient chips (`ProductResultView.kt:833`) are coloured at render time. A history row must be coloured with **the madhhab it was scanned under**, otherwise you get a green banner with an orange chip.
  - The cache treats a hit from a different madhhab as stale and re-analyzes, exactly as `language` already does.
  - ⚠️ Because of `fallbackToDestructiveMigration(dropAllTables = true)`, a **missing** migration wipes scan history. Add a Room `MigrationTestHelper` test for 4→5.
  - Old history rows get `UNSPECIFIED` and keep the verdict they were scanned with.
- **Only sourced rulings go into code:** every madhhab difference must carry a source. Unsourced items wait on the "awaiting source" list.

## Phase 1 — Preference and threading the parameter

- Add a `Madhhab` enum with values `UNSPECIFIED, HANAFI, SHAFII, MALIKI, HANBALI`.
- Add a `madhhab` flow plus a setter to `UserPreferences`.
- Add a madhhab picker: one dialog, five options. **There is no Settings screen today.** The home top bar has only a language chip, E-Codes and Share, so where the picker goes is an open question (see the questions below).
- **Every verdict path** takes a `madhhab` parameter, otherwise the label and the text will contradict each other (TagHalal's bug):
  - `analyzeOpenFoodFactsProduct`
  - `analyzeIngredientsText`, the photo path
  - `classifyIngredientToken`, which colours the ingredient chips in `ProductResultView.kt:833` and `PhotoIngredientsDialog.kt:216`
- The Nutella shortcut and the `InitialData` curated samples in `ProductRepository` return fixed verdicts. **This collides with the only override planned for v1:**
  - Typing "nutella" returns the curated sample. Its ingredients include whey (`InitialData.kt:86`), so under Hanafi it would still show Doubtful.
  - A barcode scan of Nutella goes to OFF, whose current ingredients contain no whey.
  - **Decision:** curated samples get no hand-written verdict. Their ingredient text is run through `HalalAnalyzer.analyzeIngredientsText(…, madhhab)`. If that turns out to be too large a change, write this exact case down as a known gap.

## Phase 2 — Override table

- A small table keyed by the rule's stable ID (`nameEn`, e.g. `"Animal Rennet / Cheese Enzymes"`). Each entry holds:
  `madhhab → (status, note TR/EN/…, source citation)`
- The analyzer works in two steps:
  1. Each matched rule takes its status from the table entry for the selected madhhab, or the rule's default when there is none.
  2. The final verdict follows the existing order (Haram > Doubtful > Halal).
- **First release:** only overrides the user has given sources for. Current candidate:

| Rule | Madhhab | Claimed ruling | Source status |
|---|---|---|---|
| Animal Rennet / Cheese Enzymes, Whey | Hanafi | Rennet from a halal-species animal is valid even without zabiha (Abu Hanifa's view; his students are more cautious) | **User's own research, unconfirmed.** A fatwa citation is needed |

**Awaiting source (not going into code):**
- E120 carmine under Maliki
- E904 shellac
- shrimp and seafood under Hanafi
- meat slaughtered by People of the Book

Halal certification overriding ingredient flags (the Nutella case) is **out of scope**. It would reverse the direction of the curated-data-precedence fix.

## Phase 3 — UI (one line)

- When the selected madhhab changes a verdict, or a flagged rule has a different ruling in another madhhab, add one line to `reasonOrDetails`:
  "Madhhab difference: under the Hanafi view, rennet from a halal animal is permissible (source: …)."
- `reasonOrDetails` is stored in both Room tables, so the line also appears on cache hits and in history. `flaggedDetails` is not stored, so the note must not live only there.
- **Show sources as links (user request, 2026-10-07):** "linking the sources builds trust."
  - Every override entry carries `sourceTitle` + `sourceUrl`, e.g. "Diyanet Din İşleri Yüksek Kurulu, 15.09.2020" → kurul.diyanet.gov.tr/….
  - Under the madhhab note, the result screen shows a tappable **"Source: …"** link that opens in the browser (an `Intent.ACTION_VIEW` or Compose `LinkAnnotation`).
  - Persistence: the source URL is written into the note line inside `reasonOrDetails`, so history entries also keep the link. The UI detects and linkifies URLs in that text.
  - Add a **"Sources"** section to `HowItWorksDialog`: a list of every source in the override table (Diyanet fatwas, TDV İslâm Ansiklopedisi, IslamQA 2841, …) as links.
  - Only links that were checked with `curl` go in. Every link above was checked on 2026-10-07.
  - Test: a Hanafi + rennet result contains `kurul.diyanet.gov.tr` in `reasonOrDetails`.

## Open decision: split SUPHELI or not?

| Option | Cost | Benefit |
|---|---|---|
| **A (recommended): keep 4 statuses** and write "source unknown" vs "scholars differ" in the reason text | Small | Simple, low risk |
| B: new enum value `IHTILAFLI` | Every `when` in `AppStrings`, history filter chips, home stats counter, colours | A separate colour/label |

A new enum value needs no migration of its own (`Converters` stores the enum as TEXT). The `madhhab` column in v4→v5 is needed in both options.

## Tests

1. **Default regression:** the existing 44 tests pass unchanged with `UNSPECIFIED`, with the same status and flagged set.
2. **Migration:** `MigrationTestHelper` 4→5 keeps `scan_history` rows and fills `madhhab = 'UNSPECIFIED'`.
3. **Per madhhab:** the same ingredient text with `HANAFI` vs `UNSPECIFIED` gives the expected verdict for the rennet rule.
4. **Consistency:** for each madhhab, `classifyIngredientToken`'s colour must not contradict the product verdict. A red chip under a Halal verdict is not allowed. A history row is coloured with its own stored madhhab.
5. Phase 0: the 502/404 interceptor test above.

## Decisions (user, 2026-10-07)

- Phase 0: apply now → done.
- Rulings: **Claude researches the sources, the user verifies.** No override goes into code until the user confirms it.
- SUPHELI: **not split**. "Source unknown" vs "scholars differ" is written in the reason text (option A).
- Picker: **next to the language chip** on the home screen.
- Hanafi rennet: **Abu Hanifa's view** (which Diyanet also follows) → under HANAFI, rennet (subset keywords) = Halal. The note mentions the Imameyn's cautious view.
- Whey: **follows rennet** → under HANAFI, whey = Halal. The note says plainly that this is an inference, not a direct source.
- Phase 0: commit on a new branch, no push.
- Phase 1: **start now**, while the user reviews the sources. Infrastructure only; no override goes into code in Phase 1.

Note for Phase 2: E120 sits in `HARAM_RULES` and fills `harmfulLabels`. The override step must be able to move a match **between** the haram and suspicious lists, not only change its label.

## Source research (2026-10-07) — FOR THE USER TO VERIFY. No rule goes into code until approved.

Method: every claim was fetched from its page and the **original-language** quote taken verbatim. A single fatwa site = "one fatwa", not "the madhhab's position". Access date for all sources: 2026-10-07.

### 1. Rennet (`"Animal Rennet / Cheese Enzymes"`) — ✅ actionable

| Madhhab | Position | Verbatim quote | Source | Confidence |
|---|---|---|---|---|
| Hanafi (Abu Hanifa) | Rennet from a dead/unslaughtered animal is **pure** | "Imam Abu Hanifa ruled that the rennet of a dead animal is pure (Tahir), even if the animal itself is Maytah." | [IslamOnline fiqh](https://fiqh.islamonline.net/en/islamic-ruling-on-parmesan-cheese/) | Medium (two sources agree) |
| Hanafi (Imameyn) | **Split within Hanafi:** Abu Yusuf and Muhammad are more cautious (this matches the user's own note) | Not quoted verbatim; search summary only | — | Low, needs verification |
| Hanbali | **Two narrations**; one agrees with Abu Hanifa, Ibn Taymiyya leans to "pure" | "The second view, which is that of Abu Hanifah and is one of two opinions narrated from Imam Ahmad, is that rennet from dead animals or animals that were not slaughtered according to Shari'ah is still tahir (pure). This is the opinion which Shaykh al-Islam Ibn Taymiyyah thought most correct in al-Fatawa (21/102)" | [IslamQA 2841](https://islamqa.info/en/answers/2841) | **Low (single source)** |
| Maliki, Shafi'i, (majority Hanbali) | Rennet from an unslaughtered animal is **impure** | "according to the apparent meaning of the opinions reported from the majority of scholars among the Malikis, Shafi'is and Hanbalis, it is impure (najis) and should not be eaten." | [IslamQA 2841](https://islamqa.info/en/answers/2841), plus [IslamOnline](https://fiqh.islamonline.net/en/islamic-ruling-on-parmesan-cheese/): "The majority of these schools consider rennet taken from a non-Zabiha animal to be Najis" | Medium (two sources agree) |

> **Quote verification (2026-10-07):** each page was downloaded with `curl` and every quote above was searched for in the tag-stripped text. All of them **appear on the page verbatim**. The first IslamQA quotes were the fetch tool's summary; they were replaced with the real text and the conclusion did not change. The Imameyn row is the only one not confirmed verbatim.
| **Diyanet (Turkey)** | Rennet from animals whose meat is halal is permissible **with no slaughter condition**; only pork rennet is forbidden | "Ayrıca etinin yenilmesi helal olan hayvanlardan elde edilen mayaların kullanılması caizdir. Ancak domuzun midesinden elde edilen peynir mayalarının kullanılması caiz değildir. Buna göre, domuzdan elde edilen mayanın kullanıldığına dair bir bilgi olmadığı sürece hayvansal mayalardan üretilen peynirlerin tüketilmesinde dinen bir sakınca yoktur." (15.09.2020) | [kurul.diyanet.gov.tr](https://kurul.diyanet.gov.tr/tr/fetva/peynir-yapiminda-kullanilan-mayalarin-dini-hukmu-nedir/0193c42d-aa60-7126-92d8-9c538f6b513b) | High (primary source) |

**Proposed override (USER DECIDES on the Hanafi split):**
- HANAFI → if the user chooses Abu Hanifa's view: rennet **Halal**. Note text: "According to Abu Hanifa, rennet from an unslaughtered animal is pure. The Imameyn are more cautious." Diyanet is cited **separately as an institution**: "Diyanet: no objection unless known to be pork." If the user chooses the Imameyn's view, rennet stays Doubtful.
- HANBALI → stays Doubtful, with note: "two narrations".
- SHAFII / MALIKI → stay Doubtful. The source is unknown; if it is from an unslaughtered animal it is impure.

⚠️ **Pepsin scope:** today the `"Animal Rennet / Cheese Enzymes"` rule also matches `pepsin`, `animal enzymes` and `cheese cultures`. Commercial pepsin is mostly porcine, and the Diyanet fatwa itself excludes pork-stomach rennet. **The override must not touch pepsin.**
- Decision: the override carries a **keyword subset**: `rennet`, `animal rennet`, `kälberlab`, `kalbslab`, `présure`. The rule's other keywords (pepsin, animal enzymes, cheese cultures) keep their current Doubtful status in every madhhab.
- No rule split, so the flag names under the default setting stay the same.
- `classifyIngredientToken` (chip colour) uses the same subset.
- Test: under Hanafi, "pepsin" alone → Doubtful; "rennet" alone → Halal; "rennet, pepsin" → Doubtful.

⚠️ The automated summary of the Diyanet page said "halal-slaughtered". **The verbatim text contains no slaughter condition.**

### 2. Whey (`"Whey Powder / Whey Byproduct"`) — ⚠️ derived, no direct source

No direct source was found for whey. The argument "it is a by-product of rennet, so it follows rennet's ruling" is **our inference**. It would mirror the rennet override (Hanafi → Halal), but this needs the user's approval.

### 3. E120 Carmine — ❌ no override (correction of an earlier claim)

Claude earlier told the user that "under Maliki carmine may be halal". **That was wrong.**

| Madhhab | Verbatim quote | Source |
|---|---|---|
| Maliki | "Eating insects according to the Maliki school is permissible but that is with a condition… killed with an intention of making them fit for consumptions (dhakat)." / "Based on my understanding of the current production methods, it would not be permissible to consume foods with the Carmine according to the Maliki madhab." (Shaykh Rami Nsour, cites Khalil's *Mukhtasar*) | [SeekersGuidance Maliki](https://islamqa.org/maliki/seekersguidance-maliki/85201/eating-foods-with-dye-made-from-insects-in-the-maliki-school/) |
| Hanafi / Shafi'i / Hanbali | "The majority of scholars including the Hanafi, Shafi'i and Hanbali jurists, considers the consumption of insects as impermissible." | [SeekersGuidance Shafi'i](https://islamqa.org/shafii/seekersguidance-shafii/168747/is-cochineal-red-dye-from-insects-permissible-to-consume/) |
| Diyanet | "sinek, örümcek, akrep gibi haşeratın yenilmesi İslâm âlimleri tarafından caiz görülmemiştir." (09.04.2025) | [kurul.diyanet.gov.tr](https://kurul.diyanet.gov.tr/tr/fetva/kara-hayvanlarindan-eti-yenilip-yenilmeyenler-hangileridir/0198bc0e-e91d-75d3-a056-45ee437a90ff) |

Result: E120 stays **Haram** in every madhhab. The Maliki result rests on a single fatwa (low confidence). All three quotes were confirmed verbatim with `curl`.

### 4. Gelatin — ❌ no override

- Diyanet (18.08.2017): "kollajenin kaynağı olan hayvan helal değilse, ondan üretilen jelatin de helal olmamaktadır". The fatwa says gelatin production is not *istihale* (complete transformation), so its ruling follows its source.
  [kurul.diyanet.gov.tr](https://kurul.diyanet.gov.tr/tr/fetva/domuzdan-uretilen-jelatin-caiz-midir/0193c42d-b24f-7fe0-15f8-46f4191ad5bb)
- TDV *İstihâle*: "Hanefîler'in yanı sıra Mâlikîler … istihâlenin necis maddeleri temiz hale çeviren fonksiyonunu kabul ederken Şâfiîler ve ağırlıklı olarak Hanbelîler istihâlenin bu fonksiyonunu kabul etmez."
  [islamansiklopedisi.org.tr](https://islamansiklopedisi.org.tr/istihale--fikih)
- The madhhabs differ on istihale **in principle**, but whether gelatin counts as istihale is a contemporary dispute (Diyanet: it doesn't). A "Hanafi → gelatin halal" rule would contradict Diyanet. **No override.**

### 5. Alcohol as a solvent in flavourings — ⏸ institutional view, not a madhhab view

Diyanet permits synthetic alcohol used as a solvent in flavourings ("aromatik maddelerin çözünürlüğünü sağlamaktır") and alcohol that forms naturally in trace amounts.
[kurul.diyanet.gov.tr](https://kurul.diyanet.gov.tr/Cevap-Ara/1373/uretim-asamalarinda-sentetik-alkol-kullaniminin-hukmu-nedir)

This is an **institutional ruling**, not a madhhab-by-madhhab position. Ingredient lists also rarely say "solvent". Keep it out of v1.

### 6. E904 Shellac, meat/collagen (zabiha) — not researched

There was not enough time or sources. They stay on the "awaiting source" list.

### Net v1 scope after research

**Only rennet**, plus whey if the user approves the inference, for HANAFI. Every other rule keeps its current status in every madhhab.

## Questions for the user (original)

1. Apply Phase 0 (the Tuc/5xx fix) now?
2. Which disputed items go in the first release, and with which sources (fatwa or book citation)?
3. Split SUPHELI (option B) or keep it in the reason text (option A)?
4. Where should the madhhab picker go: next to the language chip on the home screen, or a one-time screen at first launch?
