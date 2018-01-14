#!/bin/bash

set -e
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd $DIR

source ./include/version.sh

echo "==> Download and set up the NDK"
./include/download-ndk.sh
./include/setup-ndk.sh

NCPU=$(grep -c ^processor /proc/cpuinfo)
echo "==> Build using $NCPU CPUs"
mkdir -p build/$ARCH/
mkdir -p prefix/$ARCH/

# generate command_wrapper.sh
cat include/command_wrapper_head.sh.in | \
	DIR=$DIR \
	ARCH=$ARCH \
		envsubst > build/$ARCH/command_wrapper.sh
cat include/command_wrapper_tail.sh.in >> build/$ARCH/command_wrapper.sh
chmod +x build/$ARCH/command_wrapper.sh

pushd build/$ARCH/

# Get CC/CXX/etc vars
source ./command_wrapper.sh true

# Build!
cmake ../.. -DCMAKE_INSTALL_PREFIX=$DIR/prefix/$ARCH/
make -j$NCPU

popd

echo "==> Installing shared libraries"

rm -rf ../app/src/main/jniLibs/
mkdir -p ../app/src/main/jniLibs/armeabi-v7a/

# libopenmw.so is a special case
find build/$ARCH/ -iname "libopenmw.so" -exec cp "{}" ../app/src/main/jniLibs/armeabi-v7a/ \; # TODO

# copy over libs we compiled
cp prefix/$ARCH/lib/{libopenal,libSDL2}.so ../app/src/main/jniLibs/armeabi-v7a/ # TODO

# copy over libc++_shared
cp ./toolchain/$ARCH/*/lib/armv7-a/libc++_shared.so ../app/src/main/jniLibs/armeabi-v7a/ # TODO

arm-linux-androideabi-strip ../app/src/main/jniLibs/armeabi-v7a/*.so # TODO

echo "==> Success"
