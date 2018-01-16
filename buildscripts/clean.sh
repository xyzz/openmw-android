#!/bin/bash

set -e
cd "$(dirname ${BASH_SOURCE[0]})"

# Clean a single thing
if [[ $1 == "--build" ]]; then
	rm -rf build
	exit 0
fi

if [[ $1 == "--toolchain" ]]; then
	rm -rf toolchain
	exit 0
fi

if [[ $1 == "--prefix" ]]; then
	rm -rf prefix
	exit 0
fi

# Generic clean, good most of the time
rm -rf toolchain prefix build

# Clean everything, including downloads
if [[ $1 == "--all" ]]; then
	rm -rf downloads
fi
