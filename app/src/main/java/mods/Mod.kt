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
class Mod(val type: ModType, val filename: String, var order: Int, val enabled: Boolean) {

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
