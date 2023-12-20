// Originally generated by the template in CodeDAO
package kotlinadventofcode.`2022`

import kotlinadventofcode.Day
import java.math.BigInteger

/**
 * 4th to finish on the Stripe leaderboard
 */
class `2022-17` : Day {

    override fun runPartOneNoUI(input: String): String {
        fun <T> Sequence<T>.repeat() = sequence { while (true) yieldAll(this@repeat) }
        val tetris = Tetris(parse(input))
        sequenceOf(Shape.DASH, Shape.PLUS, Shape.L, Shape.BAR, Shape.SQUARE).repeat().take(2022).forEach {
            tetris.place(it)
        }

        return tetris.height.toString()
    }

    override fun runPartTwoNoUI(input: String): String {
        val iterations = "1000000000000".toBigInteger()
        fun <T> Sequence<T>.repeat() = sequence { while (true) yieldAll(this@repeat) }
        val tetris = Tetris(parse(input))
        val iterator = sequenceOf(Shape.DASH, Shape.PLUS, Shape.L, Shape.BAR, Shape.SQUARE).repeat().iterator()
        var count = 0.toBigInteger()

        val place = {
            val shape = iterator.next()
            tetris.place(shape)
            count++
        }

        do { place() } while (!tetris.isTopSolid())
        val heightOfFirstIrregularStory = tetris.height
        val countOfFirstIrregularStory = count
        do { place() } while (!tetris.isTopSolid())
        val normalStoryHeight = tetris.height - heightOfFirstIrregularStory
        val normalStoryCount = count - countOfFirstIrregularStory
        val numRepeats = (iterations - count) / normalStoryCount
        val skippedHeight = numRepeats * normalStoryHeight
        count += numRepeats * normalStoryCount
        do { place() } while (count < iterations)
        return (tetris.height + skippedHeight).toString()
    }

    private enum class Jet {
        LEFT,
        RIGHT;
    }

    /**
     * 0,0 coord is the bottom left
     */
    private enum class Shape(val coords: Set<Coord>) {
        DASH(
            setOf(
                0.toBigInteger() by 0.toBigInteger(),
                1.toBigInteger() by 0.toBigInteger(),
                2.toBigInteger() by 0.toBigInteger(),
                3.toBigInteger() by 0.toBigInteger()
            )
        ),
        PLUS(
            setOf(
                1.toBigInteger() by 1.toBigInteger(),
                1.toBigInteger() by 2.toBigInteger(),
                2.toBigInteger() by 1.toBigInteger(),
                1.toBigInteger() by 0.toBigInteger(),
                0.toBigInteger() by 1.toBigInteger()
            )
        ),
        L(
            setOf(
                0.toBigInteger() by 0.toBigInteger(),
                1.toBigInteger() by 0.toBigInteger(),
                2.toBigInteger() by 0.toBigInteger(),
                2.toBigInteger() by 1.toBigInteger(),
                2.toBigInteger() by 2.toBigInteger()
            )
        ),
        BAR(
            setOf(
                0.toBigInteger() by 0.toBigInteger(),
                0.toBigInteger() by 1.toBigInteger(),
                0.toBigInteger() by 2.toBigInteger(),
                0.toBigInteger() by 3.toBigInteger()
            )
        ),
        SQUARE(
            setOf(
                0.toBigInteger() by 0.toBigInteger(),
                1.toBigInteger() by 0.toBigInteger(),
                0.toBigInteger() by 1.toBigInteger(),
                1.toBigInteger() by 1.toBigInteger()
            )
        );
    }

    /**
     * floor is at y = 0. rocks at y > 0
     * left wall is at x = 0. rocks at x > 0
     * right wall is at x = 8. rocks at x < 8
     */
    private class Tetris(val jets: List<Jet>) {
        val rocks: MutableSet<Coord> = mutableSetOf()
        var jetIndex = 0
        var height = 0.toBigInteger()

        fun isTopSolid(): Boolean {
            return (1..7).map { it.toBigInteger() }.all { rocks.contains(it by height) }
        }

        fun place(shape: Shape) {
            var coords = shape.coords.map { it.x + 3.toBigInteger() by it.y + height + 4.toBigInteger() }.toSet()
            while (true) {
                if (jets[jetIndex] == Jet.LEFT) {
                    if (canMoveLeft(coords)) coords = coords.map { it.x - 1.toBigInteger() by it.y }.toSet()
                } else {
                    if (canMoveRight(coords)) coords = coords.map { it.x + 1.toBigInteger() by it.y }.toSet()
                }

                jetIndex = (jetIndex + 1) % jets.size

                if (canMoveDown(coords)) coords = coords.map { it.x by it.y - 1.toBigInteger() }.toSet()
                else {
                    rocks.addAll(coords)
                    height = height.max(coords.maxOf { it.y })
                    break
                }
            }
        }

        fun canMoveLeft(coords: Set<Coord>): Boolean {
            return coords.map { it.x - 1.toBigInteger() by it.y }.all { !rocks.contains(it) && it.x > 0.toBigInteger() }
        }

        fun canMoveRight(coords: Set<Coord>): Boolean {
            return coords.map { it.x + 1.toBigInteger() by it.y }.all { !rocks.contains(it) && it.x < 8.toBigInteger() }
        }

        fun canMoveDown(coords: Set<Coord>): Boolean {
            return coords.map { it.x by it.y - 1.toBigInteger() }.all { !rocks.contains(it) && it.y > 0.toBigInteger() }
        }
    }

    private fun parse(input: String): List<Jet> {
        return input.toList().map { if (it == '>') Jet.RIGHT else Jet.LEFT }
    }

    override val defaultInput = """>>>><>>>><>>><<<<>>><<<>>>><>>>><<<<>>><<>>>><<<><><<<>>>><<>><>>>><<><<>><<>><><<>><>>><>>>><>>><>>>><<><><>>><<<><<<<>>><<<><<>>>><<>>><>>><<<>><<>>><>><<>><<<<><<<<>>><<>>><<<<><>>>><<>>>><>>><<>>><<>>><><<<<>><<><<<<>>><>>>><<<><<<<><<<>>><>>>><<>>>><>>>><><<<<>><<>>><<<>><<<<>><<<><<<<>><>>><<>>><<<<>>>><<<<>>>><<<<>><<>>><<<><<<>><>>>><<>><<<>>><<<<>>>><<<<>>><<<<>>><<<<><<>><>><<<<>>><>>>><<<<>><<<>><<<><<<<><<>>>><<<<>>>><<<>>>><<<<>><<>>><>><<<<>>><>>>><<<>>>><<<>><<<>><><<<><<>>><<<<>><<<<><<>>>><>>>><<<><><<>>>><<<>><<<>>>><<<>>><<<<><<><<>>><<<<><<<>><<<<>>>><<<<><<<<>><<>>>><<>>>><<<<>><<>><<<<>>>><<<><>><<<>>>><<<<><<<>><<<<>><<<>><<<<>>>><<<>><>>><<<<>>><>>>><<<>>>><>><<<>>><>>>><<<>>><>>><><<>>>><>>><<<<><<<<>><<>>>><<<<>>><<<>>>><>><<<><<<<>>>><<<<>>>><<<>>>><<>>><>>>><<<><<><<<<>>><<<<>>>><>>><<<<>><<><><<<><<>><<<<>>><<><<>><<><<<>>><<<>><<<<>><<<<><<<>>><>><>><<<<>>><<><<<<>>><>>>><>>>><<<<>>><<>><>>><<><>>><<<<>>><<<<><<<>>><<>><>>><<<<><<>>><<>><<<<><<>><<>>>><>>>><<>><<>>>><<<><><<<<><<<<>>><<<>>><<<>>><><<<<>><<<>>>><<<><<><<<<>>><<>>>><<><<>>>><<<>>>><<<><<<<>><<<<>>>><<<>>>><<<<><>><<<>>>><>><<<>>><<<<><<<<>>><<<<>><<>>><<<<><<<>>><<>><<<>>><<>><<>>>><<<>><>>><<<<>>><<<>>>><<<<>>>><<>>>><<<<><>>><<<>>><<<><<>><>>>><<><<><<<<><<>><<>><>>>><<<>><<<<>>>><<<>>>><<<><><<><<<<><<><<<<>>>><<<<><>>>><<>>><<<<>>>><<>><<>><<>>>><<<<>><<<>><><<<<>>><>>>><<<>><<<<><<<<><<<><>><<<>><<<<><<>>>><<<<>>><>>><<<<>>><<>>>><<<<>>>><<<<>><<<<>>><<<>>>><<<<>>><<<>><<<>>><<>>>><<<<><<>><<<><>><<<<>>><<<<><<<>><<<>>>><<<<>>><<<<><>><>>>><<<<>>><<<<>><><<>><><<<><<<<><<>><>><<>>><<<>><<>>>><<>>><<<<>><<<<>>>><<<>><<>><><<<<><<<<><<<>>>><<>><<>>><<<>>><<><<><<<>><<><<<<>><>>><<<><<>>>><<<<>><<>><<<>>>><<<<>>>><<><<>>><<>><<>>>><>><<<>><<>>>><<<><><<><<<<><<<><>>><><<><><<>><>><<>>>><<>>>><>>>><<<<>>><>>>><>>><<><>>>><<<<><<<<>><<><<<>>>><>><><>>><<<>>><>>><>><<>>><<<><<<>>><<<<>><<<>>><<>><<<>><<>>>><<<<>><<<><>>>><<<<>>>><<>>><<<<>>>><<<><<>>>><<>>><<<>>><<<>>><>>><>><>>><<<<>>><<>>><<>><<>>>><<<<>>><><<<>>>><><<<<><><<<<><<><<<>>>><<<>>><>><<<>>>><<<<>>><<<<>><<<<>>><>>>><<><>>>><<<<><>>><>>><<<<>>>><<<><>><<><<<>><<<<>><<<<><<<>>>><<>>><<><<<<>><>><>>><<<>>><<<><>>>><><<>>><<>>><<>>>><>><><<<<>><<>><<<<>><<<<>>><>><>><<<><<<<><<<>><<>>><<<<>>><<>>><<<<>>>><>>>><<<<><<<<>>>><<>>><><<><<>>>><<<<>>><<<<>>><<<>>>><<<>><<<<>>><>>>><>><<<>>>><>><<>><<><<>>><>>><<<>>>><><<<>>>><<<><>>>><<<>><<<<>>><<<><<>>><<<>><<<><>>>><<<>><<<>>>><<<<>>>><>><<<>>><<>>><<<>><<<<><>><<>>>><<>><<>><>><<<>>><>><<<<>>><<<<>>>><>><>>><<<>>>><<<<>><><<<><<<<>>><<>>>><<<><<<><<<>>>><<<><<<>><<<<>>><<<<><<<<>><<<<><<<>><>>>><<<>><>><<<><><<<>><<<>>><<<<>>>><<<>><<><>>>><><<<><><>><<>>><>><<<<>>>><<><>><<<<><<>>>><<<>>>><>>><<<>>><<<<><<<<>>><>><<<<>><><>>>><><><>>>><>><<>><<<<>>>><<<><>>><<>>>><>><<<<>>>><<<<><<>>>><>>>><<<<><<<>>><>>>><<>><<<<>><<<<>><><<><<<<><<><<<>><<>><>>><><<>><<<<>><<<<>>><<<>>><<<>>><<><<>><<<<>>>><<<>>><<<<>>>><<><<<<>>><<<<>>><<>>><<>>><<<>>><<<<>>>><>>><<<>>>><<<><><><<>><<>><<<<>>><>>><<<>><>>><<<>>><<<>>><<<<>><<<<>>>><<<>>><<<><<>>><<<>>>><<>><>><<>>>><<<<>>><<>><<><<<>>>><>><<<>>>><<<><<<<>>>><<<<>>><>>>><<<<>>><<>>><>>>><<>>>><<<><<>>>><<><<<><<>>><<<>><<<><<>><<<<>>>><<<<>>><>>>><<<>>><<<<>>><>>><<<<>><<<>>>><<<<><<>>>><<<>>><>><<<>>>><<<<>>>><<<><>><<<>><<<>><><<<><<>><<<>><<<>><>>><<<><>>>><<<>><>><>>><<<<>><<>>><<>>>><><<<<>>><<>><<<>>>><<>><<<<><>>>><<<><<><<><<<>>>><>>><>><<>>>><<>>><<><><<<<>><<>><<<<>>><>>><<<><<<<><>>>><>>>><<>>><<<><<<>>><<<<>><<<>>>><><><<<<><<>>>><>><<<<><<<<>><<>>>><<<>>><<>>>><<<<>>><>><>><<<<>><<<>>><<<<>>>><<<>>>><><<<>>><><>>>><<<<>>><<<><<><<<<>>>><<><<<><<<>>><<<<>>><<<<>>><>>>><<<<><>><<>>><<<>>>><<<><<><<<>>><><<<>>><<<<><><>>>><>><>><<<>><<<><>>>><>>><<<><>><<>>><<><<<<>>>><<<>><<<>><<<>>><<<>><>>>><<>><>>>><<>><>>>><<>>><<>>>><<>><>><>>><<<<>><<>>>><<<<>>><>>><>>><<<<>><<>>><<<<>>><<<<>>><<>>><<<><<<><><<<<>>><<<>>>><><<<><<<>><>>>><<<<>>>><<<>>><<><<>>>><<<><<>>><<<><<<>>><>><<<<><<<>><>><<>>><<>><<<><>>><<>>>><<>>><<<<><<<<>><<<>><>>><<<<><>><<<<>><<<<><<>><>>>><<<<><<<>>><<>>>><<<>><<<><<>>>><<<<>>><<<<>>><<<>>><>><<<<>>><<>>>><>><<>>><<><<<>>>><<<>>>><<>>><<<>>>><<<>>><<<>><<<><<>>><<<>><><<>>><<<><<><<<><<<>><>>>><<>><<><<<><<<<>>>><>>><<<><<>><<>>><<<><<<<>>>><<>>><>>>><<<<><<<<><<>><<>>><<<<>>><><><<<><<<>><<<>>><<>>>><<>>><<>>><<<>>><<><<<<>>><<<>>><><<<<>><<>>>><<<<>>><<>><<>>>><><<<>><>>><<<>><<<><<<>>><<<><<<>>><<<<>>>><<>><<<><>>>><<<<>>>><<<<>><>>>><<><<<>>>><<<>>><<>>><<<<><<<<>>><>>><<<>>><><<<><<>><<>><<<>>><<<>>>><<<>>>><<>>>><<>>><>><<<<>>>><<>><<<>>><<<>>><>>><<<<><<>>><>>>><<<<>>><>><<<<><<<<>>><<<>>>><>><<<<>><<<>>>><<<>>>><>>><<<>><<<>>>><<>>><<<>>>><<<<><<<>><<<><<>>><<<<>>><<<<><>>>><<>>><<><>>>><<>>>><<>>>><>>>><>>>><>>><<<<><<<>><<<><<<<>>>><<<<>><>><<>><<>><<<>>>><<><>>><<<<>>>><<>>><<>>><>>>><>><<<<>><<<<>><<>>><<><<<>>><<<<>><<<<><<>>><<<<><<><<>>><<>>><>>>><<>>>><<<<><>>>><<<<>>>><<<<>>>><<>>>><<<<>>><<>><<<>>><<<>>>><<<><<<<>>><>>><<>>>><><<<<>>><<<>><<<>>><<>>>><<>>><>><<<<>>>><<<<>>><>>><<<>><<>>>><<<>><<<><<<>><<<<>>><<<>><<<>><<<><><<<<>><<>>>><>>><<<>>>><<<>>><<>>><>><<<>>><>><<>>><<<<>><<<>>>><<<<><<<<>><>><<><<<>><<>>><>>><<<><<<<><<<<>>>><<>>>><<<<>>><><<<>>>><>>><<<>>>><>>><<<>>><>>><<<><<<><<<>>>><<<<>>>><>>>><>>><<<>>><<<>><<<<><>>><<<<>><><<<<><<<>>><<<>>>><>>><<>>>><<<>>><>>>><<>>><<<<>>>><>>>><<>>>><<>>><<<<>><<<<>>>><>>>><<<>>>><>>><<><<<><>>>><<<<><>>>><<<><<<><<<><>>><<<<>>>><<<<>>>><>>><<<>>><<>>><<<<>>><<<>><<><<<><><<<<>>>><<<<><<<>>>><<<<>><<<<>>>><<<><<<<>>><<><><<<<>>><<<>>><>>>><<<><<<>><<>>>><<<>><<<>>><<>><<>><<<>>><<><<>><>>><<<<>><><<<><<><><><<<<>>>><<<<><<<><<>>><><<>>>><>><<<<><<>>><>>>><<<<>>><><<>>>><<>>><<<<>>><<<<>>>><<<><<<<>>><<<>><<<<>>><<<><<<><<>><><<>><<<<>><>>>><<<<>><<<>><<<<>>><<<<>>><>><>><<<>><<<><<<<><>><<>><<<<>><<<<>>>><<><<><>>><<>>><<<<>>><<<>>><<><<>>>><<<>>><<<<>>>><<>><<<<>>>><<>><<<><<<>>><<>>><<<<>>><>>>><>>><>>><>>>><>>>><>>><<>><>>><<>><<><<<<>>><>>><<<>><<<<>>><>>>><<>><>><><<<><<<<>><<<>>><<<><<><<<<>><<<<><>>><<<>>><<>><>>><<><<<<>><<>><<<><<>><<>>>><<><<<>>>><<<<>><<<>>><><<>>><><<<<><<<>><<>><><<<<>>>><<>><<<>><<>>><<<<>>>><<<><<<<>>><<>><<>>>><<<<>><<>><<<<><<<>><>>><<<<>><<<<>>>><>>><>>>><<>>><>><>>>><><<<<>><<<<>><>>><<>>>><<<<>>>><>>>><<<><<<><<<<><<<<><<<<>>>><<>>><<>>>><<<<><>>>><>>><<<>>><<<<><<<<>>><<<><<<<>>>><<<<>>><><<>>><<>>><<>>>><<<><<<<>>>><<<>>><<<<>>><<<>>>><<>>>><<<><<<><<<><>><<<<>>>><<<<>>>><>><<<<><<<<><<<>>>><<<<><<>>><<<<>><>><<<><<<<>><<<><>>>><<<<>>><<<<>>>><<>>><<><<<>>>><<<<>>>><<>><<<<>>><<<<>>><<><<<<><<<<>><<<<>>><<<<>>><<<><<<>><<><<<<>>><><<<<><><<<<><<<>>>><<<<><<<<>><<<<>>>><<<<>>>><<>><><<<<>><<<<>>>><<<>><<<<><><<<<><>>>><<<>><>><<>><<<>>>><>>>><>>><<<>>>><><<<<>>>><<<>>><<><<<<>>><<<><<<<>>>><<<<>>>><>>><<><>>><>>><>><>>>><>>>><<<>>>><>>>><<><<>>><<<<><<<>><>>><<<>><<<>><<<>>>><<><<<>>><<<<>>>><<>>>><<<>>><<<<>><<>><>>>><<<><<<>>><>>><><<<<>>>><<<>>>><<>>>><<>><><<<<>>>><<><<<<>>>><<<>>>><><>>><<<<>>>><>>>><<>>><>><<<><<>>>><<><<<<><>>><<><<<>>>><<<>>><<<>><<<>><<>>>><<<<>>><<<<>>>><<>>>><<<<>><<<<>>><<<<><<<<>><<>><<>>>><<<<>><<>>><>>>><><<><>>><<><<<><>>><<><<>><<>>>><<<>><<>>><<<<>>><<<<><<<<><>>>><<>>>><<<<>>>><>>>><><<>><<<<>><<<>><<>><<<<><<>><<<><>><<<>>>><>>>><<<>>>><<<>>><><<<>>>><<<<>>>><<<>>>><<<>><<>>>><>><<<>><>><<<><<<<>>><<<>>>><<><>>>><>>><<>>><>>>><<<<>>>><<<>><<>><<<>><<<<>><<<><<>><<>>><<>>><<<><<>>>><<<>>><<>><><<<<>><<<<>><>>>><<>>>><><>><>>>><<<<>><<<<>><<<<>>>><<>>>><<>>>><<<><<>>><><<>><<<>><>>><<>>><>>><<><>>><<>><<<<>>><<<>><<<><<<>>><<<<>>><>><<<>>><<<><>>><<><<<<><<<<>><>>><>>>><>>>><>>>><<<>>><<<<>>>><>>><<>>><>>><<<<>><<<>><<<<>><<><<<>><<<>>><<<<>><>>>><>>><<<<>><<<<>><<<<>><>>>><>><<>>>><<>>><<<<>>><<<<><<<>>><<>>>><<>><<>><<<><<>>>><<<>>>><>>><><<>>>><<>>><>>><<>>><>>>><<<<>><<>>><<<>><<>><<>>><>><>><<<<>>>><<>><<<>>>><>><<<<><<<<>>><<<<>>>><<<>>><<>>><<<<>>><>><>>><<>>><>>><<<<><<><<><<><<<><<<>>>><><<<<>>>><<<<>>><>>>><>>><<<<>>><>><<<<><<<><>>>><<<>>><>>><<<<>>>><<><<<<><<><<>>><<>><<<<><<>>>><<<>>>><<<>>><><<>>>><<>>>><>>><<<>>><>>>><>><<<>>><><<<<>>><<>>><<<<>>><><>><<<>>><<><<<<>>><<>>>><>>><<<<>>>><<<<><<<>><<>>><<<>><<<<>><>>><<<>>><>>>><<<<><<>>><<>>><<<<>>>><<>>>><<>>>><<<<>><<<>>><><<<>>>><>>>><<<<><<<>>>><<<<>>><>><<>><<<<>><<<<><<>><>>>><<>><<<>>>><>>><<>>><<<<>><<<>>><<>><>>>><<<<>>><>><>>><<<<>>>><<<<><<<<>>>><<>>><<<<><>><<>>>><<<<><<<>><<<>><<<<><<<<><<><<>><>><<<><<><<>><>>><>>>><<<<>>><>>><>>>><<<>>>><<<<><<<<>>>><>>>><>>><<<<>>>><<<<>><<>><<<>>>><<<<><>><<<>>><<>><><<>>>><<>>>><>>>><><<<<>>>><<>><<<>><<<<>><<>>><<<><<<><>><><<><<<>><<>><<<>><>>>><>>><<>>><<<<>>>><<<<>>><<<<><>>>><<>>>><<><<<>>>><<<<>>><<<><<<>><>>>><<><<<<>>><<<>>><<<<>><>>>><<>>><<><>><<<>>>><<<><<<<><<<>>>><<<<>><<>>>><>>><<>>><<>>>><<<<><<>>>><<<<>>>><<<<><<>>>><<<<>>>><<>>><<<>><<>>><<<<>>><>>><<><<<<><<<<>><<<>>>><>><<<>>><<<>><<<><<<><>>><<<>>>><<<><<<><<>>><>><<<>>><<<>>>><<<>>>><>>>><<<>>><<<<>>>><<><<><<<<>><<<><>>>><<>>><<>><<<<>><<<><<<>><<<>>>><<<><>>><<<><<<<>>><<>>>><<><>><<>>>><<<<><<<><<<<><<<>><<><>><>><<>>>><>>>><<<><>>><<>><<<<>><><<<>>><<>>><<>>>><<<>><<>>>><><<<<>><><<<<>>><<>><>>><<>>>><<<>>><<<<>>><>>>><<<<><<<<>>><<<><<>>><><<<><<>><>>><>><<<<>>>><>>><>>>><<<<>>>><<<>><<<<>><>>><<<>>>><>>>><<>>><<>>>><<<<>>>><<<>>>><>>>><>><<<<>>>><>><<<<>>>><<<<>>><<<>><<<>>>><<<><<<<><<>>><<<>>>><<>><<>>>><<>>><<<>>>><<>>>><<>>>><>>>><><<<>><<><<><>>><><<<>>>><>>><><<>>><<<<>>><<>>>><<<>>><<<<><<<><>><<<<>><<<>><<<>><<>>><<<><<<>><<>><<><<><<<<>>><<>>><<<<>>><>>><<<>>><<>>><<<>><><<<<>>>><>><<<>><<<>>>><<>><<>>><>>><<<<>>>><<>>><><<<>>><>>><>><<<<>>>><>>><>>><<>>>><<>><><<>><<<<><<>>>><<<>>><>>>><<<<>><<<<><<<>><<>>>><>>><<>>>><<>><<>><<<>>><<<><<<<>>><>>>><<<>>>><<<<>>><<>>>><<>>>><<<<>><<<<>><<<><>>><<>><<>>>><>><<<<>><>>>><<<>>><<<>>><<>>>><<<>>>><>><<<><<<<>><>>>><>>><<<<>>><<<>>><<<><<<<>>>><<<<><<>>><>>>><>>><>><>><<<<>>><<>>>><<<><<<<><<<>><<<<>><>><<<>><<<<>>>><><<<><<<><<>>>><<<>><<>>>><><>>><>><<<<>>>><<>>>><<<<>>>><<>>><<<<>>>><<<>>><<<>><><<>><><<><>><<>>>><<<<><<<<>>><<<<>><<>><<<<><<<"""
}

private infix fun BigInteger.by(y: BigInteger): Coord {
    return Coord(this, y)
}

private data class Coord(val x: BigInteger, val y: BigInteger)