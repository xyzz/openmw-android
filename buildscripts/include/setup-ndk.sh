#!/bin/bash

set -e

source ./include/version.sh

if [[ ! -d toolchain ]]; then
	# Unzip ndk.zip
	mkdir -p toolchain

	echo "(Extracting, this will take a while...)"
	unzip -q downloads/ndk.zip -d toolchain/
	mv toolchain/android-ndk-* toolchain/ndk/
fi

pushd toolchain

if [[ ! -d $ARCH ]]; then
	echo "==> Making standalone toolchain for architecture $ARCH"

	./ndk/build/tools/make_standalone_toolchain.py \
		--arch $ARCH \
		--api $ANDROID_API \
		--stl libc++ \
		--install-dir ./$ARCH

	# Patch it to ensure gcc is never ever never used
	rm -f $ARCH/bin/$NDK_TRIPLET-gcc
	rm -f $ARCH/bin/$NDK_TRIPLET-g++

	# symlink gcc to clang
	ln -s $NDK_TRIPLET-clang $ARCH/bin/$NDK_TRIPLET-gcc
	ln -s $NDK_TRIPLET-clang++ $ARCH/bin/$NDK_TRIPLET-g++

	# copy over gas-preprocessor for ffmpeg
	cp ../patches/gas-preprocessor.pl $ARCH/bin/
fi

popd
