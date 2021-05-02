// Originally generated by the template in CodeDAO
package kotlinadventofcode.`2015`

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull

private interface TestI {
    fun getS(): String
}

class `2015-09-Test` {

    private infix fun String.when1runsWith(input: String) {
        assertEquals(this, `2015-09`().runPart1(input))
    }

    private infix fun String.when2runsWith(input: String) {
        assertEquals(this, `2015-09`().runPart2(input))
    }

    @Test fun testDefaultPart1() {
        assertEquals("251", `2015-09`().runPart1())
    }

    @Test fun testDefaultPart2() {
        assertEquals("898", `2015-09`().runPart2())
    }

    @Test fun testPart12() {
        assertEquals("10", `2015-09`().runPart1("""a to b = 10"""))
    }

    @Test fun testPart13() {
        "9" when1runsWith """a to b = 10
            |b to c = 7
            |a to c = 2
        """.trimMargin()
    }

    @Test fun testSetEquality() {
        data class Test(val id: String, val nonId: Set<String>)
        assertEquals(Test("foo", setOf("a", "b")), Test("foo", mutableSetOf("a", "b")))
    }

    @Test fun testInterfaceEquality() {
        kotlin.test.assertNotEquals<TestI>(
            object: TestI {
                override fun getS(): String {
                    return "hi"
                }
            },
            object: TestI {
                override fun getS(): String {
                    return "hi"
                }
            })
    }
}