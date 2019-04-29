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
import java.io.InputStream
import java.io.OutputStream

import android.content.Context

class CopyFilesFromAssets(private val context: Context, private val configsPath: String) {
    fun copyFileOrDir(path: String) {

        val assetManager = context.assets
        var assets: Array<String>? = null
        try {
            assets = assetManager.list(path)
            if (assets!!.size == 0) {
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

        }

    }

    private fun copyFile(filename: String) {
        var filename = filename

        val assetManager = context.assets

        var inp: InputStream? = null
        var out: OutputStream? = null
        try {
            inp = assetManager.open(filename)
            filename = filename.replace("libopenmw", "")
            val newFileName = configsPath + filename
            val tmp = File(newFileName)
            val dirPath = newFileName.replace(tmp.name, "")
            val dir = File(dirPath)
            if (!dir.exists())
                dir.mkdirs()
            out = FileOutputStream(newFileName)

            inp.copyTo(out)
            inp!!.close()
            inp = null
            out.flush()
            out.close()
            out = null
        } catch (e: Exception) {

        }

    }

}
