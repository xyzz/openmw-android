package utils

import android.app.Application
import android.content.pm.PackageManager
import android.os.Environment
import android.preference.PreferenceManager
import android.util.Base64
import android.util.Log
import com.bugsnag.android.Bugsnag
import com.bugsnag.android.Configuration
import com.libopenmw.openmw.BuildConfig
import constants.Constants
import java.io.File
import java.security.MessageDigest

class MyApp : Application() {

    var defaultScaling = 0f

    override fun onCreate() {
        app = this

        super.onCreate()

        // Set up global paths
        // Slug will be either omw or omw_nightly
        val slug = BuildConfig.APPLICATION_ID.split(".")[2]
        Constants.USER_FILE_STORAGE = Environment.getExternalStorageDirectory().toString() + "/$slug/"
        Constants.USER_CONFIG = "${Constants.USER_FILE_STORAGE}/config"
        Constants.USER_OPENMW_CFG =  "${Constants.USER_CONFIG}/openmw.cfg"
        Constants.DEFAULTS_BIN = File(filesDir, "config/defaults.bin").absolutePath
        Constants.OPENMW_CFG = File(filesDir, "config/openmw.cfg").absolutePath
        Constants.OPENMW_BASE_CFG = File(filesDir, "config/openmw.base.cfg").absolutePath
        Constants.OPENMW_FALLBACK_CFG = File(filesDir, "config/openmw.fallback.cfg").absolutePath
        Constants.RESOURCES = File(filesDir, "resources").absolutePath
        Constants.GLOBAL_CONFIG = File(filesDir, "config").absolutePath
        Constants.VERSION_STAMP = File(filesDir, "stamp").absolutePath

        // Enable bugsnag only when API key is provided and we have user consent
        // Also don't enable bugsnag in debug builds
        if (isProductionBuild() && BugsnagApiKey.API_KEY.isNotEmpty() && !BuildConfig.DEBUG) {
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

    private fun isProductionBuild(): Boolean {
        val sig = applicationContext.packageManager.getPackageInfo(applicationContext.packageName, PackageManager.GET_SIGNATURES).signatures[0]
        val digest: MessageDigest = MessageDigest.getInstance("SHA-256")
        val hashBytes: ByteArray = digest.digest(sig.toByteArray())
        val hash: String = Base64.encodeToString(hashBytes, Base64.NO_WRAP)
        return hash == "cOqSYH3ucLraOQ7wyg/v8UKTGHlxP8N8JTN6UXO7rV0="
    }

    companion object {
        var reportCrashes = false
        var haveBugsnagApiKey = false
        lateinit var app: MyApp
    }
}
