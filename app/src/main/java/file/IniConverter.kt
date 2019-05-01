/*
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

/**
 * Converts morrowind.ini to fallback= format
 * @param data Contents of morrowind.ini as a string
 */
class IniConverter(private val data: String) {

    /**
     * Performs the actual conversion
     * @return String contents of the output file in openmw's fallback format
     */
    fun convert(): String {
        var category = ""
        var output = ""

        data
            // Split into lines
            .lines()
            // Trim whitespace/newlines
            .map { it.trim() }
            // Remove comments and empty lines
            .filter { it.isNotEmpty() && !it.startsWith(";") }
            .forEach {
                if (it.startsWith("[") && it.endsWith("]")) {
                    // It's a category
                    category = it.substring(1, it.length - 1).replace(" ", "_")
                } else if (it.contains("=")) {
                    // It's a key-value pair
                    val converted = convertLine(it)
                    if (converted.isNotEmpty())
                        output += "fallback=${category}_$converted\n"
                }
            }

        return output
    }

    /**
     * Converts a single morrowind setting line into openmw format
     * (replacing spaces with _ and = with ,)
     * @param line Line to convert
     * @return Converted result, note that this does not include the fallback=Category_ part
     */
    private fun convertLine(line: String): String {
        // key and value are separated by = in mw and by , in omw
        val kv = line.split("=".toRegex(), 2)
        val key = kv[0].replace(" ", "_")
        val value = kv[1]

        if (key.isEmpty() || value.isEmpty())
            return ""

        return "$key,$value"
    }
}
