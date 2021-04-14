package kotlinadventofcode.`2015`

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class `2015-01-Test` {
    @Test fun testDefaultPart1() {
        assertEquals("74", `2015-01`().runPart1())
    }

    @Test fun testDefaultPart2() {
        assertEquals("1795", `2015-01`().runPart2())
    }
}