package com.example.data.local

import com.example.data.analyzer.HalalAnalyzer
import com.example.data.model.HalalStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * The E-codes guide merges hand-written entries with a generated long tail (tools/e_additives).
 * These checks keep the two halves from overlapping and keep the guide agreeing with the scanner.
 */
class EAdditiveDirectoryTest {

  // "E472a-f" -> E472, E472a..E472f; "E432-436" -> E432..E436; "E470a/E470b" -> both
  private fun expand(code: String): List<String> = code.split("/").flatMap { part ->
    val numRange = Regex("""E(\d+)-(\d+)""").matchEntire(part)
    val letterRange = Regex("""E(\d+)([a-z])-([a-z])""").matchEntire(part)
    when {
      numRange != null -> (numRange.groupValues[1].toInt()..numRange.groupValues[2].toInt()).map { "E$it" }
      letterRange != null -> {
        val (num, from, to) = letterRange.destructured
        listOf("E$num") + (from[0]..to[0]).map { "E$num$it" }
      }
      else -> listOf(part)
    }
  }

  private val statusByCode: Map<String, HalalStatus> by lazy {
    InitialData.eAdditivesDirectory.flatMap { item -> expand(item.code).map { it to item.status } }.toMap()
  }

  @Test
  fun noCodeIsListedTwice() {
    val duplicates = InitialData.eAdditivesDirectory
      .flatMap { expand(it.code) }
      .groupingBy { it }.eachCount()
      .filterValues { it > 1 }
    assertTrue("E-codes listed more than once in the guide: ${duplicates.keys}", duplicates.isEmpty())
  }

  @Test
  fun guideHasTheFullCatalogue() {
    assertTrue(InitialData.eAdditivesDirectory.size > 400)
  }

  // A scanner rule that flags an E-code must not be contradicted by the guide's verdict for it.
  @Test
  fun guideAgreesWithScannerRules() {
    val haramCodes = HalalAnalyzer.HARAM_RULES.mapNotNull { it.eCode }.toSet()
    for (code in haramCodes) {
      assertEquals("Guide status for $code", HalalStatus.HARAM, statusByCode[code])
    }
    for (code in HalalAnalyzer.SUSPICIOUS_RULES.mapNotNull { it.eCode }.toSet() - haramCodes) {
      assertEquals("Guide status for $code", HalalStatus.SUPHELI, statusByCode[code])
    }
  }

  @Test
  fun everyNameIsFilledInEveryLanguage() {
    for (item in InitialData.eAdditivesDirectory) {
      val n = item.name
      assertTrue("Blank name for ${item.code}", listOf(n.en, n.de, n.fr, n.tr, n.ar).all { it.isNotBlank() })
    }
  }
}
