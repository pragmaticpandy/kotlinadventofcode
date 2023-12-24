// Originally generated by the template in CodeDAO
package kotlinadventofcode.`2023`

import com.github.h0tk3y.betterParse.combinators.*
import com.github.h0tk3y.betterParse.grammar.*
import com.github.h0tk3y.betterParse.lexer.*
import kotlinadventofcode.Day
import kotlinadventofcode.UI.UI

class `2023-23` : Day {
    override val year: Int = 2023
    override val day: Int = 23

    context(UI)
    override fun runPartOne(input: String): String {
        return input.toGrid().getLongestPathToFinish().toString()
    }

    context(UI)
    override fun runPartTwo(input: String): String {
        return input.toGrid().getLongestPathWithReducedGraph().toString()
    }

    companion object {

        data class Grid(val grid: List<List<Tile>>) {
            val height = grid.size
            val width = grid[0].size

            data class Coord(val x: Int, val y: Int)

            val Coord.tile get() = grid[y][x]

            context(UI)
            fun getLongestPathToFinish(): Int {
                val start = Coord(1, 0)
                val end = Coord(width - 2, height - 1)
                val paths: MutableList<List<Coord>> = mutableListOf(listOf(start))
                var longestLengthToEnd = -1
                val numNonForest = grid.flatten().count { it != Tile.FOREST }
                show("grid size", "$width by $height")
                show("num non-forest", numNonForest)

                while (paths.isNotEmpty()) {
                    show("paths in queue", paths.size)
                    val path = paths.removeLast()
                    show("current path length", path.size)
                    if (path.last() == end) {
                        longestLengthToEnd = maxOf(longestLengthToEnd, path.size - 1)
                        show("longest length to end", "$longestLengthToEnd")
                    } else {
                        paths.addAll(path.getNextPaths())
                    }
                }

                return longestLengthToEnd
            }

            fun List<Coord>.getNextPaths(): List<List<Coord>> {
                if (last().tile == Tile.UP_SLOPE) return listOf(this + Coord(last().x, last().y - 1))
                if (last().tile == Tile.DOWN_SLOPE) return listOf(this + Coord(last().x, last().y + 1))
                if (last().tile == Tile.LEFT_SLOPE) return listOf(this + Coord(last().x - 1, last().y))
                if (last().tile == Tile.RIGHT_SLOPE) return listOf(this + Coord(last().x + 1, last().y))

                val seen = this.toSet()
                return this.last().adjacent()
                    .filter { it !in seen }
                    .filter {
                        when(it.tile) {
                            Tile.UP_SLOPE -> Coord(it.x, it.y - 1) !in seen
                            Tile.DOWN_SLOPE -> Coord(it.x, it.y + 1) !in seen
                            Tile.LEFT_SLOPE -> Coord(it.x - 1, it.y) !in seen
                            Tile.RIGHT_SLOPE -> Coord(it.x + 1, it.y) !in seen
                            Tile.FOREST -> false
                            Tile.PATH -> true
                        }
                    }
                    .map { this + it }
            }

            fun Coord.adjacent(): List<Coord> {
                val result = listOf(
                    Coord(x - 1, y),
                    Coord(x + 1, y),
                    Coord(x, y - 1),
                    Coord(x, y + 1)
                )
                    .filter { it.x in 0 until width && it.y in 0 until height }
                    .filter { it.tile != Tile.FOREST }

                return result
            }

            context(UI)
            fun getLongestPathWithReducedGraph() : Int {
                data class Node(val location: Coord)
                val start = Coord(1, 0)
                val startNode = Node(start)
                val end = Coord(width - 2, height - 1)
                val endNode = Node(end)

                val nodes = mutableListOf(startNode, endNode)
                for (x in 0 until width) {
                    for (y in 0 until height) {
                        val coord = Coord(x, y)
                        if (coord.tile == Tile.FOREST) continue
                        if (coord.adjacent().size < 3) continue
                        nodes.add(Node(coord))
                    }
                }

                val nodesByLocation = nodes.associateBy { it.location }
                val edges: MutableMap<Node, MutableMap<Node, Int>> = mutableMapOf()
                for (node in nodes) {
                    node.location.adjacent().forEach { coordAdjacentToNode ->
                        var length = 1
                        var previous = node.location
                        var current = coordAdjacentToNode
                        while (current !in nodesByLocation) {
                            val next = current.adjacent().filter { it != previous }[0]
                            previous = current
                            current = next
                            length++
                        }

                        val adjacentNode = nodesByLocation[current]!!
                        edges.getOrPut(node) { mutableMapOf() }[adjacentNode] = length
                    }
                }

                show("num nodes", nodes.size)
                show("num edges", edges.values.sumOf { it.size } / 2)

                fun List<Node>.getNextPaths(): List<List<Node>> {
                    return edges[this.last()]!!
                        .filter { (node, _) -> node !in this }
                        .map { (node, _) -> this + node }
                }

                fun List<Node>.pathLength(): Int {
                    return windowed(2).sumOf { (from, to) -> edges[from]!![to]!! }
                }

                val paths: MutableList<List<Node>> = mutableListOf(listOf(startNode))
                var longestLengthToEnd = -1
                while (paths.isNotEmpty()) {
                    show("paths in queue", paths.size)
                    val path = paths.removeLast()
                    show("current path length", path.size)
                    if (path.last() == endNode) {
                        longestLengthToEnd = maxOf(longestLengthToEnd, path.pathLength())
                        show("longest length to end", "$longestLengthToEnd")
                    } else {
                        paths.addAll(path.getNextPaths())
                    }
                }

                return longestLengthToEnd
            }
        }

        enum class Tile {
            PATH, FOREST, UP_SLOPE, DOWN_SLOPE, RIGHT_SLOPE, LEFT_SLOPE
        }

        fun String.toGrid() : Grid {
            val grammar = object : Grammar<Grid>() {

                // tokens
                val newlineLit by literalToken("\n")
                val pathLit by literalToken(".")
                val forestLit by literalToken("#")
                val upSlopeLit by literalToken("^")
                val downSlopeLit by literalToken("v")
                val rightSlopeLit by literalToken(">")
                val leftSlopeLit by literalToken("<")

                // parsers
                val path by pathLit use { Tile.PATH }
                val forest by forestLit use { Tile.FOREST }
                val upSlope by upSlopeLit use { Tile.UP_SLOPE }
                val downSlope by downSlopeLit use { Tile.DOWN_SLOPE }
                val rightSlope by rightSlopeLit use { Tile.RIGHT_SLOPE }
                val leftSlope by leftSlopeLit use { Tile.LEFT_SLOPE }
                val tile by path or forest or upSlope or downSlope or rightSlope or leftSlope
                val row by oneOrMore(tile)
                override val rootParser by separatedTerms(row, newlineLit) map { Grid(it) }
            }

            return grammar.parseToEnd(this)
        }
    }

    override val defaultInput = """#.###########################################################################################################################################
#.........#...###.....#...#...###...#...#.....#.....#...#...###.......#...#...#...###.......###...#...#...#...#.....#####...........#...#...#
#########.#.#.###.###.#.#.#.#.###.#.#.#.#.###.#.###.#.#.#.#.###.#####.#.#.#.#.#.#.###.#####.###.#.#.#.#.#.#.#.#.###.#####.#########.#.#.#.#.#
#.........#.#.....#...#.#.#.#...#.#.#.#.#...#.#.#...#.#.#.#...#.....#.#.#.#.#...#...#.....#.....#.#.#.#.#.#.#.#.#...#.....#.........#.#.#.#.#
#.#########.#######.###.#.#.###.#.#.#.#.###.#.#.#.###.#.#.###.#####.#.#.#.#.#######.#####.#######.#.#.#.#.#.#.#.#.###.#####.#########.#.#.#.#
#.....#...#...#...#.###.#.#.#...#.#...#.....#.#.#...#.#.#.#...#.....#.#.#.#.....#...#...#...#.....#.#.#.#.#.#.#.#...#.....#...........#...#.#
#####.#.#.###.#.#.#.###.#.#.#.###.###########.#.###.#.#.#.#.###.#####.#.#.#####.#.###.#.###.#.#####.#.#.#.#.#.#.###.#####.#################.#
###...#.#...#...#.#.#...#...#...#.........#...#.#...#.#.#.#.###.....#.#.#...#...#...#.#.###.#...#...#.#.#...#...#...#...#.#.................#
###.###.###.#####.#.#.#########.#########.#.###.#.###.#.#.#.#######.#.#.###.#.#####.#.#.###.###.#.###.#.#########.###.#.#.#.#################
#...#...###...#...#...#.........#.......#.#...#.#...#.#.#.#.#...###.#...#...#.....#...#.>.>.#...#.#...#.#.........###.#...#...###...#.......#
#.###.#######.#.#######.#########.#####.#.###.#.###.#.#.#.#.#.#.###.#####.#######.#######v###.###.#.###.#.###########.#######.###.#.#.#####.#
#.....#...#...#.#.......#...#.....#.....#...#.#.#...#.#.#.#...#.#...#...#.#...###.......#...#...#.#.#...#...........#.......#...#.#.#.#.....#
#######.#.#.###.#.#######.#.#.#####.#######.#.#.#.###.#.#.#####.#.###.#.#.#.#.#########.###.###.#.#.#.#############.#######.###.#.#.#.#.#####
#.....#.#.#.....#.......#.#.#.....#.#...###.#...#...#.#.#.#...#.#.#...#.#.#.#...#.......#...###.#.#.#.#.............#...###...#...#...#.....#
#.###.#.#.#############.#.#.#####.#.#.#.###.#######.#.#.#.#.#.#.#.#.###.#.#.###.#.#######.#####.#.#.#.#.#############.#.#####.#############.#
#...#...#...#...#.....#.#.#.#.....#.#.#...#.....#...#.#.#.#.#...#...#...#.#.###.#.###...#.....#.#.#.#.#...#.>.>.###...#.#.....#.......#.....#
###.#######.#.#.#.###.#.#.#.#.#####.#.###.#####.#.###.#.#.#.#########.###.#.###.#.###.#.#####.#.#.#.#.###.#.#v#.###.###.#.#####.#####.#.#####
###.....#...#.#.#...#...#.#.#.....#.#.#...###...#.#...#.#.#...#.>.>...#...#...#.#.#...#.......#.#.#.#...#...#.#...#...#.#.....#.#...#...#...#
#######.#.###.#.###v#####.#.#####.#.#.#.#####.###.#.###.#.###.#.#v#####.#####.#.#.#.###########.#.#.###.#####.###.###.#.#####.#.#.#.#####.#.#
#.......#...#.#.###.>...#.#...#...#...#.>.>.#.#...#.#...#...#...#...###.....#.#...#...........#.#.#.....###...###...#.#...#...#...#.#.....#.#
#.#########.#.#.###v###.#.###.#.#########v#.#.#.###.#.#####.#######.#######.#.###############.#.#.#########.#######.#.###.#.#######.#.#####.#
#.........#...#...#...#.#.#...#.#.....#...#.#.#.#...#...#...###...#.......#...###.......#.....#.#.#.........#...###.#.#...#...#...#...#.....#
#########.#######.###.#.#.#.###.#.###.#.###.#.#.#.#####.#.#####.#.#######.#######.#####.#.#####.#.#.#########.#.###.#.#.#####.#.#.#####.#####
#.........#.....#.....#...#...#.#...#.#...#...#.#.....#.#.#.....#.#...#...###...#.....#.#...###...#...........#...#...#.......#.#.......#...#
#.#########.###.#############.#.###.#.###.#####.#####.#.#.#.#####.#.#.#.#####.#.#####.#.###.#####################.#############.#########.#.#
#.....#.....#...#.......#...#.#.#...#.....#.....#.....#.#.#.#...#...#...#.....#.###...#...#.....#.................#...#...#...#.........#.#.#
#####.#.#####.###.#####.#.#.#.#.#.#########.#####.#####.#.#.#.#.#########.#####.###.#####.#####.#.#################.#.#.#.#.#.#########.#.#.#
#.....#.#.....###...#...#.#.#.#.#.........#.....#...###...#...#.........#.....#.....#...#.......#...............###.#.#.#.#.#.#...#.....#.#.#
#.#####.#v#########.#.###.#.#.#.#########.#####.###.###################.#####.#######.#.#######################.###.#.#.#.#.#.#.#.#v#####.#.#
#.......#.>.#.....#.#...#.#.#...#...#...#.#.....#...#...#...#...........#.....#.....#.#.....#...................#...#...#.#.#.#.#.>.#.....#.#
#########v#.#.###.#.###.#.#.#####.#.#.#.#.#.#####.###.#.#.#.#.###########.#####.###.#.#####.#.###################.#######.#.#.#.###v#.#####.#
#.........#...#...#.#...#.#.###...#.#.#...#.......###.#...#.#.......#...#.....#.###...#...#.#...........#.......#.......#...#...###...#.....#
#.#############.###.#.###.#.###.###.#.###############.#####.#######.#.#.#####.#.#######.#.#.###########.#.#####.#######.###############.#####
#...........#...#...#...#.#.....###...#...###.......#.#...#.........#.#.#...#...###.....#...#...#.....#...#.....#.......#...............#...#
###########.#.###.#####.#.#############.#.###.#####.#.#.#.###########.#.#.#.#######.#########.#.#.###.#####.#####.#######.###############.#.#
#...#####...#.....###...#...#...#.....#.#...#.....#.#...#...#.........#.#.#.........###...###.#.#.#...#...#.....#.....###.......#.........#.#
#.#.#####.###########.#####.#.#.#.###.#.###.#####.#.#######.#.#########.#.#############.#.###.#.#.#.###.#.#####.#####.#########.#.#########.#
#.#.......#...###...#...###...#.#.#...#...#.#...#.#...#...#...#.........#.............#.#...#.#...#.#...#...#...#...#.....###...#.#.........#
#.#########.#.###.#.###.#######v#.#.#####.#.#.#.#.###.#.#.#####.#####################.#.###.#.#####.#.#####.#.###.#.#####.###.###.#.#########
#.#.......#.#.....#...#.#...#.>.>.#.#...#.#.#.#.#...#.#.#.#...#.#.......#...#...#.....#...#.#...#...#.#.....#...#.#...#...#...#...#...#.....#
#.#.#####.#.#########.#.#.#.#.#v###.#.#.#.#.#.#.###.#.#.#.#.#.#v#.#####.#.#.#.#.#.#######.#.###.#.###.#.#######v#.###.#.###.###.#####.#.###.#
#.#.###...#.#.........#...#...#...#.#.#.#.#...#.#...#.#.#.#.#.>.>.#.....#.#.#.#.#...#...#.#.....#.#...#...#...>.>.###...###...#.#.....#.#...#
#.#.###.###.#.###################.#.#.#.#.#####.#.###.#.#.#.###v###.#####.#.#.#.###v#.#.#.#######.#.#####.#.###v#############.#.#.#####.#.###
#.#...#...#.#.#...........#.......#.#.#.#.....#.#...#.#.#...###.###.....#.#...#.#.>.>.#.#...#.....#.....#.#.###.............#...#.#...#.#.###
#.###.###.#.#.#.#########.#.#######.#.#.#####.#.###.#.#.#######.#######.#.#####.#.#v###.###.#.#########.#.#.###############.#####.#.#.#.#.###
#.....###...#...#.........#.#...###.#.#.#...#.#.#...#...###.....#.....#.#...###...#...#.#...#.....#...#.#...#.....#...#...#.....#...#...#...#
#################.#########.#.#.###.#.#.#.#.#.#.#.#########.#####.###.#.###.#########.#.#.#######.#.#.#.#####.###.#.#.#.#.#####.###########.#
###...#.........#.......###...#...#...#.#.#...#...#####...#.....#.#...#...#...#.......#...###.....#.#.#.###...###.#.#.#.#.#...#.#...........#
###.#.#.#######.#######.#########.#####.#.#############.#.#####.#.#.#####.###.#.#############.#####.#.#.###.#####.#.#.#.#.#.#.#.#.###########
###.#.#.......#...#...#.#.........#...#...###...#...#...#.#.....#.#.#...#.....#.#.......#...#...#...#...#...#.....#.#.#.#...#...#.........###
###.#.#######.###.#.#.#.#.#########.#.#######.#.#.#.#.###.#.#####.#.#.#.#######.#.#####.#.#.###.#.#######.###.#####.#.#.#################.###
#...#...#...#...#...#...#...#.......#.###...#.#.#.#.#...#.#.......#.#.#.###...#...#.....#.#.#...#...###...###.......#...###...#...#.....#...#
#.#####.#.#.###.###########.#.#######.###.#.#.#.#.#.###.#.#########v#.#.###.#.#####.#####.#.#.#####.###.###################.#.#.#.#.###.###.#
#.#...#...#.....#...#...###...#.......#...#.#.#.#.#...#.#.......#.>.>.#...#.#.#...#.#...#.#.#.......#...###...#...#...#.....#.#.#.#.#...#...#
#.#.#.###########.#.#.#.#######.#######.###.#.#.#.###.#.#######.#.#v#####.#.#.#.#.#.#.#.#.#.#########.#####.#.#.#.#.#.#.#####.#.#.#.#.###.###
#.#.#.#...#.....#.#...#.....#...#.....#...#.#.#.#...#.#.#.......#.#.#.....#.#.#.#.#.#.#.#.#.#...#...#.......#.#.#...#.#.....#...#.#.#.#...###
#.#.#.#.#.#.###.#.#########.#.###.###.###.#.#.#.###.#.#.#.#######.#.#.#####.#.#.#.#v#.#.#.#.#.#.#.#.#########.#.#####.#####.#####.#.#.#.#####
#...#...#...###.#.#.........#...#...#...#.#...#.#...#.#.#.......#.#.#...#...#.#.#.>.>.#...#.#.#.#.#.#.........#...#...#...#.#.....#.#.#...###
###############.#.#v###########.###.###.#.#####.#.###.#.#######.#.#.###.#.###.#.###v#######.#.#.#.#.#v###########.#.###.#.#.#.#####.#.###.###
###...#...#...#...#.>...#.....#...#...#.#...#...#...#.#.#.....#...#...#.#.###.#.###...#.....#.#.#.#.>.>.#.....#...#.#...#.#.#.#.....#...#...#
###.#.#.#.#.#.#####v###.#.###.###.###.#.###.#.#####.#.#.#.###.#######.#.#.###.#.#####.#.#####.#.#.###v#.#.###.#.###.#.###.#.#.#.#######.###.#
#...#...#...#...###.#...#.#...#...#...#...#.#...#...#.#.#...#.#...#...#.#.#...#.#...#.#.....#.#...#...#...#...#.#...#.#...#.#.#.....#...#...#
#.#############.###.#.###.#.###v###.#####.#.###.#.###.#.###.#.#.#.#.###.#.#.###.#.#.#.#####.#.#####.#######.###.#.###.#.###.#.#####.#.###v###
#...#.........#...#.#.....#...>.>.#.....#.#.#...#.#...#.#...#.#.#.#...#...#.....#.#...#...#...#...#.......#.#...#...#.#.#...#...#...#...>.###
###.#.#######.###.#.###########v#.#####.#.#.#.###.#.###.#.###.#.#.###.###########.#####.#.#####.#.#######.#.#.#####.#.#.#.#####.#.#######v###
###...#.......#...#...#.........#.......#...#.#...#...#.#...#...#.#...###.....###.......#.....#.#.........#.#...#...#.#.#.....#.#.#.......###
#######.#######.#####.#.#####################.#.#####.#.###.#####.#.#####.###.###############.#.###########.###.#.###.#.#####.#.#.#.#########
#.....#.......#...#...#...#.........#.......#...#...#...###.....#.#.....#...#.................#.....#.....#.#...#...#.#.#.....#.#.#.........#
#.###.#######.###.#.#####.#.#######.#.#####.#####.#.###########.#.#####.###.#######################.#.###.#.#.#####.#.#.#.#####.#.#########.#
#...#.#...#...#...#.....#...#.......#.#.....#...#.#.#...#.......#.......###.................#...###.#.#...#...#...#...#.#.....#...###.......#
###.#.#.#.#.###.#######.#####.#######.#.#####.#.#.#.#.#.#.#################################.#.#.###.#.#.#######.#.#####.#####.#######.#######
#...#...#...###.........#...#.........#.#...#.#...#.#.#.#...............###...#.............#.#.###...#.........#.....#.#...#.....#...#.....#
#.#######################.#.###########.#.#.#.#####.#.#.###############.###.#.#.#############.#.#####################.#.#.#.#####.#.###.###.#
#.#...#.......#.......###.#.............#.#...#.....#.#.........#.....#.#...#.#.#...........#.#.....#...#.......#.....#.#.#.......#.....#...#
#.#.#.#.#####.#.#####.###.###############.#####.#####.#########.#.###.#.#.###.#.#.#########.#.#####.#.#.#.#####.#.#####.#.###############.###
#...#...###...#.....#...#.#...#.........#.#...#.....#.........#...###...#...#.#...#.........#.#.....#.#.#...###...#...#...###.....#...#...###
###########v#######.###.#.#.#.#.#######.#.#.#.#####.#########.#############.#.#####.#########.#.#####.#.###v#######.#.#######.###.#.#.#.#####
#.......###.>...###...#.#...#...#...###...#.#.......#.........#...#...#...#.#.#...#...###...#.#.#...#.#.#.>.>.#...#.#.###...#.#...#.#.#.....#
#.#####.###v###.#####.#.#########.#.#######v#########.#########.#.#.#.#.#.#.#.#.#.###v###.#.#.#.#.#.#.#.#.#v#.#.#.#.#.###.#.#.#.###.#.#####.#
#.....#.....###.#...#.#...#.....#.#.#.....>.>.#...###...........#.#.#...#.#.#...#...>.>...#.#.#.#.#.#.#.#.#.#...#.#.#...#.#.#.#...#.#.#.....#
#####.#########.#.#.#.###.#.###.#.#.#.#####v#.#.#.###############.#.#####.#.#########v#####.#.#.#.#.#.#.#.#.#####.#.###.#.#.#.###.#.#.#.#####
#.....#...#...#...#.#...#...###.#.#.#.###...#...#...#...#.......#.#.....#...#...#...#.#.....#.#...#...#.#.#...###...#...#.#.#.#...#.#.#...###
#.#####.#.#.#.#####.###.#######.#.#.#.###.#########.#.#.#.#####.#.#####.#####.#.#.#.#.#.#####.#########.#.###.#######.###.#.#.#.###.#.###v###
#.......#...#.....#...#.......#...#...#...#.........#.#.#.#...#...#...#...#...#.#.#.#.#.....#.........#...###...#.....#...#.#.#...#.#...>.###
#################.###.#######.#########.###.#########.#.#.#.#.#####.#.###.#.###.#.#.#.#####.#########.#########.#.#####.###.#.###.#.#####v###
#.................###...#...#.#...#.....###.....###...#.#.#.#...###.#.#...#...#...#.#...#...#...#.....#.........#.#...#.###.#...#...#.....###
#.#####################.#.#.#.#.#.#.###########.###.###.#.#.###.###.#.#.#####.#####.###.#.###.#.#.#####.#########.#.#.#.###.###.#####.#######
#...#.................#...#...#.#...#.......###...#...#.#...#...#...#.#.#.....#...#.....#...#.#.#.#...#.........#.#.#.#.###.....#...#.......#
###.#.###############.#########.#####.#####.#####.###.#.#####v###.###.#.#.#####.#.#########.#.#.#.#.#.#########.#.#.#.#.#########.#.#######.#
#...#.#...............###...#...#...#.#.....#...#.#...#...#.>.>...###.#.#.......#.......#...#.#...#.#...........#.#.#.#.#.........#.......#.#
#.###.#.#################.#.#.###.#.#.#.#####.#.#.#.#####.#.#v#######.#.###############.#.###.#####.#############.#.#.#.#.###############.#.#
#.#...#.................#.#.#.....#...#.......#.#.#.###...#.#...#...#...#...#...#.....#.#...#.#.....#.....#...###...#.#.#...............#...#
#.#.###################.#.#.###################.#.#.###.###.###.#.#.#####.#.#.#.#.###.#.###.#.#.#####.###.#.#.#######.#.###############.#####
#.#.#...........#.......#.#...#...#.............#.#.#...#...###...#.....#.#...#...###.#...#...#...#...###...#.......#...#...#...###...#.....#
#.#.#.#########.#.#######.###.#.#.#.#############.#.#.###.#############.#.###########.###.#######.#.###############.#####.#.#.#.###.#.#####.#
#...#.....#...#...#.....#.#...#.#.#.....#...#####...#.#...#...#.........#.#.....#...#.....#.....#.#.#...#...........###...#...#.....#.......#
#########.#.#.#####.###.#.#.###.#.#####.#.#.#########.#.###.#.#.#########.#.###.#.#.#######.###.#.#.#.#.#.#############.#####################
###...###...#...###...#...#...#.#.......#.#.........#...#...#...#...#...#...#...#.#...#...#.#...#...#.#...#.......#...#...#...#...#.........#
###.#.#########.#####.#######.#.#########.#########.#####.#######.#.#.#.#####v###.###.#.#.#.#.#######.#####.#####.#.#.###.#.#.#.#.#.#######.#
#...#...........#.....#.......#...........#.........#...#.......#.#...#.#...>.>...#...#.#.#.#.###...#.#.....#.....#.#...#...#.#.#.#.#.....#.#
#.###############.#####.###################.#########.#.#######.#.#####.#.###v#####.###.#.#.#.###.#.#.#.#####.#####.###.#####.#.#.#.#.###.#.#
#...............#.....#.#...#...#...###.....#...#.....#.......#...#.....#...#.#.....###.#.#.#.###.#.#.#.#...#.......#...#...#...#...#...#...#
###############.#####.#.#.#.#.#.#.#.###v#####.#.#.###########.#####.#######.#.#.#######.#.#.#.###.#.#.#.#.#.#########.###.#.###########.#####
###...#.........#.....#.#.#.#.#.#.#...>.>...#.#.#.........#...#.....#...#...#.#.......#.#...#.....#.#...#.#.......#...#...#.#.....#...#.....#
###.#.#.#########.#####.#.#.#.#.#.#####v###.#.#.#########.#.###.#####.#.#.###.#######.#.###########.#####.#######.#.###.###.#.###.#.#.#####.#
#...#.#.......###.....#.#.#...#...###...#...#.#...#...#...#...#.....#.#.#.#...#.......#.......#.....#...#.....###...#...#...#...#...#.....#.#
#.###.#######v#######.#.#.###########.###.###.###.#.#.#.#####.#####.#.#.#.#.###.#############.#.#####.#.#####v#######.###.#####.#########.#.#
#...#.#...###.>.......#.#.#.........#...#...#...#.#.#.#.#.....#...#.#.#...#...#.#...#...#...#.#.#...#.#...#.>.>.#...#...#.#...#.........#...#
###.#.#.#.###v#########.#.#.#######.###.###.###.#.#.#.#.#.#####.#.#v#.#######.#.#.#.#.#.#.#.#.#.#.#.#.###.#.#v#.#.#.###.#.#.#.#########v#####
#...#...#...#.........#...#...#...#.#...###...#.#.#.#.#.#.#...#.#.>.>.#.......#.#.#...#.#.#...#.#.#.#.#...#.#.#...#...#.#.#.#.#.....#.>.#...#
#.#########.#########.#######.#.#.#.#.#######.#.#.#.#.#.#.#.#.#.###v###.#######.#.#####.#.#####.#.#.#.#.###.#.#######.#.#.#.#.#.###.#.#v#.#.#
#.........#...#.......###.....#.#.#...###...#...#.#.#...#.#.#.#.###...#.......#.#.....#.#.....#.#.#...#.....#...#...#...#.#.#.#...#...#...#.#
#########.###.#.#########.#####.#.#######.#.#####.#.#####.#.#.#.#####.#######.#.#####.#.#####.#.#.#############.#.#.#####.#.#.###.#########.#
#.........#...#.........#.......#...#...#.#.......#.#.....#.#.#.#.....###.....#...#...#.....#.#.#.#...........#...#...#...#.#.#...#.........#
#.#########.###########.###########.#.#.#.#########.#.#####.#.#.#.#######.#######.#.#######.#.#.#.#.#########.#######.#.###.#.#.###.#########
#.........#.....#.......#...........#.#.#.......#...#.#...#.#...#.......#...###...#.......#.#.#...#.....#...#.....#...#.....#.#.###.....#...#
#########.#####.#.#######.###########.#.#######.#.###.#.#.#.###########.###.###.#########.#.#.#########.#.#.#####.#.#########.#.#######.#.#.#
###...###.....#...#...###.............#...#.....#...#.#.#.#.#.........#.###...#.#.........#...#.....###...#.....#.#.#.......#.#.#.......#.#.#
###.#.#######.#####.#.###################.#.#######.#.#.#.#.#.#######.#.#####.#.#.#############.###.###########.#.#.#.#####.#.#.#.#######.#.#
#...#.........###...#...#.......#...#.....#.........#...#...#.......#...#.....#...#...#...#...#.#...#...........#.#...#.....#.#.#.#.......#.#
#.###############.#####.#.#####.#.#.#.#############################.#####.#########.#.#.#.#.#.#.#.###.###########.#####.#####.#.#.#.#######.#
#.#...#...###...#.#.....#.#...#...#...#####.........###...#...#.....#...#...###...#.#.#.#.#.#.#.#...#...........#.......#####...#...#.......#
#.#.#.#.#.###.#.#.#.#####.#.#.#############.#######.###.#.#.#.#.#####.#.###.###.#.#.#.#.#.#.#.#.###.###########.#####################.#######
#...#...#.....#...#.....#...#.............#.......#...#.#.#.#.#.......#...#...#.#.#.#.#.#.#.#.#.#...#...#...###.###...#...#...#.....#.......#
#######################.#################.#######.###.#.#.#.#.###########.###.#.#.#.#.#.#.#.#.#.#.###.#.#.#.###v###.#.#.#.#.#.#.###.#######.#
#.............#...#.....###...............#...#...#...#.#...#.....#.....#.#...#.#.#.#.#.#.#.#.#.#...#.#.#.#.#.>.>.#.#.#.#.#.#.#...#.#...#...#
#.###########.#.#.#.#######.###############.#.#.###.###.#########.#.###.#.#.###.#.#.#.#.#.#.#.#.###.#.#.#.#.#.###.#.#.#.#.#.#.###.#.#.#.#v###
#...........#...#...#.....#...............#.#.#...#...#.........#.#...#...#.#...#.#.#.#.#.#.#.#.#...#.#...#.#.#...#.#.#.#.#.#...#.#...#.>.###
###########.#########.###.###############.#.#.###.###.#########.#.###.#####v#.###.#.#.#.#.#.#.#.#.###.#####.#.#.###.#.#.#.#.###.#.#######v###
#...........#...#...#.#...#...###.........#.#...#...#.#.....#...#.#...#...>.>.#...#.#.#.#.#.#...#...#...#...#.#.....#...#.#...#.#.#.......###
#.###########.#.#.#.#.#.###.#.###.#########.###.###.#.#.###.#.###.#.###.#######.###.#.#.#.#.#######.###.#.###.###########.###.#.#.#.#########
#...#.....#...#...#.#.#...#.#.#...#...#...#...#.###.#.#...#.#...#.#.###.......#...#.#...#.#.#.....#.#...#...#.....#####...#...#.#.#.........#
###.#.###.#.#######.#.###.#.#.#.###.#.#.#.###.#.###.#.###.#.###.#.#v#########.###.#.#####.#.#.###.#.#.#####.#####.#####.###.###.#.#########.#
#...#.#...#.#.......#.#...#.#.#...#.#.#.#.#...#...#.#...#.#.#...#.>.>.#.....#...#.#.#...#...#...#...#.###...#.....#.....#...###.#.#.....###.#
#.###.#.###.#.#######.#.###.#.###v#.#.#.#.#.#####.#.###.#.#.#.#######.#.###.###.#.#.#.#.#######.#####.###.###.#####.#####.#####.#.#.###.###.#
#...#.#...#.#.......#.#.#...#.#.>.>.#.#.#.#...#...#...#.#.#.#.......#.#...#...#.#.#...#.......#.#...#...#...#...#...#...#.#...#...#...#.....#
###.#.###.#.#######.#.#.#.###.#.#####.#.#.###.#.#####.#.#.#.#######.#.###.###.#.#.###########.#.#.#.###.###.###.#.###.#.#.#.#.#######.#######
###.#.#...#.#.......#.#.#.###.#.....#.#.#.#...#...#...#.#.#.#...#...#...#...#.#.#.#...#...#...#.#.#.#...#...#...#...#.#.#...#.#.......#.....#
###.#.#.###.#.#######.#.#.###.#####.#.#.#.#.#####.#.###.#.#.#.#.#.#####.###.#.#.#.#.#.#.#.#.###.#.#.#.###.###.#####.#.#.#####.#.#######.###.#
###...#.....#.........#...###.......#...#...#####...###...#...#...#####.....#...#...#...#...###...#...###.....#####...#.......#.........###.#
###########################################################################################################################################.#"""
}