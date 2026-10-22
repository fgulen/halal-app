"""Capture the store-screenshot screens for one app language from the debug build on the emulator.
Usage: python -I capture_lang.py <lang>   (en|de|tr|fr|ar)
Needs: debug build installed (DemoSeedReceiver), SystemUI demo mode allowed.
Writes screens/<name>_<lang>.png: gum_top, gum_mid, choc_doubt, choc_hanafi, madhhab, ecodes, history."""
import os, re, subprocess, sys, time

ADB = r"C:\Android\Sdk\platform-tools\adb.exe"
PKG = "com.fatihgulen.helalrehberi"
HERE = os.path.dirname(os.path.abspath(__file__))
LANG_NAMES = {"en": "English", "de": "Deutsch", "tr": "Türkçe", "fr": "Français", "ar": "العربية"}
CODES = {"en": "EN", "de": "DE", "tr": "TR", "fr": "FR", "ar": "AR"}
GUM, CHOC = "2001000000024", "2001000000031"
env = dict(os.environ, MSYS_NO_PATHCONV="1")


def adb(*a, out=False):
    r = subprocess.run([ADB, *a], capture_output=True, env=env)
    return r.stdout if out else None


def sh(cmd):
    adb("shell", cmd)


def nodes():
    sh("uiautomator dump /sdcard/ui.xml")
    x = adb("exec-out", "cat", "/sdcard/ui.xml", out=True).decode("utf-8", "replace")
    res = []
    for n in re.findall(r"<node [^>]*>", x):
        a = dict(re.findall(r'([\w-]+)="([^"]*)"', n))
        b = list(map(int, re.findall(r"\d+", a.get("bounds", "[0,0][0,0]"))))
        a["c"] = ((b[0] + b[2]) // 2, (b[1] + b[3]) // 2)
        res.append(a)
    return res


def tap_text(text, exact=True):
    for n in nodes():
        t = n.get("text", "") or n.get("content-desc", "")
        if (t == text) if exact else (text in t):
            sh(f"input tap {n['c'][0]} {n['c'][1]}")
            time.sleep(1.2)
            return True
    raise SystemExit(f"not found: {text}")


def shot(name, lang):
    time.sleep(1.0)
    png = adb("exec-out", "screencap", "-p", out=True)
    open(os.path.join(HERE, "screens", f"{name}_{lang}.png"), "wb").write(png)
    print("saved", name, lang)


def seed(lang, madhhab):
    sh(f"am broadcast -n {PKG}/com.example.demo.DemoSeedReceiver --es lang {lang} --es madhhab {madhhab}")
    time.sleep(1.5)


def relaunch():
    sh(f"am force-stop {PKG}")
    sh(f"monkey -p {PKG} -c android.intent.category.LAUNCHER 1")
    time.sleep(4)


def search(code, rtl):
    sh("input tap 540 541")
    time.sleep(0.6)
    sh(f"input text {code}")
    time.sleep(0.6)
    sh("input keyevent 66")          # IME action = search
    time.sleep(2.5)


# madhhab dialog rows (a Compose dialog is a separate window that uiautomator dump does not
# list, so use the measured row centres; same in RTL): not specified, Hanafi, Shafi'i, ...
MADHHAB_ROWS = [960, 1116, 1268, 1422, 1578]


def main(lang):
    rtl = lang == "ar"
    mx = lambda x: x                      # the app keeps an LTR layout in Arabic too
    for c in ["enter", "clock -e hhmm 1200", "notifications -e visible false",
              "battery -e level 100 -e plugged false", "network -e wifi show -e level 4 -e mobile show -e level 4 -e datatype none"]:
        parts = c.split(" ", 1)
        sh(f"am broadcast -a com.android.systemui.demo -e command {parts[0]} {parts[1] if len(parts) > 1 else ''}")
    seed(lang, "none")
    relaunch()
    # switch the app UI language via the language chip (shows the current code)
    for code in CODES.values():
        try:
            tap_text(code); break
        except SystemExit:
            continue
    tap_text(LANG_NAMES[lang])
    relaunch()

    sh(f"input tap {mx(524)} 2107"); shot("history", lang)
    sh(f"input tap {mx(870)} 2107"); shot("ecodes", lang)
    sh("input keyevent 4"); time.sleep(1)
    sh(f"input tap {mx(193)} 2107"); time.sleep(1)

    search(GUM, rtl); shot("gum_top", lang)
    sh("input swipe 540 1700 540 900 600"); shot("gum_mid", lang)
    sh("input keyevent 4"); time.sleep(1.2)
    search(CHOC, rtl); shot("choc_doubt", lang)
    sh("input keyevent 4"); time.sleep(1.2)

    # madhhab dialog, choose Hanafi, re-seed so the cached chocolate is analysed as Hanafi
    sh(f"input tap {mx(384)} 331"); time.sleep(1.2); shot("madhhab", lang)
    sh(f"input tap 540 {MADHHAB_ROWS[1]}"); time.sleep(1.2)
    seed(lang, "hanafi")
    search(CHOC, rtl); shot("choc_hanafi", lang)
    sh("input keyevent 4"); time.sleep(1.2)
    # back to "not specified" and the default seed
    sh(f"input tap {mx(384)} 331"); time.sleep(1.2)
    sh(f"input tap 540 {MADHHAB_ROWS[0]}"); time.sleep(1.2)
    seed(lang, "none")


def home_typed(lang):
    """Home screen with the demo barcode typed in (keyboard hidden), in the given UI language."""
    seed(lang, "none")
    relaunch()
    for code in CODES.values():
        try:
            tap_text(code); break
        except SystemExit:
            continue
    tap_text(LANG_NAMES[lang])
    relaunch()
    sh("input tap 540 541"); time.sleep(0.6)
    sh(f"input text {GUM}"); time.sleep(0.6)
    sh("input keyevent 4"); time.sleep(1.0)       # back hides the keyboard only
    shot("home_typed", lang)


# Guarded so the helpers can be imported (runpy/import) without overwriting screens/*.png.
if __name__ == "__main__":
    if len(sys.argv) > 2 and sys.argv[2] == "home":
        home_typed(sys.argv[1])
    else:
        main(sys.argv[1])
