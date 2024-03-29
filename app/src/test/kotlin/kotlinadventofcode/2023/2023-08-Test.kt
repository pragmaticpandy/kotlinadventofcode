// Originally generated by the template in CodeDAO
package kotlinadventofcode.`2023`

import kotlin.test.Test
import kotlin.test.assertEquals

class `2023-08-Test` {

    private infix fun String.when1runsWith(input: String) {
        assertEquals(this, `2023-08`().runPartOneNoUI(input))
    }

    private infix fun String.when2runsWith(input: String) {
        assertEquals(this, `2023-08`().runPartTwoNoUI(input))
    }

    @Test fun testDefaultPart1() {
        assertEquals("19783", `2023-08`().runPartOneNoUI())
    }

    @Test fun testExamplePart2() {
        assertEquals("6", `2023-08`().runPartTwoNoUI("LR\n" +
            "\n" +
            "EEA = (EEB, XXX)\n" +
            "EEB = (XXX, EEZ)\n" +
            "EEZ = (EEB, XXX)\n" +
            "FFA = (FFB, XXX)\n" +
            "FFB = (FFC, FFC)\n" +
            "FFC = (FFZ, FFZ)\n" +
            "FFZ = (FFB, FFB)\n" +
            "XXX = (XXX, XXX)"))
    }

    @Test fun testCycleRPZ() {
        val (instructions, nodes) = `2023-08`().parse(`2023-08`().defaultInput)
        val nodesById = nodes.associateBy { it.id }
        assertEquals(11_653.toBigInteger(), `2023-08`().getNumTurnsToAZ("RPZ", nodesById, instructions).first)
    }

    @Test fun testCycleZZZ() {
        val (instructions, nodes) = `2023-08`().parse(`2023-08`().defaultInput)
        val nodesById = nodes.associateBy { it.id }
        assertEquals(19_783.toBigInteger(), `2023-08`().getNumTurnsToAZ("ZZZ", nodesById, instructions).first)
    }

    @Test fun testDefaultPart2() {
        assertEquals("9177460370549", `2023-08`().runPartTwoNoUI())
    }
}
