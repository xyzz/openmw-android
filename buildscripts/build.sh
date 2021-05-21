#!/bin/bash

set -e
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd $DIR

export ARCH="arm"
export CCACHE="false"
ASAN="false"
DEPLOY_RESOURCES="true"
LTO="false"
BUILD_TYPE="release"
CFLAGS="-fPIC"
CXXFLAGS="-fPIC -frtti -fexceptions"
LDFLAGS=""

usage() {
	echo "Usage: ./build.sh [--help] [--asan] [--arch arch] [--debug|--release]"
	echo "	--help: print this message"
	echo "	--arch: build for specified architecture [arm, arm64, x86_64, x86] (default: arm)"
	echo "	--asan: build with AddressSanitizer enabled"
	echo "	--no-resources: don't deploy the resources (used in full-build.sh)"
	echo "	--lto: use LTO for linking"
	echo "	--ccache: use ccache to speed up repeated builds"
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
		--ccache)
			export CCACHE="true"
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
		--no-resources)
			DEPLOY_RESOURCES="false"
			shift
			;;
		*)
			echo "Invalid argument: $key"
			exit 1
			;;
	esac
done

if [[ $ASAN = true && $ARCH != "arm" && $ARCH != "arm64" ]]; then
	echo "AddressSanitizer is only supported on arm and aarch64 architectures"
	exit 1
fi

source ./include/version.sh

if [ $ASAN = true ]; then
	CFLAGS="$CFLAGS -fsanitize=address -fuse-ld=gold -fno-omit-frame-pointer"
	CXXFLAGS="$CXXFLAGS -fsanitize=address -fuse-ld=gold -fno-omit-frame-pointer"
	LDFLAGS="$LDFLAGS -fsanitize=address -fuse-ld=gold -fno-omit-frame-pointer"
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
	LDFLAGS="$LDFLAGS -flto -Wl,-plugin-opt=-emulated-tls -fuse-ld=gold"
fi

if [[ $ARCH = "arm" ]]; then
	CFLAGS="$CFLAGS -mthumb"
	CXXFLAGS="$CXXFLAGS -mthumb"
fi

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
mkdir -p prefix/$ARCH/osg/lib
ln -sf lib prefix/$ARCH/osg/lib64

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

rm -rf ../app/wrap/
rm -rf ../app/src/main/jniLibs/$ABI/
mkdir -p ../app/src/main/jniLibs/$ABI/

# libopenmw.so is a special case
find build/$ARCH/openmw-prefix/ -iname "libopenmw.so" -exec cp "{}" ../app/src/main/jniLibs/$ABI/libopenmw.so \;

# copy over libs we compiled
cp prefix/$ARCH/lib/{libopenal,libSDL2,libhidapi,libGL}.so ../app/src/main/jniLibs/$ABI/

# copy over libc++_shared
find ./toolchain/$ARCH/sysroot/usr/lib/$NDK_TRIPLET -iname "libc++_shared.so" -exec cp "{}" ../app/src/main/jniLibs/$ABI/ \;

if [[ $DEPLOY_RESOURCES = "true" ]]; then
	echo "==> Deploying resources"

	DST=$DIR/../app/src/main/assets/libopenmw/
	SRC=build/$ARCH/openmw-prefix/src/openmw-build/

	rm -rf "$DST" && mkdir -p "$DST"

	# resources
	cp -r "$SRC/resources" "$DST"

	# global config
	mkdir -p "$DST/openmw/"
	cp "$SRC/defaults.bin" "$DST/openmw/"
	cp "$SRC/gamecontrollerdb.txt" "$DST/openmw/"
	cat "$SRC/openmw.cfg" | grep -v "data=" | grep -v "data-local=" >> "$DST/openmw/openmw.base.cfg"
	cat "$DIR/../app/openmw.base.cfg" >> "$DST/openmw/openmw.base.cfg"

	# licensing info
	cp "$DIR/../3rdparty-licenses.txt" "$DST"
fi

echo "==> Making your debugging life easier"

# copy unstripped libs to aid debugging
rm -rf "./symbols/$ABI/" && mkdir -p "./symbols/$ABI/"
cp "./build/$ARCH/openal-prefix/src/openal-build/libopenal.so" "./symbols/$ABI/"
cp "./build/$ARCH/sdl2-prefix/src/sdl2-build/obj/local/$ABI/libSDL2.so" "./symbols/$ABI/"
cp "./build/$ARCH/sdl2-prefix/src/sdl2-build/obj/local/$ABI/libhidapi.so" "./symbols/$ABI/"
cp "./build/$ARCH/openmw-prefix/src/openmw-build/libopenmw.so" "./symbols/$ABI/libopenmw.so"
cp "./build/$ARCH/gl4es-prefix/src/gl4es-build/obj/local/$ABI/libGL.so" "./symbols/$ABI/"
cp "../app/src/main/jniLibs/$ABI/libc++_shared.so" "./symbols/$ABI/"

if [ $ASAN = true ]; then
	cp ./toolchain/$ARCH/lib64/clang/*/lib/linux/libclang_rt.asan-$ASAN_ARCH-android.so "./symbols/$ABI/"
	cp ./toolchain/$ARCH/lib64/clang/*/lib/linux/libclang_rt.asan-$ASAN_ARCH-android.so "../app/src/main/jniLibs/$ABI/"
	mkdir -p ../app/wrap/res/lib/$ABI/
	sed "s/@ASAN_ARCH@/$ASAN_ARCH/g" < include/asan-wrapper.sh > "../app/wrap/res/lib/$ABI/wrap.sh"
	chmod +x "../app/wrap/res/lib/$ABI/wrap.sh"
fi

PATH="$DIR/toolchain/ndk/prebuilt/linux-x86_64/bin/:$DIR/toolchain/$ARCH/$NDK_TRIPLET/bin/:$PATH" ./include/gdb-add-index ./symbols/$ABI/*.so

# gradle should do it, but just in case...
$NDK_TRIPLET-strip ../app/src/main/jniLibs/$ABI/*.so

echo "==> Success"
