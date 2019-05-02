package utils

import android.app.Application
import constants.Constants
import java.io.File

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // Set up global paths
        Constants.SETTINGS_DEFAULT_CFG = File(filesDir, "config/settings-default.cfg").absolutePath
        Constants.OPENMW_CFG = File(filesDir, "config/openmw.cfg").absolutePath
        Constants.OPENMW_BASE_CFG = File(filesDir, "config/openmw.base.cfg").absolutePath
        Constants.OPENMW_FALLBACK_CFG = File(filesDir, "config/openmw.fallback.cfg").absolutePath
        Constants.RESOURCES = File(filesDir, "resources").absolutePath
        Constants.GLOBAL_CONFIG = File(filesDir, "config").absolutePath
        Constants.VERSION_STAMP = File(filesDir, "stamp").absolutePath
    }
}
