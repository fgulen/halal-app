# Faz 0: Core Reliability Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Make the halal decision engine's source-based reasoning more accurate and visible, and add a zero-backend "Report Error" channel so users can flag bad verdicts.

**Architecture:** Two small, targeted accuracy fixes to `HalalAnalyzer`'s gelatin handling (following the codebase's existing TDD/regression-test pattern), plus a new pure email-content builder wired into the existing product result screen via an `ACTION_SENDTO` mailto intent. No backend, no new dependencies, no new screens.

**Tech Stack:** Kotlin, Jetpack Compose, JUnit (plain, no Robolectric — matches existing `HalalAnalyzerTest` pattern).

**Spec:** `docs/superpowers/specs/2026-08-28-cross-platform-ai-strategy-design.md` (Faz 0 section)

## Global Constraints

- No new Android-only imports in `app/src/main/java/com/example/data/analyzer/` or `app/src/main/java/com/example/data/model/` (Faz 0 discipline rule — keeps the domain layer portable for a future iOS/KMP move).
- All user-facing strings must be added to `AppStrings.kt` in all 5 supported languages: EN, DE, FR, TR, AR (see existing functions in that file for the pattern).
- Ingredient-text keyword matching in `HalalAnalyzer` covers EN/DE/FR/TR/ES text (the codebase concatenates `ingredientsTextEn/De/Fr/Es/Tr/Ar` from Open Food Facts, but no existing rule anywhere has Arabic keywords — don't introduce guessed Arabic terms; this is pre-existing, out-of-scope).
- Follow the existing commit message convention seen in `git log`: `fix: ...` / `feat: ...` lowercase, imperative, one line.
- No backend/server work — the "Report Error" feature must work with zero infrastructure (mailto intent only).

---

## File Structure

- **Modify** `app/src/main/java/com/example/data/analyzer/HalalAnalyzer.kt` — add a source-aware "Beef/Fish Gelatin" suspicious rule ahead of the generic "Gelatin (Unspecified Source)" rule, and extend `NEGATION_PHRASES` to stop flagging explicit vegetable/plant gelatin claims.
- **Modify** `app/src/test/java/com/example/data/analyzer/HalalAnalyzerTest.kt` — regression tests for both fixes.
- **Modify** `app/src/main/java/com/example/data/model/AppStrings.kt` — 3 new string getters (5 languages each) for the Report Error feature.
- **Create** `app/src/main/java/com/example/data/model/ReportEmail.kt` — pure functions building the report email's subject/body from a `FoodProduct` + `AppLanguage`. No Android imports (testable with plain JUnit).
- **Create** `app/src/test/java/com/example/data/model/ReportEmailTest.kt` — unit tests for the builder.
- **Modify** `app/src/main/java/com/example/ui/components/ProductResultView.kt` — add a "Report Error" icon button to the bottom action row, wired to an `ACTION_SENDTO` mailto intent using `ReportEmail`.

---

### Task 1: Beef/Fish gelatin — stop mislabeling a stated source as "unspecified"

**Problem:** `SUSPICIOUS_RULES` currently has one gelatin rule matching the bare keyword `"gelatin"` (and its DE/FR/ES variants), which fires identically whether the ingredient text says just "gelatin" **or** explicitly "beef gelatin" / "fish gelatin". The shown reason text is "Packaging does not specify whether fish, halal bovine, or non-halal porcine source is used" — which is factually wrong when the text *does* state beef or fish. This directly serves the Faz 0 goal of visible, accurate source-based reasoning (the fix flows straight into the UI, since `ProductResultView.kt`'s `FlaggedProblematicItemCard` already renders whatever `reason` text the matched rule provides — no UI change needed for this task).

**Files:**
- Modify: `app/src/main/java/com/example/data/analyzer/HalalAnalyzer.kt:36-53` (near `MEAT_RULE_ID`/`MEAT_KEYWORDS`) and `:107-194` (`SUSPICIOUS_RULES` list, specifically the existing gelatin rule around line 153-161 and the skip-condition around line 296-299)
- Test: `app/src/test/java/com/example/data/analyzer/HalalAnalyzerTest.kt`

**Interfaces:**
- Consumes: existing `HalalAnalyzer.SuspiciousRule` data class, existing `suspiciousRuleIds` list and `MEAT_RULE_ID`-style stable-identifier pattern already in the file.
- Produces: a new stable rule identifier `BEEF_FISH_GELATIN_RULE_ID`, used by later tasks/tests to assert which rule fired (by `FlaggedIngredient.name` in EN, since tests run with `language = AppLanguage.EN` by default).

- [ ] **Step 1: Write the failing tests**

Add to `HalalAnalyzerTest.kt`:

```kotlin
    @Test
    fun `beef gelatin is Suspicious with a source-aware reason, not the generic unspecified one`() {
        val result = analyze("water, beef gelatin, sugar")
        assertEquals(HalalStatus.SUPHELI, result.status)
        assertTrue(
            "should flag the source-stated beef/fish gelatin rule, not the generic unspecified one",
            result.flaggedDetails.any { it.name.contains("Beef", ignoreCase = true) || it.name.contains("Fish", ignoreCase = true) }
        )
        assertFalse(
            "must not also show the generic 'unspecified source' gelatin flag once source is known",
            result.flaggedDetails.any { it.name.contains("Unspecified", ignoreCase = true) }
        )
        assertFalse(
            "reason text must not claim the source is unspecified when the text says 'beef'",
            result.reasonOrDetails.contains("does not specify", ignoreCase = true)
        )
    }

    @Test
    fun `fish gelatin is Suspicious with a source-aware reason`() {
        val result = analyze("water, fish gelatin, sugar")
        assertEquals(HalalStatus.SUPHELI, result.status)
        assertTrue(
            result.flaggedDetails.any { it.name.contains("Beef", ignoreCase = true) || it.name.contains("Fish", ignoreCase = true) }
        )
    }

    @Test
    fun `plain unspecified gelatin still falls back to the generic suspicious rule`() {
        // Regression guard: the new source-specific rule must not swallow the plain case.
        val result = analyze("water, gelatin, sugar")
        assertEquals(HalalStatus.SUPHELI, result.status)
        assertTrue(result.flaggedDetails.any { it.name.contains("Unspecified", ignoreCase = true) })
    }
```

- [ ] **Step 2: Run tests to verify they fail**

Run: `gradlew.bat testDebugUnitTest --tests "com.example.data.analyzer.HalalAnalyzerTest"`
Expected: the two new "beef"/"fish" tests FAIL (no such rule exists yet — `flaggedDetails` only contains the generic "Gelatin (Unspecified Source)" entry, and `reasonOrDetails` still contains "does not specify"/similar). The third test passes already (no code change needed for it yet, it's a guard for the next step).

- [ ] **Step 3: Add the stable rule ID constant**

In `HalalAnalyzer.kt`, next to the existing `MEAT_RULE_ID` constant (line 42):

```kotlin
    private const val MEAT_RULE_ID = "Meat / Poultry (Slaughter Method Unconfirmed)"
    // Stable identifier for the beef/fish-gelatin rule, used below to skip the generic
    // "unspecified source" gelatin rule once the more specific source-stated rule has fired.
    private const val BEEF_FISH_GELATIN_RULE_ID = "Beef/Fish Gelatin (Source Stated)"
```

- [ ] **Step 4: Add the new SuspiciousRule ahead of the generic gelatin rule**

In `HalalAnalyzer.kt`, insert immediately **before** the existing generic gelatin rule (the one starting `keywords = listOf("gelatin", "gélatine", "gelatine", "gelatina")` around line 153):

```kotlin
        SuspiciousRule(
            keywords = listOf(
                "beef gelatin", "bovine gelatin", "fish gelatin",
                "rindergelatine", "fischgelatine",
                "gélatine de bœuf", "gélatine de poisson",
                "sığır jelatini", "balık jelatini",
                "gelatina de res", "gelatina de pescado"
            ),
            nameEn = BEEF_FISH_GELATIN_RULE_ID,
            nameTr = "Sığır/Balık Jelatini (Kaynak Belirtilmiş)",
            reasonEn = "Source is stated as beef or fish, not pork - but halal status still depends on whether the animal was slaughtered according to zabiha (Islamic) method, which cannot be confirmed from packaging alone.",
            reasonTr = "Kaynağın sığır veya balık olduğu belirtilmiş - domuz değil. Ancak helal olması için hayvanın zebiha usulüne uygun kesilmiş olması gerekir; bu bilgi ambalajdan doğrulanamaz.",
            eCode = "E441",
            origin = "Beef / Fish"
        ),
```

- [ ] **Step 5: Update the skip condition so the generic gelatin rule doesn't also fire**

In `HalalAnalyzer.kt`, find the existing skip block inside the `SUSPICIOUS_RULES` loop (around line 296-299):

```kotlin
                // If gelatin is already identified as pork gelatin, skip general gelatin
                if (rule.keywords.contains("gelatin") && harmfulLabels.any { it.contains("Gelatin") || it.contains("Pork") }) {
                    continue
                }
```

Replace with:

```kotlin
                // If gelatin is already identified as pork gelatin (Haram) or as beef/fish
                // gelatin (the more specific Suspicious rule above), skip the generic
                // "unspecified source" gelatin rule - it would otherwise also fire on the
                // same ingredient and either duplicate the flag or contradict the more
                // specific reason text already shown.
                if (rule.keywords.contains("gelatin") &&
                    (harmfulLabels.any { it.contains("Gelatin") || it.contains("Pork") } ||
                        suspiciousRuleIds.contains(BEEF_FISH_GELATIN_RULE_ID))
                ) {
                    continue
                }
```

- [ ] **Step 6: Run tests to verify they pass**

Run: `gradlew.bat testDebugUnitTest --tests "com.example.data.analyzer.HalalAnalyzerTest"`
Expected: PASS (all tests in the file, including the 3 new ones and all pre-existing ones).

- [ ] **Step 7: Commit**

```bash
git add app/src/main/java/com/example/data/analyzer/HalalAnalyzer.kt app/src/test/java/com/example/data/analyzer/HalalAnalyzerTest.kt
git commit -m "fix: distinguish stated beef/fish gelatin from unspecified-source gelatin"
```

---

### Task 2: Vegetable/plant gelatin claims must not be flagged Suspicious

**Problem:** Some packaging explicitly labels a plant-based gelling agent as "vegetable gelatin" / "gélatine végétale" / "pflanzliche Gelatine" / "bitkisel jelatin" / "gelatina vegetal". Today the bare `"gelatin"` keyword still matches inside that phrase, so a product making an explicit plant-based claim gets wrongly flagged Şüpheli with "source unspecified" — the opposite of what the label says. The codebase already has a proven mechanism for exactly this shape of problem: `NEGATION_PHRASES` + `stripNegatedPhrases`, used today for "alcohol-free" (see `HalalAnalyzer.kt:469-486` and the `alcohol-free flavoring is not flagged Haram` test).

**Files:**
- Modify: `app/src/main/java/com/example/data/analyzer/HalalAnalyzer.kt:475-478` (`NEGATION_PHRASES` list)
- Test: `app/src/test/java/com/example/data/analyzer/HalalAnalyzerTest.kt`

**Interfaces:**
- Consumes: existing `NEGATION_PHRASES` list and `stripNegatedPhrases` function (no signature change).
- Produces: nothing new consumed by later tasks.

- [ ] **Step 1: Write the failing test**

Add to `HalalAnalyzerTest.kt`:

```kotlin
    @Test
    fun `vegetable gelatin claim is not flagged Suspicious`() {
        // Regression: the bare "gelatin" keyword matched inside "vegetable gelatin" and its
        // DE/FR/TR/ES equivalents, flagging an explicit plant-based claim as if the source
        // were unspecified.
        val result = analyze("water, vegetable gelatin, sugar")
        assertFalse(
            "an explicit vegetable/plant gelatin claim must not be flagged",
            result.flaggedDetails.any { it.name.contains("Gelatin", ignoreCase = true) }
        )
        assertEquals(HalalStatus.HELAL, result.status)
    }
```

- [ ] **Step 2: Run test to verify it fails**

Run: `gradlew.bat testDebugUnitTest --tests "com.example.data.analyzer.HalalAnalyzerTest"`
Expected: FAIL — `flaggedDetails` contains a "Gelatin (Unspecified Source)" entry, `status` is `SUPHELI`.

- [ ] **Step 3: Extend NEGATION_PHRASES**

In `HalalAnalyzer.kt`, find `NEGATION_PHRASES` (line 475-478):

```kotlin
    private val NEGATION_PHRASES = listOf(
        "alcohol-free", "alcohol free", "non-alcoholic", "alkoholfrei", "alkoholfreie", "alkoholfreies",
        "ohne alkohol", "sans alcool", "sin alcohol"
    )
```

Replace with:

```kotlin
    private val NEGATION_PHRASES = listOf(
        "alcohol-free", "alcohol free", "non-alcoholic", "alkoholfrei", "alkoholfreie", "alkoholfreies",
        "ohne alkohol", "sans alcool", "sin alcohol",
        // Explicit plant-based gelatin claims - stripping the whole phrase (not just "gelatin")
        // means the bare "gelatin" keyword below no longer matches this specific mention, while
        // a separate, unrelated "gelatin" elsewhere in the same ingredient list still does.
        "vegetable gelatin", "gélatine végétale", "pflanzliche gelatine", "bitkisel jelatin",
        "gelatina vegetal"
    )
```

- [ ] **Step 4: Run test to verify it passes**

Run: `gradlew.bat testDebugUnitTest --tests "com.example.data.analyzer.HalalAnalyzerTest"`
Expected: PASS (all tests, including all of Task 1's).

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/example/data/analyzer/HalalAnalyzer.kt app/src/test/java/com/example/data/analyzer/HalalAnalyzerTest.kt
git commit -m "fix: do not flag explicit vegetable/plant gelatin claims as suspicious"
```

---

### Task 3: AppStrings entries for the Report Error feature

**Files:**
- Modify: `app/src/main/java/com/example/data/model/AppStrings.kt`

**Interfaces:**
- Produces: `AppStrings.getReportError(lang)`, `AppStrings.getReportErrorEmailSubject(lang)`, `AppStrings.getReportErrorPromptLine(lang)` — all `(AppLanguage) -> String`, consumed by Task 4 (`ReportEmail.kt`) and Task 5 (UI button).

- [ ] **Step 1: Add the three string getters**

In `AppStrings.kt`, add near the existing `getShare...` group (after `getShareChooserTitle`, around line 437):

```kotlin
    fun getReportError(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Report Error"
        AppLanguage.DE -> "Fehler melden"
        AppLanguage.FR -> "Signaler une erreur"
        AppLanguage.TR -> "Hata Bildir"
        AppLanguage.AR -> "الإبلاغ عن خطأ"
    }

    fun getReportErrorEmailSubject(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Incorrect Result Report"
        AppLanguage.DE -> "Meldung eines falschen Ergebnisses"
        AppLanguage.FR -> "Signalement d'un résultat incorrect"
        AppLanguage.TR -> "Hatalı Sonuç Bildirimi"
        AppLanguage.AR -> "الإبلاغ عن نتيجة غير صحيحة"
    }

    fun getReportErrorPromptLine(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Please describe what's incorrect and what the correct classification should be:"
        AppLanguage.DE -> "Bitte beschreiben Sie, was falsch ist und wie die richtige Einstufung lauten sollte:"
        AppLanguage.FR -> "Merci de décrire ce qui est incorrect et quelle devrait être la bonne classification :"
        AppLanguage.TR -> "Lütfen neyin yanlış olduğunu ve doğru sınıflandırmanın ne olması gerektiğini açıklayın:"
        AppLanguage.AR -> "يرجى وصف الخطأ وما ينبغي أن يكون التصنيف الصحيح:"
    }
```

- [ ] **Step 2: Verify it compiles**

Run: `gradlew.bat compileDebugKotlin`
Expected: BUILD SUCCESSFUL (no test needed — this is a pure string table, matching the existing convention of not unit-testing `AppStrings` directly; its content is exercised indirectly through Task 4's tests and manual QA in Task 5).

- [ ] **Step 3: Commit**

```bash
git add app/src/main/java/com/example/data/model/AppStrings.kt
git commit -m "feat: add strings for the report-error feature"
```

---

### Task 4: ReportEmail pure builder

**Files:**
- Create: `app/src/main/java/com/example/data/model/ReportEmail.kt`
- Test: `app/src/test/java/com/example/data/model/ReportEmailTest.kt`

**Interfaces:**
- Consumes: `AppStrings.getReportErrorEmailSubject`, `AppStrings.getShareProductLabel`, `AppStrings.getShareBarcodeLabel`, `AppStrings.getShareStatusLabel`, `AppStrings.getShareFlaggedIngredientsLabel`, `AppStrings.getStatusLabel`, `AppStrings.getReportErrorPromptLine` (all pre-existing except the three added in Task 3); `FoodProduct`, `AppLanguage`.
- Produces: `ReportEmail.SUPPORT_EMAIL: String`, `ReportEmail.buildSubject(product: FoodProduct, language: AppLanguage): String`, `ReportEmail.buildBody(product: FoodProduct, language: AppLanguage): String` — consumed by Task 5.

- [ ] **Step 1: Write the failing test**

Create `app/src/test/java/com/example/data/model/ReportEmailTest.kt`:

```kotlin
package com.example.data.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ReportEmailTest {

    private val product = FoodProduct(
        barcode = "1234567890123",
        name = "Test Snack",
        brand = "Test Brand",
        category = "Snacks",
        status = HalalStatus.SUPHELI,
        harmfulOrSuspiciousIngredients = listOf("E471 Mono- and Diglycerides"),
        language = AppLanguage.EN
    )

    @Test
    fun `subject includes the product name`() {
        val subject = ReportEmail.buildSubject(product, AppLanguage.EN)
        assertTrue(subject.contains("Test Snack"))
    }

    @Test
    fun `body includes barcode, product, status and flagged ingredients`() {
        val body = ReportEmail.buildBody(product, AppLanguage.EN)
        assertTrue(body.contains("1234567890123"))
        assertTrue(body.contains("Test Snack"))
        assertTrue(body.contains("Test Brand"))
        assertTrue(body.contains("E471 Mono- and Diglycerides"))
    }

    @Test
    fun `body omits the flagged ingredients line when there are none`() {
        val clean = product.copy(status = HalalStatus.HELAL, harmfulOrSuspiciousIngredients = emptyList())
        val body = ReportEmail.buildBody(clean, AppLanguage.EN)
        assertEquals(false, body.contains("Flagged"))
    }

    @Test
    fun `support email is the developer contact address`() {
        assertEquals("fatihgulen@gmail.com", ReportEmail.SUPPORT_EMAIL)
    }
}
```

- [ ] **Step 2: Run test to verify it fails**

Run: `gradlew.bat testDebugUnitTest --tests "com.example.data.model.ReportEmailTest"`
Expected: FAIL with "Unresolved reference: ReportEmail" (file doesn't exist yet).

- [ ] **Step 3: Create the implementation**

Create `app/src/main/java/com/example/data/model/ReportEmail.kt`:

```kotlin
package com.example.data.model

object ReportEmail {

    const val SUPPORT_EMAIL = "fatihgulen@gmail.com"

    fun buildSubject(product: FoodProduct, language: AppLanguage): String =
        "${AppStrings.getReportErrorEmailSubject(language)}: ${product.name}"

    fun buildBody(product: FoodProduct, language: AppLanguage): String = buildString {
        append("${AppStrings.getShareProductLabel(language)}: ${product.name} (${product.brand})\n")
        append("${AppStrings.getShareBarcodeLabel(language)}: ${product.barcode}\n")
        append("${AppStrings.getShareStatusLabel(language)}: ${AppStrings.getStatusLabel(product.status, language)}\n")
        if (product.harmfulOrSuspiciousIngredients.isNotEmpty()) {
            append(
                "${AppStrings.getShareFlaggedIngredientsLabel(language)}: " +
                    "${product.harmfulOrSuspiciousIngredients.joinToString(", ")}\n"
            )
        }
        append("\n${AppStrings.getReportErrorPromptLine(language)}\n")
    }
}
```

- [ ] **Step 4: Run test to verify it passes**

Run: `gradlew.bat testDebugUnitTest --tests "com.example.data.model.ReportEmailTest"`
Expected: PASS.

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/example/data/model/ReportEmail.kt app/src/test/java/com/example/data/model/ReportEmailTest.kt
git commit -m "feat: add pure builder for report-error email content"
```

---

### Task 5: Wire the "Report Error" button into the product result screen

**Files:**
- Modify: `app/src/main/java/com/example/ui/components/ProductResultView.kt`

**Interfaces:**
- Consumes: `ReportEmail.SUPPORT_EMAIL`, `ReportEmail.buildSubject`, `ReportEmail.buildBody` (Task 4), `AppStrings.getReportError` (Task 3).
- Produces: nothing consumed by other tasks (UI leaf).

- [ ] **Step 1: Add imports**

In `ProductResultView.kt`, add to the import block (alongside the existing `androidx.compose.material.icons.filled.*` imports and `import android.content.Intent`):

```kotlin
import android.content.ActivityNotFoundException
import android.net.Uri
import android.widget.Toast
import androidx.compose.material.icons.filled.Flag
import com.example.data.model.ReportEmail
```

- [ ] **Step 2: Add the Report button to the bottom action row**

In `ProductResultView.kt`, inside `ProductResultBottomSheet`, the bottom action `Row` currently has two children: the "Scan Again" `Button` and the Share `FilledTonalButton` (lines 239-291). Add a third `FilledTonalButton` after the Share button, inside the same `Row`:

```kotlin
                    FilledTonalButton(
                        onClick = {
                            val reportIntent = Intent(Intent.ACTION_SENDTO).apply {
                                data = Uri.parse("mailto:")
                                putExtra(Intent.EXTRA_EMAIL, arrayOf(ReportEmail.SUPPORT_EMAIL))
                                putExtra(Intent.EXTRA_SUBJECT, ReportEmail.buildSubject(product, language))
                                putExtra(Intent.EXTRA_TEXT, ReportEmail.buildBody(product, language))
                            }
                            try {
                                context.startActivity(reportIntent)
                            } catch (e: ActivityNotFoundException) {
                                Toast.makeText(
                                    context,
                                    "${ReportEmail.SUPPORT_EMAIL}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        },
                        modifier = Modifier
                            .height(52.dp)
                            .testTag("report_error_button"),
                        shape = CircleShape
                    ) {
                        Icon(imageVector = Icons.Default.Flag, contentDescription = AppStrings.getReportError(language))
                    }
```

- [ ] **Step 3: Build the app**

Run: `gradlew.bat assembleDebug`
Expected: BUILD SUCCESSFUL.

- [ ] **Step 4: Manual verification**

Install the debug build on an emulator or device (`gradlew.bat installDebug`), scan or search any product to open the result sheet, tap the new flag icon in the bottom row, and confirm:
- An email app chooser (or the default mail app) opens.
- The "To" field is pre-filled with `fatihgulen@gmail.com`.
- The subject contains the product name.
- The body contains the barcode, product/brand, status, flagged ingredients (if any), and the prompt line — all in the currently selected app language.

This screen has no existing Compose UI test coverage (only `HalalAnalyzer`'s pure logic is unit-tested in this codebase) — manual verification is the appropriate check here, matching the established pattern.

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/example/ui/components/ProductResultView.kt
git commit -m "feat: add report-error button to the product result screen"
```

---

## Self-Review Notes

- **Spec coverage:** Faz 0 spec items — (1) visible source-based reasoning → Tasks 1-2 (fixes flow directly into the already-existing `FlaggedProblematicItemCard` reason display, no UI change needed); (2) continued accuracy/regression fixes → Tasks 1-2, following the exact pattern already established in `HalalAnalyzerTest.kt`; (3) community "Report Error" button → Tasks 3-5. All three Faz 0 spec bullets are covered.
- **Placeholder scan:** No TBD/TODO; every step has real code.
- **Type consistency:** `ReportEmail.buildSubject`/`buildBody` signatures match between Task 4 (definition) and Task 5 (call site). `BEEF_FISH_GELATIN_RULE_ID` is defined once (Task 1, Step 3) and referenced once (Task 1, Step 5).
