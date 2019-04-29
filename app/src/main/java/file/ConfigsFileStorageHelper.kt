package file

import android.os.Environment
import com.libopenmw.openmw.BuildConfig

/**
 * Created by sandstranger on 07.01.16.
 */
object ConfigsFileStorageHelper {

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
    val OPENMW_BASE_CFG = "$CONFIGS_FILES_STORAGE_PATH/config/openmw/openmw-base.cfg"

}
