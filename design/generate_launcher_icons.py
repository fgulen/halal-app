"""Regenerate all launcher icons from design/play_store_icon_512.png (the Play Store icon).

Run from the repo root: python design/generate_launcher_icons.py  (needs Pillow)
The white artwork is cut out of the green background and used as the adaptive-icon
foreground + monochrome layer; the green is res/values/ic_launcher_background.xml.
"""
from PIL import Image, ImageDraw

SRC = "design/play_store_icon_512.png"
RES = "app/src/main/res"
BG = (5, 88, 60, 255)  # #05583C, keep in sync with ic_launcher_background.xml
ART_BOX = (81, 87, 434, 415)  # white artwork bounds inside the 512px source

src = Image.open(SRC).convert("RGB").crop(ART_BOX)
# White artwork -> alpha mask (red channel: green bg ~5, white ~255)
mask = src.split()[0].point(lambda v: max(0, min(255, int((v - 10) * 255 / 235))))
art = Image.new("RGBA", src.size, (255, 255, 255, 0))
art.putalpha(mask)
aw, ah = art.size


def place(canvas, frac):
    w = int(canvas * frac)
    h = round(w * ah / aw)
    out = Image.new("RGBA", (canvas, canvas), (0, 0, 0, 0))
    out.alpha_composite(art.resize((w, h), Image.LANCZOS), ((canvas - w) // 2, (canvas - h) // 2))
    return out


for d, s in {"mdpi": 1, "hdpi": 1.5, "xhdpi": 2, "xxhdpi": 3, "xxxhdpi": 4}.items():
    # Adaptive foreground: 108dp canvas, artwork 46% wide so it stays inside the
    # 66dp safe zone under circle/squircle masks.
    place(int(108 * s), 0.46).save(f"{RES}/mipmap-{d}/ic_launcher_foreground.png", optimize=True)
    # Legacy icons (API < 26): 48dp, drawn at 4x then downscaled.
    n = int(48 * s)
    N = n * 4
    sq = Image.new("RGBA", (N, N), (0, 0, 0, 0))
    ImageDraw.Draw(sq).rounded_rectangle((0, 0, N - 1, N - 1), radius=int(N * 0.18), fill=BG)
    sq.alpha_composite(place(N, 0.62))
    sq.resize((n, n), Image.LANCZOS).save(f"{RES}/mipmap-{d}/ic_launcher.png", optimize=True)
    ci = Image.new("RGBA", (N, N), (0, 0, 0, 0))
    ImageDraw.Draw(ci).ellipse((0, 0, N - 1, N - 1), fill=BG)
    ci.alpha_composite(place(N, 0.56))
    ci.resize((n, n), Image.LANCZOS).save(f"{RES}/mipmap-{d}/ic_launcher_round.png", optimize=True)
