#!/bin/bash

set -e

./clean.sh
./build.sh --arch arm --lto
./build.sh --arch arm64 --lto
./build.sh --arch x86_64 --lto
./build.sh --arch x86 --lto
