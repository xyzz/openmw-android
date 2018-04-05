#!/bin/bash

set -e

./clean.sh

./build.sh --arch arm --lto

./build.sh --arch arm64 --lto &
PID1=$!

./build.sh --arch x86_64 --lto &
PID2=$!

./build.sh --arch x86 --lto &
PID3=$!

wait $PID1 $PID2 $PID3
