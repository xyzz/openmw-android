package mods

import org.jetbrains.anko.db.*
import java.io.File

/**
 * Represents an ordered list of mods of a specific type
 * @param type Type of the mods represented by this collection, Plugin or Resource
 * @param dataFiles Path to the directory of the mods (the Data Files directory)
 */
class ModsCollection(private val type: ModType,
                     private val dataFiles: String,
                     private val db: ModsDatabaseOpenHelper) {

    val mods = arrayListOf<Mod>()
    private var extensions: Array<String> = if (type == ModType.Resource)
        arrayOf("bsa")
    else
        arrayOf("esm", "esp", "omwaddon", "omwgame")

    init {
        if (isEmpty())
            initDb()
        syncWithFs()
        // The database might have become empty (e.g. if user deletes all mods) after the FS sync
        if (isEmpty())
            initDb()
    }

    /**
     * Checks if the mod DB is empty, i.e. no mods defined yet. This can happen for example
     * on first startup
     * @return True if the DB doesn't have any mods
     */
    private fun isEmpty(): Boolean {
        var count = 0
        db.use {
            count = select("mod", "count(1)").exec {
                parseSingle(IntParser)
            }
        }
        return count == 0
    }

    /**
     * Inserts built-in mods into the database, in proper order.
     * Also checks to make sure only installed mods are inserted.
     */
    private fun initDb() {
        val builtIn = arrayOf("Morrowind", "Tribunal", "Bloodmoon")
        initDbMods(builtIn.map { "$it.esm" }, ModType.Plugin)
        initDbMods(builtIn.map { "$it.bsa" }, ModType.Resource)
    }

    /**
     * Inserts built-in mods of a specific mod type. All of the built-in mods will be enabled
     * by default.
     * @param files Filenames of the mods, including extensions
     * @param type Type of the mods (plugins/resources)
     */
    private fun initDbMods(files: List<String>, type: ModType) {
        db.use {
            var order = 0
            files
                .map { File(dataFiles, it) }
                .filter { it.exists() }
                .map { order += 1; Mod(type, it.name, order, true) }
                .forEach { it.insert(this) }
        }
    }

    /**
     * Synchronizes state of mods in database with the actual mod files on disk
     * This could result in it deleting or adding mods to the database.
     */
    private fun syncWithFs() {
        var dbMods = listOf<Mod>()

        // Get mods from the database
        db.use {
            select("mod", "type", "filename", "load_order", "enabled")
                .whereArgs("type = {type}", "type" to type.v).exec {
                    dbMods = parseList(ModRowParser())
                }
        }

        // Get file names matching the extensions
        val modFiles = File(dataFiles).listFiles()?.filter {
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
                newMods.forEach { it.insert(this) }
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
