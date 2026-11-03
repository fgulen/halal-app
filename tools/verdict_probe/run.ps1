# One-command verdict probe: fetch random OFF products, run the analyzer on each under UNSPECIFIED and
# HANAFI, and print a summary plus the rows that need a human look.
#
# Usage:  powershell -ExecutionPolicy Bypass -File tools/verdict_probe/run.ps1 -Count 60 [-Seed 42]
#
# Rows flagged "REVIEW" are HELAL verdicts whose ingredient text contains a term that the rules treat as
# doubtful or haram elsewhere (alcohol, aroma, gelatin, whey, rennet, pork, carmine). A human decides
# whether the verdict is right; the probe only points at them.

param(
    [int]$Count = 40,
    [int]$Seed = -1
)

$ErrorActionPreference = "Stop"
$root = Resolve-Path (Join-Path $PSScriptRoot "..\..")
Set-Location $root

$tsv = & powershell -NoProfile -ExecutionPolicy Bypass -File (Join-Path $PSScriptRoot "fetch.ps1") -Count $Count -Seed $Seed |
    Select-Object -Last 1
if (-not (Test-Path $tsv)) { throw "fetch produced no file" }

$pointer = Join-Path $PSScriptRoot "out\.probe-input"
Set-Content -Path $pointer -Value $tsv -Encoding utf8
$env:JAVA_HOME = "C:\Android\jdk-21"
$env:ANDROID_HOME = "C:\Android\Sdk"
# Gradle caches test results and does not treat environment variables as inputs, so clean first;
# otherwise the probe can be skipped as up-to-date.
& .\gradlew.bat cleanTestDebugUnitTest testDebugUnitTest --tests "com.example.analyzer.VerdictProbeTest" --console=plain -q
$exit = $LASTEXITCODE
# Remove the pointer so ordinary test runs skip the probe again.
Remove-Item -Path $pointer -ErrorAction SilentlyContinue
if ($exit -ne 0) { throw "probe run failed (exit $exit)" }

$report = $tsv -replace "\.tsv$", ".report.tsv"
$rows = Import-Csv -Path $report -Delimiter "`t"

Write-Output ""
Write-Output "== Summary (UNSPECIFIED / HANAFI) =="
$rows | Group-Object unspecified | ForEach-Object { "{0,-10} UNSPECIFIED: {1}" -f $_.Name, $_.Count }
$rows | Group-Object hanafi | ForEach-Object { "{0,-10} HANAFI:      {1}" -f $_.Name, $_.Count }
$diff = @($rows | Where-Object { $_.unspecified -ne $_.hanafi })
Write-Output "Rows where madhhab changes the verdict: $($diff.Count)"

$riskPattern = "alkohol|alcohol|alcool|vanill|aroma|aromen|ethanol|gelatin|gelatine|molke|whey|lab\b|schwein|pork|carmin|e120|e471|e441|rum\b|wein|bier"
$ingredientsByCode = @{}
Import-Csv -Path $tsv -Delimiter "`t" | ForEach-Object { $ingredientsByCode[$_.code] = $_.ingredients }
$review = @($rows | Where-Object {
    $text = "$($_.name) $($_.harmful) $($ingredientsByCode[$_.code])"
    $_.unspecified -eq "HELAL" -and $text -match $riskPattern
})
Write-Output ""
Write-Output "== REVIEW (HELAL but risky term present in name/flags) =="
$review | ForEach-Object { "{0} | {1} | {2}" -f $_.code, $_.name, $_.category }
Write-Output ""
Write-Output "Full report: $report"
