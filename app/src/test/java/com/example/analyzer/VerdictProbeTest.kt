package com.example.analyzer

import com.example.data.analyzer.HalalAnalyzer
import com.example.data.model.AppLanguage
import com.example.data.model.Madhhab
import org.junit.Assume
import org.junit.Test
import java.io.File

// Permanent verdict probe. It does nothing unless VERDICT_PROBE_INPUT points to a TSV written by
// tools/verdict_probe/fetch.ps1 (columns: code, name, category, ingredients). Normal test runs skip it.
// Output: <input>.report.tsv with the verdict under UNSPECIFIED and HANAFI for every row.
class VerdictProbeTest {

    // Gradle's daemon does not pass new environment variables to test JVMs, so the input path is read
    // from a file written by run.ps1 (tools/verdict_probe/out/.probe-input). The env var still works.
    private fun inputPath(): String? {
        System.getenv("VERDICT_PROBE_INPUT")?.takeIf { it.isNotBlank() }?.let { return it }
        val pointer = File("../tools/verdict_probe/out/.probe-input")
        // trimStart: PowerShell's utf8 encoding writes a BOM that would otherwise stay in the path.
        return if (pointer.exists()) pointer.readText(Charsets.UTF_8).trimStart('﻿').trim().takeIf { it.isNotBlank() } else null
    }

    @Test
    fun probe() {
        val input = inputPath()
        Assume.assumeTrue("run tools/verdict_probe/run.ps1 to run the probe", !input.isNullOrBlank())
        val source = File(input!!)
        val rows = source.readLines(Charsets.UTF_8)
            .drop(1) // header
            .map { it.trimStart('﻿').split('\t') }
            .filter { it.size >= 4 && it[3].isNotBlank() }

        val report = StringBuilder("code\tname\tcategory\tunspecified\thanafi\tharmful\treason\n")
        for (cols in rows) {
            val (code, name, category, ingredients) = cols
            val u = HalalAnalyzer.analyzeIngredientsText(name, ingredients, code, AppLanguage.DE, Madhhab.UNSPECIFIED)
            val h = HalalAnalyzer.analyzeIngredientsText(name, ingredients, code, AppLanguage.DE, Madhhab.HANAFI)
            val harmful = (u.harmfulOrSuspiciousIngredients).joinToString(" | ").replace('\t', ' ')
            val reason = u.reasonOrDetails.replace('\n', ' ').replace('\t', ' ').take(160)
            report.append("$code\t${name.replace('\t', ' ')}\t$category\t${u.status}\t${h.status}\t$harmful\t$reason\n")
        }
        File(source.path.removeSuffix(".tsv") + ".report.tsv").writeText(report.toString(), Charsets.UTF_8)
    }
}
