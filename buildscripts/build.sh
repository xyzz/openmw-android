#!/bin/bash

set -e
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd $DIR

export ARCH="arm"
ASAN="false"
LTO="false"
BUILD_TYPE="release"
CFLAGS="-fPIC"
CXXFLAGS="-fPIC -frtti -fexceptions"
LDFLAGS="-Wl,--exclude-libs,libgcc.a -Wl,--exclude-libs,libatomic.a -Wl,--exclude-libs,libunwind.a"

usage() {
	echo "Usage: ./build.sh [--help] [--asan] [--arch arch] [--debug|--release]"
	echo "	--help: print this message"
	echo "	--arch: build for specified architecture [arm, arm64, x86_64, x86] (default: arm)"
	echo "	--asan: build with AddressSanitizer enabled"
	echo "	--debug: produce a debug build without optimizations"
	echo "	--release: produce a release build with optimizations (default)"
	exit 0
}

# Parse command-line arguments
while [[ $# -gt 0 ]]; do
	key="$1"

	case $key in
		--help)
			usage
			shift
			;;
		--arch)
			export ARCH="$2"
			shift 2
			;;
		--asan)
			ASAN=true
			shift
			;;
		--lto)
			LTO=true
			shift
			;;
		--debug)
			BUILD_TYPE="debug"
			shift
			;;
		--release)
			BUILD_TYPE="release"
			shift
			;;
		*)
			echo "Invalid argument: $key"
			exit 1
			;;
	esac
done

if [[ $ASAN = true && $ARCH != "arm" ]]; then
	echo "AddressSanitizer is only supported on arm and aarch64 architectures"
	exit 1
fi

source ./include/version.sh

if [ $ASAN = true ]; then
	CFLAGS="$CFLAGS -fsanitize=address"
	CXXFLAGS="$CXXFLAGS -fsanitize=address"
	LDFLAGS="$LDFLAGS -fsanitize=address"
fi

if [ $BUILD_TYPE = "release" ]; then
	CFLAGS="$CFLAGS -O3"
	CXXFLAGS="$CXXFLAGS -O3"
else
	CFLAGS="$CFLAGS -O0 -g"
	CXXFLAGS="$CXXFLAGS -O0 -g"
fi

if [[ $LTO = "true" ]]; then
	CFLAGS="$CFLAGS -flto"
	CXXFLAGS="$CXXFLAGS -flto"
	# emulated-tls should not be needed in ndk r18 https://github.com/android-ndk/ndk/issues/498#issuecomment-327825754
	LDFLAGS="$LDFLAGS -flto -Wl,-plugin-opt=-emulated-tls"
fi

if [[ $ARCH = "arm" ]]; then
	CFLAGS="$CFLAGS -mthumb"
	CXXFLAGS="$CXXFLAGS -mthumb"
fi

# https://github.com/android-ndk/ndk/issues/105 to be fixed in r17
LDFLAGS="$LDFLAGS -stdlib=libc++ -L$DIR/toolchain/ndk/sources/cxx-stl/llvm-libc++/libs/$ABI/"

echo ""
echo "================================================================================"
echo ""
echo "Build configuration:"
echo " - Architecture: $ARCH"
echo " - Build type: $BUILD_TYPE"
echo " - AddressSanitizer: $ASAN"
echo ""
echo " ------------------------------------------------------------------------------ "
echo ""
echo "Computed flags:"
echo " - CFLAGS: $CFLAGS"
echo " - CXXFLAGS: $CXXFLAGS"
echo " - LDFLAGS: $LDFLAGS"
echo ""
echo "================================================================================"
echo "(Please run ./clean.sh manually if you modify any of the options)"
echo ""

echo "==> Download and set up the NDK"
./include/download-ndk.sh
./include/setup-ndk.sh

NCPU=$(grep -c ^processor /proc/cpuinfo)
echo "==> Build using $NCPU CPUs"
mkdir -p build/$ARCH/
mkdir -p prefix/$ARCH/

# symlink lib64 -> lib so we don't get half the libs in one directory half in another
mkdir -p prefix/$ARCH/lib
ln -sf lib prefix/$ARCH/lib64

# generate command_wrapper.sh
cat include/command_wrapper_head.sh.in | \
	DIR=$DIR \
	ARCH=$ARCH \
	ENV_CFLAGS=$CFLAGS \
	ENV_CXXFLAGS=$CXXFLAGS \
	NDK_TRIPLET=$NDK_TRIPLET \
	ENV_LDFLAGS=$LDFLAGS \
		envsubst > build/$ARCH/command_wrapper.sh
cat include/command_wrapper_tail.sh.in >> build/$ARCH/command_wrapper.sh
chmod +x build/$ARCH/command_wrapper.sh

pushd build/$ARCH/

# Get CC/CXX/etc vars
source ./command_wrapper.sh true

# Build!
cmake ../.. \
	-DCMAKE_INSTALL_PREFIX=$DIR/prefix/$ARCH/ \
	-DARCH=$ARCH \
	-DBUILD_TYPE=$BUILD_TYPE \
	-DNDK_TRIPLET=$NDK_TRIPLET \
	-DANDROID_API=$ANDROID_API \
	-DABI=$ABI \
	-DBOOST_ARCH=$BOOST_ARCH \
	-DBOOST_ADDRESS_MODEL=$BOOST_ADDRESS_MODEL \
	-DFFMPEG_CPU=$FFMPEG_CPU
make -j$NCPU

popd

echo "==> Installing shared libraries"

rm -rf ../app/src/main/jniLibs/$ABI/
mkdir -p ../app/src/main/jniLibs/$ABI/

# libopenmw.so is a special case
find build/$ARCH/ -iname "libopenmw.so" -exec cp "{}" ../app/src/main/jniLibs/$ABI/ \;

# copy over libs we compiled
cp prefix/$ARCH/lib/{libopenal,libSDL2,libGL}.so ../app/src/main/jniLibs/$ABI/

# copy over libc++_shared
find ./toolchain/$ARCH/ -iname "libc++_shared.so" -exec cp "{}" ../app/src/main/jniLibs/$ABI/ \;

$NDK_TRIPLET-strip ../app/src/main/jniLibs/$ABI/*.so

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

if [ $ASAN = true ]; then
	cp "./toolchain/arm/lib64/clang/5.0/lib/linux/libclang_rt.asan-arm-android.so" "./build/$ARCH/symbols/"
fi

if [[ $ARCH = "arm" ]]; then
	for file in ./build/$ARCH/symbols/*.so; do
		PATH="$DIR/toolchain/ndk/prebuilt/linux-x86_64/bin/:$DIR/toolchain/$ARCH/$NDK_TRIPLET/bin/:$PATH" ./include/gdb-add-index $file
	done
fi

echo "==> Success"
