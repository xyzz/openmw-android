package ui.activity

import android.content.SharedPreferences
import android.os.Bundle
import android.os.Process
import android.preference.PreferenceManager
import android.system.ErrnoException
import android.system.Os
import android.util.Log
import android.view.WindowManager
import android.widget.RelativeLayout

import org.libsdl.app.SDLActivity

import constants.Constants
import cursor.MouseCursor
import parser.CommandlineParser
import ui.controls.Osc
import file.ConfigsFileStorageHelper

import utils.Utils.hideAndroidControls

class GameActivity : SDLActivity() {

    private var prefs: SharedPreferences? = null

    internal val openmwLibName: String
        get() = "openmw_osg_" + prefs!!.getString("pref_osg", "")!!

    val layout: RelativeLayout
        get() = SDLActivity.mLayout as RelativeLayout

    override fun loadLibraries() {
        prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val graphicsLibrary = prefs!!.getString("pref_graphicsLibrary", "")
        val physicsFPS = prefs!!.getString("pref_physicsFPS", "")
        if (!physicsFPS!!.isEmpty()) {
            try {
                Os.setenv("OPENMW_PHYSICS_FPS", physicsFPS, true)
                Os.setenv("OSG_TEXT_SHADER_TECHNIQUE", "NO_TEXT_SHADER", true)
            } catch (e: ErrnoException) {
                Log.e("OpenMW", "Failed setting environment variables.")
                e.printStackTrace()
            }

        }

        System.loadLibrary("c++_shared")
        System.loadLibrary("openal")
        System.loadLibrary("SDL2")
        if (graphicsLibrary == "gles2") {
            try {
                Os.setenv("OPENMW_GLES_VERSION", "2", true)
                Os.setenv("LIBGL_ES", "2", true)
            } catch (e: ErrnoException) {
                Log.e("OpenMW", "Failed setting environment variables.")
                e.printStackTrace()
            }

        }
        System.loadLibrary("GL")
        System.loadLibrary(openmwLibName)
    }

    override fun getMainSharedObject(): String {
        return "lib$openmwLibName.so"
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        KeepScreenOn()
        getPathToJni(ConfigsFileStorageHelper.CONFIGS_FILES_STORAGE_PATH)
        showControls()
    }

    private fun showControls() {
        val hideControls = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(Constants.HIDE_CONTROLS, false)
        var osc: Osc? = null
        if (!hideControls) {
            val layout = layout
            osc = Osc()
            osc.placeElements(layout)
        }
        MouseCursor(this, osc)
    }

    private fun KeepScreenOn() {
        val needKeepScreenOn = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("screen_keeper", false)
        if (needKeepScreenOn) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    public override fun onDestroy() {
        finish()
        Process.killProcess(Process.myPid())
        super.onDestroy()
    }


    override fun onWindowFocusChanged(hasFocus: Boolean) {
        if (hasFocus) {
            hideAndroidControls(this)
        }
    }

    override fun getArguments(): Array<String> {
        val cmd = PreferenceManager.getDefaultSharedPreferences(this).getString("commandLine", "")
        val commandlineParser = CommandlineParser(cmd!!)
        return commandlineParser.argv
    }

    private external fun getPathToJni(path: String)
}
