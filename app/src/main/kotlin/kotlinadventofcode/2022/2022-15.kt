// Originally generated by the template in CodeDAO
package kotlinadventofcode.`2022`

import com.github.h0tk3y.betterParse.combinators.*
import com.github.h0tk3y.betterParse.grammar.*
import com.github.h0tk3y.betterParse.lexer.*
import kotlinadventofcode.Day
import java.math.BigInteger
import kotlin.math.abs

class `2022-15` : Day {

    override fun runPart1(input: String): String {
        val targetY = 2000000
        val map: MutableMap<Coord, Item> = mutableMapOf()
        parse(input).forEach { pair ->
            val sensor = pair.first
            val beacon = pair.second
            map[sensor] = Item.SENSOR
            map[beacon] = Item.BEACON
            sensor.allCoordsAtMaxManhattanDistance(sensor.manhattanDistanceFrom(beacon), targetY).forEach {
                if (!map.containsKey(it)) map[it] = Item.EMPTY
            }
        }

        return map.filter { it.key.y == targetY }.filter { it.value == Item.EMPTY }.count().toString()
    }

    override fun runPart2(input: String): String {
        val coordinateRange = 0..4000000
        val sensors = parse(input).map { Sensor(it.first, it.second) }
        sensors
            .flatMap { it.coordsOutsideBorder }
            .toSet()
            .filter { it.x in coordinateRange }
            .filter { it.y in coordinateRange }
            .forEach { coord ->
            if (!sensors.any { sensor -> sensor.coord.manhattanDistanceFrom(coord) <= sensor.manhattanDistance })
                return coord.x.toBigInteger().times(4000000.toBigInteger()).plus(coord.y.toBigInteger()).toString()
        }

        throw Exception("not found")
    }

    private data class Sensor(val coord: Coord, val beacon: Coord) {
        val manhattanDistance = coord.manhattanDistanceFrom(beacon)
        val coordsOutsideBorder: Set<Coord> get() {
            val result: MutableSet<Coord> = mutableSetOf()

            // top
            var tmpCoord = Coord(coord.x, coord.y - (manhattanDistance + 1))
            result += tmpCoord
            while (tmpCoord.y < coord.y) {
                tmpCoord = Coord(tmpCoord.x + 1, tmpCoord.y + 1)
                result += tmpCoord
            }

            // right
            tmpCoord = Coord(coord.x + manhattanDistance + 1, coord.y)
            result += tmpCoord
            while (tmpCoord.x > coord.x) {
                tmpCoord = Coord(tmpCoord.x - 1, tmpCoord.y + 1)
                result += tmpCoord
            }

            // bottom
            tmpCoord = Coord(coord.x, coord.y + manhattanDistance + 1)
            result += tmpCoord
            while (tmpCoord.y > coord.y) {
                tmpCoord = Coord(tmpCoord.x - 1, tmpCoord.y - 1)
                result += tmpCoord
            }

            // left
            tmpCoord = Coord(coord.x - (manhattanDistance + 1), coord.y)
            result += tmpCoord
            while (tmpCoord.x < coord.x) {
                tmpCoord = Coord(tmpCoord.x + 1, tmpCoord.y - 1)
                result += tmpCoord
            }

            return result
        }
    }

    private enum class Item {
        SENSOR,
        BEACON,
        EMPTY;
    }

    private data class Coord(val x: Int, val y: Int) {
        fun manhattanDistanceFrom(other: Coord): Int {
            return abs(x - other.x) + abs(y - other.y)
        }

        fun allCoordsAtMaxManhattanDistance(distance: Int, targetY: Int): Set<Coord> {
            val result: MutableSet<Coord> = mutableSetOf()
            for (tmpX in x - distance..x + distance) {
                val coord = Coord(tmpX, targetY)
                if (manhattanDistanceFrom(coord) <= distance) result += coord
            }

            return result
        }
    }

    private fun parse(input: String): List<Pair<Coord, Coord>> {
        val grammar = object : Grammar<List<Pair<Coord, Coord>>>() {

            /*
             * Tokens must be declared by themselves—i.e. they must be a declared property in this object.
             * Declaration order will be used in the case that multiple tokens match.
             */
            val newlineLit by literalToken("\n")
            val sensorAtLit by literalToken("Sensor at x=")
            val xySeparatorLit by literalToken(", y=")
            val closestBeaconLit by literalToken(": closest beacon is at x=")
            val intRegex by regexToken("-?\\d+")

            /*
             * Intermediate parsers.
             */
            val int by intRegex use { text.toInt() }
            val coordPair by skip(sensorAtLit) and int and skip(xySeparatorLit) and int and
                skip(closestBeaconLit) and int and skip(xySeparatorLit) and int map {
                    Coord(it.t1, it.t2) to Coord(it.t3, it.t4)
                }

            /*
             * Root parser.
             */
            override val rootParser by separatedTerms(coordPair, newlineLit)
        }

        return grammar.parseToEnd(input)
    }

    override val defaultInput = """Sensor at x=407069, y=1770807: closest beacon is at x=105942, y=2000000
Sensor at x=2968955, y=2961853: closest beacon is at x=2700669, y=3091664
Sensor at x=3069788, y=2289672: closest beacon is at x=3072064, y=2287523
Sensor at x=2206, y=1896380: closest beacon is at x=105942, y=2000000
Sensor at x=3010408, y=2580417: closest beacon is at x=2966207, y=2275132
Sensor at x=2511130, y=2230361: closest beacon is at x=2966207, y=2275132
Sensor at x=65435, y=2285654: closest beacon is at x=105942, y=2000000
Sensor at x=2811709, y=3379959: closest beacon is at x=2801189, y=3200444
Sensor at x=168413, y=3989039: closest beacon is at x=-631655, y=3592291
Sensor at x=165506, y=2154294: closest beacon is at x=105942, y=2000000
Sensor at x=2720578, y=3116882: closest beacon is at x=2700669, y=3091664
Sensor at x=786521, y=1485720: closest beacon is at x=105942, y=2000000
Sensor at x=82364, y=2011850: closest beacon is at x=105942, y=2000000
Sensor at x=2764729, y=3156203: closest beacon is at x=2801189, y=3200444
Sensor at x=1795379, y=1766882: closest beacon is at x=1616322, y=907350
Sensor at x=2708986, y=3105910: closest beacon is at x=2700669, y=3091664
Sensor at x=579597, y=439: closest beacon is at x=1616322, y=907350
Sensor at x=2671201, y=2736834: closest beacon is at x=2700669, y=3091664
Sensor at x=3901, y=2089464: closest beacon is at x=105942, y=2000000
Sensor at x=144449, y=813212: closest beacon is at x=105942, y=2000000
Sensor at x=3619265, y=3169784: closest beacon is at x=2801189, y=3200444
Sensor at x=2239333, y=3878605: closest beacon is at x=2801189, y=3200444
Sensor at x=2220630, y=2493371: closest beacon is at x=2966207, y=2275132
Sensor at x=1148022, y=403837: closest beacon is at x=1616322, y=907350
Sensor at x=996105, y=3077490: closest beacon is at x=2700669, y=3091664
Sensor at x=3763069, y=3875159: closest beacon is at x=2801189, y=3200444
Sensor at x=3994575, y=2268273: closest beacon is at x=3072064, y=2287523
Sensor at x=3025257, y=2244500: closest beacon is at x=2966207, y=2275132
Sensor at x=2721366, y=1657084: closest beacon is at x=2966207, y=2275132
Sensor at x=3783491, y=1332930: closest beacon is at x=3072064, y=2287523
Sensor at x=52706, y=2020407: closest beacon is at x=105942, y=2000000
Sensor at x=2543090, y=47584: closest beacon is at x=3450858, y=-772833
Sensor at x=3499766, y=2477193: closest beacon is at x=3072064, y=2287523"""
}