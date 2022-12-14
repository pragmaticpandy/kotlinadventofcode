// Originally generated by the template in CodeDAO
package kotlinadventofcode.`2022`

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class `2022-14-Test` {

    private infix fun String.when1runsWith(input: String) {
        assertEquals(this, `2022-14`().runPart1(input))
    }

    private infix fun String.when2runsWith(input: String) {
        assertEquals(this, `2022-14`().runPart2(input))
    }

    @Test fun testDefaultPart1() {
        assertEquals("1001", `2022-14`().runPart1())
    }

    @Test fun testDefaultPart2() {
        assertEquals("27976", `2022-14`().runPart2())
    }

}
