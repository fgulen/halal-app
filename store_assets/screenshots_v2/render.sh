#!/bin/sh
# Render the panorama for each language and slice it into 8 Play screenshots (1080x1920).
cd "$(dirname "$0")"
CH="$HOME/.cache/hyperframes/chrome/chrome-headless-shell/win64-152.0.7977.30/chrome-headless-shell-win64/chrome-headless-shell.exe"
for l in "$@"; do
  "$CH" --headless --disable-gpu --allow-file-access-from-files --hide-scrollbars --force-device-scale-factor=1 \
    --window-size=8640,1920 --virtual-time-budget=4000 --screenshot="$(cygpath -w "$PWD")\out\pano_$l.png" \
    "file:///$(cygpath -m "$PWD")/panorama.html?lang=$l" >/dev/null 2>&1
  python -I -c "
from PIL import Image
im = Image.open('out/pano_$l.png').convert('RGB')
for i in range(8): im.crop((i*1080, 0, (i+1)*1080, 1920)).save('out/${l}_%d.png' % (i+1))
im.resize((2160, 480)).save('out/pano_${l}_small.png')"
  echo "rendered $l"
done
