"""v2 Shorts: full-bleed AI b-roll (LTX-2.5) under word-synced captions, quick cuts, app proof at the end.
Usage: python build_v2.py <short-id>   (e.g. gelatine_v2)
Needs <short-id>/assets/vo/lines.json (tts.py) and <short-id>/assets/broll/*.mp4 (1080x1920, muted).
Why: v1 (text cards on flat green) kept only ~12% of viewers past the first second."""
import html, json, os, re, subprocess, sys

HERE = os.path.dirname(os.path.abspath(__file__))
GAP, LEAD, TAIL = 0.15, 0.0, 2.0

# Per short: accent, key word per line, and per line a list of b-roll cuts (file, share of the line, media offset s).
# The last line is the app proof + lockup (no b-roll).
SHORTS = {
    "gelatine_v2": {
        "accent": "#fb923c",
        "keys": ["pig", "gelatine", "animal", "halal-certified", "Checker"],
        "broll": [
            [("melt.mp4", 1.0, 0.4)],  # melt is sped up 1.6x in prep, so the bear visibly collapses under the hook line
            [("hook_a.mp4", 0.55, 1.5), ("hook_b.mp4", 0.45, 2.0)],
            [("gelatin.mp4", 1.0, 0.5)],
            [("fruit.mp4", 1.0, 0.0)],
        ],
        "screen": "gummies_en_top.png",
    },
    # Rennet short (DE). 7 lines: 1-6 b-roll, line 7 = app proof + lockup.
    "lab_de": {
        "accent": "#22c55e",
        "keys": ["Lab", "Enzym", "Mikroben", "Diyanet", "umstritten", "markiert", "Scan"],
        "broll": [
            [("cut_a.mp4", 1.0, 0.3)],
            [("milk_b.mp4", 1.0, 0.3)],
            [("mold_c.mp4", 1.0, 0.3)],
            [("slices_d.mp4", 1.0, 0.3)],
            [("cut_a.mp4", 1.0, 1.8)],
            [("block_e.mp4", 1.0, 0.3)],
        ],
        "screen": "milbona_doubtful.png",
    },
}


def norm(w):
    return re.sub(r"[^a-z0-9'-]", "", w.lower())


def dur(p):
    return float(subprocess.check_output(["ffprobe", "-v", "error", "-show_entries", "format=duration", "-of", "csv=p=0", p]).decode())


def build(short):
    cfg = SHORTS[short]
    root = os.path.join(HERE, short)
    lines = json.load(open(os.path.join(root, "assets", "vo", "lines.json"), encoding="utf-8"))
    durs = [dur(os.path.join(root, "assets", "vo", f"line{i}.mp3")) for i in range(1, len(lines) + 1)]
    starts, t = [], LEAD
    for d in durs:
        starts.append(round(t, 3))
        t += d + GAP
    total = round(starts[-1] + durs[-1] + TAIL, 2)
    ends = [starts[i + 1] if i + 1 < len(starts) else total for i in range(len(starts))]

    def W(li, word):
        hits = [w for w in lines[li]["words"] if norm(w["w"]) == norm(word)]
        if not hits:
            raise SystemExit(f"{short}: word '{word}' not in line {li + 1}: {lines[li]['text']}")
        return round(starts[li] + hits[0]["t"], 3)

    audio = [f'<audio id="vo{i + 1}" src="assets/vo/line{i + 1}.mp3" data-start="{starts[i]}" data-volume="1"></audio>'
             for i in range(len(lines))]
    clips, js, n = [], [], 0

    # b-roll: one <video> per cut, slow push-in on each, a small scale punch on every cut
    for li, cuts in enumerate(cfg["broll"]):
        s0, length = starts[li], ends[li] - starts[li]
        t = s0
        for k, (f, share, off) in enumerate(cuts):
            d = round(length * share, 3) if k < len(cuts) - 1 else round(s0 + length - t, 3)
            vid = f"b{n}"
            clips.append(f'<video id="{vid}" class="clip broll" src="assets/broll/{f}" data-start="{round(t, 3)}" data-duration="{d}" '
                         f'data-media-start="{off}" data-track-index="0" muted playsinline></video>')
            js.append(f'tl.fromTo("#{vid}", {{ scale: 1.06 }}, {{ scale: 1.16, duration: {d}, ease: "none" }}, {round(t, 3)});')
            n += 1
            t += d

    # captions: line 1 is fully on screen at frame 0 (the hook must read with the sound off), the rest pop word by word
    for i, ln in enumerate(lines):
        toks = ln["text"].split()
        if len(toks) != len(ln["words"]):
            raise SystemExit(f"{short}: line {i + 1} token/word mismatch: {toks} vs {[w['w'] for w in ln['words']]}")
        spans = []
        for k, w in enumerate(ln["words"]):
            cls = "cw key" if norm(w["w"]) == norm(cfg["keys"][i]) else "cw"
            spans.append(f'<span class="{cls}" id="c{i}w{k}">{html.escape(toks[k])}</span>')
        clips.append(f'<div class="clip cap" id="cap{i}" data-start="{starts[i]}" data-duration="{round(ends[i] - starts[i], 3)}" '
                     f'data-track-index="2"><div class="capline">{" ".join(spans)}</div></div>')
        for k, w in enumerate(ln["words"]):
            if i == 0:
                js.append(f'tl.set("#c0w{k}", {{ opacity: 1, y: 0, scale: 1 }}, 0);')
            else:
                js.append(f'pop("#c{i}w{k}", {round(starts[i] + w["t"], 3)});')
        if i + 1 < len(lines):
            js.append(f'tl.to("#cap{i} .capline", {{ opacity: 0, duration: 0.1 }}, {round(ends[i] - 0.1, 3)});')
    key0 = W(0, cfg["keys"][0])
    js.append(f'tl.fromTo("#c0w{[norm(w["w"]) for w in lines[0]["words"]].index(norm(cfg["keys"][0]))}", '
              f'{{ scale: 1 }}, {{ scale: 1.25, duration: 0.18, yoyo: true, repeat: 1, ease: "power2.out", immediateRender: false }}, {key0});')

    # end: phone with the real result screen, scan beam, then the lockup
    i = len(lines) - 1
    s, e = starts[i], total
    clips.append(f'<div class="clip endbg" id="endbg" data-start="{s}" data-duration="{round(e - s, 3)}" data-track-index="1"></div>')
    clips.append(
        f'<div class="clip vis" id="end" data-start="{s}" data-duration="{round(e - s, 3)}" data-track-index="3">'
        f'<div class="phone" id="ph" data-layout-allow-overflow><div class="pscreen"><img class="scr" src="assets/screens/{cfg["screen"]}" alt="" />'
        f'<div class="beam" id="beam" data-layout-allow-overflow></div></div></div>'
        f'<div class="lockup" id="lock"><div class="icon" id="icon"><img src="assets/logo/icon_clean.png" alt="" /></div>'
        f'<div class="wordmark" id="wm">Halal Checker</div><div class="store" id="gp">Free on Google Play</div></div></div>')
    tscan, tbrand = W(i, "Scan"), W(i, "Halal")
    js += [f'tl.fromTo("#ph", {{ y: 1300 }}, {{ y: 0, duration: 0.45, ease: "expo.out" }}, {s});',
           f'tl.fromTo("#beam", {{ opacity: 1, y: -120 }}, {{ opacity: 1, y: 1300, duration: 0.8, ease: "power1.inOut", immediateRender: false }}, {tscan});',
           f'tl.to("#beam", {{ opacity: 0, duration: 0.1 }}, {round(tscan + 0.8, 3)});',
           f'tl.to("#ph", {{ y: 1400, duration: 0.4, ease: "power3.in" }}, {round(tbrand + 0.2, 3)});',
           f'tl.fromTo("#icon", {{ opacity: 0, scale: 0 }}, {{ opacity: 1, scale: 1, duration: 0.5, ease: "back.out(1.8)", immediateRender: false }}, {round(tbrand + 0.45, 3)});',
           f'tl.fromTo("#wm", {{ opacity: 0, scale: 1.2 }}, {{ opacity: 1, scale: 1, duration: 0.22, ease: "power4.out", immediateRender: false }}, {round(tbrand + 0.6, 3)});',
           f'tl.fromTo("#gp", {{ opacity: 0, y: 26 }}, {{ opacity: 1, y: 0, duration: 0.3, ease: "power3.out", immediateRender: false }}, {round(tbrand + 0.95, 3)});']

    tpl = open(os.path.join(HERE, "template_v2.html"), encoding="utf-8").read()
    out = (tpl.replace("{{TOTAL}}", str(total)).replace("{{ACCENT}}", cfg["accent"])
           .replace("{{AUDIO}}", "\n      ".join(audio)).replace("{{CLIPS}}", "\n      ".join(clips))
           .replace("{{JS}}", "\n      ".join(js)))
    open(os.path.join(root, "index.html"), "w", encoding="utf-8").write(out)
    print(short, "total", total, "s; line starts", starts)


build(sys.argv[1])
