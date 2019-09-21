#!/bin/bash

NDK_VERSION="r20"
NDK_HASH="57435158f109162f41f2f43d5563d2164e4d5d0364783a9a6fab3ef12cb06ce0"
ANDROID_API="21"

# End of configurable options

NDK_FILE="ndk.zip"

if [[ $ARCH = "arm" ]]; then
	ABI="armeabi-v7a"
	NDK_TRIPLET="arm-linux-androideabi"
	FFMPEG_CPU="armv7-a"
	BOOST_ARCH="arm"
	BOOST_ADDRESS_MODEL="32"
	ASAN_ARCH="arm"
elif [[ $ARCH = "arm64" ]]; then
	ABI="arm64-v8a"
	NDK_TRIPLET="aarch64-linux-android"
	FFMPEG_CPU="armv8-a"
	BOOST_ARCH="arm"
	BOOST_ADDRESS_MODEL="64"
	ASAN_ARCH="aarch64"
elif [[ $ARCH = "x86_64" ]]; then
	ABI="x86_64"
	NDK_TRIPLET="x86_64-linux-android"
	FFMPEG_CPU="intel"
	BOOST_ARCH="x86"
	BOOST_ADDRESS_MODEL="64"
elif [[ $ARCH = "x86" ]]; then
	ABI="x86"
	NDK_TRIPLET="i686-linux-android"
	FFMPEG_CPU="intel"
	BOOST_ARCH="x86"
	BOOST_ADDRESS_MODEL="32"
else
	echo "Unknown architecture: $ARCH"
	exit 1
fi
