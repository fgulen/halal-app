# Fetches random food and drink products with ingredient lists from OpenFoodFacts (German site)
# and writes them as a TSV for the verdict probe.
#
# Usage:  powershell -ExecutionPolicy Bypass -File tools/verdict_probe/fetch.ps1 -Count 60 [-Seed 42]
# Output: tools/verdict_probe/out/probe-<timestamp>.tsv   (gitignored; OFF data is ODbL)
#
# Sampling: random category, random result page, random product per page. Use -Seed to repeat a run.

param(
    [int]$Count = 40,
    [int]$Seed = -1,
    [string]$Lang = "de"
)

$ErrorActionPreference = "Stop"
$categories = @(
    "chocolates", "biscuits", "ice-creams", "breakfast-cereals", "chips-and-crisps", "gummy-candies",
    "yogurts", "cheeses", "soft-drinks", "fruit-juices", "energy-drinks", "beers", "wines",
    "sauces", "breads", "soups", "pastas", "desserts", "spreads", "plant-based-foods",
    "frozen-pizzas", "sausages", "breakfast-spreads", "candies", "tea", "coffee", "snacks"
)

$rng = if ($Seed -ge 0) { New-Object System.Random $Seed } else { New-Object System.Random }
$outDir = Join-Path $PSScriptRoot "out"
New-Item -ItemType Directory -Force -Path $outDir | Out-Null
$stamp = Get-Date -Format "yyyyMMdd-HHmmss"
$outFile = Join-Path $outDir "probe-$stamp.tsv"

$rows = New-Object System.Collections.Generic.List[string]
$rows.Add("code`tname`tcategory`tingredients")
$seen = @{}
$attempts = 0
$errors = 0

# The category endpoint returned 503 during testing; the search endpoint with a category tag works.
while ($rows.Count - 1 -lt $Count -and $attempts -lt ($Count * 4)) {
    $attempts++
    $cat = $categories[$rng.Next($categories.Count)]
    $page = $rng.Next(1, 6)
    $url = "https://$Lang.openfoodfacts.org/cgi/search.pl?action=process&tagtype_0=categories&tag_contains_0=contains&tag_0=$cat" +
           "&page=$page&page_size=20&json=1&fields=code,product_name,brands,ingredients_text_de,ingredients_text"
    $r = $null
    for ($try = 1; $try -le 3 -and -not $r; $try++) {
        try { $r = Invoke-RestMethod -Uri $url -TimeoutSec 60 }
        catch { Start-Sleep -Seconds (3 * $try) }
    }
    if (-not $r) { $errors++; continue }
    $candidates = @($r.products | Where-Object { $_.code -and ($_.ingredients_text_de -or $_.ingredients_text) })
    if ($candidates.Count -eq 0) { continue }
    $p = $candidates[$rng.Next($candidates.Count)]
    if ($seen.ContainsKey($p.code)) { continue }
    $seen[$p.code] = $true

    $ing = if ($p.ingredients_text_de) { $p.ingredients_text_de } else { $p.ingredients_text }
    $name = ("$($p.brands) $($p.product_name)").Trim() -replace "[\t\r\n]+", " "
    $ing = $ing -replace "[\t\r\n]+", " "
    $rows.Add("$($p.code)`t$name`t$cat`t$ing")
    Start-Sleep -Milliseconds 1500
}

$rows | Set-Content -Path $outFile -Encoding utf8
Write-Output "Wrote $($rows.Count - 1) products to $outFile (failed page requests: $errors)"
if ($rows.Count - 1 -eq 0) { throw "no products fetched; OFF may be down (see failed page requests above)" }
Write-Output $outFile
