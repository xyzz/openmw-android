#!/bin/bash

set -e
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd $DIR

mv fakeJni jni
../../../buildscripts/toolchain/ndk/ndk-gdb --launch --nowait -x "gdb.exec"
