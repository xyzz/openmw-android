package mods

import org.jetbrains.anko.db.*
import java.io.File

/**
 * Represents an ordered list of mods of a specific type
 * @param type Type of the mods represented by this collection, Plugin or Resource
 * @param path Path to the directory of the mods (the Data Files directory)
 * @param extensions List of supported extensions e.g. ["bsa"] or ["esm", "esp", ...]
 */
class ModsCollection(private val type: ModType,
                     path: String,
                     private val extensions: Array<String>,
                     private val db: ModsDatabaseOpenHelper) {

    val mods = arrayListOf<Mod>()

    init {
        syncWithFs(path)
    }

    /**
     * Synchronizes state of mods in database with the actual mod files on disk
     * This could result in it deleting or adding mods to the database.
     */
    private fun syncWithFs(path: String) {
        var dbMods = listOf<Mod>()

        // Get mods from the database
        db.use {
            select("mod", "type", "filename", "load_order", "enabled")
                .whereArgs("type = {type}", "type" to type.v).exec {
                    dbMods = parseList(ModRowParser())
                }
        }

        // Get file names matching the extensions
        val modFiles = File(path).listFiles()?.filter {
            extensions.contains(it.extension.toLowerCase())
        }

        // Collect filenames of mods on the FS
        val fsNames = mutableSetOf<String>()
        modFiles?.forEach {
            fsNames.add(it.name)
        }

        // Collect filenames of mods in the DB
        val dbNames = mutableSetOf<String>()
        dbMods.forEach {
            dbNames.add(it.filename)
        }

        // Get mods which are both in DB and on FS
        dbMods.filter { fsNames.contains(it.filename) }.forEach {
            mods.add(it)
        }

        // Figure current maximum order, new mods will be pushed below it
        var maxOrder = mods.maxBy { it.order }?.order ?: 0

        // Create an entry for each mod that's on FS but not in DB and assign proper order
        val newMods = arrayListOf<Mod>()
        (fsNames - dbNames).forEach {
            maxOrder += 1
            val mod = Mod(type, it, maxOrder, false)
            newMods.add(mod)
            mods.add(mod)
        }

        // Commit changes to the database
        db.use {
            transaction {
                // Delete all mods which are in db but not on fs
                (dbNames - fsNames).forEach {
                    delete("mod",
                        "type = {type} AND filename = {filename}",
                        "type" to type.v,
                        "filename" to it)
                }

                // Create all mods which are on fs but not in db
                newMods.forEach {
                    insert("mod",
                        "type" to it.type.v,
                        "filename" to it.filename,
                        "load_order" to it.order,
                        "enabled" to (if (it.enabled) 1 else 0))
                }
            }
        }

        // Sort the mods in order
        mods.sortBy { it.order }
    }

    /**
     * Performs DB updates for all mods marked as dirty
     */
    fun update() {
        db.use {
            mods.filter { it.dirty }.forEach {
                it.update(this)
                it.dirty = false
            }
        }
    }
}
