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
mkdir -p ../app/src/main/jniLibs/$ABI/

# libopenmw.so is a special case
find build/$ARCH/ -iname "libopenmw.so" -exec cp "{}" ../app/src/main/jniLibs/$ABI/ \;

# copy over libs we compiled
cp prefix/$ARCH/lib/{libopenal,libSDL2,libGL}.so ../app/src/main/jniLibs/$ABI/

# copy over libc++_shared
cp ./toolchain/$ARCH/*/lib/armv7-a/libc++_shared.so ../app/src/main/jniLibs/$ABI/

arm-linux-androideabi-strip ../app/src/main/jniLibs/$ABI/*.so

echo "==> Deploying resources"

DST=$DIR/../app/src/main/assets/libopenmw/
SRC=build/$ARCH/openmw-prefix/src/openmw-build/

rm -rf "$DST" && mkdir -p "$DST"

# resources
cp -r "$SRC/resources" "$DST"

# global config
mkdir -p "$DST/openmw/"
cp "$SRC/settings-default.cfg" "$DST/openmw/"
cp "$SRC/gamecontrollerdb.txt" "$DST/openmw/"

# local config
mkdir -p "$DST/config/openmw/"
# TODO: do we really need this twice?
cp "$SRC/gamecontrollerdb.txt" "$DST/config/openmw/"
cp "$DIR/../app/openmw-base.cfg" "$DST/config/openmw/openmw.cfg"
cp "$DIR/../app/settings-base.cfg" "$DST/config/openmw/settings.cfg"

echo "==> Making your debugging life easier"

# copy unstripped libs to aid debugging
rm -rf "./build/$ARCH/symbols" && mkdir -p "./build/$ARCH/symbols"
cp "./build/$ARCH/openal-prefix/src/openal-build/libopenal.so" "./build/$ARCH/symbols/"
cp "./build/$ARCH/sdl2-prefix/src/sdl2-build/obj/local/$ABI/libSDL2.so" "./build/$ARCH/symbols/"
cp "./build/$ARCH/openmw-prefix/src/openmw-build/libopenmw.so" "./build/$ARCH/symbols/"
cp "./build/$ARCH/gl4es-prefix/src/gl4es-build/obj/local/$ABI/libGL.so" "./build/$ARCH/symbols/"

for file in ./build/$ARCH/symbols/*.so; do
	PATH="$DIR/toolchain/ndk/prebuilt/linux-x86_64/bin/:$DIR/toolchain/arm/arm-linux-androideabi/bin/:$PATH" ./include/gdb-add-index $file
done

echo "==> Success"
