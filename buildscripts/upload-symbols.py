#!/usr/bin/env python3

"""
    Uploads native symbols to bugsnag.
"""

import os
import subprocess
import requests
import io
import datetime
from multiprocessing import Pool


def log(s):
    print("[{}] {}".format(datetime.datetime.now(), s))


def find_version_code():
    with open("../app/build.gradle", "r") as fin:
        for line in fin.readlines():
            line = line.strip()
            if line.startswith("versionCode"):
                return line.split()[1]


def find_app_id():
    user_input = ""
    while user_input not in ["y", "n"]:
        user_input = input("Nightly? (y/n) ")
    if user_input == "y":
        return "is.xyz.omw_nightly"
    return "is.xyz.omw"


def find_api_key():
    with open("../app/src/main/java/utils/BugsnagApiKey.kt", "r") as fin:
        for line in fin.readlines():
            line = line.strip()
            if line.startswith("const val API_KEY"):
                return line.split(" = ")[1].replace('"', "")


def remove_if_exists(path):
    if os.path.exists(path):
        os.remove(path)


def do_symbol_file(args):
    abi, so = args

    path = os.path.join("symbols", abi, so)

    log("[{}/{}] Generating symbols".format(abi, so))
    filename = "{}_{}.txt".format(abi, so)
    symbols_txt = os.path.join("tmp", filename)
    symbols_gz = os.path.join("tmp", filename + ".gz")
    remove_if_exists(symbols_txt)
    remove_if_exists(symbols_gz)
    with open(symbols_txt, "wb") as f:
        subprocess.call(["objdump", "--dwarf=info", "--dwarf=rawline", os.path.join("symbols", abi, so)], stdout=f)

    log("[{}/{}] Compressing symbols".format(abi, so))
    subprocess.check_output(["gzip", symbols_txt])

    files = {
        'soSymbolFile': (so + ".txt.gz", open(symbols_gz, "rb")),
        'apiKey': (None, api_key),
        'versionCode': (None, version_code),
        'appId': (None, app_id),
        'arch': (None, abi),
        'sharedObjectName': (None, so),
        'overwrite': (None, "1"),
    }

    log("[{}/{}] Uploading ({:.2f} MB)".format(abi, so, os.path.getsize(symbols_gz) / 1024.0 / 1024.0))
    response = requests.post('https://upload.bugsnag.com/', files=files)

    log("[{}/{}] Result: {} {}".format(abi, so, response.status_code, response.text))

    if response.status_code != 200:
        raise RuntimeError("bugsnag api failed")

    remove_if_exists(symbols_txt)
    remove_if_exists(symbols_gz)


def main():
    global version_code
    global app_id
    global api_key

    version_code = find_version_code()
    print("Version code: {}".format(version_code))

    app_id = find_app_id()
    print("App ID: {}".format(app_id))

    api_key = find_api_key()
    print("API key: {}".format(api_key))

    if len(api_key) == 0:
        raise RuntimeError("No API key provided")

    print("")

    if not os.path.exists("tmp"):
        os.makedirs("tmp")

    libraries = []
    for abi in os.listdir("symbols"):
        for so in os.listdir(os.path.join("symbols", abi)):
            libraries.append((abi, so))

    with Pool(16) as p:
        p.map(do_symbol_file, libraries)

if __name__ == "__main__":
    main()
