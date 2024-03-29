// Originally generated by the template in CodeDAO
package kotlinadventofcode.`2023`

import com.github.h0tk3y.betterParse.combinators.*
import com.github.h0tk3y.betterParse.grammar.*
import com.github.h0tk3y.betterParse.lexer.*
import kotlinadventofcode.Day

class `2023-14` : Day {

    override fun runPartOneNoUI(input: String): String {
        return input.toPlatform().tiltedNorth.northernLoad.toString()
    }

    override fun runPartTwoNoUI(input: String): String {
        return input.toPlatform().cycled(1000000000).northernLoad.toString()
    }

    companion object {

        data class Platform(val grid: List<List<Terrain>>) {
            val tiltedNorth by lazy { getTiltedNorthPlatform() }
            val northernLoad by lazy { yIndices.sumOf { getNorthSupportBeamLoad(it) } }

            private val rotatedRight by lazy { Platform(grid.rotatedRight()) }
            private val xIndices = grid.first().indices
            private val yIndices = grid.indices
            private val cycled by lazy {
                var result = this
                repeat(4) { result = result.tiltedNorth.rotatedRight }
                result
            }

            fun cycled(times: Int): Platform {
                val seen = mutableMapOf<Platform, Int>()
                var current = this
                repeat(times) { i ->
                    seen[current]?.let { firstSeenAt ->
                        val cycleLength = firstSeenAt - i
                        val remainingCycles = (times - i) % cycleLength
                        return seen.entries.first { it.value == firstSeenAt + remainingCycles }.key
                    } ?: seen += current to i

                    current = current.cycled
                }

                return current
            }

            private fun getTiltedNorthPlatform(): Platform {
                val newGrid: List<MutableList<Terrain>> = grid.map { it.toMutableList() }
                for (y in 1..yIndices.last) {
                    for (x in xIndices) {
                        if (newGrid[y][x] == Terrain.ROUND_ROCK) {
                            val firstNonEmptyY = (y - 1 downTo 0).firstOrNull { newGrid[it][x] != Terrain.EMPTY } ?: -1
                            if (firstNonEmptyY == y - 1) continue
                            newGrid[y][x] = Terrain.EMPTY
                            newGrid[firstNonEmptyY + 1][x] = Terrain.ROUND_ROCK
                        }
                    }
                }

                return Platform(newGrid)
            }

            private fun getNorthSupportBeamLoad(row: Int): Int {
                val loadPerRoundRock = grid.size - row
                return grid[row].count { it == Terrain.ROUND_ROCK } * loadPerRoundRock
            }

            override fun toString(): String {
                return grid.joinToString("\n") { it.joinToString("") }
            }
        }

        enum class Terrain(val char: Char) {
            EMPTY('.'),
            SQUARE_ROCK('#'),
            ROUND_ROCK('O');

            override fun toString(): String = char.toString()
        }

        fun <T> List<List<T>>.rotatedRight(): List<List<T>> {
            val result = Array(this.firstOrNull()?.size ?: 0) { mutableListOf<T>() }
            for (x in this.first().indices)
                for (y in this.indices.reversed())
                    result[x].add(this[y][x])

            return result.map { it }
        }

        fun String.toPlatform(): Platform {
            val grammar = object : Grammar<Platform>() {

                // tokens
                val newlineLit by literalToken("\n")
                val dotLit by literalToken(".")
                val poundLit by literalToken("#")
                val oLit by literalToken("O")

                // parsers
                val empty by dotLit use { Terrain.EMPTY }
                val squareRock by poundLit use { Terrain.SQUARE_ROCK }
                val roundRock by oLit use { Terrain.ROUND_ROCK }
                val terrain by (empty or squareRock or roundRock)
                val row by oneOrMore(terrain)
                override val rootParser by separatedTerms(row, newlineLit) map { Platform(it) }
            }

            return grammar.parseToEnd(this)
        }
    }


    override val defaultInput = """....#.#....O...OO..#..#....OO...#.#..OO#.#O.#.O.#.....O....O......O..........O.....O....#..#O..#O...
O#....O..#O........O.#...OO....#O...O#.#.....O.....O.O....##...O.O..O.#.O......#....O.O.....###....#
....#.#O..........O...............O.O..#....#..O#.#.........#..#......#..O..O.OO..O.O#O.OO..O..OO..#
...#OO....O.......O#.OO#.O#..O#.O..O#OO..#OO.O.#..OO......O...##.....#..O##O.#O.......OO.#......#...
.#..#.O..#....#O....#O..OO...#O.#..O..#.....O#....OO......OOO#.#.....#..O.##.OO....#...O.#.O#....#O.
..#.#OO#..#.O..O.#..#O.......O...###......#.#.O..#.O.#...O.....O.O#...OO...#..O..#...O....#....#.O#.
......OO....OO...#O.......O##..#........#O....O.........#.###..O...O.O........OO#.....O#.#.##......O
..#....#.#...O...#..##...#O....##..#..#....O..O...O.#.....O.#.......O.OO....#OOOO.#.....O#.#..OOO...
.O..##.#O...#...O......OOO.......#O.O..#.#....#.....OO.O...OO#.O.O....#.#.............O##....##O##..
O#..O........#..#...#.....#..#O.#O....###...#....#..#OOO.....O.O###.O..#O...O...##..O....O..O....O.#
...O....#..O.....O..O...O....#.O...#.#....OO..O.##....#.OO..O...##....#..#OOO.#O#...O#O#.....#..OO..
.....#.##.#..OO..#..#O#O#OO.O......O....#.O..##O....#..#..#...O#O.#.O..O....O.......##.O#...O..#...#
.....OO.....#.....OO........#.OOO.O.#OO......#.#....#....#.##..O..###.....#....O.O.......OO.OO.#.#..
#.............OO......OO.OO#.#...#.##.......#.#..O#..#.O.#.....#.O..O...O.O........#O..O..O..O....#.
O..O.O..OO#O...OO...O...#.O....#...O...O#...#.........O...#.#OO..OO..O.O........#OO.##.O#..O......OO
..##.##.O.##O.O...O.OO#...........#O...O..O..#O...........#.#.###.#.#.O.O....##..#.O.OO...#.....O#..
....O......OO.O..#.......O..O.#.#O....O..O.#....O..##.O#...#..O#.#.##..O...#O...#.O.....#.O...O#....
.#.O.O.#....##...#..##..#.#.O.#..O.....#..##..#..O.......#....O......O.O.........OO.#...#.#O...O.O.O
....O#..#....OO#..OO.OO.O..#OO#.O.......#OO.#O..O.#..#..#OO...O.....#...O........#..#..O...O..O.O#..
#.O..........OO#O...........OO....##.#.O.....OOO.#.......#.......O...#..OO....#....#.......O.O....#.
..O#......O.OO......O##......O#O....#OO...#.OO....OO#.O#.O.#O..O.....O......O.....#.#O...#OO#..O.#O.
.#...#OOO...O....O...OO..#OO.#.O.OO.O.O.O.O...#..O.....O.#O#.O.O.....O##...#....O##.#.O....#..O...OO
...........#..........O#...#.....O#..O.....#O.O.....O.O..O.#.OO.O...O#......O....O#......OO..O.O..#.
#..#.OO..#.O.##O.O..#....#O#O#..#...#OO..O....O...O..##....OOO.....O#..O..##O..O.O##.OO..#.........O
O.O.##......#...#O.##.#.OO..O....O.O.O#..O..####........O......O....O.......O#.....O.O.#.O#.O.#OO#..
.....OO.#..OO..O..O.#.#....#..OO..OO.....O#......O.#.#........#...........#....#......O....O..OO.#.O
O##..O.#...OO#..#..##O...O##.#O##.O.........O...OO#..O.#.....#O.#.O.....#...##..#O.#...OO.#O.#.#.#..
...#..O..O....OOOOO..#..#OO..O.#...#....#...O........#.#........O.....O..O....OOO.....#.O..##..O.##O
.O.#O.....#O.O#...#..#.OOO.O#.O....#.O...........#.....#...#...O....O.#...O..#...O#.......O..O......
..........O##...O....##.....O..#.......##.....#..##.#O....#..O..##O...OO...O.....#.O..#..###O.##..O#
..###O.O.#.#O....O#.##..O.#..##.##O.O.#..O#.#.O#.O.##...O.......#...O.#.......#..##.O...#..O....O.O.
...O..O#.##O.....O.O.#O..O#.O##.O.#....O#.#O.O..#......#..###.O#O.##.........O...O...O#....O........
.O....O.#....#.OO#..#O...#.......#......OOO.#OO.O#.#.O......#O#OO..#O.O..#..#.O.............O#.OO#.#
#O.O.....#.O.O#...OO.......O...O..O.#..........O......O.O..#O.#.......O..#O#...O...OO.#..#.#...#...O
.....#....#.....#O..O...O...#.....#OO....#..#O.O.#OO..#.O.O#...#.O.O..#..#..#OO#..#..#..O...#..O#..O
....O..OOO.##...#.O#......#O.O.O....#O..#.O..#........O#.O...O........#O#O..#..#.O...O.##.O.O.....O#
.O..O#.O..#.....###..#......#OO..........O.#.O#..OOO.###.....#..OO.#O...O...OOOO...O.........O......
O...O.........O......OO...#..O.##..#..#..O.....O..O.........O.O..OO....#..O.#..##OO#.#....#.###.....
.OO.O.#......#.O.......##..O.O.O#..O.OO...O#..#.......#.........OO#.....#..O..O...O.O....#.O..O#....
O...#..O.OO..O.#.O...#.......#.O.O.O.....#.....O..OOO#...O.#O.O#..O...O#...........O#..O........O...
O#O.#O..#.......#..O.#..#....#..O..#..O.#......O.OO.O.....#.O.#....O.#.......O......O#.O...#.#.#.O..
..O#O.OO..#......#.#.#....#..#...#..O.#..#O.O.......O.##...O.O.O.#O...O.O.O##O..#..O...O..OO.#..#.#.
...O.#.....#O...O....O....O....OO..........O.O#O.O.O###...##O.#O...#O.#.OO...#..#..O##...O......#...
.##.##..O..O#...#O...OO.#...O..##....O....#.OO...#O.O..#..O.#.....O.O.#.##O..O..O.O.......O...O...#.
O.OO.O.##.O..##OO.............#.....O....#O..O#...#.##.#.#.........OO..OO..O....OO.#O.O..O..........
#..O.O....OO..O..#...O.....O.#.O#.O.#.#OO#......O.O..O.O...#.OO.#.O#..#O.#..OO.#..#.##.....#OO...OO.
..OO##.#........OOO..O.##....O.O#...OOO......O...O..O.#......#.O...O.#..O.O..O.O..O.O...#O.....OO.#O
O..#..O.#.O...#OO##......O.O.....#O.#O#O.#..#..#.OO..#.O##......O.#O....#.....#...#...O#...O.O....#.
O.........O#O#.#.#......#.O##.O.O#......O.#...OO....#...#..#O#.OO#.###.O.#.#..O..#..#OOO#O#.O...#O.#
.O#.O....O..OO..OO#...#O........O.OO.O#O##.O#O#....#...#O#........#.#O.O#..#..O##..##.........#.O##.
...##...O.#O.##....#.....O....#..OO..O...#.O.O......OO#..O#..#####.#O..#.#....O.#...O...........#O..
.O.#.O..OO.O...#.#....#...O#....#O..#O.#.O.O.O..#..O#.O...O..O.O#O.....O......#O......#O..#.#O.O..OO
......#........O.#O.OO.#...O...##.....#......OO...#O#OO..OOO....#......#..##..#.O...OOOO.#....O...#.
O...#O..OO.....OO.OOOOO#.O....#...#...O..#..#O....##.OO.#OO.OO..#O..#O.O#.OOO.............#OOO....##
.#.O.O..O.....#..#....#...OO...#...OO.#........O.O....##..........#.#O...OO###OOO..#...#..OOO..O..O#
O#...OO..O.#........##......#O....#.........#....O#.#....#..O...O#..O.#......OOO.#....O..OO...O##.OO
....#O#.O..O.O..#O.O#....O.#.O...OO...O..OOO..O.O.O#.#...#.......O.O..#....O...#O.##.....#.#O#.OOO#.
.O###...#...OO.O.....O.....#..OOO.##O....OO.O.............O......O..#.O..#....#..#...#..#O..O...#OO.
...O...#..........#.O..O.........O.O.....#..#....O#O...#.O.#OO.##O...OO..........#O..#....#O....O...
..O..O.#.....O#O..#O##O.#O.O##.#.....O.O.O.#.O.....OO..OOO.O#.OO.#...O..O...#.O.OO#..OO.#..OO####OO.
O.......O#..O.O.#.#...O.OOO...#O....#O.O.O.O...#......O.##...#........#..#..#.#.O##..O.OO.#O..O.....
..O..OO..#....O..OO.#.##....#.......#..#.#..O#..#.#OO#..........#O#O...O.OO...O..#....O...#.......O.
....#.#.#O..O........#.O#.O#..#.......#.#...##OOOOO.OO.O#..#....OO..OOO.O......O.........#..OO.O.#.O
O...O.....#O###..O.OO.O...O...#...#O.....OO...#.....O.O.O..#...O.O..#O..O.O.#...#O.O..#..O....#OO..#
...#..#......#.O#....#O#.O....O..O..O..O....O...#..#O..#.....#...O.....OOO..OO.#O#.......#.O...OOOOO
OOO#.O.O.#O....O..#...O..O#O..O#...#...#..O#..O..O.O#......O...#O..O#.O...#.O.#...O...OO.#....O.....
O..#O...O#..#...O.#.O......#.#..#.O...##.O..#O...O#.#.O.....O...OO.OOO.O.#..#.O....##.#.#O..OO...#OO
...O.O..O.OO.O..O.OOO.O#.#..O.....OO#..OOO.#.#....O.#O.#.O.OO....O..#..O#O###OO#.......##........#..
#...#..O.###.O.#O......OO#......O.O..#O......OO.O.#....#....O...OO##OO#O...O.........O#OOO..........
..O.O..............O.....OOOO#.OO..O#..O.###.O..O.#.O.O.....O.#O.OOO..OO#..OO.#.....#.......O#...O..
O...#.OOO....OO......O..O......O.....O....O..#O..#.O.............O.#..............#....#O....#O#O...
.OO.OOO.........OOO#.OO....#...#....OO...O..O.....#...#O..#O.O.#.O.#O......O.O.O#..O.O.#...#O....O#.
O.....#OO......#.#..OO.O.##O#......#......O..#...#O#..#O..O..#.........O..##.O..##O.#.#.O.#..OOO#..O
...O....#..#....OO.O.##...OO#.O##..........##.#..OO.#..O#.#.O..#...O..#..O..#....O..#....O..#.###O..
.O....O...O#....O#O..O..OO...#O.O.....#...#.......#...##.O..#...#.....O......O..##...##......O.#...O
O#.O#OO.O.#O.#.#O..#.O..O..#O#O.#O...........O#.O...O...O....O......O.OO......O..O##..O....OO#...##O
..O..O.OOO....#O#..O#...#.O....#....#O..O#..##.O.....O.O#O..O.....##...#..#............#....O.O.#.O#
.....O.#..O..#.OO.....O...O..#.......O....#.##.O#O....O.O...#.OO....##O....O....#OO#O.......O#..#O.O
#O.O.#......#.O....O....#..O##......O.#.#....#OO.....O.OOO...#.O.....O#.O.O#..O#..#.....#.OO...OO.O.
..#O........O.O.O.O.....O..#...O.O.#O.OO....O...#.......##...O.......OO.#O.O.O..O.##...O.O#..##..#O.
O#O....O.O.O#.###O...#.....O..#....#.#.O.....O..####.O.O..#..OO.#..O....##..#O.##.....O...O.....OO..
#...OO.#.#.#...O.#O....#O#.............###.O..O.O.O.#...##O..O#...OO#OO.....###.O..#.#...O.....O..O.
.O.OO#..O..OO#.O#.O..##....O#...O...O....OO.....#.#O.....O......O##......#....#..#......O##.#....#O#
.#O#O..#....##.OO.............O..##....O......#.........#.......#O..O##....#.....#..#......#O.OOO...
.O.O#OO..#..........O.#O.OO......##....#..O###O..OO..O..O.##O..#.......#..O..#..#.#......O...#....#.
.#.O....OO.O....#O..OOO.#....O.....OO..O..O...O...#..O....O......O...........#.O.....#...##..O.O#.OO
.O.O..O#O.OO..OO#.OO.#O.....O...#.OOO....O#......O.OO..#.#..O.....#..O.O#O.O#..O##O..O....O....O....
..OO......#..#O...OO..O.#..#O##....O..#.#.O...O..#O....#.O.....OO.O.OO...OO...O#.O.....OOO...#....#.
#OO..#.....#OO...O.......O....#...O...O.#O....O....O....#.......O....#..O.....#....OO.##.OO#O.O#....
#..#O....O#...#.#OO..............#OO#OO#.O....O..OO.O.O#...O...#..#O.#.......O....#O..#..#O.....##O.
...#.....#.#..#OOOO..O.O#O..O..#..O..O.............OO...#.....#O.OO..#..#.....OO.....#O....#..O#....
...O.#.O.#O.O.O##..O..O.O......O#O##...........O..#..#...........O.O.O...O..#.....OO.O.......#O.....
#.O#O.....#O......O#.......OO.O.O..#..#..#O....#......#.O..O..#O#.###.#..#..#...O.OO....#.#O#..O...#
#..O..#.O.O......#...#......#..#......O...O..#....O#.O.O..O...O..#..#O.OOO..O...#.......O..OO.O....#
...O..OO#O..#....#....###O...O#.#OO..O...O....O#.#....#.O......#..#...#..#.O......O..O...#.O....#.O.
OO..#..O##.O..O...O..#O....#....#.O.O....O#....OO.#.OO..O..OO..O#...O....OO..#.OOOO.#..O.O.....#..##
.OO.O....O...O#O.....##....O..#O##....O.....##....O.#..#O#O.OOOO..O..O.O.......OO..O.#....O.....O..O
.O.O..O#..#OO..O.O.....OO..OO..##.#..O...........#.O..#.O.#...O...O..OOO..O.#O.#....O.#.OO..O#......
..O#..O#.O...##..#.#.###....O#..O.O##.O.#.O....OO.#..O.OO.#....O..O#O.#...##...OO#.OO..O.....#.....O
...#OO#.......O.O.##...#O..O#.O#O#..OO..O..O#O..O.#.OO#.....#.....O...O.O.....#O#.OO.O...O.O#..#O..#"""
}