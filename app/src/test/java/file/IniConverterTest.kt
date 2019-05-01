/*
    Copyright (C) 2019 Ilya Zhuravlev

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

package file

import org.junit.Test

import org.junit.Assert.*

class IniConverterTest {

    @Test
    fun convertSimple() {
        val ini = IniConverter("""
[Something]
; A comment
; Another comment
Long key with spaces=Some value also with spaces

[Another thing]
First=Value
This ones empty=
Second=Another value
        """.trimIndent())
        val res = ini.convert()
        assertEquals(
            "fallback=Something_Long_key_with_spaces,Some value also with spaces\n" +
                "fallback=Another_thing_First,Value\n" +
                "fallback=Another_thing_Second,Another value\n",
            res)
    }
}
