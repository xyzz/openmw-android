#!/bin/bash

set -e

REMOTE=$1
PEM=$2

CMAKE_VERSION="3.11"

if [[ $REMOTE = "" || $PEM = "" ]]; then
	echo "Usage: ./ssh-build.sh HOST-IP PEM-FILE"
	exit 1
fi

append() {
	echo $1 >> tmp_script.sh
}

rm -rf ../app/src/main/jniLibs/
rm -rf ../app/wrap/

echo "==> Build on $1 using $2 as the key..."

echo "" > tmp_script.sh
append "#!/bin/bash"
append "set -e"
append "sudo apt -y update"
append "sudo DEBIAN_FRONTEND=noninteractive apt-get upgrade -yq"
append "sudo apt -y install build-essential htop python unzip pkg-config p7zip-full"
append "sudo umount /mnt || true"
append "sudo mount -t tmpfs -o size=64G tmpfs /mnt"
append "wget https://cmake.org/files/v$CMAKE_VERSION/cmake-$CMAKE_VERSION.0-Linux-x86_64.tar.gz"
append "tar xvf cmake-$CMAKE_VERSION.0-Linux-x86_64.tar.gz"
append "cd /home/ubuntu/"
append "export PATH=/home/ubuntu/cmake-$CMAKE_VERSION.0-Linux-x86_64/bin/:\$PATH"
append "cd /mnt"
append "git clone https://github.com/xyzz/android-port.git"
append "cd android-port/buildscripts"
append "time ./full-build.sh"
append "./package-symbols.sh"

echo "==> Uploading the script"

scp -i $PEM tmp_script.sh ubuntu@$REMOTE:/home/ubuntu/

echo "==> Running the script"
ssh -i $PEM ubuntu@$REMOTE "bash tmp_script.sh"

echo "==> Retrieving the libraries"
scp -r -i $PEM ubuntu@$REMOTE:/mnt/android-port/app/src/main/jniLibs ../app/src/main/

rm -rf ../app/src/main/assets/libopenmw/
echo "==> Retrieving the resources"
scp -r -i $PEM ubuntu@$REMOTE:/mnt/android-port/app/src/main/assets/libopenmw ../app/src/main/assets/

echo "==> Retrieving the symbols"
rm -f symbols.7z
scp -r -i $PEM ubuntu@$REMOTE:/mnt/android-port/buildscripts/symbols.7z .

echo "==> Extracting the symbols"
rm -rf symbols
7z x symbols.7z

echo "==> All OK!"
