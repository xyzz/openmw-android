#!/bin/bash

rm -rf tmp
mkdir -p tmp/symbols

for path in build/*; do
    arch=$(basename $path)
    echo "Found symbols for $arch"
    mkdir -p tmp/symbols/$arch
    cp build/$arch/symbols/* tmp/symbols/$arch/
done

cd tmp
7z a symbols.7z symbols
cd ..

mv tmp/symbols.7z .
