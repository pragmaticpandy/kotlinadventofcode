// Originally generated by the template in CodeDAO
package kotlinadventofcode.`2023`

import kotlin.test.Test
import kotlin.test.assertEquals

class `2023-15-Test` {

    private infix fun String.when1runsWith(input: String) {
        assertEquals(this, `2023-15`().runPartOneNoUI(input))
    }

    private infix fun String.when2runsWith(input: String) {
        assertEquals(this, `2023-15`().runPartTwoNoUI(input))
    }

    @Test fun testDefaultPart1() {
        assertEquals("511257", `2023-15`().runPartOneNoUI())
    }

    @Test fun testDefaultPart2() {
        assertEquals("239484", `2023-15`().runPartTwoNoUI())
    }

}
