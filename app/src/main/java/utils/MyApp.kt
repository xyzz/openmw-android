package utils

import android.app.Application
import android.preference.PreferenceManager
import constants.Constants
import java.io.File
import com.bugsnag.android.Bugsnag
import com.bugsnag.android.Configuration

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

        // Enable bugsnag only when API key is provided and we have user consent
        if (BugsnagApiKey.API_KEY.isNotEmpty()) {
            haveBugsnagApiKey = true

            val prefs = PreferenceManager.getDefaultSharedPreferences(this)
            if (prefs.getString("bugsnag_consent", "false")!! == "true") {
                val config = Configuration(BugsnagApiKey.API_KEY)
                config.buildUUID = ""
                Bugsnag.init(this, config)
                reportCrashes = true
            }
        }
    }

    companion object {
        var reportCrashes = false
        var haveBugsnagApiKey = false
    }
}
