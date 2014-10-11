eclipse-project
===============
== Android build OpenMW ==
===============
=== Preparation of building environment ===
Before you start building OpenMW on Android you have to do few steps of preparation:
# Download Crystax NDK from [https://www.crystax.net/android/ndk.php] 
You can use only crystax ndk for building openmw !
# Download and install [http://developer.android.com/sdk/index.html Google Android SDK]
# Download [https://github.com/taka-no-me/android-cmake Cmake for Android] (it will be used for OpenMW and its dependencies compilation)
# Java
http://www.oracle.com/technetwork/java/javase/downloads/index-jsp-138363.html
# Ant
http://ant.apache.org/
# Download all OpenMW dependencies, ie:
# Ogre 1.9 
#https://bitbucket.org/sinbad/ogre/src/4578da5bf5b00fdf023b87e98099d647c5cb92ab?at=v1-9-0
#* Openal
#http://repo.or.cz/w/openal-soft/android.git
#* boost
#https://github.com/MysticTreeGames/Boost-for-Android
#* bullet
#https://github.com/bulletphysics/bullet3
#* Sdl2
#https://www.libsdl.org/hg.php
#* Qt 4.8 android
http://necessitas.kde.org/necessitas/qt4_framework.php
#* ffmpeg
https://www.ffmpeg.org/download.html
#* freeimage
#* freetype
http://www.freetype.org/
#* mygui 3.2.1
https://github.com/MyGUI/mygui
#* zzip
#* zlib


=== Compilation of OpenMW dependencies ===

Then you should add PATH for crystax ndk and google sdk.
you should copy this library 

android-ndk-r8-crystax-1/sources/crystax/libs/armeabi-v7a/libcrystax.so

and

android-ndk-r8-crystax-1/sources/crystax/libs/armeabi-v7a/libcrystax.a

to path 
android-ndk-r8-crystax-1/sources/cxx-stl/gnu-libstdc++/4.7/libs/armeabi-v7a
/android-ndk-r8-crystax-1/toolchains/arm-linux-androideabi-4.7/prebuilt/linux-x86/lib/gcc/arm-linux-androideabi/4.7
/android-ndk-r8-crystax-1/toolchains/arm-linux-androideabi-4.7/prebuilt/linux-x86/lib/gcc/arm-linux-androideabi/4.7/armv7-a
if you not do it, you will not be able to build anything

==== Building Boost ====
For building boost I used it. You must build Ogre with Boost!
https://github.com/MysticTreeGames/Boost-for-Android
Build boost with crystax ndk had problems, so here modified scripts
https://github.com/OpenMW/eclipse-project/tree/master/jni/Boost-for-Android-master

==== Building Freetype ====
Then you must build freetype
http://www.freetype.org/
For example like this
cmake /home/sylar/freetype -DCMAKE_TOOLCHAIN_FILE=/home/sylar/android-cmake-master/android.toolchain.cmake -DANDROID_NATIVE_API_LEVEL=14

==== Building Freeimage ====
Then you must build freeimage. I used this tutorial for building.

http://freeimage.sourceforge.net/

http://recursify.com/blog/2013/05/25/building-freeimage-for-android

But as it turned out it is difficult to get to work freeimage.
So I put the source code with the modified code here.
https://github.com/OpenMW/eclipse-project/tree/master/jni/Freeimage/jni

Since my phone supports architecture armeabi-v7a I used this architecture for building. For example.
ndk-build APP_PLATFORM=android-14 APP_ABI=armeabi-v7a
==== Building Ogre3D ====
Next you must build ogre from source. I build ogre 1.9 with this tutorial.

http://www.ogre3d.org/tikiwiki/tiki-index.php?page=CMake+Quick+Start+Guide&tikiversion=Android

Next you must add it


if (!mEglConfig)
{
  _createInternalResources(mWindow, config);
  mHwGamma = false;
}
        
mEglDisplay = mGLSupport->getGLDisplay();

To
RenderSystems/GLES2/src/EGL/Android/OgreAndroidEGLWindow.cpp

Next you must comment this 
 else if (mSoftwareMipmap)
        {
/*
            if (data.getWidth() != data.rowPitch)
            {
                OGRE_EXCEPT(Exception::ERR_INVALIDPARAMS,
                            "Unsupported texture format",
                            "GLES2TextureBuffer::upload");
            }

            if (data.getHeight() * data.getWidth() != data.slicePitch)
            {
                OGRE_EXCEPT(Exception::ERR_INVALIDPARAMS,
                            "Unsupported texture format",
                            "GLES2TextureBuffer::upload");
            }
*/
            if ((data.getWidth() * PixelUtil::getNumElemBytes(data.format)) & 3)
            {
                // Standard alignment of 4 is not right
                OGRE_CHECK_GL_ERROR(glPixelStorei(GL_UNPACK_ALIGNMENT, 1));
            }

            buildMipmaps(data);
        }
        else
        {
/*
            if (data.getWidth() != data.rowPitch)
            {
                OGRE_EXCEPT(Exception::ERR_INVALIDPARAMS,
                            "Unsupported texture format",
                            "GLES2TextureBuffer::upload");
            }

            if (data.getHeight() * data.getWidth() != data.slicePitch)
            {
                OGRE_EXCEPT(Exception::ERR_INVALIDPARAMS,
                            "Unsupported texture format",
                            "GLES2TextureBuffer::upload");
            }
*/
in
RenderSystems/GLES2/src/OgreGLES2HardwarePixelBuffer.cpp
If you  not do it, the locations in game will not be loaded .

Next you must change Eglcontext in function 
::EGLContext EGLSupport::createNewContext(EGLDisplay eglDisplay,
					      ::EGLConfig glconfig,
                                              ::EGLContext shareList) const 
if file
/RenderSystems/GLES2/src/EGL/OgreEGLSupport.cpp
from 
 context = eglCreateContext(mGLDisplay, glconfig, shareList, contextAttrs);
 context = eglCreateContext(mGLDisplay, glconfig, 0, contextAttrs);
to
context = eglGetCurrentContext(); .
Next you must comment this lines in
RenderSystems/GLES2/src/OgreGLES2RenderSystem.cpp
/*
        if (mGLSupport->checkExtension("GL_IMG_texture_compression_pvrtc") ||
            mGLSupport->checkExtension("GL_EXT_texture_compression_dxt1") ||
            mGLSupport->checkExtension("GL_EXT_texture_compression_s3tc") ||
            mGLSupport->checkExtension("GL_OES_compressed_ETC1_RGB8_texture") ||
            mGLSupport->checkExtension("GL_AMD_compressed_ATC_texture"))
        {
            rsc->setCapability(RSC_TEXTURE_COMPRESSION);

            if(mGLSupport->checkExtension("GL_IMG_texture_compression_pvrtc") ||
               mGLSupport->checkExtension("GL_IMG_texture_compression_pvrtc2"))
                rsc->setCapability(RSC_TEXTURE_COMPRESSION_PVRTC);
				
            if(mGLSupport->checkExtension("GL_EXT_texture_compression_dxt1") && 
               mGLSupport->checkExtension("GL_EXT_texture_compression_s3tc"))
                rsc->setCapability(RSC_TEXTURE_COMPRESSION_DXT);

            if(mGLSupport->checkExtension("GL_OES_compressed_ETC1_RGB8_texture"))
                rsc->setCapability(RSC_TEXTURE_COMPRESSION_ETC1);

            if(gleswIsSupported(3, 0))
                rsc->setCapability(RSC_TEXTURE_COMPRESSION_ETC2);

			if(mGLSupport->checkExtension("GL_AMD_compressed_ATC_texture"))
                rsc->setCapability(RSC_TEXTURE_COMPRESSION_ATC);
        }
*/

If your device based on Tegra gpu you should also use this patch for Ogre
https://bitbucket.org/sinbad/ogre/pull-request/390/fix-for-nvidia-tegra-3-for-android

Also in Ogre 1.9 forgot to add this line to  cmake file
if(ANDROID)
  set(CMAKE_FIND_ROOT_PATH ${OGRE_DEPENDENCIES_DIR} "${CMAKE_FIND_ROOT_PATH}")
endif()


==== Building MyGUI ====
Then you must build mygui for ogre

https://github.com/MyGUI/mygui

==== Building Bullet ====
Then you should build bullet. You can use cmake for it
https://github.com/bulletphysics/bullet3

==== Building OpenAL ====
Then you must build openal .
http://repo.or.cz/w/openal-soft/android.git

==== Building Qt (optional) ====

Note: Qt is only used by the launcher and OpenCS, and can be skipped.
To build Qt 4.8 for android:
http://necessitas.kde.org/ I used this tutorial for building: https://community.kde.org/Necessitas/CompileQtFramework

==== Building FFmpeg ====
Then you must build ffmpeg . Important! You must use ffmpeg 1.26: 
tutorial how to build ffmpeg for android
http://www.roman10.net/how-to-build-ffmpeg-with-ndk-r9/
my script for build
./configure \
    --prefix=$(pwd)/android/$CPU  \
    --enable-shared \
    --disable-static \
    --disable-doc \
    --disable-ffmpeg \
    --disable-ffplay \
    --disable-ffprobe \
    --disable-ffserver \
    --disable-doc \
    --disable-symver \
    --cross-prefix=/home/sylar/android-ndk-r8-crystax-1/toolchains/arm-linux-androideabi-4.7/prebuilt/linux-x86/bin/arm-linux-androideabi- \
    --target-os=linux \
    --arch=arm \
    --enable-asm \
    --enable-cross-compile \
    --sysroot=/home/sylar/android-ndk-r8-crystax-1/platforms/android-14/arch-arm \
    --extra-cflags="-Os -fpic $ADDI_CFLAGS" \
    --extra-ldflags="$ADDI_LDFLAGS"
    
==== Building SDL2 ====
Then you must build SDL2: https://www.libsdl.org/hg.php
I used SDL2 mercurial latest source.

==== Building zzip====
I used this library here
http://sourceforge.net/projects/ogre/files/ogre-dependencies-android/1.9/AndroidDependencies_27_08_2013.zip/download

==== Building zlip====
I used this library from ndk  because this library is available in android

==== Building Openal====
Then you must build openal .
http://repo.or.cz/w/openal-soft/android.git
=== Building OpenMW ===
Then I started to build OpenMW. 
I copied all  libraries  in one folder . For example AndroidDependencies
Also you must copy tis library to the same folder
https://github.com/OpenMW/eclipse-project/tree/master/libs
it is used by ogre to determine the parameters  processor
and build openmw like this

cmake /home/sylar/openmw -DCMAKE_TOOLCHAIN_FILE=/home/sylar/android-cmake-master/android.toolchain.cmake 
-DOPENMW_DEPENDENCIES_DIR=/home/sylar/AndroidDependencies -DANDROID_NATIVE_API_LEVEL=14

In building, you will have bug with pthread library .
I solved this problem as follows . I found this file
/home/sylar/openmw21/apps/openmw/CMakeFiles/openmw.dir/link.txt
and change -lpthread on -pthread
After builing openmw library, you must copy all the libraries to libs folder in the  java project folder
for example :
 /home/sylar/SDL/android-project/libs/armeabi-v7a
Then you must import this java project in eclipse , which included with the android sdk
And last thing . You must add line  filtering linear linear none to texture_unit diffuse_Map in file objects.mat 
If you not do it you will have a black screen in the game
