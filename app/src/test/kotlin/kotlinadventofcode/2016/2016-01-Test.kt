// Originally generated by the template in CodeDAO
package kotlinadventofcode.`2016`

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class `2016-01-Test` {

    private infix fun String.when1runsWith(input: String) {
        assertEquals(this, `2016-01`().runPart1(input))
    }

    private infix fun String.when2runsWith(input: String) {
        assertEquals(this, `2016-01`().runPart2(input))
    }

    @Test fun testDefaultPart1() {
        assertEquals("231", `2016-01`().runPart1())
    }

    @Test fun testDefaultPart2() {
        assertEquals("147", `2016-01`().runPart2())
    }

}
