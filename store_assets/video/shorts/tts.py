"""Voiceover + word timings for the Halal Checker Shorts.
Usage: python tts.py <short-id>   (run with the youtube project's .venv, which has edge_tts)
Writes <short-id>/assets/vo/line{n}.mp3 and lines.json (same format as sample-c)."""
import asyncio, json, os, sys
import edge_tts

sys.stdout.reconfigure(encoding="utf-8")
VOICE, RATE = "en-US-AndrewMultilingualNeural", "+8%"
SCRIPTS = {
    "e120": [
        "That red in your yogurt might come from crushed bugs.",
        "It's called carmine. E one-twenty.",
        "Made from cochineal insects, dried and crushed into dye.",
        "Around seventy thousand of them, for one pound.",
        "You'll find it in sweets, drinks, yogurts, even lipstick.",
        "On the label: E one-twenty, carmine, or cochineal.",
        "Not sure? Scan it with Halal Checker.",
    ],
    "e904": [
        "That shine on your candy? It's bug resin.",
        "Shellac. E nine-oh-four.",
        "The lac insect secretes it, and it's scraped off tree branches.",
        "The same stuff is used to varnish wooden furniture.",
        "It coats jelly beans, chocolate drops, and even some apples.",
        "Labels call it E nine-oh-four, shellac, or confectioner's glaze.",
        "Not sure? Scan it with Halal Checker.",
    ],
    "gelatine": [
        "Most gummy bears are made from boiled animal skin and bones.",
        "That's gelatine.",
        "Much of it comes from pig skin, the rest from cattle and fish.",
        "And the label rarely says which animal.",
        "It hides in marshmallows, yogurts, desserts, even some juices.",
        "Look for pectin, agar, fish gelatine, or a halal-certified label.",
        "Not sure? Scan it with Halal Checker.",
    ],
}


async def main(short):
    out = os.path.join(os.path.dirname(os.path.abspath(__file__)), short, "assets", "vo")
    os.makedirs(out, exist_ok=True)
    lines, timings = SCRIPTS[short], []
    for i, text in enumerate(lines, 1):
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
                print("retry", i, e)
                await asyncio.sleep(2)
    json.dump([{"text": t, "words": w} for t, w in zip(lines, timings)],
              open(f"{out}/lines.json", "w", encoding="utf-8"), ensure_ascii=False, indent=1)


asyncio.run(main(sys.argv[1]))
