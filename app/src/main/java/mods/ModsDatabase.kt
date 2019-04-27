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
