/*
    Copyright (C) 2015, 2016 sandstranger
    Copyright (C) 2019 Ilya Zhuravlev

    This file is part of OpenMW-Android.

    OpenMW-Android is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    OpenMW-Android is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with OpenMW-Android.  If not, see <https://www.gnu.org/licenses/>.
*/

package constants

import android.os.Environment
import com.libopenmw.openmw.BuildConfig

object Constants {
    val APP_PREFERENCES = "settings"
    val HIDE_CONTROLS = "pref_hide_controls"

    // Base path: [/sdcard]/Android/data/[com.libopenmw.openmw]/
    // * /sdcard - in theory, can be different, haven't seen any on modern android though
    // * com.libopenmw.openmw - our application id
    //
    // $base/share - savedata, shouldn't touch this
    // $base/resources - resource files from openmw, ok to overwrite
    // $base/openmw - default settings, ok to overwrite
    // $base/config - user settings

    val CONFIGS_FILES_STORAGE_PATH = Environment.getExternalStorageDirectory().toString() + "/Android/data/" + BuildConfig.APPLICATION_ID
    val SETTINGS_CFG = "$CONFIGS_FILES_STORAGE_PATH/config/openmw/settings.cfg"
    val OPENMW_CFG = "$CONFIGS_FILES_STORAGE_PATH/config/openmw/openmw.cfg"
    val OPENMW_FALLBACK_CFG = "$CONFIGS_FILES_STORAGE_PATH/config/openmw/openmw.fallback.cfg"
    val OPENMW_BASE_CFG = "$CONFIGS_FILES_STORAGE_PATH/config/openmw/openmw-base.cfg"
}
