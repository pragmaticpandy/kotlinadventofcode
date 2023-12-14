// Originally generated by the template in CodeDAO
package kotlinadventofcode.`2023`

import kotlinadventofcode.`2023`.`2023-13`.Companion.toGrids
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class `2023-13-Test` {

    private infix fun String.when1runsWith(input: String) {
        assertEquals(this, `2023-13`().runPart1(input))
    }

    private infix fun String.when2runsWith(input: String) {
        assertEquals(this, `2023-13`().runPart2(input))
    }

    val exampleInput = """#.##..##.
..#.##.#.
##......#
##......#
..#.##.#.
..##..##.
#.#.##.#.

#...##..#
#....#..#
..##..###
#####.##.
#####.##.
..##..###
#....#..#"""

    val exampleGrid1Fixed = """..##..##.
..#.##.#.
##......#
##......#
..#.##.#.
..##..##.
#.#.##.#."""

    val exampleGrid2Fixed = """#...##..#
#...##..#
..##..###
#####.##.
#####.##.
..##..###
#....#..#"""

    @Test fun testExampleGrid1Fixed() {
        val grid = exampleInput.toGrids().first()
        val fixedGrid = grid.withSmudgeFixed
        assertEquals(exampleGrid1Fixed, grid.withSmudgeFixed.toString())
    }

    @Test fun testExampleGrid2Fixed() {
        val grid = exampleInput.toGrids().last()
        val fixedGrid = grid.withSmudgeFixed
        assertEquals(1, grid.withSmudgeFixed.topReflectionLengths.firstOrNull())
    }

    @Test fun testExample() {
        val grid = exampleInput.toGrids().first()
        assertEquals(5, grid.leftReflectionLengths.firstOrNull())
        assertNull(grid.topReflectionLengths.firstOrNull())
        val grid2 = exampleInput.toGrids().last()
        assertNull(grid2.leftReflectionLengths.firstOrNull())
        assertEquals(4, grid2.topReflectionLengths.firstOrNull())
    }

    @Test fun testExamplePart2() {
        assertEquals("400", `2023-13`().runPart2(exampleInput))
    }

    @Test fun testDefaultPart1() {
        assertEquals("30535", `2023-13`().runPart1())
    }

    @Test fun testDefaultPart2() {
        assertEquals("30844", `2023-13`().runPart2())
    }

}
