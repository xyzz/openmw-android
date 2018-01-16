#!/bin/bash

set -e

source ./include/version.sh

if [[ -d toolchain ]]; then
	# We've already set it up
	exit 0
fi

mkdir -p toolchain

unzip downloads/ndk.zip -d toolchain/
mv toolchain/android-ndk-* toolchain/ndk/

pushd toolchain

echo "==> Making standalone toolchain for architecture $ARCH"

./ndk/build/tools/make_standalone_toolchain.py \
	--arch $ARCH \
	--api $ANDROID_API \
	--stl libc++ \
	--install-dir ./$ARCH

# Patch it to ensure gcc is never ever never used
rm -f $ARCH/bin/$ARCH-linux-androideabi-gcc
rm -f $ARCH/bin/$ARCH-linux-androideabi-g++

# symlink gcc to clang
ln -s $ARCH-linux-androideabi-clang $ARCH/bin/$ARCH-linux-androideabi-gcc
ln -s $ARCH-linux-androideabi-clang++ $ARCH/bin/$ARCH-linux-androideabi-g++

# copy over gas-preprocessor for ffmpeg
cp ../patches/gas-preprocessor.pl $ARCH/bin/

popd
