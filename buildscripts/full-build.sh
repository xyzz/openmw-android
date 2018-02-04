#!/bin/bash

set -e

./clean.sh
./build.sh --arch arm
./build.sh --arch arm64
./build.sh --arch x86_64
./build.sh --arch x86
