/*
    Copyright (C) 2016 sandstranger
    Copyright (C) 2018 Ilya Zhuravlev

    This file is part of OpenMW-Android.

    OpenMW-Android is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    OpenMW-Android is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with OpenMW-Android.  If not, see <https://www.gnu.org/licenses/>.
*/

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
