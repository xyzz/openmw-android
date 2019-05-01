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
