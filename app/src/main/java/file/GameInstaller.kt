package file

import com.crashlytics.android.Crashlytics
import java.io.File
import java.io.IOException

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
        return findCaseInsensitive(INI_NAME) != null
            && findCaseInsensitive(DATA_NAME) != null
    }

    /**
     * Returns path to the Data Files directory as a string
     */
    fun findDataFiles(): String {
        return File(dir, DATA_NAME).absolutePath
    }

    /**
     * Adds a .nomedia to the game folder so that it doesn't bloat up the gallery
     * If this fails, then who cares
     */
    fun setNomedia() {
        try {
            val file = File(dir, ".nomedia")
            if (!file.exists())
                file.createNewFile()
        } catch (e: IOException) {
        }
    }

    companion object {
        const val INI_NAME = "Morrowind.ini"
        const val DATA_NAME = "Data Files"
    }

}