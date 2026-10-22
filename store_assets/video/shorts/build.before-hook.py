"""Builds <short-id>/index.html (1080x1920 HyperFrames composition) from assets/vo/lines.json.
Usage: python build.py <short-id>
Layout: word-synced caption block on top (y 300-650), one visual per narration line in the
middle (y 700-1450), phone + lockup at the end. Bottom 350px kept clear for the Shorts UI."""
import html, json, os, re, sys

HERE = os.path.dirname(os.path.abspath(__file__))
GAP, LEAD, TAIL = 0.22, 0.35, 1.6  # pause between lines, silence before line 1, hold after the last line

# Per short: accent colour, the caption word to tint per line, and one visual per line.
# Visual specs: ("card", ingredients_html, highlight_word) | ("giant", text, sub, at_word)
# | ("steps", [(label, at_word)...]) | ("stat", big, sub, at_word) | ("tiles", [(label, at_word)...])
# | ("chips", [(label, at_word)...], tone) | ("equals", left, right, at_word) | ("end", screen, scroll_px, card)
SHORTS = {
    "e120": {
        "accent": "#fb7185",
        "keys": ["bugs", "carmine", "cochineal", "seventy", "lipstick", "cochineal", "Checker"],
        "visuals": [
            ("card", 'Milk, sugar, strawberries (8%), thickener, <b id="hl">colour: carmine (E120)</b>, flavouring.', "bugs"),
            ("giant", "E120", "Carmine", "carmine"),
            ("steps", [("Insect", "insects"), ("Dried", "dried"), ("Crushed", "crushed"), ("Red dye", "dye")]),
            ("stat", "70,000", "insects for about 1 lb (450 g) of dye", "seventy"),
            ("tiles", [("Sweets", "sweets"), ("Drinks", "drinks"), ("Yogurts", "yogurts"), ("Lipstick", "lipstick")]),
            ("chips", [("E120", "one-twenty"), ("Carmine", "carmine"), ("Cochineal", "cochineal")], "bad"),
            ("end", "ecodes_en.png", 985, None),
        ],
    },
    "e904": {
        "accent": "#fbbf24",
        "keys": ["resin", "Shellac", "secretes", "varnish", "apples", "glaze", "Checker"],
        "visuals": [
            ("card", 'Sugar, glucose syrup, cocoa butter, colours, <b id="hl">glazing agent: shellac (E904)</b>.', "resin"),
            ("giant", "E904", "Shellac", "Shellac"),
            ("steps", [("Lac insect", "insect"), ("Secretes resin", "secretes"), ("Scraped off branches", "scraped")]),
            ("equals", "Candy shine", "Wood varnish", "varnish"),
            ("tiles", [("Jelly beans", "jelly"), ("Chocolate drops", "chocolate"), ("Some apples", "apples")]),
            ("chips", [("E904", "nine-oh-four"), ("Shellac", "shellac"), ("Confectioner's glaze", "confectioner's")], "doubt"),
            ("end", "ecodes_en.png", 0, ("E904", "Shellac / Confectioner's Glaze", "DOUBTFUL",
                                          "Glazing agent from lac insect secretion. Deemed doubtful by several Halal standards.")),
        ],
    },
    "gelatine": {
        "accent": "#fb923c",
        "keys": ["bones", "gelatine", "pig", "animal", "juices", "agar", "Checker"],
        "visuals": [
            ("card", 'Glucose syrup, sugar, <b id="hl">gelatine</b>, dextrose, fruit juice from concentrate (5%), citric acid.', "skin"),
            ("giant", "Gelatine", "E441", "gelatine"),
            ("steps", [("Pig skin", "pig"), ("Cattle", "cattle"), ("Fish", "fish")]),
            ("giant", "Which animal?", "“gelatine”", "which"),
            ("tiles", [("Marshmallows", "marshmallows"), ("Yogurts", "yogurts"), ("Desserts", "desserts"), ("Some juices", "juices")]),
            ("chips", [("Pectin", "pectin"), ("Agar", "agar"), ("Beef / fish, stated", "stated")], "good"),
            ("end", "gummies_en_top.png", 0, None),
        ],
    },
}


SPOKEN_NUM = {"one-twenty": "120", "nine-oh-four": "904"}


def norm(w):
    return re.sub(r"[^a-z0-9'-]", "", w.lower())


def build(short):
    cfg = SHORTS[short]
    root = os.path.join(HERE, short)
    lines = json.load(open(os.path.join(root, "assets", "vo", "lines.json"), encoding="utf-8"))
    import subprocess
    durs = [float(subprocess.check_output(["ffprobe", "-v", "error", "-show_entries", "format=duration", "-of", "csv=p=0",
                                           os.path.join(root, "assets", "vo", f"line{i}.mp3")]).decode().strip())
            for i in range(1, len(lines) + 1)]
    starts, t = [], LEAD
    for d in durs:
        starts.append(round(t, 3))
        t += d + GAP
    total = round(starts[-1] + durs[-1] + TAIL, 2)
    ends = [starts[i + 1] if i + 1 < len(starts) else total for i in range(len(starts))]

    def W(li, word, nth=0):
        hits = [w for w in lines[li]["words"] if norm(w["w"]) == norm(word)]
        if not hits:
            raise SystemExit(f"{short}: word '{word}' not in line {li + 1}: {lines[li]['text']}")
        return round(starts[li] + hits[min(nth, len(hits) - 1)]["t"], 3)

    audio, clips, js = [], [], []
    for i in range(len(lines)):
        audio.append(f'<audio id="vo{i + 1}" src="assets/vo/line{i + 1}.mp3" data-start="{starts[i]}" data-volume="1"></audio>')

    # captions: one clip per line, words fade up on their spoken time; the line's key word takes the accent
    for i, ln in enumerate(lines):
        # display tokens come from the script text (keeps punctuation); spoken E-numbers collapse to "E120"
        toks = ln["text"].split()
        if len(toks) != len(ln["words"]):
            raise SystemExit(f"{short}: line {i + 1} token/word mismatch: {toks} vs {[w['w'] for w in ln['words']]}")
        disp = list(toks)
        for k in range(len(toks) - 1):
            num = SPOKEN_NUM.get(norm(toks[k + 1]))
            if norm(toks[k]) == "e" and num:
                disp[k], disp[k + 1] = "E" + num + re.sub(r"[\w'-]", "", toks[k + 1]), ""
        size = "" if len(toks) <= 7 else ' style="font-size: 78px"'
        spans = []
        for k, w in enumerate(ln["words"]):
            if not disp[k]:
                continue
            cls = "cw key" if norm(w["w"]) == norm(cfg["keys"][i]) or (disp[k] != toks[k]) else "cw"
            spans.append(f'<span class="{cls}" id="c{i}w{k}"{size}>{html.escape(disp[k])}</span>')
        clips.append(f'<div class="clip cap" id="cap{i}" data-start="{starts[i]}" data-duration="{round(ends[i] - starts[i], 3)}" '
                     f'data-track-index="1"><div class="capline">{" ".join(spans)}</div></div>')
        ln["_disp"] = disp
        for k, w in enumerate(ln["words"]):
            if ln["_disp"][k]:
                js.append(f'pop("#c{i}w{k}", {round(starts[i] + w["t"], 3)});')
        if i + 1 < len(lines):
            js.append(f'tl.to("#cap{i} .capline", {{ opacity: 0, duration: 0.12 }}, {round(ends[i] - 0.12, 3)});')

    for i, v in enumerate(cfg["visuals"]):
        s, e, kind, vid = starts[i], ends[i], v[0], f"v{i}"
        last = i == len(cfg["visuals"]) - 1
        open_ = f'<div class="clip vis" id="{vid}" data-start="{s}" data-duration="{round(e - s, 3)}" data-track-index="{2 + i % 2}">'
        if kind == "card":
            body = f'<div class="card" id="{vid}-card"><div class="cardh">Ingredients</div><div class="cardb">{v[1]}</div></div>'
            js += [f'tl.fromTo("#{vid}-card", {{ opacity: 0, y: 120, rotation: -6 }}, {{ opacity: 1, y: 0, rotation: -3, duration: 0.6, ease: "expo.out" }}, {s});',
                   f'tl.fromTo("#hl", {{ backgroundColor: "rgba(0,0,0,0)", color: "#1f2937" }}, {{ backgroundColor: ACC, color: "#ffffff", duration: 0.2 }}, {W(i, v[2])});',
                   f'tl.fromTo("#{vid}-card", {{ scale: 1 }}, {{ scale: 1.06, duration: 0.25, ease: "back.out(2)" }}, {W(i, v[2])});']
        elif kind == "giant":
            body = f'<div class="center"><div class="giant" id="{vid}-g">{html.escape(v[1])}</div><div class="gsub" id="{vid}-s">{html.escape(v[2])}</div></div>'
            js += [f'slamIn("#{vid}-g", {W(i, v[3])}, 1.4);', f'rise("#{vid}-s", {round(W(i, v[3]) + 0.25, 3)});']
        elif kind == "steps":
            items = []
            for k, (lab, _) in enumerate(v[1]):
                if k:
                    items.append(f'<div class="arrow" id="{vid}-a{k}">&#8595;</div>')
                items.append(f'<div class="step" id="{vid}-i{k}">{html.escape(lab)}</div>')
            body = f'<div class="center col">{"".join(items)}</div>'
            for k, (_, at) in enumerate(v[1]):
                tt = W(i, at)
                js.append(f'slamIn("#{vid}-i{k}", {tt}, 1.25);')
                if k:
                    js.append(f'rise("#{vid}-a{k}", {round(tt - 0.12, 3)});')
        elif kind == "stat":
            body = f'<div class="center"><div class="stat" id="{vid}-n">{html.escape(v[1])}</div><div class="gsub" id="{vid}-s">{html.escape(v[2])}</div></div>'
            js += [f'slamIn("#{vid}-n", {W(i, v[3])}, 1.5);', f'rise("#{vid}-s", {round(W(i, v[3]) + 0.5, 3)});']
        elif kind == "equals":
            body = (f'<div class="center col"><div class="step" id="{vid}-l">{html.escape(v[1])}</div>'
                    f'<div class="eq" id="{vid}-e">=</div><div class="step acc" id="{vid}-r">{html.escape(v[2])}</div></div>')
            js += [f'slamIn("#{vid}-l", {round(s + 0.05, 3)});', f'slamIn("#{vid}-e", {round(W(i, v[3]) - 0.2, 3)}, 1.6);',
                   f'slamIn("#{vid}-r", {W(i, v[3])}, 1.3);']
        elif kind in ("tiles", "chips"):
            cls = "tile" if kind == "tiles" else f"chip {v[2]}"
            wrap = "tiles" if kind == "tiles" else "center col"
            items = "".join(f'<div class="{cls}" id="{vid}-i{k}">{html.escape(lab)}</div>' for k, (lab, _) in enumerate(v[1]))
            body = f'<div class="{wrap}">{items}</div>'
            js += [f'slamIn("#{vid}-i{k}", {W(i, at)}, 1.3);' for k, (_, at) in enumerate(v[1])]
        elif kind == "end":
            screen, scroll, card = v[1], v[2], v[3]
            over = ""
            if card:
                over = (f'<div class="ecard" id="{vid}-ec"><div class="ech"><span class="ecode">{card[0]}</span>'
                        f'<span class="ename">{html.escape(card[1])}</span><span class="ebadge">{card[2]}</span></div>'
                        f'<div class="ereason">{html.escape(card[3])}</div></div>')
            if scroll:
                over += f'<div class="ring" id="{vid}-ring" style="top: {scroll}px;"></div>'
            body = (f'<div class="phone" id="{vid}-ph" data-layout-allow-overflow><div class="pscreen"><img class="scr" id="{vid}-scr" '
                    f'data-layout-allow-overflow src="assets/screens/{screen}" alt="" />{over}'
                    f'<div class="beam" id="{vid}-beam" data-layout-allow-overflow></div></div></div>'
                    f'<div class="lockup" id="{vid}-lock"><div class="icon" id="{vid}-icon"><img src="assets/logo/icon_clean.png" alt="" /></div>'
                    f'<div class="wordmark" id="{vid}-wm">Halal Checker</div><div class="tag" id="{vid}-tag">Scan it. Know what\'s inside.</div></div>')
            tscan, tbrand = W(i, "Scan"), W(i, "Halal")
            js += [f'tl.fromTo("#{vid}-ph", {{ y: 1300 }}, {{ y: 0, duration: 0.5, ease: "expo.out" }}, {s});',
                   f'tl.fromTo("#{vid}-beam", {{ opacity: 1, y: -120 }}, {{ y: 1300, duration: 0.9, ease: "power1.inOut" }}, {tscan});',
                   f'tl.to("#{vid}-beam", {{ opacity: 0, duration: 0.1 }}, {round(tscan + 0.9, 3)});']
            if scroll:  # ring the row at this y (screen px) once the scan beam has passed
                js.append(f'tl.fromTo("#{vid}-ring", {{ opacity: 0, scale: 1.12 }}, {{ opacity: 1, scale: 1, duration: 0.3, ease: "back.out(1.8)" }}, {round(tscan + 0.6, 3)});')
            if card:
                js.append(f'tl.fromTo("#{vid}-ec", {{ opacity: 0, y: 80, scale: 0.92 }}, {{ opacity: 1, y: 0, scale: 1, duration: 0.4, ease: "back.out(1.6)" }}, {round(tscan + 0.5, 3)});')
            js += [f'tl.to("#{vid}-ph", {{ y: 1400, duration: 0.45, ease: "power3.in" }}, {round(tbrand - 0.2, 3)});',
                   f'tl.fromTo("#{vid}-icon", {{ opacity: 0, scale: 0 }}, {{ opacity: 1, scale: 1, duration: 0.55, ease: "back.out(1.8)" }}, {tbrand});',
                   f'slamIn("#{vid}-wm", {round(tbrand + 0.15, 3)}, 1.15);', f'rise("#{vid}-tag", {round(tbrand + 0.7, 3)});']
        else:
            raise SystemExit(kind)
        clips.append(open_ + body + "</div>")
        if not last:
            js.append(f'tl.to("#{vid} > *", {{ opacity: 0, y: -50, duration: 0.16, ease: "power2.in" }}, {round(e - 0.16, 3)});')

    tpl = open(os.path.join(HERE, "template.html"), encoding="utf-8").read()
    out = (tpl.replace("{{TOTAL}}", str(total)).replace("{{ACCENT}}", cfg["accent"])
           .replace("{{AUDIO}}", "\n      ".join(audio)).replace("{{CLIPS}}", "\n      ".join(clips))
           .replace("{{JS}}", "\n      ".join(js)))
    open(os.path.join(root, "index.html"), "w", encoding="utf-8").write(out)
    print(short, "total", total, "s; line starts", starts)


build(sys.argv[1])
