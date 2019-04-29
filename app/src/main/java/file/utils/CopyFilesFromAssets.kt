/*
    Copyright (C) 2016 sandstranger
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

package file.utils

import java.io.File
import java.io.FileOutputStream
import java.io.IOException

import android.content.Context
import com.crashlytics.android.Crashlytics

class CopyFilesFromAssets(private val context: Context, private val configsPath: String) {

    fun copyFileOrDir(path: String) {
        val assetManager = context.assets
        try {
            val assets = assetManager.list(path) ?: return
            if (assets.isEmpty()) {
                copyFile(path)
            } else {
                val fullPath = configsPath
                val dir = File(fullPath)
                if (!dir.exists())
                    dir.mkdirs()
                for (i in assets.indices) {
                    copyFileOrDir(path + "/" + assets[i])
                }
            }
        } catch (ex: IOException) {
            Crashlytics.logException(ex)
        }
    }

    private fun copyFile(filename: String) {
        try {
            val inp = context.assets.open(filename)
            val newFileName = configsPath + filename.replace("libopenmw", "")

            val dirPath = newFileName.replace(File(newFileName).name, "")
            val dir = File(dirPath)
            if (!dir.exists())
                dir.mkdirs()

            val out = FileOutputStream(newFileName)
            inp.copyTo(out)
            out.flush()

            inp.close()
            out.close()
        } catch (e: Exception) {
            Crashlytics.logException(e)
        }
    }
}
