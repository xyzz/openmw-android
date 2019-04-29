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

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

class ModsDatabaseOpenHelper private constructor(ctx: Context)
    : ManagedSQLiteOpenHelper(ctx, "ModsDatabase", null, 1) {

    init {
        instance = this
    }

    companion object {
        private var instance: ModsDatabaseOpenHelper? = null

        @Synchronized
        fun getInstance(ctx: Context) = instance ?: ModsDatabaseOpenHelper(ctx.applicationContext)
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable("mod", true,
            "type" to INTEGER,
            "filename" to TEXT,
            "load_order" to INTEGER,
            "enabled" to INTEGER)
        db.createIndex("mod_type", "mod", false, true,
            "type")
        db.createIndex("mod_filename", "mod", false, true,
            "filename")
        db.createIndex("mod_type_name", "mod", true, true,
            "type", "filename")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }
}

// Access property for Context
val Context.database: ModsDatabaseOpenHelper
    get() = ModsDatabaseOpenHelper.getInstance(this)
