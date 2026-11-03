# Verdict probe

Runs the halal analyzer on random real products from OpenFoodFacts and reports the verdict under
both madhhab settings. Use it after any rule change to see what moved and to find HELAL verdicts
that need a human look.

## Run

    powershell -ExecutionPolicy Bypass -File tools/verdict_probe/run.ps1 -Count 60
    powershell -ExecutionPolicy Bypass -File tools/verdict_probe/run.ps1 -Count 60 -Seed 42   # repeatable

Outputs go to `tools/verdict_probe/out/` (gitignored). `-Seed` makes the sample repeatable.

## Parts

- `fetch.ps1`: random category + page + product from de.openfoodfacts.org. Writes `probe-<time>.tsv`.
- `app/src/test/.../VerdictProbeTest.kt`: runs the analyzer. Skips itself unless `VERDICT_PROBE_INPUT` is set,
  so normal `testDebugUnitTest` runs are unaffected.
- `run.ps1`: fetch, run, summary, and a REVIEW list.

## Reading the result

- The summary counts verdicts under UNSPECIFIED and HANAFI and lists rows where madhhab changes the verdict.
- REVIEW lists HELAL verdicts whose name, flags or ingredients contain a risky term (alcohol, aroma, gelatin,
  whey, rennet, pork, carmine, ...). The probe does not decide these; a person checks each one.
- A random sample is not a benchmark. Use it to find problems, then write a unit test for each case.

## Rules for changing the analyzer

1. Find a primary source first (see the commit log for examples). Quote it and keep the URL.
2. Write the unit test first, with a comment naming the source.
3. Re-run the probe, compare with the previous report, and explain every changed row.
4. Do not bulk-update existing test expectations. Justify each one.
