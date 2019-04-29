#!/bin/bash

set -e

./clean.sh --all

# ARCH=arm below is a hack for our Great Build System
ARCH=arm ./include/download-ndk.sh
ARCH=arm ./include/setup-ndk.sh

./build.sh --arch arm --lto

./build.sh --arch arm64 --lto --no-resources &
PID1=$!

./build.sh --arch x86_64 --lto --no-resources &
PID2=$!

./build.sh --arch x86 --lto --no-resources &
PID3=$!

wait $PID1 $PID2 $PID3
