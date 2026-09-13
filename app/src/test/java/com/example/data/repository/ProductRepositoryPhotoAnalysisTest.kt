package com.example.data.repository

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.AppDatabase
import com.example.data.model.AppLanguage
import com.example.data.model.HalalStatus
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

// sdk 34 (not 36): Robolectric's SDK-36 shadow requires Java 21, but this toolchain runs
// Java 17 (see ExampleRobolectricTest's identical pre-existing failure at sdk=36). minSdk is
// 24, so 34 is a valid, already-Java-17-compatible shadow for this DB-only test.
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ProductRepositoryPhotoAnalysisTest {

    private lateinit var database: AppDatabase
    private lateinit var repository: ProductRepository

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        repository = ProductRepository(database.productDao())
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `photo analysis flags haram ingredient and records to history`() = runTest {
        val result = repository.analyzeIngredientsPhoto(
            productName = "Test Snack",
            ingredientsText = "water, pork gelatin, sugar",
            language = AppLanguage.EN
        )

        assertEquals(HalalStatus.HARAM, result.status)
        assertEquals("Test Snack", result.name)
        assertTrue("expected a synthetic PHOTO- barcode, got '${result.barcode}'", result.barcode.startsWith("PHOTO-"))

        val history = repository.getScanHistory().first()
        assertEquals(1, history.size)
        assertEquals(result.barcode, history.first().barcode)
    }

    @Test
    fun `blank product name falls back to localized default`() = runTest {
        val result = repository.analyzeIngredientsPhoto(
            productName = "",
            ingredientsText = "water, sugar",
            language = AppLanguage.TR
        )

        assertEquals("Fotoğraftan Analiz", result.name)
    }
}
