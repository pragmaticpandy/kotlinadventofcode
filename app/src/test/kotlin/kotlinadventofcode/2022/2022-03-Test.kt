// Originally generated by the template in CodeDAO
package kotlinadventofcode.`2022`

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class `2022-03-Test` {

    private infix fun String.when1runsWith(input: String) {
        assertEquals(this, `2022-03`().runPart1(input))
    }

    private infix fun String.when2runsWith(input: String) {
        assertEquals(this, `2022-03`().runPart2(input))
    }

    @Test fun testDefaultPart1() {
        assertEquals("7850", `2022-03`().runPart1())
    }

    @Test fun testDefaultPart2() {
        assertEquals("2581", `2022-03`().runPart2())
    }

}