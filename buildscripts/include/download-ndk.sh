#!/bin/bash

set -e

source ./include/version.sh

mkdir -p downloads

pushd downloads

if [[ -f $NDK_FILE ]]; then
	# We've already downloaded it
	exit 0
fi

curl --http1.1 "https://dl.google.com/android/repository/android-ndk-${NDK_VERSION}-linux-x86_64.zip" > $NDK_FILE

echo "==> Checking NDK zip file integrity"
FILE_HASH=$(sha256sum $NDK_FILE | awk '{print $1}' )
if [[ $FILE_HASH != $NDK_HASH ]]; then
	echo "Failed, expected $NDK_HASH got $FILE_HASH"
	exit 1
fi

popd
