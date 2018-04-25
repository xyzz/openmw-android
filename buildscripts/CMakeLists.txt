cmake_minimum_required(VERSION 3.2.0)

# https://github.com/libjpeg-turbo/libjpeg-turbo/releases
set(LIBJPEG_TURBO_VERSION 1.5.3)
set(LIBJPEG_TURBO_HASH SHA256=b24890e2bb46e12e72a79f7e965f409f4e16466d00e1dd15d93d73ee6b592523)

# http://www.libpng.org/pub/png/libpng.html
set(LIBPNG_VERSION 1.6.34)
set(LIBPNG_HASH SHA256=2f1e960d92ce3b3abd03d06dfec9637dfbd22febf107a536b44f7a47c60659f6)

# https://www.freetype.org/index.html#news
set(FREETYPE2_VERSION 2.9)
set(FREETYPE2_HASH SHA256=bf380e4d7c4f3b5b1c1a7b2bf3abb967bda5e9ab480d0df656e0e08c5019c5e6)

# http://kcat.strangesoft.net/openal.html
set(OPENAL_VERSION 1.18.2)
set(OPENAL_HASH SHA256=9f8ac1e27fba15a59758a13f0c7f6540a0605b6c3a691def9d420570506d7e82)

# http://www.boost.org/users/download/
set(BOOST_VERSION 1.67.0)
set(BOOST_HASH SHA256=2684c972994ee57fc5632e03bf044746f6eb45d4920c343937a465fd67a5adba)

# https://www.ffmpeg.org/download.html
set(FFMPEG_VERSION 4.0)
set(FFMPEG_HASH SHA256=318a39d906c9107d49766c63787798dd078d2a36e6670a9dfeda3c55be4573b8)

# https://www.libsdl.org/download-2.0.php
set(SDL2_VERSION 2.0.8)
set(SDL2_HASH SHA256=edc77c57308661d576e843344d8638e025a7818bff73f8fbfab09c3c5fd092ec)

# https://github.com/bulletphysics/bullet3/releases
set(BULLET_VERSION 2.87)
set(BULLET_HASH SHA256=438c151c48840fe3f902ec260d9496f8beb26dba4b17769a4a53212903935f95)

# https://github.com/MyGUI/mygui/releases
set(MYGUI_VERSION 3.2.2)
set(MYGUI_HASH SHA256=0a28d7ec8a47993cb68deb48b36331e28f12dd92580b709eaef21d599b67a78f)

# https://github.com/ptitSeb/gl4es
set(GL4ES_VERSION 509d3843b2fe273eec85a8242a7ddab62be8bad0)
set(GL4ES_HASH SHA256=8f928c965818da1d6bc5d11541548e0864eb05574d79b3e3f36df3b4f1f1f199)

# https://github.com/openscenegraph/OpenSceneGraph/releases
set(OSG_VERSION 3.6.0)
set(OSG_HASH SHA256=6f57134ea74a39f1c7b24c285e6278cf906c47f6c681573b86d12173a466efed)

# https://github.com/xyzz/openmw/tree/android
set(OPENMW_TAG android)

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
set(osg_plugins_dir ${prefix}/lib/osgPlugins-${OSG_VERSION})

set(COMMON_CMAKE_ARGS
	-DCMAKE_TOOLCHAIN_FILE=${CMAKE_SOURCE_DIR}/toolchain/ndk/build/cmake/android.toolchain.cmake
	-DCMAKE_INSTALL_PREFIX=${prefix}
	-DCMAKE_FIND_ROOT_PATH=${prefix}
	-DANDROID_ABI=${app_abi}
	-DANDROID_PLATFORM=${android_platform}
	-DANDROID_STL=c++_shared
	-DANDROID_CPP_FEATURES=rtti\ exceptions
	-DCMAKE_C_FLAGS=-DMYGUI_DONT_REPLACE_NULLPTR\ -I${prefix}/include/\ $ENV{CFLAGS}
	-DCMAKE_CXX_FLAGS=-DMYGUI_DONT_REPLACE_NULLPTR\ -I${prefix}/include/\ $ENV{CXXFLAGS}
	-DCMAKE_SHARED_LINKER_FLAGS=$ENV{LDFLAGS}
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
	URL_HASH ${LIBPNG_HASH}
	DOWNLOAD_DIR ${download_dir}

	CONFIGURE_COMMAND ${wrapper_command} <SOURCE_DIR>/configure
	${COMMON_AUTOCONF_FLAGS}

	BUILD_COMMAND ${wrapper_command} $(MAKE) check_PROGRAMS= bin_PROGRAMS=

	INSTALL_COMMAND ${wrapper_command} $(MAKE) install check_PROGRAMS= bin_PROGRAMS=
)

ExternalProject_Add(freetype2
	URL https://download.savannah.gnu.org/releases/freetype/freetype-${FREETYPE2_VERSION}.tar.gz
	URL_HASH ${FREETYPE2_HASH}
	DOWNLOAD_DIR ${download_dir}

	CONFIGURE_COMMAND ${wrapper_command} <SOURCE_DIR>/configure
	${COMMON_AUTOCONF_FLAGS}
	--with-png=no

	BUILD_COMMAND ${wrapper_command} $(MAKE)

	INSTALL_COMMAND ${wrapper_command} $(MAKE) install
)

ExternalProject_Add(openal
	URL http://kcat.strangesoft.net/openal-releases/openal-soft-${OPENAL_VERSION}.tar.bz2
	URL_HASH ${OPENAL_HASH}
	DOWNLOAD_DIR ${download_dir}

	CONFIGURE_COMMAND ${wrapper_command} cmake <SOURCE_DIR>
	${COMMON_CMAKE_ARGS}
	-DALSOFT_EXAMPLES=OFF
	-DALSOFT_TESTS=OFF
	-DALSOFT_NO_CONFIG_UTIL=ON
	-DALSOFT_BACKEND_OPENSL=ON
	-DALSOFT_BACKEND_WAVE=OFF

	BUILD_COMMAND ${wrapper_command} $(MAKE)

	INSTALL_COMMAND ${wrapper_command} $(MAKE) install
)

string(REPLACE "." "_" BOOST_VERSION_UNDERSCORE ${BOOST_VERSION})
ExternalProject_Add(boost
	URL https://dl.bintray.com/boostorg/release/${BOOST_VERSION}/source/boost_${BOOST_VERSION_UNDERSCORE}.tar.bz2
	URL_HASH ${BOOST_HASH}
	DOWNLOAD_DIR ${download_dir}

	BUILD_IN_SOURCE 1

	PATCH_COMMAND patch -d <SOURCE_DIR> -p2 -t -N < ${CMAKE_SOURCE_DIR}/patches/boost-fix13172.diff

	CONFIGURE_COMMAND ${wrapper_command} <SOURCE_DIR>/bootstrap.sh
	--prefix=${prefix}

	BUILD_COMMAND ""

	INSTALL_COMMAND ${wrapper_command} ./b2
	-j4
	--with-filesystem
	--with-program_options
	--with-system
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
)

ExternalProject_Add(ffmpeg
	URL http://ffmpeg.org/releases/ffmpeg-${FFMPEG_VERSION}.tar.bz2
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
	URL_HASH ${SDL2_HASH}
	DOWNLOAD_DIR ${download_dir}

	PATCH_COMMAND patch -d <SOURCE_DIR> -p1 -t -N < ${CMAKE_SOURCE_DIR}/patches/sdl2-android-gamepad.patch
	COMMAND cp ${CMAKE_SOURCE_DIR}/patches/sdl2-SDL_gamecontrollerdb.h <SOURCE_DIR>/src/joystick/SDL_gamecontrollerdb.h

	CONFIGURE_COMMAND ""

	BUILD_COMMAND ${wrapper_command} ndk-build
	${COMMON_NDK_BUILD_FLAGS}

	INSTALL_COMMAND mkdir -p ${prefix}/lib/
	COMMAND cp libs/${app_abi}/libSDL2.so ${prefix}/lib/
	COMMAND cp -r <SOURCE_DIR>/include ${prefix}/
)

ExternalProject_Add(bullet
	URL https://github.com/bulletphysics/bullet3/archive/${BULLET_VERSION}.tar.gz
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
	URL_HASH ${GL4ES_HASH}
	DOWNLOAD_DIR ${download_dir}

	PATCH_COMMAND patch -d <SOURCE_DIR> -p1 -t -N < ${CMAKE_SOURCE_DIR}/patches/gl4es-build-shared.patch

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
	URL_HASH ${MYGUI_HASH}
	DOWNLOAD_DIR ${download_dir}

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

ExternalProject_Add(osg
	DEPENDS libjpeg-turbo libpng gl4es

	URL https://github.com/openscenegraph/OpenSceneGraph/archive/OpenSceneGraph-${OSG_VERSION}.tar.gz
	URL_HASH ${OSG_HASH}
	DOWNLOAD_DIR ${download_dir}

	CONFIGURE_COMMAND ${wrapper_command} cmake <SOURCE_DIR>
	${COMMON_CMAKE_ARGS}
	-DOPENGL_PROFILE="GL1"
	-DDYNAMIC_OPENTHREADS=OFF
	-DDYNAMIC_OPENSCENEGRAPH=OFF
	-DBUILD_OSG_PLUGIN_OSG=ON
	-DBUILD_OSG_PLUGIN_DDS=ON
	-DBUILD_OSG_PLUGIN_TGA=ON
	-DBUILD_OSG_PLUGIN_BMP=ON
	-DBUILD_OSG_PLUGIN_JPEG=ON
	-DBUILD_OSG_PLUGIN_PNG=ON
	-DJPEG_INCLUDE_DIR=${prefix}/include/
	-DPNG_INCLUDE_DIR=${prefix}/include/
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
	-DDYNAMIC_OPENTHREADS=OFF
	-DDYNAMIC_OPENSCENEGRAPH=OFF
	-DBUILD_OSG_APPLICATIONS=OFF
	-DBUILD_OSG_PLUGINS_BY_DEFAULT=OFF
	-DBUILD_OSG_PLUGIN_OSG=ON
	-DBUILD_OSG_PLUGIN_DDS=ON
	-DBUILD_OSG_PLUGIN_TGA=ON
	-DBUILD_OSG_PLUGIN_BMP=ON
	-DBUILD_OSG_PLUGIN_JPEG=ON
	-DBUILD_OSG_PLUGIN_PNG=ON
	-DBUILD_OSG_DEPRECATED_SERIALIZERS=OFF

	BUILD_COMMAND ${wrapper_command} $(MAKE)

	INSTALL_COMMAND mkdir -p <SOURCE_DIR>/3rdparty/build/libpng <SOURCE_DIR>/3rdparty/build/libjpeg
	COMMAND $(MAKE) install
)

ExternalProject_Add(openmw
	DEPENDS boost openal osg mygui ffmpeg sdl2 bullet

	GIT_REPOSITORY https://github.com/xyzz/openmw.git
	GIT_TAG ${OPENMW_TAG}

	CONFIGURE_COMMAND ${wrapper_command} cmake <SOURCE_DIR>
	${COMMON_CMAKE_ARGS}
	-DBUILD_BSATOOL=0
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
	-DOSG_PLUGINS_DIR=${osg_plugins_dir}
	-DOSG_STATIC=TRUE
	-DOSGDB_BMP_LIBRARY=${osg_plugins_dir}/libosgdb_bmp.a
	-DOSGDB_DDS_LIBRARY=${osg_plugins_dir}/libosgdb_dds.a
	-DOSGDB_JPEG_LIBRARY=${osg_plugins_dir}/libosgdb_jpeg.a
	-DOSGDB_OSG_LIBRARY=${osg_plugins_dir}/libosgdb_osg.a
	-DOSGDB_PNG_LIBRARY=${osg_plugins_dir}/libosgdb_png.a
	-DOSGDB_TGA_LIBRARY=${osg_plugins_dir}/libosgdb_tga.a 
	-DOSGDB_SERIALIZERS_OSG_LIBRARY=${osg_plugins_dir}/libosgdb_serializers_osg.a
	-DMyGUI_LIBRARY=${prefix}/lib/libMyGUIEngineStatic.a

	BUILD_COMMAND $(MAKE)

	INSTALL_COMMAND ""
)
