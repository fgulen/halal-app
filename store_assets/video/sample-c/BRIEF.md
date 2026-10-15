---
workflow: general-video
flow: automation
storyboard: no
message: "Scan a product and know instantly if it's halal - in your language"
destination: youtube
aspect: 1920x1080
language: en
length: 32.5s
angle: sample-c-narrated
---

## Intent

Version 3: English, narrated. Sample B's dark emerald look with cuts paced to the voice; every
on-screen word is placed on its spoken word (edge-tts word boundaries in assets/vo/lines.json).

## Assets

- assets/vo/line1-8.mp3 - edge-tts en-US-AndrewMultilingualNeural, rate +8% (same voice as the
  user's YouTube pipeline); regenerate with assets/vo/tts.py (youtube project's .venv).
- assets/bgm/bed.m4a - ACE-Step 1.5 instrumental (seed 88), carved under the voice
  (hyperframes-audio carve.mjs, strength 0.8).

## Notes

- edge-tts is an unofficial Microsoft Edge read-aloud endpoint; commercial terms are unclear.
- Fictional products only; no real brands; no Turkish; no price/ad claims or "download now".
