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

package mods

import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

enum class ModType(val v: Int) {
    Plugin(1),
    Resource(2);

    companion object {
        private val reverseValues: Map<Int, ModType> = values().associate { it.v to it }
        fun valueFrom(i: Int): ModType = reverseValues.getValue(i)
    }
}

/**
 * Representation of a single mod in the database
 * @param type Type of the mod: plugin or resource
 * @param filename Filename of the mod, without the path
 * @param order Load order, or order in the list
 * @param enabled Whether the mod is enabled
 */
class Mod(val type: ModType, val filename: String, var order: Int, var enabled: Boolean) {

    /// Set to true when DB update is needed to keep consistency
    var dirty: Boolean = false

    /**
     * Updates the representation of this mod in the database
     * @param db Database connection
     */
    fun update(db: SQLiteDatabase) {
        db.update("mod",
            "load_order" to order,
            "enabled" to enabled)
            .whereArgs("filename = {filename} AND type = {type}",
                "filename" to filename,
                "type" to type.v).exec()
    }

    /**
     * Inserts this mod into the database
     * @param db Database connection
     */
    fun insert(db: SQLiteDatabase) {
        db.insert("mod",
            "type" to type.v,
            "filename" to filename,
            "load_order" to order,
            "enabled" to (if (enabled) 1 else 0))
    }
}

class ModRowParser : RowParser<Mod> {
    override fun parseRow(columns: Array<Any?>): Mod {
        return Mod(
            ModType.valueFrom((columns[0] as Long).toInt()),
            columns[1] as String,
            (columns[2] as Long).toInt(),
            (columns[3] as Long) != 0L)
    }
}
