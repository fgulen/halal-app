package com.example

import com.example.data.local.InitialData
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Plain JUnit (no Robolectric/Android framework needed) checks over the curated
 * [InitialData.sampleProducts] list itself.
 */
class InitialDataIntegrityTest {

  // Guards against the copy-paste class of bug found 2026-08-30: a product's imageUrl gets
  // copied from a neighboring entry instead of pointing at its own barcode's photo (e.g. a
  // "Kraft Marshmallows" entry showing Haribo's image). Two entries may legitimately share an
  // imageUrl only when they're the same physical product under different barcode encodings
  // (e.g. a 12-digit UPC vs its 13-digit EAN padding) - i.e. their digits agree once leading
  // zeros are stripped.
  @Test
  fun noSampleProductImageIsCopiedFromADifferentProduct() {
    val groups = InitialData.sampleProducts
      .filter { !it.imageUrl.isNullOrBlank() }
      .groupBy { it.imageUrl }

    for ((url, entries) in groups) {
      if (entries.size <= 1) continue
      val distinctProducts = entries.map { it.barcode.filter { c -> c.isDigit() }.trimStart('0') }.distinct()
      assertTrue(
        "imageUrl is shared by barcodes that are not the same physical product " +
          "(likely copy-pasted from a different entry): url=$url entries=${entries.map { "${it.barcode} (${it.name})" }}",
        distinctProducts.size == 1
      )
    }
  }

  // Every curated barcode must be unique - a duplicate primary key means one entry silently
  // overwrites the other when the list is inserted, and only one of the two products' data is
  // ever reachable.
  @Test
  fun noSampleProductBarcodeIsDuplicated() {
    val duplicates = InitialData.sampleProducts
      .groupBy { it.barcode }
      .filterValues { it.size > 1 }
    assertTrue(
      "Duplicate barcodes in InitialData.sampleProducts: ${duplicates.keys}",
      duplicates.isEmpty()
    )
  }
}
