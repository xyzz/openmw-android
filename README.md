# OpenMW for Android

## Building

There are two steps for building OpenMW for Android. The first step is building C/C++ libraries. The second step is building the Java launcher.

### Step 1: Build the libraries

Go into the `buildscripts` directory and run `./build.sh`. The script will automatically download the Android native toolchain and all dependencies, and will compile and install them.

### Step 2: Build the Java launcher

To get an APK file you can install, open the `android-port` directory in Android Studio and run the project.

Alternatively, if you do not have Android Studio installed or would rather not use it, run `./gradlew assembleDebug` from the root directory of this repository. The resulting APK, located at `./app/build/outputs/apk/debug/app-debug.apk`, can be transferred to the device and installed.

## Credits

Original Java code written by sandstranger. Build scripts originally written by sandstranger and bwhaines.
