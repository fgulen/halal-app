package com.example.demo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.data.local.AppDatabase
import com.example.data.local.ScanHistoryEntity
import com.example.data.local.toEntity
import com.example.data.model.AppLanguage
import com.example.data.model.Madhhab
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Debug-only: replaces scan history with the fictional demo catalogue so screenshots and the
 * promo video never show test scans or real brands.
 *
 *   adb shell am broadcast -n com.fatihgulen.helalrehberi/com.example.demo.DemoSeedReceiver \
 *     --es lang de --es madhhab none
 *
 * Products are also written to the products cache in the requested language/madhhab, so typing a
 * demo barcode in the app opens its real result screen (the repository is cache-first).
 */
class DemoSeedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val language = AppLanguage.values().find { it.code == intent.getStringExtra("lang") } ?: AppLanguage.EN
        val madhhab = Madhhab.values().find { it.code == intent.getStringExtra("madhhab") } ?: Madhhab.UNSPECIFIED
        val pending = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val dao = AppDatabase.getDatabase(context).productDao()
                dao.clearScanHistory()
                val now = System.currentTimeMillis()
                // Oldest first, so the first catalogue item ends up at the top of "Recent scans".
                DemoProducts.items.withIndex().reversed().forEach { (index, item) ->
                    val product = item.analyze(language, madhhab)
                    dao.insertProduct(product.toEntity())
                    dao.insertScanHistory(
                        ScanHistoryEntity.fromDomain(product).copy(scannedAt = now - index * 47 * 60_000L)
                    )
                    Log.i(TAG, "seeded ${item.barcode} ${product.name} -> ${product.status}")
                }
            } finally {
                pending.finish()
            }
        }
    }

    private companion object {
        const val TAG = "DemoSeed"
    }
}
