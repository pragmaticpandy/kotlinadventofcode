// Originally generated by the template in CodeDAO
package kotlinadventofcode.`2022`

import kotlinadventofcode.Day

class `2022-12` : Day {

    override fun runPartOneNoUI(input: String): String {
        val map = parse(input)
        return shortestPathLength(map.startCoord, map).toString()
    }

    private fun shortestPathLength(start: Pair<Int, Int>, map: TopoMap): Int? {
        val queue: MutableList<Path> = mutableListOf(Path(listOf(start), map))
        val endsAlreadyEnqueued: MutableSet<Pair<Int, Int>> = mutableSetOf(start)
        while (queue.isNotEmpty()) {
            val path = queue.removeFirst()
            val nextPaths = path.nextPaths
            nextPaths.firstOrNull { it.end == map.endCoord }?.let { return it.length - 1 }
            nextPaths.filter { !endsAlreadyEnqueued.contains(it.end) }.forEach { queue.add(it) }
            endsAlreadyEnqueued.addAll(nextPaths.map { it.end })
        }

        return null
    }

    override fun runPartTwoNoUI(input: String): String {
        val map = parse(input)
        return map.map.filter { it.value == 'a' }.keys
            .mapNotNull { shortestPathLength(it, map) }
            .min()
            .toString()
    }

    private data class Path(val path: List<Pair<Int, Int>>, val map: TopoMap) {
        val end = path.last()
        val length = path.size
        val nextPaths: Set<Path>
            get() {
                return path
                    .last()
                    .let {
                        setOf(
                            it.first + 1 to it.second,
                            it.first - 1 to it.second,
                            it.first to it.second + 1,
                            it.first to it.second - 1
                        )
                    }
                    .filter { it.first >= map.minX && it.first <= map.maxX && it.second >= map.minY && it.second <= map.maxY }
                    .filter { map.height(it) <= map.height(end) + 1 }
                    .filter { !path.contains(it) }
                    .map { Path(path + it, map) }
                    .toSet()
            }
    }

    private data class TopoMap(val map: Map<Pair<Int, Int>, Char>) {
        val startCoord = map.filter { it.value == 'S' }.keys.first()
        val endCoord = map.filter { it.value == 'E' }.keys.first()
        val minX = map.keys.map { it.first }.min()
        val maxX = map.keys.map { it.first }.max()
        val minY = map.keys.map { it.second }.min()
        val maxY = map.keys.map { it.second }.max()

        fun height(coord: Pair<Int, Int>): Int {
            return if (map[coord]!! == 'S') 0
                else if (map[coord]!! == 'E') 25
                else map[coord]!!.code - 'a'.code
        }
    }

    private fun parse(input: String): TopoMap {
        val map: MutableMap<Pair<Int, Int>, Char> = mutableMapOf()
        val lines = input.lines()
        for (x in 0..lines[0].lastIndex) {
            for (y in 0..lines.lastIndex) {
                map[x to y] = lines[y][x]
            }
        }

        return TopoMap(map)
    }

    override val defaultInput = """abcccaaaaacccacccccccccccccccccccccccccccccccccccccccccccccccccccccccaaaaaacaccccccaaacccccccccccccccccccccccccccccccccccaaaaaaaaccccccccccccccccccccccccccccccccaaaaaa
abcccaaaaacccaaacaaacccccccccccccccccaaccccccacccaacccccccccccaacccccaaaaaaaaaaaccaaaaaaccccccccccccccccccccccccccccccccccaaaaaccccccccccccccccccccccccccccccccccaaaaaa
abccccaaaaaccaaaaaaaccccccccccccccaaaacccccccaacaaacccccccccaaaaaacccaaaaaaaaaaaccaaaaaacccccccccccccccccccccccccccccccccccaaaaaccccccccccccccaaacccccccccccccccccaaaaa
abccccaacccccaaaaaacccccccccccccccaaaaaacccccaaaaaccccccccccaaaaaacaaaaaaaaaaaaaccaaaaaacccccccccccccccccccccccccccccccccccaacaaccccccccccccccaaaaccccccccccccccccccaaa
abccccccccccaaaaaaaacccccccccccaaccaaaaaccccccaaaaaacccccccccaaaaacaaaaaaaaccccccccaaaaacccccccccccccccccccccccccccccccccccaacccccccccccccccccaaaaccaaacccccccccccccaac
abaaaaaccccaaaaaaaaaaccccccaaccaacaaaaacccccaaaaaaaaaaacccccaaaaacaaaacaaaaacccccccaacaacccccccccccccccccccccccccccccccccccccccccccccccccccccccaaaaaaaaacccccccccccaaac
abaaaaaccccaaaaaaaaaacaacccaaaaaacaccaacccccaaaaaaaaaaacccccaaaaaccccccaaaaaccccccccccaacccccccccccccccccccccccccccccccccccccccccccccccccccccccaaaakkkllccccccccccccccc
abaaaaacccccccaaacaaaaaaccccaaaaaaaccccccaaacccaacccaaaaaaacccccccccccccaaaaacccccccccaaaaaaccccccccccccccccccccccccccaaccccccccccccccccccccccackkkkkklllccccaaaccccccc
abaaaaacccccccaaacaaaaaaacccaaaaaaaccccccaaaacaaacaaaaaaaacccccccccccccaaaaaacccccccccaaaaaaccaacaacccccccccccccccaaaaaacccccccccccccccccccaaakkkkkkkkllllcccaaacaccccc
abaaaaaccccccccaacaaaaaaaacaaaaaaccccccccaaaaaaacaaaaaaaaacccccccccccccaaaacccccccccaaaaaaacccaaaaacccccccccccccccaaaaaaccccccccccccccccjjjjjkkkkkkpppplllcccaaaaaacccc
abaaaccccccccccccccaaaaaaacaaaaaacccccccccaaaaaaccaaaaaaaccccccccccccccccaaaccccccccaaaaaaaccccaaaaacccccccccccccccaaaaaaaccccaaccccccjjjjjjjkkkkppppppplllcccaaaaacccc
abccccccccccccccccaaaaaacccccccaaccccccaaaaaaaacccccaaaaaaccccccccccccccccccccccccccccaaaaaaccaaaaaacccccccccccccccaaaaaaaaaacaacccccjjjjjjjjjkooppppppplllcccaaacccccc
abccccccccccccccccaaaaaacccccccccccccccaaaaaaaaacccaaacaaacccccccccccccccccccccccaaaccaaccaaccaaaaccccccccccccccccaaaaaaaccaaaaaccccjjjjooooooooopuuuupppllccccaaaccccc
abccccccccccccccccccccaaccccccccccccccccaaaaaaaacccaaaccaacccccccccccccccccccccccaaaaaaacccccccaaaccccccccccccccccaaaaaaccccaaaaaaccjjjoooooooooouuuuuupplllccccaaccccc
abccaaaaccccaaacccccccccccccccccccccccccccaaaaaaaccaaccccccccccccaacccccccccccccccaaaaacccaaccaaaccccccccccccccccccccaaacccaaaaaaaccjjjoootuuuuuuuuuuuuppllllccccaccccc
abccaaaaaccaaaacccccccccccccccccccccccccccaacccacccccccccccccccacaaaacccccccccccaaaaaaacccaaaaaaacccccccccccccccccccccccccaaaaaacccciijnoottuuuuuuxxyuvpqqlmmcccccccccc
abcaaaaaaccaaaacccccccaaaaccccccccccacccccaaccccaaaccccccccccccaaaaaacccccccccccaaaaaaaaccaaaaaacccccccccccccccccaacccccccaacaaacccciiinntttxxxxuxxyyvvqqqqmmmmddddcccc
abcaaaaaacccaaacccccccaaaaccccaaaaaaaaccaaaaccccaacaacccccccccccaaaaccccccccccccaaaaaaaacccaaaaaaaacccccccccccccaaaaccccccccccaacccciiinntttxxxxxxxyyvvqqqqqmmmmdddcccc
abcaaaaaacccccccccccccaaaacccccaaaaaacccaaaaaaaaaaaaacccccccccccaaaaccccccccccccccaaacacccaaaaaaaaacccccccccccccaaaacccccccccccccccciiinnnttxxxxxxxyyvvvvqqqqmmmdddcccc
abcccaaccccccccccccccccaaccccccaaaaaacccaaaaaaaaaaaaaaccccccccccaacaccccccccccccccaaaccccaaaaaaaaaacccccccccccccaaaacccccccccccccccciiinnntttxxxxxyyyyyvvvqqqqmmmdddccc
SbccccccccccccccccccccccccccccaaaaaaaaccaaaccaaaaaaaaacccccccccccccccccccccccccccccccccccaaaaaaacccccccccaacccccccccccccccccccccccccciiinntttxxxxEzyyyyyvvvqqqmmmdddccc
abcccccccccccccccccccccccccccaaaaaaaaaacccccccaaaaaacccccccccccccaaacccccaacaacccccccccccccccaaaaaaccccccaacaaacccccccccccccccccccccciiinntttxxxyyyyyyyvvvvqqqmmmdddccc
abcccccccccccccccccccccccccccaaaaaaaaaaccccccaaaaaaaaccccccccccccaaaccccccaaaacccccccccccccccaaaaaaccccccaaaaacccccccccccccccccccccciiinnnttxxyyyyyyyvvvvvqqqqmmmdddccc
abcccccccccccccccccccccccccccacacaaacccccccccaaaaaaacccccccccccaaaaaaaacccaaaaacccccccccccccccaaaaaaaacaaaaaaccccccccccccccccccccccciiinntttxxwyyyyywwvvrrrqqmmmdddcccc
abaccccccccccccccccccccccccccccccaaacccccccccaaacaaaaacccccccccaaaaaaaaccaaaaaacccccccccccccccaaaaaaaacaaaaaaacccccccccccccccccccccchhnnnttwwwwwwwyyywvrrrrnnnnmdddcccc
abaccccccccccccccccccccccccccccccaaccccccccccccccaaaaaacccccccccaaaaaccccaaaacaccccccccccccccccaaaaacccccaaaaaaccccccaaaccccccaaaccchhnmmttswwwwwwywwwrrrrnnnnneeeccccc
abaccccccccccccccccccccccccccccccccccccccccccccccaaaaaacccccaaccaaaaaacccccaaccccccccccccccccccaaaaaaccccaaccaaccccaaaaaacccccaaacahhhmmmsssssssswwwwwrrrnnnneeeecccccc
abaaaccccccccccccccccccccccccccccaaaccccccccccccccaaaaaccccaaaccaaaaaacccccccccccccccccccccccccaaaaaaccccaaccccccccaaaaaacccaaaaaaahhhmmmmsssssssswwwwrrnnnneeeeacccccc
abaaaccccccccccccccccccccccccccccaaaaaaccccccccccaaaaacaaaaaaaccaaaccaccccccccaaaaaccccccccccccaaaacacccccccccccccccaaaaacccaaaaaaahhhhmmmmssssssswwwwrrnnneeeeaacccccc
abaaacccccccccccccccccccccccccccaaaaaaaccccccccccaaaaacaaaaaaaaaacccaaaaacccccaaaaacccccccccacaaaaaccacccccccccccccaaaaacccccaaaaaachhhmmmmmmmmmsssrrrrrnnneeeaaaaacccc
abaccccccccccccccaaaaccccccccccaaaaaaaacccccccccccccccccaaaaaaaaacccaaaaaccccaaaaaacccccccccaaaaaaaaaacccccccccccccaaaaaccccccaaaaachhhhmmmmmmmooossrrronneeeaaaaaacccc
abaccccccccccccccaaaaccccccccccaaaaaaacccccccccccccccccccaaaaaaaccccaaaaaacccaaaaaaccccaaaccaaaaaaaaaacccccccccccaaccccccccccaaaaaacchhhhhggggooooorrroonnfeeaaaaaccccc
abcccccccccccccccaaaaccccccccccccaaaaaacccccccccccccccccaaaaaaccccccaaaaaacccaaaaaaccccaaaaaacaaaaaacccccccccaaccaacccccccccccaacccccchhhhggggggoooooooooffeaaaaacccccc
abccccccccccccccccaacccccccccccccaaaaaacccccccaaccacccccaaaaaaacccccaaaaaaccccaaaccccccaaaaaacaaaaaacccccccccaaaaacccccccccccccccccccccccgggggggggooooooffffaaaaaaccccc
abccccccccccccccccccccccccccaaaccaacccccccccccaaaaacccccaaccaaacccccccaaacccccccccccccaaaaaaacaaaaaacccccccccaaaaaaaaccccccccccccccccccccccaaaggggfooooffffccccaacccccc
abaaccccccccccccccccccccccccaaacaccccccccccccaaaaacccccccccccaacccccccaaaacccaacccccccaaaaaaaaaaaaaaaccccccccccaaaaacccccccccccccaaaccccccccccccggfffffffffcccccccccccc
abaaccccccccccccccccccccccaacaaaaacccccccccccaaaaaacccccccccccccccccccaaaacaaaacccccccaaaaaaaaaccccaccccccccccaaaaaccccccccccccccaaaccccccccccccagfffffffccccccccccccca
abaacccccccaacccccccccccccaaaaaaaaccccccaacccccaaaacccccccccccccccccccaaaaaaaaacccccccccaaacaacaaacccccccccccaaacaaccccaaccaaccaaaaaaaaccccccccaaaccffffcccccccccccccaa
abaaaaaaaccaaccccccccccccccaaaaacccccccaaaacccaaccccccccccccccccccacaaaaaaaaaaccccccccccaaacaaaaaacccccccccccccccaaccccaaaaaaccaaaaaaaacccccccccaacccccccccccccccaaacaa
abaaaaaaaaaaccccccccccccccccaaaaaccccccaaaacccccccccccccccccccccccaaaaaaaaaaaaccccccccccccccaaaaaacccccccccccccccccccccaaaaaacccaaaaaacccccccccaaacccccccccccccccaaaaaa
abaaaacaaaaaaaacccccccccccccaacaaccccccaaaaccccccccccccccccccccccccaaaaaaaaaaacccccccccccccaaaaaaaacccccccccccccccccccaaaaaaaaccaaaaaaccccccccccccccccccccccccccccaaaaa"""
}