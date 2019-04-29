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

package file

import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileWriter
import java.io.IOException
import java.io.InputStreamReader

object Writer {

    /**
     * Replaces a key=value in config file with the new value
     * @param path Path to the configuration file to edit
     * @param key Key to replace
     * @param value New value to put
     */
    @Throws(IOException::class)
    fun write(path: String, key: String, value: String) {
        // Create a new empty file if it doesn't already exist
        val fin = File(path)

        fin.createNewFile()

        val file = FileInputStream(path)
        val reader = BufferedReader(InputStreamReader(file))
        var line: String? = reader.readLine()
        val builder = StringBuilder()
        var contains = false
        while (line != null) {
            if (line.startsWith(key) && !contains) {
                builder.append("$key=$value")
                contains = true
            } else
                builder.append(line)
            builder.append("\n")
            line = reader.readLine()
        }
        if (!contains)
            builder.append("$key=$value")

        reader.close()
        val writer = FileWriter(path)
        writer.write(builder.toString())
        writer.flush()
        writer.close()
    }
}
