// Originally generated by the template in CodeDAO
package kotlinadventofcode.`2015`

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class `2015-17-Test` {

    private infix fun String.when1runsWith(input: String) {
        assertEquals(this, `2015-17`().runPart1(input))
    }

    private infix fun String.when2runsWith(input: String) {
        assertEquals(this, `2015-17`().runPart2(input))
    }

    @Test fun testDefaultPart1() {
        assertEquals("4372", `2015-17`().runPart1())
    }

    @Test fun testDefaultPart2() {
        assertEquals("4", `2015-17`().runPart2())
    }

}
