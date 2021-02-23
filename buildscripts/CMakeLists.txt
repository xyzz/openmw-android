cmake_minimum_required(VERSION 3.2.0)

# https://github.com/libjpeg-turbo/libjpeg-turbo/releases
set(LIBJPEG_TURBO_VERSION 1.5.3)
set(LIBJPEG_TURBO_HASH SHA256=b24890e2bb46e12e72a79f7e965f409f4e16466d00e1dd15d93d73ee6b592523)

# http://www.libpng.org/pub/png/libpng.html
set(LIBPNG_VERSION 1.6.37)
set(LIBPNG_HASH SHA256=505e70834d35383537b6491e7ae8641f1a4bed1876dbfe361201fc80868d88ca)

# https://www.freetype.org/index.html#news
set(FREETYPE2_VERSION 2.10.2)
set(FREETYPE2_HASH SHA256=e09aa914e4f7a5d723ac381420949c55c0b90b15744adce5d1406046022186ab)

# https://github.com/kcat/openal-soft/releases
set(OPENAL_VERSION 1.19.1)
set(OPENAL_HASH SHA256=9f3536ab2bb7781dbafabc6a61e0b34b17edd16bd6c2eaf2ae71bc63078f98c7)

# http://www.boost.org/users/download/
set(BOOST_VERSION 1.69.0)
set(BOOST_HASH SHA256=8f32d4617390d1c2d16f26a27ab60d97807b35440d45891fa340fc2648b04406)

# https://www.ffmpeg.org/download.html
set(FFMPEG_VERSION 4.3.1)
set(FFMPEG_HASH SHA256=f4a4ac63946b6eee3bbdde523e298fca6019d048d6e1db0d1439a62cea65f0d9)

# https://www.libsdl.org/download-2.0.php
set(SDL2_VERSION 2.0.12)
set(SDL2_HASH SHA256=349268f695c02efbc9b9148a70b85e58cefbbf704abd3e91be654db7f1e2c863)

# https://github.com/bulletphysics/bullet3/releases
set(BULLET_VERSION 2.89)
set(BULLET_HASH SHA256=621b36e91c0371933f3c2156db22c083383164881d2a6b84636759dc4cbb0bb8)

# https://github.com/MyGUI/mygui/releases
set(MYGUI_VERSION 3.4.0)
set(MYGUI_HASH SHA256=d1d5f294670ae71f7200ed4b30859018281d8cfd45d6a38d18b97a4aba604c42)

# https://github.com/ptitSeb/gl4es/releases
set(GL4ES_VERSION v1.1.4)
set(GL4ES_HASH SHA256=b565e717c7d192e936bda25f3cb90ad8db398af56414ec08294b6716574c1a6d)

# https://github.com/openscenegraph/OpenSceneGraph/releases
set(OSG_VERSION 3.6.5)
set(OSG_HASH SHA256=aea196550f02974d6d09291c5d83b51ca6a03b3767e234a8c0e21322927d1e12)

# https://github.com/OpenMW/openmw/commits/master
set(OPENMW_VERSION 0abcb54f51ec4a3979039b2e94ccdc5aa57920ec)
set(OPENMW_HASH SHA256=1d46a81060f789d8dd11519321a47e5309000e5423e5acf0d854440349321d9f)

# End of configurable options

project(openmw-android)

include(ExternalProject)

set(wrapper_command ${CMAKE_BINARY_DIR}/command_wrapper.sh)
set(ndk_triplet ${NDK_TRIPLET})
set(download_dir ${CMAKE_SOURCE_DIR}/downloads/)
set(prefix ${CMAKE_INSTALL_PREFIX})
set(host ${ndk_triplet})
set(android_platform android-${ANDROID_API})
set(app_abi ${ABI})

set(COMMON_CMAKE_ARGS
	-DCMAKE_TOOLCHAIN_FILE=${CMAKE_SOURCE_DIR}/toolchain/ndk/build/cmake/android.toolchain.cmake
	-DANDROID_ABI=${app_abi}
	-DANDROID_PLATFORM=${android_platform}
	-DANDROID_STL=c++_shared
	-DANDROID_CPP_FEATURES=rtti\ exceptions
	-DCMAKE_C_FLAGS=-DMYGUI_DONT_REPLACE_NULLPTR\ -I${prefix}/include/\ $ENV{CFLAGS}
	-DCMAKE_CXX_FLAGS=-DMYGUI_DONT_REPLACE_NULLPTR\ -I${prefix}/include/\ $ENV{CXXFLAGS}
	-DCMAKE_SHARED_LINKER_FLAGS=$ENV{LDFLAGS}
	-DCMAKE_BUILD_TYPE=${BUILD_TYPE}
	-DCMAKE_DEBUG_POSTFIX=
	-DCMAKE_INSTALL_PREFIX=${prefix}
	-DCMAKE_FIND_ROOT_PATH=${prefix}
)

set(COMMON_NDK_BUILD_FLAGS
	NDK_PROJECT_PATH=.
	APP_BUILD_SCRIPT=<SOURCE_DIR>/Android.mk
	APP_PLATFORM=${android_platform}
	APP_ABI=${app_abi}
	LOCAL_LDFLAGS=$ENV{LDFLAGS}
	-j4
)

set(COMMON_AUTOCONF_FLAGS
	--enable-static
	--disable-shared
	--prefix=${prefix}
)

# ffmpeg doesn't like --host, so sandwich it between adding --host to the flags
set(ffmpeg_configure_flags ${COMMON_AUTOCONF_FLAGS})
if (${BUILD_TYPE} STREQUAL "debug")
	# ffmpeg fails to compile if we have asm enabled and optimizations disabled, so disable both
	set(ffmpeg_configure_flags ${ffmpeg_configure_flags} --disable-asm --disable-optimizations)
endif()

if ((${ARCH} STREQUAL "x86_64") OR (${ARCH} STREQUAL "x86"))
	# ffmpeg fails to link with assembly optimizations on x86-64
	set(ffmpeg_configure_flags ${ffmpeg_configure_flags} --disable-asm)
endif()

set(COMMON_AUTOCONF_FLAGS
	--host=${host}
	${COMMON_AUTOCONF_FLAGS}
)

if (${ARCH} STREQUAL "arm")
	set(ffmpeg_configure_flags ${ffmpeg_configure_flags} --enable-thumb)
endif()

# on x86 enabling simd results in text relocations, which are banned on Android
set(libjpeg_turbo_flags "")
if (${ARCH} STREQUAL "x86")
	set(libjpeg_turbo_flags ${libjpeg_turbo_flags} --without-simd)
endif()

ExternalProject_Add(libjpeg-turbo
	URL https://sourceforge.net/projects/libjpeg-turbo/files/${LIBJPEG_TURBO_VERSION}/libjpeg-turbo-${LIBJPEG_TURBO_VERSION}.tar.gz/download
	https://github.com/xyzz/openmw-deps/releases/download/v0/libjpeg-turbo-${LIBJPEG_TURBO_VERSION}.tar.gz
	URL_HASH ${LIBJPEG_TURBO_HASH}
	DOWNLOAD_DIR ${download_dir}

	CONFIGURE_COMMAND ${wrapper_command} <SOURCE_DIR>/configure
	${COMMON_AUTOCONF_FLAGS}
	${libjpeg_turbo_flags}

	BUILD_COMMAND ${wrapper_command} $(MAKE) PROGRAMS=

	INSTALL_COMMAND ${wrapper_command} $(MAKE) install-libLTLIBRARIES install-data-am
)

ExternalProject_Add(libpng
	URL http://prdownloads.sourceforge.net/libpng/libpng-${LIBPNG_VERSION}.tar.xz
	https://github.com/xyzz/openmw-deps/releases/download/v0/libpng-${LIBPNG_VERSION}.tar.xz
	URL_HASH ${LIBPNG_HASH}
	DOWNLOAD_DIR ${download_dir}

	CONFIGURE_COMMAND ${wrapper_command} <SOURCE_DIR>/configure
	${COMMON_AUTOCONF_FLAGS}

	BUILD_COMMAND ${wrapper_command} $(MAKE) check_PROGRAMS= bin_PROGRAMS=

	INSTALL_COMMAND ${wrapper_command} $(MAKE) install check_PROGRAMS= bin_PROGRAMS=
)

ExternalProject_Add(freetype2
	URL https://download.savannah.gnu.org/releases/freetype/freetype-${FREETYPE2_VERSION}.tar.gz
	https://github.com/xyzz/openmw-deps/releases/download/v0/freetype-${FREETYPE2_VERSION}.tar.gz
	URL_HASH ${FREETYPE2_HASH}
	DOWNLOAD_DIR ${download_dir}

	CONFIGURE_COMMAND ${wrapper_command} <SOURCE_DIR>/configure
	${COMMON_AUTOCONF_FLAGS}
	--with-png=no

	BUILD_COMMAND ${wrapper_command} $(MAKE)

	INSTALL_COMMAND ${wrapper_command} $(MAKE) install
)

ExternalProject_Add(openal
	URL https://github.com/kcat/openal-soft/archive/openal-soft-${OPENAL_VERSION}.tar.gz
	https://github.com/xyzz/openmw-deps/releases/download/v0/openal-soft-${OPENAL_VERSION}.tar.gz
	URL_HASH ${OPENAL_HASH}
	DOWNLOAD_DIR ${download_dir}

	PATCH_COMMAND patch -d <SOURCE_DIR> -p1 -t -N < ${CMAKE_SOURCE_DIR}/patches/openal-unset-compiler.patch

	CONFIGURE_COMMAND ${wrapper_command} cmake <SOURCE_DIR>
	${COMMON_CMAKE_ARGS}
	-DALSOFT_EXAMPLES=OFF
	-DALSOFT_TESTS=OFF
	-DALSOFT_UTILS=OFF
	-DALSOFT_NO_CONFIG_UTIL=ON
	-DALSOFT_BACKEND_OPENSL=ON
	-DALSOFT_BACKEND_WAVE=OFF

	BUILD_COMMAND ${wrapper_command} $(MAKE)

	INSTALL_COMMAND ${wrapper_command} $(MAKE) install
)

string(REPLACE "." "_" BOOST_VERSION_UNDERSCORE ${BOOST_VERSION})
ExternalProject_Add(boost
	URL https://dl.bintray.com/boostorg/release/${BOOST_VERSION}/source/boost_${BOOST_VERSION_UNDERSCORE}.tar.bz2
	https://github.com/xyzz/openmw-deps/releases/download/v0/boost_${BOOST_VERSION_UNDERSCORE}.tar.bz2
	URL_HASH ${BOOST_HASH}
	DOWNLOAD_DIR ${download_dir}

	BUILD_IN_SOURCE 1

	CONFIGURE_COMMAND ${wrapper_command} <SOURCE_DIR>/bootstrap.sh
	--prefix=${prefix}

	BUILD_COMMAND ""

	INSTALL_COMMAND ${wrapper_command} ./b2
	-j4
	--with-filesystem
	--with-program_options
	--with-system
	--with-iostreams
	--ignore-site-config
	toolset=clang-android
	architecture=${BOOST_ARCH}
	address-model=${BOOST_ADDRESS_MODEL}
	cflags=$ENV{CFLAGS}
	cxxflags=$ENV{CXXFLAGS}
	variant=release
	target-os=android
	threading=multi
	threadapi=pthread
	link=static
	runtime-link=static
	install
	COMMAND ${host}-ranlib ${prefix}/lib/libboost_filesystem.a
	COMMAND ${host}-ranlib ${prefix}/lib/libboost_program_options.a
	COMMAND ${host}-ranlib ${prefix}/lib/libboost_system.a
	COMMAND ${host}-ranlib ${prefix}/lib/libboost_iostreams.a
)

ExternalProject_Add(ffmpeg
	URL http://ffmpeg.org/releases/ffmpeg-${FFMPEG_VERSION}.tar.bz2
	https://github.com/xyzz/openmw-deps/releases/download/v0/ffmpeg-${FFMPEG_VERSION}.tar.bz2
	URL_HASH ${FFMPEG_HASH}
	DOWNLOAD_DIR ${download_dir}

	CONFIGURE_COMMAND ${wrapper_command} <SOURCE_DIR>/configure
	${ffmpeg_configure_flags}
	--target-os=android
	--enable-cross-compile
	--cross-prefix=${host}-
	--cc=${host}-clang
	--arch=${ARCH}
	--cpu=${FFMPEG_CPU}
	--enable-version3
	--enable-pic
	--disable-everything
	--disable-doc
	--disable-programs
	--disable-autodetect
	--disable-iconv
	--enable-decoder=mp3
	--enable-demuxer=mp3
	--enable-decoder=bink
	--enable-decoder=binkaudio_rdft
	--enable-decoder=binkaudio_dct
	--enable-demuxer=bink
	--enable-demuxer=wav
	--enable-decoder=pcm_*
	--enable-decoder=vp8
	--enable-decoder=vp9
	--enable-decoder=opus
	--enable-decoder=vorbis
	--enable-demuxer=matroska
	--enable-demuxer=ogg

	BUILD_COMMAND ${wrapper_command} $(MAKE)

	INSTALL_COMMAND ${wrapper_command} $(MAKE) install
)

ExternalProject_Add(sdl2
	URL https://www.libsdl.org/release/SDL2-${SDL2_VERSION}.tar.gz
	https://github.com/xyzz/openmw-deps/releases/download/v0/SDL2-${SDL2_VERSION}.tar.gz
	URL_HASH ${SDL2_HASH}
	DOWNLOAD_DIR ${download_dir}

	PATCH_COMMAND patch -d <SOURCE_DIR> -p1 -t -N < ${CMAKE_SOURCE_DIR}/patches/sdl2-android-120hz-gyroscope.patch

	CONFIGURE_COMMAND ""

	BUILD_COMMAND ${wrapper_command} ndk-build
	${COMMON_NDK_BUILD_FLAGS}

	INSTALL_COMMAND mkdir -p ${prefix}/lib/
	COMMAND cp libs/${app_abi}/libSDL2.so ${prefix}/lib/
	COMMAND cp libs/${app_abi}/libhidapi.so ${prefix}/lib/
	COMMAND cp -r <SOURCE_DIR>/include ${prefix}/
)

ExternalProject_Add(bullet
	URL https://github.com/bulletphysics/bullet3/archive/${BULLET_VERSION}.tar.gz
	https://github.com/xyzz/openmw-deps/releases/download/v0/bullet3-${BULLET_VERSION}.tar.gz
	URL_HASH ${BULLET_HASH}
	DOWNLOAD_DIR ${download_dir}

	PATCH_COMMAND patch -d <SOURCE_DIR> -p1 -t -N < ${CMAKE_SOURCE_DIR}/patches/bullet-fix-cmake.patch

	CONFIGURE_COMMAND ${wrapper_command} cmake <SOURCE_DIR>
	${COMMON_CMAKE_ARGS}
	-DBUILD_BULLET2_DEMOS=OFF
	-DBUILD_CPU_DEMOS=OFF
	-DBUILD_UNIT_TESTS=OFF
	-DBUILD_EXTRAS=OFF

	BUILD_COMMAND ${wrapper_command} $(MAKE)

	INSTALL_COMMAND $(MAKE) install
)

ExternalProject_Add(gl4es
	URL https://github.com/ptitSeb/gl4es/archive/${GL4ES_VERSION}.tar.gz
	https://github.com/xyzz/openmw-deps/releases/download/v0/gl4es-${GL4ES_VERSION}.gz
	URL_HASH ${GL4ES_HASH}
	DOWNLOAD_DIR ${download_dir}

	PATCH_COMMAND patch -d <SOURCE_DIR> -p1 -t -N < ${CMAKE_SOURCE_DIR}/patches/gl4es/shared-library.patch
	COMMAND patch -d <SOURCE_DIR> -p1 -t -N < ${CMAKE_SOURCE_DIR}/patches/gl4es/gamma.patch

	CONFIGURE_COMMAND ""

	BUILD_COMMAND ${wrapper_command} ndk-build
	${COMMON_NDK_BUILD_FLAGS}

	INSTALL_COMMAND mkdir -p ${prefix}/lib/
	COMMAND cp libs/${app_abi}/libGL.so ${prefix}/lib/
	COMMAND cp -r <SOURCE_DIR>/include ${prefix}/
)

ExternalProject_Add(mygui
	DEPENDS freetype2

	URL https://github.com/MyGUI/mygui/archive/MyGUI${MYGUI_VERSION}.tar.gz
	https://github.com/xyzz/openmw-deps/releases/download/v0/MyGUI${MYGUI_VERSION}.tar.gz
	URL_HASH ${MYGUI_HASH}
	DOWNLOAD_DIR ${download_dir}

	PATCH_COMMAND patch -d <SOURCE_DIR> -p1 -t -N < ${CMAKE_SOURCE_DIR}/patches/mygui-dont-set-debug-postfix.patch

	CONFIGURE_COMMAND ${wrapper_command} cmake <SOURCE_DIR>
	${COMMON_CMAKE_ARGS}
	-DMYGUI_RENDERSYSTEM=ON
	-DMYGUI_BUILD_DEMOS=OFF
	-DMYGUI_BUILD_TOOLS=OFF
	-DMYGUI_BUILD_PLUGINS=OFF
	-DFREETYPE_FT2BUILD_INCLUDE_DIR=${prefix}/include/freetype2/

	-DMYGUI_STATIC=ON

	BUILD_COMMAND ${wrapper_command} $(MAKE)

	INSTALL_COMMAND ${wrapper_command} $(MAKE) install
)

set(OSG_COMMON
	-DOPENGL_PROFILE="GL1"
	-DDYNAMIC_OPENTHREADS=OFF
	-DDYNAMIC_OPENSCENEGRAPH=OFF
	-DBUILD_OSG_PLUGIN_OSG=ON
	-DBUILD_OSG_PLUGIN_DDS=ON
	-DBUILD_OSG_PLUGIN_TGA=ON
	-DBUILD_OSG_PLUGIN_BMP=ON
	-DBUILD_OSG_PLUGIN_JPEG=ON
	-DBUILD_OSG_PLUGIN_PNG=ON
	-DBUILD_OSG_PLUGIN_FREETYPE=ON
	-DJPEG_INCLUDE_DIR=${prefix}/include/
	-DPNG_INCLUDE_DIR=${prefix}/include/
	-DFREETYPE_DIR=${prefix}
	-DOSG_CPP_EXCEPTIONS_AVAILABLE=TRUE
	-DOSG_GL1_AVAILABLE=ON
	-DOSG_GL2_AVAILABLE=OFF
	-DOSG_GL3_AVAILABLE=OFF
	-DOSG_GLES1_AVAILABLE=OFF
	-DOSG_GLES2_AVAILABLE=OFF
	-DOSG_GL_LIBRARY_STATIC=OFF
	-DOSG_GL_DISPLAYLISTS_AVAILABLE=OFF
	-DOSG_GL_MATRICES_AVAILABLE=ON
	-DOSG_GL_VERTEX_FUNCS_AVAILABLE=ON
	-DOSG_GL_VERTEX_ARRAY_FUNCS_AVAILABLE=ON
	-DOSG_GL_FIXED_FUNCTION_AVAILABLE=ON
	-DBUILD_OSG_APPLICATIONS=OFF
	-DBUILD_OSG_PLUGINS_BY_DEFAULT=OFF
	-DBUILD_OSG_DEPRECATED_SERIALIZERS=OFF
)

set(OSG_PATCH
	patch -d <SOURCE_DIR> -p1 -t -N < ${CMAKE_SOURCE_DIR}/patches/osg/remove-lib-prefix-from-plugins.patch &&
	patch -d <SOURCE_DIR> -p1 -t -N < ${CMAKE_SOURCE_DIR}/patches/osg/fix-freetype-include-dirs.patch &&
	patch -d <SOURCE_DIR> -p1 -t -N < ${CMAKE_SOURCE_DIR}/patches/osg/0001-Replace-Atomic-impl-with-std-atomic.patch &&
	patch -d <SOURCE_DIR> -p1 -t -N < ${CMAKE_SOURCE_DIR}/patches/osg/0002-BufferObject-make-numClients-atomic.patch &&
	patch -d <SOURCE_DIR> -p1 -t -N < ${CMAKE_SOURCE_DIR}/patches/osg/0003-Array-make-possible-to-use-thread-safely-with-setTex.patch &&
	patch -d <SOURCE_DIR> -p1 -t -N < ${CMAKE_SOURCE_DIR}/patches/osg/0004-IncrementalCompileOperation-wrap-some-stuff-in-atomi.patch &&
	patch -d <SOURCE_DIR> -p1 -t -N < ${CMAKE_SOURCE_DIR}/patches/osg/0005-CullSettings-make-inheritanceMask-atomic-to-silence-.patch
)

ExternalProject_Add(osg
	DEPENDS libjpeg-turbo libpng gl4es freetype2

	URL https://github.com/openscenegraph/OpenSceneGraph/archive/OpenSceneGraph-${OSG_VERSION}.tar.gz
	https://github.com/xyzz/openmw-deps/releases/download/v0/OpenSceneGraph-${OSG_VERSION}.tar.gz
	URL_HASH ${OSG_HASH}
	DOWNLOAD_DIR ${download_dir}

	PATCH_COMMAND ${OSG_PATCH}

	CONFIGURE_COMMAND ${wrapper_command} cmake <SOURCE_DIR>
	${COMMON_CMAKE_ARGS}
	${OSG_COMMON}

	BUILD_COMMAND ${wrapper_command} $(MAKE)

	INSTALL_COMMAND mkdir -p <SOURCE_DIR>/3rdparty/build/libpng <SOURCE_DIR>/3rdparty/build/libjpeg <SOURCE_DIR>/3rdparty/build/freetype
	COMMAND $(MAKE) install
)

set(OPENMW_COMMON
	-DBUILD_BSATOOL=0
	-DBUILD_NIFTEST=0
	-DBUILD_ESMTOOL=0
	-DBUILD_LAUNCHER=0
	-DBUILD_MWINIIMPORTER=0
	-DBUILD_ESSIMPORTER=0
	-DBUILD_OPENCS=0
	-DBUILD_WIZARD=0
	-DBUILD_MYGUI_PLUGIN=0
	-DOPENAL_INCLUDE_DIR=${prefix}/include/AL/
	-DBullet_INCLUDE_DIR=${prefix}/include/bullet/
	-DOPENGL_ES=OFF
	-DOSG_STATIC=TRUE
	-DMyGUI_LIBRARY=${prefix}/lib/libMyGUIEngineStatic.a
)

set(OPENMW_PATCH
	patch -d <SOURCE_DIR> -p1 -t -N < ${CMAKE_SOURCE_DIR}/patches/openmw/gamma.patch &&
	patch -d <SOURCE_DIR> -p1 -t -N < ${CMAKE_SOURCE_DIR}/patches/openmw/0001-loadingscreen-disable-for-now.patch &&
	patch -d <SOURCE_DIR> -p1 -t -N < ${CMAKE_SOURCE_DIR}/patches/openmw/0003-globalmap-fix-race-vs-savegame-load.patch &&
	patch -d <SOURCE_DIR> -p1 -t -N < ${CMAKE_SOURCE_DIR}/patches/openmw/0004-getUVBuffer-pre-set-array-binding-to-avoid-thread-ra.patch &&
	patch -d <SOURCE_DIR> -p1 -t -N < ${CMAKE_SOURCE_DIR}/patches/openmw/0005-android-Revert-UI-dimensions.patch &&
	patch -d <SOURCE_DIR> -p1 -t -N < ${CMAKE_SOURCE_DIR}/patches/openmw/0006-Add-an-option-to-enable-simple-bounding-box.patch &&
	patch -d <SOURCE_DIR> -p1 -t -N < ${CMAKE_SOURCE_DIR}/patches/openmw/0007-android-Fix-red-sky-after-shadows-merge.patch &&
	patch -d <SOURCE_DIR> -p1 -t -N < ${CMAKE_SOURCE_DIR}/patches/openmw/0009-windowmanagerimp-always-show-mouse-when-possible-pat.patch &&
    patch -d <SOURCE_DIR> -p1 -t -N < ${CMAKE_SOURCE_DIR}/patches/openmw/0010-android-fix-context-being-lost-on-app-minimize.patch &&
	patch -d <SOURCE_DIR> -p1 -t -N < ${CMAKE_SOURCE_DIR}/patches/openmw/0013-android-Update-default-settings-for-android.patch &&
	patch -d <SOURCE_DIR> -p1 -t -N < ${CMAKE_SOURCE_DIR}/patches/openmw/0014-settingswindow-save-user-settings-file-when-ok-is-pr.patch &&
	patch -d <SOURCE_DIR> -p1 -t -N < ${CMAKE_SOURCE_DIR}/patches/openmw/0012-components-misc-stringops-use-boost-format-instead-o.patch &&
	cp ${CMAKE_SOURCE_DIR}/patches/openmw/android_main.cpp <SOURCE_DIR>/apps/openmw/android_main.cpp
)

ExternalProject_Add(openmw
	DEPENDS boost openal osg mygui ffmpeg sdl2 bullet

	URL https://github.com/OpenMW/openmw/archive/${OPENMW_VERSION}.tar.gz
	URL_HASH ${OPENMW_HASH}
	DOWNLOAD_DIR ${download_dir}

	PATCH_COMMAND ${OPENMW_PATCH}

	CONFIGURE_COMMAND ${wrapper_command} cmake <SOURCE_DIR>
	${COMMON_CMAKE_ARGS}
	${OPENMW_COMMON}

	BUILD_COMMAND $(MAKE)

	INSTALL_COMMAND ""
)
