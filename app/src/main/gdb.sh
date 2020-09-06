#!/bin/bash

set -e
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd $DIR

# first argument: arch of the executable used (you have to figure it out yourself!)
ARCH=${1:-arm}
source ../../../buildscripts/include/version.sh

# set up fake "jni" so that ndk-gdb can find a "valid" Android.mk
rm -rf jni && mkdir jni
echo "APP_ABI := $ABI" > jni/Android.mk

rm -f gdb.exec
echo "shell rm -rf jni" >> gdb.exec
echo "set solib-search-path ../../../buildscripts/symbols/$ABI/" >> gdb.exec
echo "set history save on" >> gdb.exec
echo "set breakpoint pending on" >> gdb.exec

../../../buildscripts/toolchain/ndk/ndk-gdb --attach is.xyz.omw_nightly.debug -x "gdb.exec"
