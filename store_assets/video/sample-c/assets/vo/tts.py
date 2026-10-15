import asyncio, json, sys
import edge_tts
sys.stdout.reconfigure(encoding="utf-8")
VOICE, RATE = "en-US-AndrewMultilingualNeural", "+8%"
LINES = [
    "Standing in the aisle, wondering if it's halal?",
    "Just scan the barcode.",
    "Halal Checker reads the ingredients and gives a clear answer: halal, doubtful, or haram.",
    "And it shows exactly which ingredient is the problem.",
    "Follow a madhhab? Choose yours, and the results adapt, with sources.",
    "It reads labels in English, German, French, and Arabic.",
    "Plus your scan history, and a full E-number guide.",
    "Halal Checker. Scan, check, shop with confidence.",
]
async def main(out):
    timings = []
    for i, text in enumerate(LINES, 1):
        for attempt in range(4):
            try:
                words, audio = [], bytearray()
                async for ch in edge_tts.Communicate(text, VOICE, rate=RATE, boundary="WordBoundary").stream():
                    if ch["type"] == "audio":
                        audio += ch["data"]
                    elif ch["type"] == "WordBoundary":
                        words.append({"w": ch["text"], "t": round(ch["offset"] / 1e7, 3), "d": round(ch["duration"] / 1e7, 3)})
                open(f"{out}/line{i}.mp3", "wb").write(audio)
                timings.append(words)
                break
            except Exception as e:
                print("retry", i, e); await asyncio.sleep(2)
    json.dump([{"text": t, "words": w} for t, w in zip(LINES, timings)], open(f"{out}/lines.json", "w", encoding="utf-8"), ensure_ascii=False, indent=1)
asyncio.run(main(sys.argv[1]))
