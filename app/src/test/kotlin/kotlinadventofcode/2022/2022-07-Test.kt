// Originally generated by the template in CodeDAO
package kotlinadventofcode.`2022`

import kotlin.test.Test
import kotlin.test.assertEquals

class `2022-07-Test` {

    private infix fun String.when1runsWith(input: String) {
        assertEquals(this, `2022-07`().runPartOneNoUI(input))
    }

    private infix fun String.when2runsWith(input: String) {
        assertEquals(this, `2022-07`().runPartTwoNoUI(input))
    }

    @Test fun testDefaultPart1() {
        assertEquals("1611443", `2022-07`().runPartOneNoUI())
    }

    @Test fun testDefaultPart2() {
        assertEquals("2086088", `2022-07`().runPartTwoNoUI())
    }

}
