// Originally generated by the template in CodeDAO
package kotlinadventofcode.`2023`

import kotlin.test.Test
import kotlin.test.assertEquals

class `2023-07-Test` {

    private infix fun String.when1runsWith(input: String) {
        assertEquals(this, `2023-07`().runPartOneNoUI(input))
    }

    private infix fun String.when2runsWith(input: String) {
        assertEquals(this, `2023-07`().runPartTwoNoUI(input))
    }

    @Test fun testDefaultPart1() {
        assertEquals("253866470", `2023-07`().runPartOneNoUI())
    }

    @Test fun testDefaultPart2() {
        assertEquals("254494947", `2023-07`().runPartTwoNoUI())
    }

}
