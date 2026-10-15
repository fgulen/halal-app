import asyncio, json, sys
import edge_tts
sys.stdout.reconfigure(encoding="utf-8")
TEXT = "Halal Checker. Scan. Check. Shop with confidence."
async def main():
    words, audio = [], bytearray()
    async for ch in edge_tts.Communicate(TEXT, "en-US-AndrewMultilingualNeural", rate="+8%", boundary="WordBoundary").stream():
        if ch["type"] == "audio":
            audio += ch["data"]
        elif ch["type"] == "WordBoundary":
            words.append({"w": ch["text"], "t": round(ch["offset"] / 1e7, 3), "d": round(ch["duration"] / 1e7, 3)})
    open("line8.mp3", "wb").write(audio)
    lines = json.load(open("lines.json", encoding="utf-8"))
    lines[7] = {"text": TEXT, "words": words}
    json.dump(lines, open("lines.json", "w", encoding="utf-8"), ensure_ascii=False, indent=1)
    print(" ".join(f'{w["w"]}@{w["t"]}' for w in words))
asyncio.run(main())
