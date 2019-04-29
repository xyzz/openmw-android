package file

import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileWriter
import java.io.IOException
import java.io.InputStreamReader

/**
 * Created by sandstranger on 13.10.16.
 */

object Writer {

    /**
     * Replaces a key=value in config file with the new value
     * @param path Path to the configuration file to edit
     * @param key Key to replace
     * @param value New value to put
     */
    @Throws(IOException::class)
    fun write(path: String, key: String, value: String) {
        // Create a new empty file if it doesn't already exist
        val fin = File(path)

        fin.createNewFile()

        val file = FileInputStream(path)
        val reader = BufferedReader(InputStreamReader(file))
        var line: String? = reader.readLine()
        val builder = StringBuilder()
        var contains = false
        while (line != null) {
            if (line.startsWith(key) && !contains) {
                builder.append("$key=$value")
                contains = true
            } else
                builder.append(line)
            builder.append("\n")
            line = reader.readLine()
        }
        if (!contains)
            builder.append("$key=$value")

        reader.close()
        val writer = FileWriter(path)
        writer.write(builder.toString())
        writer.flush()
        writer.close()
    }
}
