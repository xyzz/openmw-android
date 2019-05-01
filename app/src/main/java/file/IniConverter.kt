package file

/**
 * Converts morrowind.ini to fallback= format
 * @param data Contents of morrowind.ini as a string
 */
class IniConverter(private val data: String) {

    /**
     * Performs the actual conversion
     * @return String contents of the output file in openmw's fallback format
     */
    fun convert(): String {
        var category = ""
        var output = ""

        data
            // Split into lines
            .lines()
            // Trim whitespace/newlines
            .map { it.trim() }
            // Remove comments and empty lines
            .filter { it.isNotEmpty() && !it.startsWith(";") }
            .forEach {
                // It's a category
                if (it.startsWith("[") && it.endsWith("]"))
                    category = it.substring(1, it.length - 1).replace(" ", "_")
                // It's a key-value pair
                else if (it.contains("="))
                    output += "fallback=${category}_${convertLine(it)}\n"
            }

        return output
    }

    /**
     * Converts a single morrowind setting line into openmw format
     * (replacing spaces with _ and = with ,)
     * @param line Line to convert
     * @return Converted result, note that this does not include the fallback=Category_ part
     */
    private fun convertLine(line: String): String {
        // key and value are separated by = in mw and by , in omw
        val kv = line.split("=".toRegex(), 2)
        val key = kv[0].replace(" ", "_")
        val value = kv[1]

        return "$key,$value"
    }
}
