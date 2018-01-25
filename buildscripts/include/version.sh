#!/bin/bash

NDK_VERSION="r16b"
NDK_HASH="bcdea4f5353773b2ffa85b5a9a2ae35544ce88ec5b507301d8cf6a76b765d901"
ANDROID_API="21"

# End of configurable options

NDK_FILE="ndk.zip"

if [[ $ARCH = "arm" ]]; then
	ABI="armeabi-v7a"
	NDK_TRIPLET="arm-linux-androideabi"
	FFMPEG_CPU="armv7-a"
	BOOST_ARCH="arm"
	BOOST_ADDRESS_MODEL="32"
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
