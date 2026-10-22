"""Transcribe every voiceover line with faster-whisper and print it under the script text.
Usage (a venv with faster_whisper): python verify_vo.py de tr fr ar"""
import json, os, subprocess, sys
import numpy as np
from faster_whisper import WhisperModel

sys.stdout.reconfigure(encoding="utf-8")
HERE = os.path.dirname(os.path.abspath(__file__))
model = WhisperModel("large-v3-turbo", device="cpu", compute_type="int8")
for lang in sys.argv[1:]:
    lines = json.load(open(os.path.join(HERE, lang, "lines.json"), encoding="utf-8"))
    print(f"===== {lang}")
    for i, l in enumerate(lines, 1):
        # decode with ffmpeg (this venv's PyAV is too old for faster-whisper's own decoder)
        raw = subprocess.check_output(["ffmpeg", "-v", "error", "-i", os.path.join(HERE, lang, f"line{i}.mp3"),
                                       "-f", "f32le", "-ac", "1", "-ar", "16000", "-"])
        segs, _ = model.transcribe(np.frombuffer(raw, np.float32), language=lang, beam_size=5)
        heard = " ".join(s.text.strip() for s in segs)
        print(f"{i} script: {l['text']}\n  heard : {heard}")
