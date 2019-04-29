package parser

import java.util.ArrayList
import java.util.Collections

class CommandlineParser(data: String) {
    private val args = ArrayList<String>()
    val argv: Array<String>

    val argc: Int
        get() = args.size

    init {
        args.clear()
        args.add("openmw")
        if (data.contains("--")) {
            Collections.addAll(args, *data.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
        }
        argv = args.toTypedArray()
    }
}
