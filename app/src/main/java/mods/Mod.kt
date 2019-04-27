package mods

enum class ModType {
    Plugin,
    Resource
}

/**
 * Representation of a single mod in the database
 * @param type Type of the mod: plugin or resource
 * @param filename Filename of the mod, without the path
 * @param order Load order, or order in the list
 * @param enabled Whether the mod is enabled
 */
class Mod(val type: ModType, val filename: String, val order: Int, val enabled: Boolean) {

}
