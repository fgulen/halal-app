"""Generate app/src/main/java/com/example/data/local/EAdditivesGenerated.kt.

Source: Open Food Facts additives taxonomy (ODbL), downloaded 2026-10-23 from
https://static.openfoodfacts.org/data/taxonomies/additives.json

Usage:  python tools/e_additives/generate.py <path/to/additives.json>

Status rule (vegetarian flag from OFF, then the OVERRIDES below):
  vegetarian yes + vegan yes  -> HELAL / SAFE
  vegetarian yes + vegan not  -> HELAL / LIVING_ANIMAL (milk, egg, bees, wool)
  vegetarian maybe            -> SUPHELI / MIXED
  vegetarian no               -> SUPHELI / ANIMAL
  no flag                     -> HELAL / SAFE (all synthetic polymers/compounds; reviewed by hand)
HARAM is only ever set by hand. Codes already curated in InitialData.kt are skipped.
"""
import json
import re
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[2]
OUT = ROOT / "app/src/main/java/com/example/data/local/EAdditivesGenerated.kt"
NAMES = Path(__file__).with_name("names.tsv")

# Covered by the hand-written entries in InitialData.eAdditivesDirectory (ranges expanded)
CURATED = {"E441", "E120", "E471", "E472", "E904", "E920", "E631", "E542", "E100", "E322", "E330",
           "E407", "E412", "E415", "E162", "E160a", "E422", "E432", "E433", "E434", "E435", "E436",
           "E470", "E470a", "E470b", "E473", "E475", "E481", "E482", "E491", "E492", "E493", "E494",
           "E495", "E570", "E627", "E635", "E640"}
# E428 = gelatine (the app lists it as E441); E638/E931: OFF has no real name for them
SKIP = {"E428", "E638", "E931"}

GLYCEROL = ("SUPHELI", "GLYCEROL")
OVERRIDES = {
    "E1510": ("HARAM", "ALCOHOL"),          # analyzer alcohol rule is HARAM
    "E963": ("HELAL", "LIVING_ANIMAL"),     # tagatose is made from milk sugar; OFF "no" is wrong
    "E101": ("HELAL", "SAFE"),              # riboflavin: synthetic/fermentation
    "E375": ("HELAL", "SAFE"),              # nicotinic acid: synthetic
    "E913": ("HELAL", "LIVING_ANIMAL"),     # lanolin: sheep's wool
    "E909": ("SUPHELI", "ANIMAL"),          # spermaceti: sperm whale
    "E429": ("SUPHELI", "ANIMAL"),          # peptone: meat or casein digest
    "E921": ("SUPHELI", "ANIMAL"),          # L-cysteine HCl: like E920 (hair/feathers)
    # nucleotides and lipase: yeast/microbial or meat, fish, pancreas
    "E626": ("SUPHELI", "UNSPECIFIED"), "E628": ("SUPHELI", "UNSPECIFIED"), "E629": ("SUPHELI", "UNSPECIFIED"),
    "E630": ("SUPHELI", "UNSPECIFIED"), "E632": ("SUPHELI", "UNSPECIFIED"), "E633": ("SUPHELI", "UNSPECIFIED"),
    "E634": ("SUPHELI", "UNSPECIFIED"), "E1104": ("SUPHELI", "UNSPECIFIED"),
    "E160": ("SUPHELI", "CARRIER"),
    "E161": ("SUPHELI", "CARRIER"),
    "E571": ("SUPHELI", "MIXED"),           # stearates: fatty acids, like E570
    "E573": ("SUPHELI", "MIXED"),
    "E911": ("SUPHELI", "MIXED"),
    "E488": ("SUPHELI", "MIXED"),           # mono-/diglycerides, like E471
    # glycerol-based, same verdict as E422
    "E383": GLYCEROL, "E445": GLYCEROL, "E476": GLYCEROL, "E1411": GLYCEROL, "E1423": GLYCEROL,
    "E1430": GLYCEROL, "E1441": GLYCEROL, "E1443": GLYCEROL, "E1517": GLYCEROL, "E1518": GLYCEROL,
    # tartaric acid and salts: istihala, like vinegar
    "E334": ("HELAL", "TARTARIC"), "E335": ("HELAL", "TARTARIC"), "E336": ("HELAL", "TARTARIC"),
    "E337": ("HELAL", "TARTARIC"), "E353": ("HELAL", "TARTARIC"), "E354": ("HELAL", "TARTARIC"),
}


def group_for(n: int) -> str:
    if n < 200: return "COLOUR"
    if n < 300: return "PRESERVATIVE"
    if n < 400: return "ANTIOXIDANT"
    if n < 500: return "THICKENER"
    if n < 600: return "MINERAL"
    if n < 700: return "FLAVOUR"
    if n < 800: return "ANTIBIOTIC"
    if n <= 915: return "GLAZING"
    if n <= 931: return "FLOUR"
    if n < 950: return "GAS"
    if n < 999: return "SWEETENER"
    if n == 999: return "THICKENER"
    if 1100 <= n < 1200: return "ENZYME"
    if 1200 <= n < 1300: return "BULKING"
    if 1400 <= n < 1500: return "STARCH"
    if n >= 1500: return "SOLVENT"
    return "OTHER"


def clean(name):
    if not name:
        return None
    name = re.sub(r"^E\s?\d+[a-z]*\s*[-–]\s*", "", name, flags=re.I).strip().replace("‚", ",")
    if not name or re.fullmatch(r"E\d+.*", name, flags=re.I):
        return None
    return name[0].upper() + name[1:]


def kt(s: str) -> str:
    return '"' + s.replace("\\", "\\\\").replace('"', '\\"').replace("$", "\\$") + '"'


def main(src: str):
    data = json.loads(Path(src).read_text(encoding="utf-8"))
    names = {}
    for line in NAMES.read_text(encoding="utf-8").splitlines():
        if not line or line.startswith("#"):
            continue
        code, en, tr, ar = (line.split("\t") + ["", "", ""])[:4]
        names[code] = (en, tr, ar)

    entries = []
    for key, v in data.items():
        m = re.fullmatch(r"en:e(\d{3,4})", key)
        if not m:
            continue
        code = f"E{m[1]}"
        if code in CURATED or code in SKIP:
            continue
        if code not in names:
            sys.exit(f"{code} has no row in names.tsv")
        en_fix, tr, ar = names[code]
        en = en_fix or clean(v["name"].get("en"))
        de = clean(v["name"].get("de")) or en
        fr = clean(v["name"].get("fr")) or en
        ar = ar or clean(v["name"].get("ar")) or en
        if not (en and tr):
            sys.exit(f"{code}: missing en/tr name")

        if code in OVERRIDES:
            status, kind = OVERRIDES[code]
        else:
            veg = v.get("vegetarian", {}).get("en")
            vegan = v.get("vegan", {}).get("en")
            if veg == "maybe":
                status, kind = "SUPHELI", "MIXED"
            elif veg == "no":
                status, kind = "SUPHELI", "ANIMAL"
            elif veg == "yes" and vegan != "yes":
                status, kind = "HELAL", "LIVING_ANIMAL"
            else:
                status, kind = "HELAL", "SAFE"

        # Letter sub-codes (E150d, E160c...) as search aliases; roman-numeral variants are noise
        subs = sorted(
            k[3:].upper()[:-1] + k[-1] for k in data
            if re.fullmatch(rf"en:e{m[1]}[a-h]", k) and (k[3:].upper()[:-1] + k[-1]) not in CURATED
        )
        alt = [en] + subs
        entries.append((int(m[1]), code, (en, de, fr, tr, ar), status, kind, group_for(int(m[1])), alt))

    entries.sort()
    chunk = 60
    parts = [entries[i:i + chunk] for i in range(0, len(entries), chunk)]
    out = [
        "package com.example.data.local",
        "",
        "import com.example.data.local.EAdditiveTemplates.Group",
        "import com.example.data.local.EAdditiveTemplates.Kind",
        "import com.example.data.local.EAdditiveTemplates.entry",
        "import com.example.data.model.EAdditive",
        "import com.example.data.model.HalalStatus",
        "import com.example.data.model.LocalizedText",
        "",
        "// GENERATED by tools/e_additives/generate.py from the Open Food Facts additives taxonomy (ODbL).",
        "// Do not edit by hand: change names.tsv or the script's overrides and regenerate.",
        "// Split into chunks so no single method exceeds the JVM's 64 KB bytecode limit.",
        "internal object EAdditivesGenerated {",
        "",
        "    val all: List<EAdditive> by lazy { " + " + ".join(f"part{i + 1}()" for i in range(len(parts))) + " }",
    ]
    for i, part in enumerate(parts):
        out += ["", f"    private fun part{i + 1}(): List<EAdditive> = listOf("]
        rows = []
        for _, code, (en, de, fr, tr, ar), status, kind, group, alt in part:
            rows.append(
                f"        entry({kt(code)}, LocalizedText({kt(en)}, {kt(de)}, {kt(fr)}, {kt(tr)}, {kt(ar)}), "
                f"HalalStatus.{status}, Kind.{kind}, Group.{group}, listOf({', '.join(kt(a) for a in alt)}))"
            )
        out.append(",\n".join(rows))
        out.append("    )")
    out += ["}", ""]
    OUT.write_text("\n".join(out), encoding="utf-8", newline="\n")

    from collections import Counter
    print(len(entries), "entries", Counter(e[3] for e in entries))
    print("non-HELAL:", [(e[1], e[3], e[4]) for e in entries if e[3] != "HELAL"])
    print("fallback to EN: de", sum(1 for e in entries if e[2][1] == e[2][0]),
          "fr", sum(1 for e in entries if e[2][2] == e[2][0]),
          "ar", sum(1 for e in entries if e[2][4] == e[2][0]))


if __name__ == "__main__":
    main(sys.argv[1])
