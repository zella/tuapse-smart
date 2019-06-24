#!/usr/bin/env python3

# NOTE: this example requires PyAudio because it uses the Microphone class
import sys
import speech_recognition as sr

key = sys.argv[1]
lang = sys.argv[2]

# obtain audio from the microphone
r = sr.Recognizer()
with sr.Microphone() as source:
    print("Say something!")
    audio = r.listen(source)

    print("listen ok")

# recognize speech using Sphinx
# try:
#     print("Sphinx thinks you said " + r.recognize_sphinx(audio))
# except sr.UnknownValueError:
#     print("Sphinx could not understand audio")
# except sr.RequestError as e:
#     print("Sphinx error; {0}".format(e))

# recognize speech using Google Speech Recognition
try:
    # for testing purposes, we're just using the default API key
    # to use another API key, use `r.recognize_google(audio, key="GOOGLE_SPEECH_RECOGNITION_API_KEY")`
    # instead of `r.recognize_google(audio)`
    recognized = r.recognize_google(audio, language=lang, key=str(key))
    print("Google Speech Recognition thinks you said " + recognized)
    print("[recognized]" + recognized, flush=True)
except sr.UnknownValueError:
    print("Google Speech Recognition could not understand audio")
except sr.RequestError as e:
    print("Could not request results from Google Speech Recognition service; {0}".format(e))
#
# # recognize speech using Google Cloud Speech
# GOOGLE_CLOUD_SPEECH_CREDENTIALS = r"""{
#   "type": "service_account",
#   "project_id": "test-rpi-assist",
#   "private_key_id": "780210dbad60fdb379a1409b7a554ed81fecd07f",
#   "private_key": "-----BEGIN PRIVATE KEY-----\nMIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDxs+CurfS9aaH0\nSN3PZfhtXM9Db6tPXUDHF2L1hHAdf/lm8bvbiXFHJBC8452stqfUNkjd1hAylh13\nGqDxZwJCEttuZ4EjtgMoJT6yP6i6irZgi5m3cLwUO0QBBkCl3lkvurT9n6afvfNx\n2M9CxPzEh/Drzyg8TFk78F0WIIzLwuAuLymvCJK+QWb4NSIzArLVUbcE607AYkMK\nzkaZUofDb6fQe/YiYx5ozACfa1HZI7s/oEA+qk6/lVLzSHFu5SdZ2+DNGVXEHZnc\nuDb+qUQUfzUrB+oSQnhKVfKu8ndVt9kIntlQQsYtGCRjHWAm7tE8z2Heshb/tiey\nQpXtEIwRAgMBAAECggEAA5Ie8kRej/X5lxaEqgxwEYE24dX1IRKoEwuRh34dxWkh\n+JSUQXOr/UtHO4Z91SLBgzvg6Epsr2tNaLhsyFriGx2J9B06Q0+sXNltrhFiceIR\nfHGSM2t1EzPxHDZDpNJZJ+CRYrNV0U5A5IL5rztM3tmWybcOuZL+WP7jCIFchHiX\ngLfPebfyH9Wyn+Fvou50U975xmeR//+c6Q+HzwWnFt5a/x99x4CHOQw514ogPCpn\nXORDejMeONZrT1arM/oQhDu2MafZUaiXfLMcpWQffuhWnFvHij1fCATlEu2OLzGP\n9+tv20R+g0+ig+LxDYPnL85JgbNJx4w+lGFIPtMjmQKBgQD67lrUTMKHFeMGqwVu\nv9dVNaIdey4kncMnx1oGyqVdWCXxOCZXfQDqP+mTomDApeoV++YfQG2ejBruRCAL\n6SIDS/dUAbWoThCgCTmAK79cLRdJyAS3YKS9nFcLUY94iVkBNRp19AP8MvsnZTpd\ndlBzz12FQvP3xWWCATLmyyF/GQKBgQD2lcy5ir+gKyoW88SfguCliIOopA4evJBj\n1H8RwPWmSaKF8GVVaEeVHxqefo23j57RPjqv03osHrqdrwUs7ygL51E6oeNqFGjL\n3+r8L4F8BKqqhbCQoUS6oa0iOE14u9iR9jOM1DnSw9Dc/jTnfVMTuWb7iUFk0ieh\ncHNXzmaruQKBgQD5EfwwPFzeLxhgdwL2cg9wMcNPlgQwLjQW2OaAVUccxzEqJPzR\n3G87FONpOkTlFAqtdZJutMnaFOFqXU6d4/Nx6EaAKQ5CF/Kil2Vu1n2kJdG214Xu\n60u6NWOlBXjuQQ6Kh0ZTjDkkbQokiXMMcDLdMRh3/QQijNn71+rJcRk3iQKBgQDy\nxCScfzeN7zEvC6wHBwk4r6A/hdTI+giF4I2B4yF2J4S0rSp23YWsEMVaSWoo0GMh\nykxpEqFzkoVT/R3xUTtyDLjyKrQEmjdakQd+ZR7sBIQqAhkFK2DJBae4ywmq1fWw\nLl4uRjrvgA2/pR5c0gvanWwANO+G3UhbLqTAS9rVMQKBgEEUiZ35QgVIp52/WUmm\nvfSspQqR0A0Q1QOnfXh1/pE/3zaQ8+3q0mTbsHyr9LIPHueIpv4YcMHigi7twZZ+\nUeZ3JjmjWBunc7ccEuaBbQLLRuMH9z3yIVXipsqfNZ7CNuECLogAq28y+kdvB91K\nEUlrQKpkONRva5TXFwyKpyqy\n-----END PRIVATE KEY-----\n",
#   "client_email": "test-rpi-assist@appspot.gserviceaccount.com",
#   "client_id": "115740424770702835071",
#   "auth_uri": "https://accounts.google.com/o/oauth2/auth",
#   "token_uri": "https://oauth2.googleapis.com/token",
#   "auth_provider_x509_cert_url": "https://www.googleapis.com/oauth2/v1/certs",
#   "client_x509_cert_url": "https://www.googleapis.com/robot/v1/metadata/x509/test-rpi-assist%40appspot.gserviceaccount.com"
# }
# """
# try:
#     print("Google Cloud Speech thinks you said " + r.recognize_google_cloud(audio, credentials_json=GOOGLE_CLOUD_SPEECH_CREDENTIALS))
# except sr.UnknownValueError:
#     print("Google Cloud Speech could not understand audio")
# except sr.RequestError as e:
#     print("Could not request results from Google Cloud Speech service; {0}".format(e))
#
# # recognize speech using Wit.ai
# WIT_AI_KEY = "INSERT WIT.AI API KEY HERE"  # Wit.ai keys are 32-character uppercase alphanumeric strings
# try:
#     print("Wit.ai thinks you said " + r.recognize_wit(audio, key=WIT_AI_KEY))
# except sr.UnknownValueError:
#     print("Wit.ai could not understand audio")
# except sr.RequestError as e:
#     print("Could not request results from Wit.ai service; {0}".format(e))
#
# # recognize speech using Microsoft Bing Voice Recognition
# BING_KEY = "INSERT BING API KEY HERE"  # Microsoft Bing Voice Recognition API keys 32-character lowercase hexadecimal strings
# try:
#     print("Microsoft Bing Voice Recognition thinks you said " + r.recognize_bing(audio, key=BING_KEY))
# except sr.UnknownValueError:
#     print("Microsoft Bing Voice Recognition could not understand audio")
# except sr.RequestError as e:
#     print("Could not request results from Microsoft Bing Voice Recognition service; {0}".format(e))
#
# # recognize speech using Microsoft Azure Speech
# AZURE_SPEECH_KEY = "INSERT AZURE SPEECH API KEY HERE"  # Microsoft Speech API keys 32-character lowercase hexadecimal strings
# try:
#     print("Microsoft Azure Speech thinks you said " + r.recognize_azure(audio, key=AZURE_SPEECH_KEY))
# except sr.UnknownValueError:
#     print("Microsoft Azure Speech could not understand audio")
# except sr.RequestError as e:
#     print("Could not request results from Microsoft Azure Speech service; {0}".format(e))
#
# # recognize speech using Houndify
# HOUNDIFY_CLIENT_ID = "INSERT HOUNDIFY CLIENT ID HERE"  # Houndify client IDs are Base64-encoded strings
# HOUNDIFY_CLIENT_KEY = "INSERT HOUNDIFY CLIENT KEY HERE"  # Houndify client keys are Base64-encoded strings
# try:
#     print("Houndify thinks you said " + r.recognize_houndify(audio, client_id=HOUNDIFY_CLIENT_ID, client_key=HOUNDIFY_CLIENT_KEY))
# except sr.UnknownValueError:
#     print("Houndify could not understand audio")
# except sr.RequestError as e:
#     print("Could not request results from Houndify service; {0}".format(e))
#
# # recognize speech using IBM Speech to Text
# IBM_USERNAME = "INSERT IBM SPEECH TO TEXT USERNAME HERE"  # IBM Speech to Text usernames are strings of the form XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX
# IBM_PASSWORD = "INSERT IBM SPEECH TO TEXT PASSWORD HERE"  # IBM Speech to Text passwords are mixed-case alphanumeric strings
# try:
#     print("IBM Speech to Text thinks you said " + r.recognize_ibm(audio, username=IBM_USERNAME, password=IBM_PASSWORD))
# except sr.UnknownValueError:
#     print("IBM Speech to Text could not understand audio")
# except sr.RequestError as e:
#     print("Could not request results from IBM Speech to Text service; {0}".format(e))
