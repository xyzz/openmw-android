package file

import java.io.File

/**
 * Class responsible for initial game setup which involves
 * transforming morrowind.ini into openmw.cfg
 */
class GameInstaller(path: String) {

    val dir = File(path)

    /**
     * Lists the root directory and finds a file or directory named "name",
     * doing case-insensitive checks
     * @param name Name to search
     * @return File object if it was found, null otherwise
     */
    private fun findCaseInsensitive(name: String): File? {
        val nameLower = name.toLowerCase()
        return dir
            .list { _, fileName -> fileName.toLowerCase() == nameLower }
            .map { File(dir, it) }
            .firstOrNull()
    }

    /**
     * Checks that the "path" directory contains a morrowind.ini,
     * and that there's a "Data Files" directory
     */
    fun check(): Boolean {
        // Root directory must exist and be a directory
        if (!dir.exists() || !dir.isDirectory)
            return false

        // morrowind.ini as well as data files must exist
        return findCaseInsensitive(Companion.INI_NAME) != null
            && findCaseInsensitive(Companion.DATA_NAME) != null
    }

    companion object {
        const val INI_NAME = "Morrowind.ini"
        const val DATA_NAME = "Data Files"
    }

}