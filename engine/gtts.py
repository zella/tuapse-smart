import sys
from gtts import gTTS
from io import BytesIO
import pygame

text = sys.argv[1]
lang = sys.argv[2]

mp3_fp = BytesIO()
tts = gTTS(text, lang)
tts.write_to_fp(mp3_fp)
mp3_fp.seek(0)

pygame.mixer.init()
pygame.mixer.music.load(mp3_fp)
pygame.mixer.music.play()

print('[done]', flush=True)
