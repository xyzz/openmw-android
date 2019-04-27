package mods

import java.io.File

/**
 * Represents an ordered list of mods of a specific type
 * @param type Type of the mods represented by this collection, Plugin or Resource
 * @param path Path to the directory of the mods (the Data Files directory)
 * @param extensions List of supported extensions e.g. ["bsa"] or ["esm", "esp", ...]
 */
class ModsCollection(type: ModType, path: String, extensions: Array<String>) {

    val mods = arrayListOf<Mod>()

    init {
        // Get file names matching the extensions
        val modFiles = File(path).listFiles()?.filter {
            extensions.contains(it.extension.toLowerCase())
        }

        // Create Mod objects for every matched file
        modFiles?.forEachIndexed { idx, it ->
            mods.add(Mod(type, it.name, idx, true))
        }
    }
}
