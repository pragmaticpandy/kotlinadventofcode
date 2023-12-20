// Originally generated by the template in CodeDAO
package kotlinadventofcode.`2022`

import kotlin.test.Test
import kotlin.test.assertEquals

class `2022-04-Test` {

    private infix fun String.when1runsWith(input: String) {
        assertEquals(this, `2022-04`().runPartOneNoUI(input))
    }

    private infix fun String.when2runsWith(input: String) {
        assertEquals(this, `2022-04`().runPartTwoNoUI(input))
    }

    @Test fun testDefaultPart1() {
        assertEquals("605", `2022-04`().runPartOneNoUI())
    }

    @Test fun testDefaultPart2() {
        assertEquals("914", `2022-04`().runPartTwoNoUI())
    }

}
