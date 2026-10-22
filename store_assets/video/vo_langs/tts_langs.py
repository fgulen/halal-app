"""Localized voiceover for the narrated store/YouTube video (sample C structure: 8 lines).
Usage: python tts_langs.py <lang> [<lang> ...]   (youtube project's .venv has edge_tts)
Writes <lang>/line{n}.mp3 + <lang>/lines.json (same format as sample-c/assets/vo)."""
import asyncio, json, os, sys
import edge_tts

sys.stdout.reconfigure(encoding="utf-8")
RATE = {"de": "+8%", "fr": "+8%", "tr": "+15%", "ar": "+15%"}
VOICES = {
    "de": "de-DE-FlorianMultilingualNeural",
    "fr": "fr-FR-RemyMultilingualNeural",
    "tr": "tr-TR-AhmetNeural",
    "ar": "ar-SA-HamedNeural",
}
# Verdict words match the app's own strings (AppStrings.kt).
LINES = {
    "de": [
        "Im Supermarkt und unsicher, ob es halal ist?",
        "Scann einfach den Barcode.",
        "Halal Checker liest die Zutaten und gibt eine klare Antwort: halal, zweifelhaft oder haram.",
        "Und es zeigt genau, welche Zutat das Problem ist.",
        "Du folgst einer Rechtsschule? Wähle deine, und die Ergebnisse passen sich an, mit Quellen.",
        "Es liest Etiketten auf Deutsch, Englisch, Französisch und Arabisch.",
        "Dazu dein Scan-Verlauf und ein kompletter E-Nummern-Ratgeber.",
        "Halal Checker. Scannen. Prüfen. Sicher einkaufen.",
    ],
    "fr": [
        "Au rayon, vous vous demandez si c'est halal ?",
        "Scannez simplement le code-barres.",
        "Halal Checker lit les ingrédients et donne une réponse claire : halal, douteux ou haram.",
        "Et il montre exactement quel ingrédient pose problème.",
        "Vous suivez une école juridique ? Choisissez la vôtre, et les résultats s'adaptent, sources à l'appui.",
        "Il lit les étiquettes en français, anglais, allemand et arabe.",
        "Avec votre historique de scans et un guide complet des additifs alimentaires.",
        "Halal Checker. Scannez. Vérifiez. Achetez en toute confiance.",
    ],
    "tr": [
        "Markette, bu ürün helal mi diye mi düşünüyorsunuz?",
        "Barkodu okutmanız yeterli.",
        "Halal Çeker içindekileri okur ve net bir cevap verir: helal, şüpheli ya da haram.",
        "Ve sorunun hangi maddede olduğunu tam olarak gösterir.",
        "Bir mezhebe mi uyuyorsunuz? Mezhebinizi seçin, sonuçlar kaynaklarıyla birlikte uyarlansın.",
        "Türkçe, İngilizce, Almanca, Fransızca ve Arapça etiketleri okur.",
        "Ayrıca tarama geçmişiniz ve eksiksiz bir katkı maddesi rehberi.",
        "Halal Çeker. Okut. Kontrol et. Gönül rahatlığıyla alışveriş yap.",
    ],
    "ar": [
        "في السوق، وتتساءل هل هذا المنتج حلال؟",
        "امسح الباركود فقط.",
        "يقرأ تطبيق حلال تشيكر المكونات ويعطيك جوابًا واضحًا: حلال، مشبوه، أو حرام.",
        "ويُظهر بالضبط أيّ مكوّن هو المشكلة.",
        "تتبع مذهبًا؟ اختر مذهبك، وتتكيّف النتائج، مع ذكر المصادر.",
        "يقرأ الملصقات بالعربية والإنجليزية والألمانية والفرنسية.",
        "بالإضافة إلى سجل المسح، ودليل كامل لأرقام الإضافات.",
        "حلال تشيكر. امسح. تحقّق. تسوّق بثقة.",
    ],
}


async def main(lang):
    out = os.path.join(os.path.dirname(os.path.abspath(__file__)), lang)
    os.makedirs(out, exist_ok=True)
    timings = []
    for i, text in enumerate(LINES[lang], 1):
        for attempt in range(8):
            try:
                words, audio = [], bytearray()
                async for ch in edge_tts.Communicate(text, VOICES[lang], rate=RATE[lang], boundary="WordBoundary").stream():
                    if ch["type"] == "audio":
                        audio += ch["data"]
                    elif ch["type"] == "WordBoundary":
                        words.append({"w": ch["text"], "t": round(ch["offset"] / 1e7, 3), "d": round(ch["duration"] / 1e7, 3)})
                open(f"{out}/line{i}.mp3", "wb").write(audio)
                timings.append(words)
                break
            except Exception as e:
                print("retry", lang, i, e)
                await asyncio.sleep(5)
    json.dump([{"text": t, "words": w} for t, w in zip(LINES[lang], timings)],
              open(f"{out}/lines.json", "w", encoding="utf-8"), ensure_ascii=False, indent=1)


for lang in sys.argv[1:]:
    asyncio.run(main(lang))
