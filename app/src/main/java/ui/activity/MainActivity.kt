/*
    Copyright (C) 2015, 2016 sandstranger
    Copyright (C) 2018, 2019 Ilya Zhuravlev

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

package ui.activity

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.DisplayMetrics
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast

import com.crashlytics.android.Crashlytics
import com.crashlytics.android.ndk.CrashlyticsNdk
import com.libopenmw.openmw.R
import constants.Constants
import file.GameInstaller

import io.fabric.sdk.android.Fabric

import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStreamReader

import file.utils.CopyFilesFromAssets
import mods.ModType
import mods.ModsCollection
import mods.ModsDatabaseOpenHelper
import ui.fragments.FragmentSettings
import permission.PermissionHelper
import utils.Utils.hideAndroidControls

class MainActivity : AppCompatActivity() {
    private lateinit var prefs: SharedPreferences
    var defaultScaling = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        defaultScaling = determineScaling()

        Fabric.with(this, Crashlytics(), CrashlyticsNdk())
        PermissionHelper.getWriteExternalStoragePermission(this@MainActivity)
        setContentView(R.layout.main)
        prefs = PreferenceManager.getDefaultSharedPreferences(this)

        fragmentManager.beginTransaction()
            .replace(R.id.content_frame, FragmentSettings()).commit()

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener { startGame() }
    }

    private fun deleteRecursive(fileOrDirectory: File) {
        if (fileOrDirectory.isDirectory)
            for (child in fileOrDirectory.listFiles())
                deleteRecursive(child)

        fileOrDirectory.delete()
    }

    private fun logConfig() {
        try {
            val openmwCfg = File(Constants.OPENMW_CFG)
            if (openmwCfg.exists()) {
                val reader = BufferedReader(InputStreamReader(FileInputStream(openmwCfg)))
                Crashlytics.log("openmw.cfg")
                Crashlytics.log("--------------------------------------------------------------------------------")
                reader.forEachLine {
                    // Don't log fallback lines, they are mostly useless
                    if (!it.contains("fallback="))
                        Crashlytics.log(it)
                }
                Crashlytics.log("--------------------------------------------------------------------------------")
            }
        } catch (e: Exception) {
            // not a big deal if we can't log the contents
        }

    }

    private fun runGame() {
        logConfig()
        val intent = Intent(this@MainActivity,
            GameActivity::class.java)
        finish()

        this@MainActivity.startActivity(intent)
    }

    /**
     * Resets $base/config to default values. This contains user-modifiable openmw.cfg and settings.cfg
     * (and we also write some values to both on startup such as screen res or some options)
     */
    private fun resetUserConfig() {
        reinstallStaticFiles()
    }

    /**
     * Set up fixed screen resolution
     * This doesn't do anything unless the user chose to override screen resolution
     */
    private fun obtainFixedScreenResolution() {
        // Split resolution e.g 640x480 to width/height
        val customResolution = prefs.getString("pref_customResolution", "")
        val sep = customResolution!!.indexOf("x")
        if (sep > 0) {
            try {
                val x = Integer.parseInt(customResolution.substring(0, sep))
                val y = Integer.parseInt(customResolution.substring(sep + 1))

                resolutionX = x
                resolutionY = y
            } catch (e: NumberFormatException) {
                // user entered resolution wrong, just ignore it
            }
        }
    }

    /**
     * Generates openmw.cfg using values from openmw.base.cfg combined with mod manager settings
     */
    private fun generateOpenmwCfg() {
        // contents of openmw.base.cfg
        val base: String
        // contents of openmw.fallback.cfg
        val fallback: String

        // try to read the files
        try {
            base = File(Constants.OPENMW_BASE_CFG).readText()
            // TODO: support user custom options
            fallback = File(Constants.OPENMW_FALLBACK_CFG).readText()
        } catch (e: IOException) {
            Log.e(TAG, "Failed to read openmw.base.cfg or openmw.fallback.cfg", e)
            Crashlytics.logException(e)
            return
        }

        val dataFiles = GameInstaller.getDataFiles(this)
        val db = ModsDatabaseOpenHelper.getInstance(this)
        val resources = ModsCollection(ModType.Resource, dataFiles, db)
        val plugins = ModsCollection(ModType.Plugin, dataFiles, db)

        try {
            // generate final output.cfg
            var output = base + "\n" + fallback + "\n"

            // output resources
            resources.mods
                .filter { it.enabled }
                .forEach { output += "fallback-archive=${it.filename}\n" }

            // output plugins
            plugins.mods
                .filter { it.enabled }
                .forEach { output += "content=${it.filename}\n" }

            // write everything to openmw.cfg
            File(Constants.OPENMW_CFG).writeText(output)
        } catch (e: IOException) {
            Log.e(TAG, "Failed to generate openmw.cfg.", e)
            Crashlytics.logException(e)
        }
    }

    /**
     * Shows an alert dialog displaying a specific message
     * @param title Title string resource
     * @param message Message string resource
     */
    private fun showAlert(title: Int, message: Int) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(android.R.string.ok) { _: DialogInterface, _: Int -> }
            .show()
    }

    /**
     * Determines required screen scaling based on resolution and physical size of the device
     */
    private fun determineScaling(): Float {
        // The idea is to stretch an old-school 1024x768 monitor to the device screen
        // Assume that 1x scaling corresponds to resolution of 1024x768
        // Assume that the longest side of the device corresponds to the 1024 side
        // Therefore scaling is calculated as longest size of the device divided by 1024
        // Note that it doesn't take into account DPI at all. Which is fine for now, but in future
        // we might want to add some bonus scaling to e.g. phone devices so that it's easier
        // to click things.

        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        return maxOf(dm.heightPixels, dm.widthPixels) / 1024.0f
    }

    /**
     * Removes old and creates new files located in private application directories
     * (i.e. under getFilesDir(), or /data/data/.../files)
     */
    private fun reinstallStaticFiles() {
        // we store global "config" and "resources" under private files

        // wipe old version first
        deleteRecursive(File(filesDir, "config"))
        deleteRecursive(File(filesDir, "resources"))

        // copy in the new version
        val assetCopier = CopyFilesFromAssets(this)
        assetCopier.copy("libopenmw/resources", File(filesDir, "resources").absolutePath)
        assetCopier.copy("libopenmw/openmw", File(filesDir, "config").absolutePath)

        // set version stamp
    }

    private fun startGame() {
        // First, check that there are game files present
        val inst = GameInstaller(prefs.getString("game_files", "")!!)
        if (!inst.check()) {
            showAlert(R.string.no_data_files_title, R.string.no_data_files_message)
            return
        }

        // Get scaling factor from config; if invalid or not provided, generate one
        var scaling = 0f

        try {
            scaling = prefs.getString("pref_uiScaling", "")!!.toFloat()
        } catch (e: NumberFormatException) {
            // Reset the invalid setting
            with(prefs.edit()) {
                putString("pref_uiScaling", "")
                apply()
            }
        }

        // If scaling didn't get set, determine it automatically
        if (scaling == 0f) {
            scaling = defaultScaling
        }

        val dialog = ProgressDialog.show(
            this, "", "Preparing for launch...", true)

        val activity = this

        // hide the controls so that ScreenResolutionHelper can get the right resolution
        hideAndroidControls(this)

        val th = Thread {
            try {
                val openmwBaseCfg = File(Constants.OPENMW_BASE_CFG)
                val settingsCfg = File(Constants.SETTINGS_CFG)
                if (!openmwBaseCfg.exists() || !settingsCfg.exists()) {
                    Log.i(TAG, "Config files don't exist, re-creating them.")
                    resetUserConfig()
                }

                reinstallStaticFiles()

                // Regenerate the fallback file in case user edits their Morrowind.ini
                inst.convertIni(prefs.getString("pref_encoding", GameInstaller.DEFAULT_CHARSET_PREF)!!)

                generateOpenmwCfg()

                // openmw.cfg: data, resources
                file.Writer.write(
                    Constants.OPENMW_CFG, "resources", Constants.CONFIGS_FILES_STORAGE_PATH + "/resources"
                )
                file.Writer.write(Constants.OPENMW_CFG, "data", "\"" + inst.findDataFiles() + "\"")

                file.Writer.write(Constants.OPENMW_CFG, "encoding", prefs!!.getString("pref_encoding", GameInstaller.DEFAULT_CHARSET_PREF)!!)

                file.Writer.write(Constants.SETTINGS_CFG, "scaling factor", "%.2f".format(scaling))

                file.Writer.write(Constants.SETTINGS_CFG, "allow capsule shape", prefs!!.getString("pref_allowCapsuleShape", "true")!!)

                file.Writer.write(Constants.SETTINGS_CFG, "preload enabled", prefs!!.getString("pref_preload", "false")!!)

                runOnUiThread {
                    obtainFixedScreenResolution()
                    dialog.hide()
                    runGame()
                }
            } catch (e: IOException) {
                Log.e(TAG, "Failed to write config files.", e)
                Crashlytics.logException(e)
            }
        }
        th.start()
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        menu.clear()
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_settings, menu)
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_reset_config -> {
                resetUserConfig()
                Toast.makeText(this, getString(R.string.config_was_reset), Toast.LENGTH_SHORT).show()
            }

            R.id.action_about -> AlertDialog.Builder(this)
                .setTitle(getString(R.string.about_title))
                .setMessage(R.string.about_contents)
                .show()

            else -> {
            }
        }

        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val TAG = "OpenMW-Launcher"

        var resolutionX = 0
        var resolutionY = 0
    }
}
