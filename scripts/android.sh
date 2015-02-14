#!/bin/bash

DEST=`pwd`/build/android && rm -rf $DEST
SOURCE=`pwd`

TOOLCHAIN=/tmp/Shou
SYSROOT=$TOOLCHAIN/sysroot
$ANDROID_NDK/build/tools/make-standalone-toolchain.sh --toolchain=arm-linux-androideabi-4.9 \
  --system=linux-x86 --platform=android-19 --install-dir=$TOOLCHAIN


export PATH=$TOOLCHAIN/bin:$PATH
export CC="ccache arm-linux-androideabi-gcc"
export AR=arm-linux-androideabi-ar
export LD=arm-linux-androideabi-ld
export STRIP=arm-linux-androideabi-strip

CFLAGS="-std=c99  -Ofast -Os -mtune=cortex-a7 -Wall -marm -pipe -fpic -ftree-vectorize -fasm -funsafe-math-optimizations -funroll-loops \
  -march=armv7-a -mfpu=neon -D_NDK_MATH_NO_SOFTFP=1 -mfloat-abi=hard -mvectorize-with-neon-quad \
  -finline-limit=300 -ffast-math \
  -fstrict-aliasing -Werror=strict-aliasing \
  -fmodulo-sched -fmodulo-sched-allow-regmoves \
  -Wno-psabi -Wa,--noexecstack \
  -DANDROID"
LDFLAGS="-lz -Wl,--no-undefined -Wl,-z,noexecstack \
  -Wl,--no-warn-mismatch -lm_hard -Wl,--fix-cortex-a8"

FFMPEG_FLAGS="--target-os=linux \
  --arch=arm \
  --cross-prefix=arm-linux-androideabi- \
  --enable-cross-compile \
  --enable-static \
  --disable-shared \
    --disable-doc \
    --disable-ffmpeg \
    --disable-ffplay \
    --disable-ffprobe \
    --disable-ffserver \
    --disable-doc \
    --disable-symver \
   --disable-armv6t2 \
--disable-debug \
 --disable-armv6 \
--disable-armv5te \
  --disable-runtime-cpudetect \
  --enable-protocol=file \
  --enable-protocol=rtp \
  --enable-protocol=srtp \
  --enable-filter=atempo \
  --enable-filter=volume \
  --enable-filter=aresample \
  --enable-filter=aconvert \
  --enable-filter=aformat \
  --enable-muxer=rtp \
  --enable-muxer=rtsp \
  --enable-muxer=mpegts \
  --enable-muxer=hls \
  --enable-muxer=matroska \
  --enable-muxer=mp4 \
  --enable-muxer=mov \
  --enable-muxer=tgp \
  --enable-muxer=flv \
  --enable-muxer=image2 \
  --enable-muxer=tee \
  --enable-encoder=mpeg4 \
  --enable-encoder=h263p \
  --enable-encoder=alac \
  --enable-encoder=pcm_s16le \
  --enable-asm \
  --enable-version3"


PREFIX="$DEST/neon" && mkdir -p $PREFIX
FFMPEG_FLAGS="$FFMPEG_FLAGS --prefix=$PREFIX"

./configure $FFMPEG_FLAGS --extra-cflags="$CFLAGS" --extra-ldflags="$LDFLAGS $FFMPEG_LIB_FLAGS" | tee $PREFIX/configuration.txt
cp config.* $PREFIX
[ $PIPESTATUS == 0 ] || exit 1

make clean
find . -name "*.o" -type f -delete
make -j7 install || exit 1

