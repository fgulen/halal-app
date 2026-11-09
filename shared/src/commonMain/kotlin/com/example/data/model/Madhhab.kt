package com.example.data.model

// The user's chosen school of fiqh. UNSPECIFIED is the default and must reproduce the app's
// original (cautious) verdicts exactly; the other values only change a verdict where a sourced
// override exists for that madhhab (see docs/superpowers/plans/2026-10-07-madhhab-setting-and-off-5xx-fix.md).
enum class Madhhab(val code: String) {
    UNSPECIFIED("none"),
    HANAFI("hanafi"),
    SHAFII("shafii"),
    MALIKI("maliki"),
    HANBALI("hanbali")
}
