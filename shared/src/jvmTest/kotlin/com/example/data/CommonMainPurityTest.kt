package com.example.data

import java.io.File
import kotlin.test.Test
import kotlin.test.assertTrue

/**
 * commonMain yalnızca Kotlin çekirdeği/ortak kütüphaneleri kullanmalı; JVM'e özgü
 * çağrılar iOS/native derlemesini bozar. Windows'ta native derleme yapılamadığı için
 * bu test kaynakları tarar. Gerçek iOS derlemesi CI'da (P4) ayrıca yapılır.
 */
class CommonMainPurityTest {

    private val forbidden = listOf(
        Regex("""(?<![\w.])System\."""),
        Regex("""\bjava\."""),
        Regex("""\bjavax\."""),
        Regex("""\bThread\b"""),
        Regex("""\bsynchronized\("""),
        Regex("""\bCharacter\."""),
        Regex("""\bMath\."""),
        Regex("""String\.format"""),
        Regex("""\.format\("""),
        Regex("""@JvmStatic|@JvmName|@Throws"""),
        Regex("""\bandroid\."""),
        Regex("""\borg\.json\b"""),
    )

    @Test
    fun commonMainHasNoJvmOnlyCalls() {
        val root = File("src/commonMain/kotlin")
        assertTrue(root.isDirectory, "commonMain kaynak klasörü bulunamadı: ${root.absolutePath}")
        val violations = mutableListOf<String>()
        root.walkTopDown().filter { it.isFile && it.extension == "kt" }.forEach { file ->
            file.readLines().forEachIndexed { index, raw ->
                val line = raw.trim()
                if (line.startsWith("//") || line.startsWith("*") || line.startsWith("/*")) return@forEachIndexed
                if (forbidden.any { it.containsMatchIn(line) }) {
                    violations += "${file.name}:${index + 1}: $line"
                }
            }
        }
        assertTrue(violations.isEmpty(), "JVM-only çağrı(lar) commonMain'de:\n" + violations.joinToString("\n"))
    }
}
