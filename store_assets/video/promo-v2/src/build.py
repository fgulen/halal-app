"""Build index.html for one language from src/template.html.
Usage: python -I src/build.py en          -> promo-v2/index.html
       python -I src/build.py de tr fr ar -> ../promo-v2-<lang>/ (own HyperFrames project, assets copied)
Line starts are laid out from the measured voiceover durations, and cue times come from the
edge-tts word boundaries in lines.json, so every language is timed to its own narration."""
import json, os, shutil, subprocess, sys

HERE = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
VIDEO = os.path.dirname(HERE)
GAP, LEAD_IN, TAIL = 0.25, 0.4, 1.3
LANG_NAMES = {"en": "English", "de": "Deutsch", "fr": "Français", "ar": "العربية"}

# cue -> (line, word index); l1..l4 are the four label languages in the order they are spoken
CUES = {
    "en": {"know": (1, 7), "scan": (2, 1), "halal": (3, 10), "doubtful": (3, 11), "haram": (3, 13), "exactly": (4, 3), "problem": (4, 8),
           "madhhab": (5, 2), "results": (5, 7), "l1": (6, 4), "l2": (6, 5), "l3": (6, 6), "l4": (6, 8), "enum": (7, 7),
           "p1": (8, 2), "p2": (8, 3), "p3": (8, 4)},
    "de": {"know": (1, 6), "scan": (2, 0), "halal": (3, 10), "doubtful": (3, 11), "haram": (3, 13), "exactly": (4, 3), "problem": (4, 7),
           "madhhab": (5, 3), "results": (5, 8), "l1": (6, 4), "l2": (6, 5), "l3": (6, 6), "l4": (6, 8), "enum": (7, 6),
           "p1": (8, 2), "p2": (8, 3), "p3": (8, 4)},
    "tr": {"know": (1, 4), "scan": (2, 0), "halal": (3, 9), "doubtful": (3, 10), "haram": (3, 13), "exactly": (4, 5), "problem": (4, 7),
           "madhhab": (5, 1), "results": (5, 6), "l1": (6, 1), "l2": (6, 2), "l3": (6, 3), "l4": (6, 5), "enum": (7, 6),
           "p1": (8, 2), "p2": (8, 3), "p3": (8, 5)},
    "fr": {"know": (1, 7), "scan": (2, 0), "halal": (3, 10), "doubtful": (3, 11), "haram": (3, 13), "exactly": (4, 3), "problem": (4, 7),
           "madhhab": (5, 3), "results": (5, 10), "l1": (6, 5), "l2": (6, 6), "l3": (6, 7), "l4": (6, 9), "enum": (7, 10),
           "p1": (8, 2), "p2": (8, 3), "p3": (8, 4)},
    "ar": {"know": (1, 6), "scan": (2, 0), "halal": (3, 8), "doubtful": (3, 9), "haram": (3, 11), "exactly": (4, 1), "problem": (4, 5),
           "madhhab": (5, 1), "results": (5, 5), "l1": (6, 2), "l2": (6, 3), "l3": (6, 4), "l4": (6, 5), "enum": (7, 6),
           "p1": (8, 2), "p2": (8, 3), "p3": (8, 4)},
}
LORDER = {"en": ["en", "de", "fr", "ar"], "de": ["de", "en", "fr", "ar"], "tr": ["en", "de", "fr", "ar"],
          "fr": ["fr", "en", "de", "ar"], "ar": ["ar", "en", "de", "fr"]}

RIBBON = {
    "en": ["Ingredients:", "glucose syrup", "sugar", "!pork gelatine", "dextrose", "citric acid", "?whey powder", "cocoa butter", "!E120 carmine",
           "beeswax", "?emulsifier E471", "salt", "wheat flour", "!wine extract", "vanilla", "sunflower oil", "?flavouring", "milk powder",
           "!E441", "starch", "?rennet", "glycerol"],
    "de": ["Zutaten:", "Glukosesirup", "Zucker", "!Schweinegelatine", "Dextrose", "Zitronensäure", "?Molkenpulver", "Kakaobutter", "!E120 Karmin",
           "Bienenwachs", "?Emulgator E471", "Salz", "Weizenmehl", "!Weinextrakt", "Vanille", "Sonnenblumenöl", "?Aroma", "Milchpulver",
           "!E441", "Stärke", "?Lab", "Glycerin"],
    "tr": ["İçindekiler:", "glikoz şurubu", "şeker", "!domuz jelatini", "dekstroz", "sitrik asit", "?peynir altı suyu tozu", "kakao yağı",
           "!E120 karmin", "balmumu", "?emülgatör E471", "tuz", "buğday unu", "!şarap özütü", "vanilya", "ayçiçek yağı", "?aroma", "süt tozu",
           "!E441", "nişasta", "?peynir mayası", "gliserol"],
    "fr": ["Ingrédients :", "sirop de glucose", "sucre", "!gélatine de porc", "dextrose", "acide citrique", "?lactosérum en poudre",
           "beurre de cacao", "!E120 carmin", "cire d'abeille", "?émulsifiant E471", "sel", "farine de blé", "!extrait de vin", "vanille",
           "huile de tournesol", "?arôme", "lait en poudre", "!E441", "amidon", "?présure", "glycérol"],
    "ar": ["المكونات:", "شراب الجلوكوز", "سكر", "!جيلاتين الخنزير", "دكستروز", "حمض الستريك", "?مصل اللبن", "زبدة الكاكاو", "!E120 كارمين",
           "شمع العسل", "?مستحلب E471", "ملح", "دقيق القمح", "!مستخلص النبيذ", "فانيليا", "زيت دوار الشمس", "?منكهات", "حليب مجفف",
           "!E441", "نشا", "?منفحة", "جلسرين"],
}

STR = {
    "en": {
        "s1": {"eyebrow": "Halal Checker", "h1": "Halal or haram?", "h2": "Know why."},
        "s2": {"eyebrow": "One scan", "head": ["Scan the", "barcode."]},
        "s3": {"eyebrow": "Three clear answers", "head": ["Halal, doubtful", "or haram."], "sub": "Doubtful means the source isn't stated, and we tell you so."},
        "s4": {"eyebrow": "Every flag explained", "head": ["See exactly", "what's wrong."], "c4a": "pork gelatine", "tag": "HARAM", "c4b": "Pork / Lard / Swine Derivatives"},
        "s5": {"eyebrow": "Madhhab aware", "head": ["Your school,", "your answer."], "sub": "Hanafi, Shafi'i, Maliki or Hanbali. Differences come with sources.",
               "c5a": "Hanafi: whey chocolate is halal"},
        "s6": {"eyebrow": "Made for European shelves", "head": ["Reads real", "EU labels."]},
        "s7a": {"eyebrow": "No account needed", "head": ["History stays", "on your phone."]},
        "s7b": {"eyebrow": "E-number guide", "head": ["Look up any", "E-number."]},
        "s8": {"word": "Halal Checker", "pills": ["Scan.", "Check.", "Shop with confidence."],
               "fine": "Product data from Open Food Facts. An automated check, not a religious ruling."},
    },
    "de": {
        "s1": {"eyebrow": "Halal Checker", "h1": "Halal oder haram?", "h2": "Wisse, warum."},
        "s2": {"eyebrow": "Ein Scan", "head": ["Scann den", "Barcode."]},
        "s3": {"eyebrow": "Drei klare Antworten", "head": ["Halal, zweifelhaft", "oder haram."], "sub": "Zweifelhaft heißt: Die Herkunft ist nicht angegeben. Das sagen wir dir offen."},
        "s4": {"eyebrow": "Jeder Treffer erklärt", "head": ["Sieh genau,", "was nicht passt."], "c4a": "Schweinegelatine", "tag": "HARAM", "c4b": "Schweinegelatine (E441)"},
        "s5": {"eyebrow": "Mit Rechtsschulen", "head": ["Deine Schule,", "deine Antwort."], "sub": "Hanafitisch, schafiitisch, malikitisch oder hanbalitisch. Unterschiede mit Quellen.",
               "c5a": "Hanafitisch: Milchschokolade ist halal"},
        "s6": {"eyebrow": "Für Europas Regale", "head": ["Liest echte", "EU-Etiketten."]},
        "s7a": {"eyebrow": "Kein Konto nötig", "head": ["Verlauf bleibt", "auf dem Handy."]},
        "s7b": {"eyebrow": "E-Nummern-Ratgeber", "head": ["Jede E-Nummer", "nachschlagen."]},
        "s8": {"word": "Halal Checker", "pills": ["Scannen.", "Prüfen.", "Sicher einkaufen."],
               "fine": "Produktdaten von Open Food Facts. Eine automatische Prüfung, kein religiöses Urteil."},
    },
    "tr": {
        "s1": {"eyebrow": "Halal Checker", "h1": "Helal mi, haram mı?", "h2": "Nedenini bil."},
        "s2": {"eyebrow": "Tek tarama", "head": ["Barkodu", "okut."]},
        "s3": {"eyebrow": "Üç net cevap", "head": ["Helal, şüpheli", "ya da haram."], "sub": "Şüpheli, kaynağın belirtilmediği anlamına gelir. Bunu açıkça söyleriz."},
        "s4": {"eyebrow": "Her uyarı açıklanır", "head": ["Sorunu", "tam olarak gör."], "c4a": "domuz jelatini", "tag": "HARAM", "c4b": "Domuz Eti / Domuz Yağı (Lard)"},
        "s5": {"eyebrow": "Mezhebe duyarlı", "head": ["Senin mezhebin,", "senin cevabın."], "sub": "Hanefi, Şafii, Maliki ya da Hanbeli. Farklar kaynaklarıyla gösterilir.",
               "c5a": "Hanefi: sütlü çikolata helal"},
        "s6": {"eyebrow": "Avrupa rafları için", "head": ["Gerçek AB", "etiketlerini okur."]},
        "s7a": {"eyebrow": "Hesap gerekmez", "head": ["Geçmişin", "telefonunda kalır."]},
        "s7b": {"eyebrow": "E kodu rehberi", "head": ["Her E kodunu", "öğren."]},
        "s8": {"word": "Halal Checker", "pills": ["Okut.", "Kontrol et.", "Gönül rahatlığıyla alışveriş yap."],
               "fine": "Ürün verileri Open Food Facts'ten. Otomatik bir kontroldür, dini hüküm değildir."},
    },
    "fr": {
        "s1": {"eyebrow": "Halal Checker", "h1": "Halal ou haram ?", "h2": "Sachez pourquoi."},
        "s2": {"eyebrow": "Un seul scan", "head": ["Scannez le", "code-barres."]},
        "s3": {"eyebrow": "Trois réponses claires", "head": ["Halal, douteux", "ou haram."], "sub": "Douteux signifie que l'origine n'est pas indiquée, et nous vous le disons."},
        "s4": {"eyebrow": "Chaque alerte expliquée", "head": ["Voyez ce qui", "pose problème."], "c4a": "gélatine de porc", "tag": "HARAM", "c4b": "Gélatine de porc (E441)"},
        "s5": {"eyebrow": "Selon votre école", "head": ["Votre école,", "votre réponse."], "sub": "Hanafite, chaféite, malékite ou hanbalite. Les différences sont sourcées.",
               "c5a": "Hanafite : chocolat au lait halal"},
        "s6": {"eyebrow": "Pour les rayons européens", "head": ["Lit les vraies", "étiquettes UE."]},
        "s7a": {"eyebrow": "Aucun compte requis", "head": ["L'historique reste", "sur votre téléphone."]},
        "s7b": {"eyebrow": "Guide des additifs E", "head": ["Cherchez", "tout additif E."]},
        "s8": {"word": "Halal Checker", "pills": ["Scannez.", "Vérifiez.", "Achetez en toute confiance."],
               "fine": "Données produits d'Open Food Facts. Un contrôle automatique, pas un avis religieux."},
    },
    "ar": {
        "s1": {"eyebrow": "Halal Checker", "h1": "حلال أم حرام؟", "h2": "اعرف السبب."},
        "s2": {"eyebrow": "مسحة واحدة", "head": ["امسح", "الباركود."]},
        "s3": {"eyebrow": "ثلاث إجابات واضحة", "head": ["حلال أو مشبوه", "أو حرام."], "sub": "مشبوه يعني أن المصدر غير مذكور، ونخبرك بذلك بوضوح."},
        "s4": {"eyebrow": "كل تنبيه مُفسَّر", "head": ["اعرف بالضبط", "ما المشكلة."], "c4a": "جيلاتين الخنزير", "tag": "حرام", "c4b": "جيلاتين الخنزير (E441)"},
        "s5": {"eyebrow": "يراعي المذاهب", "head": ["مذهبك،", "وجوابك."], "sub": "الحنفي أو الشافعي أو المالكي أو الحنبلي، مع ذكر المصادر.",
               "c5a": "الحنفي: شوكولاتة الحليب حلال"},
        "s6": {"eyebrow": "لرفوف المتاجر الأوروبية", "head": ["يقرأ الملصقات", "الأوروبية."]},
        "s7a": {"eyebrow": "بدون حساب", "head": ["سجلّك يبقى", "على هاتفك."]},
        "s7b": {"eyebrow": "دليل أرقام E", "head": ["ابحث عن أي", "رقم E."]},
        "s8": {"word": "Halal Checker", "pills": ["امسح.", "تحقّق.", "تسوّق بثقة."],
               "fine": "بيانات المنتجات من Open Food Facts. فحص آلي وليس فتوى."},
    },
}


def dur(path):
    return float(subprocess.check_output(["ffprobe", "-v", "error", "-show_entries", "format=duration", "-of", "csv=p=0", path]))


def build(lang):
    en = lang == "en"
    out_dir = HERE if en else os.path.join(VIDEO, f"promo-v2-{lang}")
    vo_dir = os.path.join(HERE, "assets", "vo") if en else os.path.join(VIDEO, "vo_langs", lang)
    lines = json.load(open(os.path.join(vo_dir, "lines.json"), encoding="utf-8"))
    durs = [dur(os.path.join(vo_dir, f"line{i}.mp3")) for i in range(1, 9)]
    S, t = [], LEAD_IN
    for d in durs:
        S.append(round(t, 3)); t += d + GAP
    total = round(S[7] + durs[7] + TAIL, 2)
    T = {k: round(S[line - 1] + lines[line - 1]["words"][w]["t"], 3) for k, (line, w) in CUES[lang].items()}
    st = STR[lang]
    sc = {k: f"assets/screens/{v}_{lang}.png" for k, v in
          {"home": "home_typed", "gum": "gum_top", "gmid": "gum_mid", "dlg": "madhhab", "han": "choc_hanafi",
           "hist": "history", "eco": "ecodes", "halal": "choc_hanafi", "doubt": "choc_doubt"}.items()}

    def copy(sid, start, end, inner_extra=""):
        c = st[sid]
        head = "".join(f'<span class="ln{" em" if i == len(c["head"]) - 1 else ""}">{h}</span>' for i, h in enumerate(c["head"]))
        sub = f'<div class="sub">{c["sub"]}</div>' if "sub" in c else ""
        return (f'      <div class="clip" id="{sid}" data-start="{start:.3f}" data-duration="{end - start:.3f}" data-track-index="3">\n'
                f'        <div class="copy"><div class="eyebrow">{c["eyebrow"]}</div><div class="head">{head}</div>{sub}{inner_extra}</div>\n')

    b = [s - 0.15 for s in S] + [total]
    scenes = [copy("s2", b[1], b[2]) + "      </div>\n"]
    vc = "".join(f'<div class="vcard" id="vc{i+1}" style="top:{150 + i * 260}px"><img src="{src}" alt="" /></div>'
                 for i, src in enumerate([sc["halal"], sc["doubt"], sc["gum"]]))
    scenes.append(copy("s3", b[2], b[3]) + f"        {vc}\n      </div>\n")
    s4 = st["s4"]
    scenes.append(copy("s4", b[3], b[4])
                  + f'        <div class="chip" id="c4a" style="left:930px; top:540px"><span class="dot" style="background:#e11d48"></span>{s4["c4a"]}<span class="tag">{s4["tag"]}</span></div>\n'
                  + f'        <div class="chip" id="c4b" style="left:860px; top:760px"><span class="dot" style="background:#e11d48"></span>{s4["c4b"]}</div>\n      </div>\n')
    scenes.append(copy("s5", b[4], b[5])
                  + f'        <div class="chip" id="c5a" style="left:880px; top:600px"><span class="dot" style="background:#059669"></span>{st["s5"]["c5a"]}</div>\n      </div>\n')
    pills = '<div class="pills">' + "".join(f'<div class="pill" id="lp{i+1}">{LANG_NAMES[c]}</div>' for i, c in enumerate(LORDER[lang])) + "</div>"
    scenes.append(copy("s6", b[5], b[6], pills) + "      </div>\n")
    scenes.append(copy("s7a", b[6], T["enum"] - 0.1) + "      </div>\n")
    scenes.append(copy("s7b", T["enum"] - 0.1, b[7]) + "      </div>\n")
    s8 = st["s8"]
    scenes.append(f'      <div class="clip" id="s8" data-start="{b[7]:.3f}" data-duration="{total - b[7]:.3f}" data-track-index="3">\n'
                  f'        <div class="lockup"><div class="icon"><img src="assets/logo/icon_clean.png" alt="" /></div>'
                  f'<div class="word">{s8["word"]}</div><div class="pills">'
                  + "".join(f'<div class="pill" id="t{i+1}">{p}</div>' for i, p in enumerate(s8["pills"]))
                  + f'</div></div>\n        <div class="fine">{s8["fine"]}</div>\n      </div>\n')

    ends = {"s1": b[1], "s2": b[2], "s3": b[3], "s4": b[4], "s5": b[5], "s6": b[6], "s7a": T["enum"] - 0.1}
    data = {"t": T, "s": S, "dur": total, "str": {"s1": st["s1"]}, "ribbon": RIBBON[lang], "ends": ends,
            "lscr": [f"#p-{c}" for c in LORDER[lang]]}

    bgm = open(os.path.join(HERE, "assets", "bgm_tag.txt"), encoding="utf-8").read().rstrip("\n")
    bgm = bgm.replace('data-duration="32.5"', f'data-duration="{total}"')
    vo_rel = "assets/vo"
    audio = [bgm] + [f'      <audio id="vo{i}" src="{vo_rel}/line{i}.mp3" data-start="{S[i-1]}" data-volume="1"></audio>' for i in range(1, 9)]

    html = open(os.path.join(HERE, "src", "template.html"), encoding="utf-8").read()
    for k, v in {"__LANG__": lang, "__DIR__": "rtl" if lang == "ar" else "ltr", "__DUR__": str(total), "__D1__": f"{b[1]:.3f}",
                 "__PH_START__": f"{b[1]:.3f}", "__PH_DUR__": f"{b[7] + 0.2 - b[1]:.3f}",
                 "__AUDIO__": "\n".join(audio), "__SCENES__": "".join(scenes),
                 "__DATA__": json.dumps(data, ensure_ascii=False),
                 "__SCR_home__": sc["home"], "__SCR_gum__": sc["gum"], "__SCR_gmid__": sc["gmid"], "__SCR_dlg__": sc["dlg"],
                 "__SCR_han__": sc["han"], "__SCR_hist__": sc["hist"], "__SCR_eco__": sc["eco"]}.items():
        html = html.replace(k, v)

    if not en:
        # standalone project per language: shared assets + this language's voice + a music bed cut to length
        os.makedirs(out_dir, exist_ok=True)
        for d in ["fonts", "logo", "screens"]:
            shutil.copytree(os.path.join(HERE, "assets", d), os.path.join(out_dir, "assets", d), dirs_exist_ok=True)
        os.makedirs(os.path.join(out_dir, "assets", "vo"), exist_ok=True)
        for f in os.listdir(vo_dir):
            shutil.copy(os.path.join(vo_dir, f), os.path.join(out_dir, "assets", "vo", f))
        os.makedirs(os.path.join(out_dir, "assets", "bgm"), exist_ok=True)
        subprocess.run(["ffmpeg", "-v", "error", "-y", "-i", os.path.join(HERE, "assets", "bgm", "bed_long.m4a"), "-t", str(total),
                        "-af", f"afade=t=out:st={total - 2.5}:d=2.5", "-c:a", "aac", "-b:a", "192k",
                        os.path.join(out_dir, "assets", "bgm", "bed.m4a")], check=True)
        for f in ["package.json", "hyperframes.json", "CLAUDE.md", "AGENTS.md"]:
            shutil.copy(os.path.join(HERE, f), os.path.join(out_dir, f))
        pkg = os.path.join(out_dir, "package.json")
        open(pkg, "w").write(open(pkg).read().replace('"name": "promo-v2"', f'"name": "promo-v2-{lang}"'))
        json.dump({"id": f"promo-v2-{lang}", "name": f"promo-v2-{lang}", "createdAt": "2026-10-22T12:00:00.000Z"},
                  open(os.path.join(out_dir, "meta.json"), "w"))
    open(os.path.join(out_dir, "index.html"), "w", encoding="utf-8").write(html)
    print(lang, "total", total)


for lang in sys.argv[1:]:
    build(lang)
