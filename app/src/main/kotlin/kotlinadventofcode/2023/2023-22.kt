// Originally generated by the template in CodeDAO
package kotlinadventofcode.`2023`

import com.github.h0tk3y.betterParse.combinators.*
import com.github.h0tk3y.betterParse.grammar.*
import com.github.h0tk3y.betterParse.lexer.*
import kotlinadventofcode.Day
import kotlinadventofcode.UI.UI
import kotlinadventofcode.UI.forEach
import kotlin.math.max
import kotlin.math.min

class `2023-22` : Day {
    override val year: Int = 2023
    override val day: Int = 22

    context(UI)
    override fun runPartOne(input: String): String {
        val bricks = input.toBricks()
        val newBricks = bricks.gravitized().first
        var count = 0
        newBricks.forEach("testing each brick") { brick ->
            val sut = (newBricks - brick).gravitized().first
            if (sut == newBricks - brick) {
                count++
            }
        }

        return count.toString()
    }

    context(UI)
    override fun runPartTwo(input: String): String {
        val bricks = input.toBricks()
        val newBricks = bricks.gravitized().first
        var count = 0
        newBricks.forEach("testing each brick") { brick ->
            count += (newBricks - brick).gravitized().second
        }

        return count.toString()
    }

    companion object {
        fun Collection<Brick>.gravitized(): Pair<Set<Brick>, Int> {
            val brickAt: MutableMap<Coord, Brick> = mutableMapOf()
            forEach { brick -> brick.allCoords.forEach { brickAt[it] = brick } }
            val xMin = brickAt.keys.minOf { it.x }
            val xMax = brickAt.keys.maxOf { it.x }
            val yMin = brickAt.keys.minOf { it.y }
            val yMax = brickAt.keys.maxOf { it.y }
            val zMin = 1
            val zMax = brickAt.keys.maxOf { it.z }

            fun getBricksAtLevel(z: Int): Set<Brick> {
                val result = mutableSetOf<Brick>()
                for (x in xMin..xMax) {
                    for (y in yMin..yMax) {
                        brickAt[Coord(x, y, z)]?.let { result.add(it) }
                    }
                }

                return result
            }

            val newBrickAt = mutableMapOf<Coord, Brick>()
            val newBricks = mutableSetOf<Brick>()
            var numMoved = 0

            fun getLoweredBrick(brick: Brick, currentZ: Int): Brick {
                val xyCoords = brick.allXyCoords

                var firstBlockedZ = zMin - 1
                for (z in currentZ - 1 downTo zMin) {
                    if (xyCoords.any { Coord(it.x, it.y, z) in newBrickAt }) {
                        firstBlockedZ = z
                        break
                    }
                }

                val zOffset = currentZ - (firstBlockedZ + 1)
                if (zOffset != 0) numMoved++
                return Brick(
                    Coord(brick.first.x, brick.first.y, brick.first.z - zOffset),
                    Coord(brick.second.x, brick.second.y, brick.second.z - zOffset))
            }

            val originalBricksPlaced = mutableSetOf<Brick>()
            for (z in zMin..zMax) {
                val bricksAtZ = getBricksAtLevel(z) - originalBricksPlaced
                bricksAtZ.forEach {
                    val newBrick = getLoweredBrick(it, z)
                    newBricks.add(newBrick)
                    newBrick.allCoords.forEach { coord -> newBrickAt[coord] = newBrick }
                    originalBricksPlaced += it
                }
            }

            return newBricks to numMoved
        }

        data class Brick(val first: Coord, val second: Coord) {
            val allCoords: Set<Coord> = run {
                val result = mutableSetOf<Coord>()
                for (x in min(first.x, second.x)..max(first.x, second.x)) {
                    for (y in min(first.y, second.y)..max(first.y, second.y)) {
                        for (z in min(first.z, second.z)..max(first.z, second.z)) {
                            result.add(Coord(x, y, z))
                        }
                    }
                }

                result
            }

            val allXyCoords: Set<XYCoord> = run {
                val result = mutableSetOf<XYCoord>()
                for (x in min(first.x, second.x)..max(first.x, second.x)) {
                    for (y in min(first.y, second.y)..max(first.y, second.y)) {
                        result.add(XYCoord(x, y))
                    }
                }

                result
            }
        }

        data class Coord(val x: Int, val y: Int, val z: Int)
        data class XYCoord(val x: Int, val y: Int)

        fun String.toBricks(): List<Brick> {
            val grammar = object : Grammar<List<Brick>>() {

                // tokens
                val newlineLit by literalToken("\n")
                val commaLit by literalToken(",")
                val tildeLit by literalToken("~")
                val positiveIntRegex by regexToken("\\d+")

                // parsers
                val positiveInt by positiveIntRegex use { text.toInt() }
                val coord by positiveInt and skip(commaLit) and positiveInt and skip(commaLit) and positiveInt map { Coord(it.t1, it.t2, it.t3) }
                val brick by (coord and skip(tildeLit) and coord) map { Brick(it.t1, it.t2) }
                override val rootParser by separatedTerms(brick, newlineLit)
            }

            return grammar.parseToEnd(this)
        }
    }

    override val defaultInput = """5,3,258~7,3,258
9,3,189~9,4,189
6,5,33~8,5,33
7,6,159~9,6,159
1,5,198~3,5,198
7,6,314~8,6,314
6,1,8~8,1,8
2,6,77~2,9,77
3,1,158~6,1,158
4,5,153~4,7,153
0,9,221~1,9,221
0,7,257~0,9,257
4,6,105~5,6,105
5,7,239~5,9,239
0,0,279~2,0,279
8,4,279~9,4,279
1,5,145~1,8,145
7,7,303~8,7,303
8,2,185~8,5,185
7,2,283~7,3,283
6,9,111~8,9,111
2,1,272~2,3,272
5,8,118~5,8,120
1,6,200~2,6,200
4,2,258~4,4,258
0,1,222~0,4,222
7,4,268~7,5,268
4,0,182~4,3,182
2,7,270~4,7,270
3,8,48~5,8,48
2,0,44~2,1,44
8,3,148~8,5,148
0,6,227~0,8,227
8,4,138~8,4,140
9,2,210~9,4,210
3,1,17~6,1,17
3,4,141~3,7,141
3,0,214~3,0,215
0,8,50~2,8,50
3,8,39~6,8,39
1,2,309~3,2,309
5,4,293~7,4,293
2,5,11~5,5,11
7,2,327~7,4,327
0,7,139~0,7,141
9,1,105~9,3,105
1,0,89~1,1,89
1,0,86~1,1,86
3,3,80~3,5,80
8,4,198~8,6,198
6,6,54~8,6,54
8,6,188~9,6,188
2,0,309~2,0,310
5,3,236~5,5,236
2,6,17~2,8,17
8,0,183~8,1,183
7,3,122~7,5,122
6,6,90~6,8,90
0,1,294~0,3,294
5,0,222~7,0,222
8,6,155~8,9,155
7,1,103~7,2,103
4,4,156~4,6,156
7,4,16~7,6,16
7,5,197~7,8,197
3,6,76~3,8,76
8,6,51~8,9,51
4,6,117~4,9,117
8,6,1~8,6,4
3,0,179~3,2,179
6,7,57~6,9,57
5,1,243~6,1,243
2,6,152~2,8,152
7,5,38~9,5,38
9,0,149~9,0,151
3,8,18~5,8,18
3,0,240~6,0,240
5,5,65~6,5,65
6,2,169~6,2,172
3,1,284~3,4,284
6,6,14~8,6,14
3,5,6~6,5,6
7,3,163~7,6,163
6,2,306~6,3,306
4,2,238~6,2,238
3,4,218~3,5,218
3,6,13~3,8,13
5,4,297~5,6,297
4,2,95~4,2,98
6,3,79~7,3,79
3,0,38~3,3,38
0,5,241~1,5,241
7,5,186~7,7,186
7,5,201~8,5,201
1,6,280~3,6,280
3,1,77~3,4,77
3,6,155~5,6,155
0,1,43~2,1,43
3,8,83~5,8,83
4,7,122~7,7,122
3,2,199~3,3,199
8,6,304~8,6,306
1,8,243~1,8,243
9,7,12~9,9,12
0,3,79~2,3,79
9,0,85~9,1,85
9,1,169~9,3,169
3,9,278~3,9,281
3,4,175~4,4,175
3,9,104~6,9,104
7,2,136~7,4,136
1,0,209~4,0,209
9,0,8~9,0,10
2,2,307~2,3,307
3,8,292~5,8,292
6,4,98~6,6,98
3,8,201~6,8,201
3,1,159~5,1,159
7,6,217~7,8,217
1,8,108~1,9,108
3,5,8~4,5,8
2,0,204~5,0,204
9,1,258~9,3,258
7,3,167~8,3,167
2,8,6~5,8,6
3,4,29~6,4,29
5,9,95~8,9,95
4,8,174~6,8,174
1,2,27~1,4,27
9,6,22~9,8,22
1,9,277~3,9,277
5,3,192~7,3,192
3,4,286~6,4,286
4,7,149~4,9,149
9,0,190~9,2,190
3,5,315~5,5,315
1,0,30~1,2,30
5,1,200~7,1,200
0,3,81~0,6,81
1,0,91~1,0,93
3,3,79~5,3,79
1,6,252~1,9,252
6,2,97~6,3,97
6,1,82~9,1,82
6,2,134~7,2,134
1,2,201~1,5,201
5,3,330~5,5,330
2,0,19~2,0,21
4,3,177~4,6,177
0,6,141~0,6,142
7,0,47~9,0,47
8,6,20~8,9,20
0,2,248~0,4,248
2,3,328~2,7,328
4,7,36~4,8,36
4,4,45~5,4,45
7,7,251~7,8,251
2,4,118~2,5,118
5,5,290~5,8,290
4,8,42~5,8,42
2,2,303~4,2,303
3,1,148~6,1,148
3,0,302~5,0,302
6,2,133~6,5,133
9,5,96~9,7,96
8,0,32~8,2,32
5,4,91~8,4,91
2,3,275~2,4,275
2,0,79~5,0,79
0,4,246~0,7,246
6,7,56~7,7,56
1,6,11~3,6,11
2,3,4~2,5,4
6,7,258~7,7,258
7,4,124~9,4,124
0,2,12~2,2,12
7,3,92~7,5,92
1,2,207~3,2,207
8,5,102~8,7,102
7,4,321~9,4,321
1,5,273~1,7,273
7,9,167~8,9,167
7,7,309~9,7,309
8,3,81~8,3,83
3,7,161~5,7,161
0,5,269~2,5,269
6,2,270~6,3,270
5,6,102~5,9,102
1,3,322~1,6,322
3,1,175~3,3,175
1,4,55~1,4,58
6,7,330~6,9,330
0,0,283~3,0,283
8,6,302~8,8,302
1,1,224~1,1,226
8,9,243~9,9,243
6,4,165~7,4,165
7,3,40~7,5,40
3,1,186~5,1,186
4,0,300~4,2,300
9,5,11~9,7,11
2,2,263~2,5,263
2,4,82~4,4,82
2,7,21~2,9,21
6,1,206~6,1,208
9,4,299~9,7,299
1,6,2~1,8,2
9,7,213~9,9,213
6,6,69~7,6,69
8,1,29~8,4,29
1,3,136~1,5,136
4,1,205~6,1,205
5,5,193~5,7,193
0,3,153~0,5,153
1,1,40~3,1,40
3,3,220~3,4,220
4,2,37~6,2,37
9,1,53~9,2,53
3,6,79~3,7,79
0,8,134~1,8,134
1,9,162~3,9,162
9,2,254~9,4,254
1,0,124~1,0,127
2,6,150~2,8,150
4,7,123~4,7,125
5,6,15~5,9,15
5,7,163~5,7,166
6,9,220~8,9,220
7,4,203~7,5,203
7,7,23~7,7,26
8,1,33~8,2,33
6,7,223~7,7,223
6,0,298~6,3,298
2,4,278~2,4,279
9,3,177~9,3,177
4,2,126~4,3,126
3,4,294~3,4,294
5,1,110~5,2,110
2,4,87~5,4,87
1,7,324~3,7,324
4,2,149~4,4,149
1,2,181~2,2,181
2,8,44~5,8,44
0,9,161~1,9,161
9,2,45~9,4,45
5,6,212~8,6,212
5,2,109~5,5,109
1,6,115~3,6,115
2,3,103~2,4,103
4,3,4~5,3,4
8,1,301~8,4,301
5,6,304~5,9,304
4,4,239~4,7,239
4,4,24~7,4,24
9,6,246~9,8,246
4,3,183~5,3,183
4,2,49~7,2,49
3,5,163~3,5,164
6,0,94~8,0,94
8,0,130~8,2,130
4,5,213~7,5,213
7,7,310~7,9,310
5,7,159~5,8,159
5,3,193~5,3,194
9,1,171~9,3,171
2,4,211~2,6,211
1,7,75~3,7,75
2,4,104~2,5,104
0,3,9~0,5,9
7,5,167~7,8,167
4,7,203~6,7,203
7,0,107~9,0,107
5,7,104~7,7,104
0,5,22~2,5,22
5,2,35~5,5,35
6,9,59~8,9,59
2,0,153~5,0,153
0,8,265~2,8,265
4,2,281~4,4,281
3,3,114~5,3,114
2,6,244~4,6,244
5,1,189~5,3,189
5,2,154~5,4,154
9,1,261~9,3,261
4,0,323~7,0,323
7,3,277~7,4,277
7,0,171~7,2,171
3,3,230~3,5,230
2,5,294~2,7,294
3,5,146~3,7,146
5,7,283~7,7,283
8,5,290~8,8,290
4,6,110~4,8,110
6,6,100~6,9,100
2,6,242~2,8,242
1,4,23~3,4,23
5,3,328~5,5,328
5,5,173~5,8,173
1,4,277~2,4,277
6,7,97~8,7,97
7,5,226~7,8,226
1,8,261~1,8,263
8,1,38~9,1,38
3,0,9~3,2,9
0,0,94~3,0,94
1,4,96~2,4,96
8,8,55~8,8,57
9,5,207~9,5,209
5,7,148~7,7,148
3,2,126~3,5,126
1,0,115~3,0,115
7,5,121~7,7,121
2,9,82~4,9,82
7,0,2~9,0,2
1,0,42~1,1,42
5,5,216~7,5,216
8,2,225~8,4,225
4,1,194~4,2,194
2,4,172~5,4,172
3,7,255~6,7,255
4,6,333~4,8,333
5,2,137~7,2,137
0,5,119~0,7,119
6,2,7~7,2,7
2,2,185~4,2,185
6,0,161~6,1,161
5,6,48~8,6,48
4,7,115~7,7,115
8,1,19~8,3,19
5,6,328~8,6,328
1,0,212~1,2,212
9,6,83~9,9,83
7,6,296~7,8,296
0,4,293~2,4,293
3,5,279~3,8,279
1,9,134~2,9,134
0,8,155~2,8,155
3,1,244~3,3,244
6,3,225~6,5,225
2,1,119~3,1,119
6,4,131~9,4,131
1,3,47~1,5,47
1,5,213~3,5,213
5,4,12~8,4,12
5,2,296~5,4,296
5,4,74~7,4,74
9,1,191~9,1,193
6,0,101~6,2,101
1,1,162~3,1,162
3,4,287~3,6,287
2,4,269~4,4,269
7,5,223~9,5,223
3,4,184~3,7,184
6,0,301~6,2,301
3,5,26~6,5,26
4,2,285~5,2,285
2,0,185~4,0,185
5,0,184~8,0,184
4,1,303~5,1,303
4,3,264~6,3,264
8,4,53~8,7,53
1,6,48~1,8,48
3,1,169~3,1,172
6,9,97~8,9,97
1,1,10~3,1,10
6,5,100~8,5,100
6,7,249~8,7,249
5,8,115~5,9,115
3,5,199~3,8,199
2,5,54~2,8,54
8,6,191~8,8,191
5,8,161~6,8,161
3,5,320~4,5,320
3,7,33~6,7,33
0,6,236~0,8,236
0,9,23~2,9,23
2,9,151~4,9,151
2,6,55~2,9,55
1,8,4~1,9,4
0,8,224~0,9,224
6,8,216~8,8,216
3,6,139~3,8,139
1,2,108~3,2,108
1,6,117~3,6,117
5,9,241~8,9,241
4,4,224~6,4,224
0,6,230~1,6,230
3,6,188~5,6,188
6,2,326~8,2,326
3,4,318~6,4,318
4,2,309~5,2,309
2,7,53~2,8,53
2,6,130~2,8,130
3,3,186~3,4,186
8,0,104~9,0,104
5,8,248~5,8,250
5,1,169~5,1,173
3,7,104~3,8,104
5,8,289~7,8,289
4,4,95~4,6,95
9,1,168~9,5,168
6,1,287~6,4,287
7,3,78~8,3,78
1,9,300~3,9,300
0,4,163~0,5,163
1,3,112~1,3,115
6,6,247~6,7,247
2,0,199~3,0,199
4,1,51~4,1,53
5,5,208~7,5,208
0,4,101~2,4,101
2,0,111~2,2,111
2,7,297~4,7,297
7,5,288~9,5,288
8,2,188~9,2,188
7,6,120~7,9,120
0,8,133~1,8,133
5,6,296~5,7,296
1,6,22~3,6,22
2,6,98~2,9,98
5,0,45~7,0,45
6,9,171~8,9,171
3,3,120~3,6,120
1,8,81~3,8,81
7,0,220~7,2,220
1,8,163~2,8,163
1,2,196~1,4,196
7,6,49~7,7,49
2,2,128~2,4,128
7,4,117~7,7,117
7,1,278~9,1,278
2,6,327~4,6,327
3,6,217~3,6,218
3,8,231~5,8,231
0,4,252~0,6,252
0,3,203~1,3,203
2,7,20~4,7,20
0,6,86~0,8,86
6,5,210~6,8,210
5,1,12~7,1,12
0,1,115~0,2,115
7,5,34~8,5,34
7,8,319~9,8,319
6,7,293~8,7,293
7,4,1~9,4,1
7,8,37~8,8,37
6,8,292~8,8,292
2,0,10~5,0,10
7,5,170~7,8,170
7,7,39~8,7,39
0,5,108~0,5,110
4,6,242~6,6,242
7,7,124~9,7,124
1,3,238~1,6,238
2,9,234~4,9,234
2,0,208~4,0,208
3,9,248~3,9,250
5,3,25~8,3,25
9,0,83~9,1,83
2,0,269~2,3,269
8,2,277~8,5,277
6,0,248~9,0,248
4,6,214~6,6,214
1,1,204~1,2,204
4,3,200~7,3,200
3,5,254~6,5,254
5,1,197~5,4,197
7,6,285~8,6,285
2,2,152~2,2,152
3,0,246~3,2,246
0,2,17~0,4,17
0,3,107~0,5,107
2,1,191~2,1,192
4,0,219~6,0,219
2,3,99~2,5,99
1,5,181~2,5,181
2,2,51~4,2,51
6,8,86~9,8,86
1,5,222~1,7,222
6,5,193~7,5,193
5,1,46~5,3,46
3,4,217~6,4,217
0,6,8~2,6,8
5,1,246~5,1,248
8,4,36~8,7,36
2,9,242~2,9,243
4,4,136~6,4,136
9,5,210~9,9,210
1,5,14~3,5,14
9,0,255~9,2,255
3,2,147~4,2,147
7,2,316~7,3,316
3,9,23~4,9,23
5,5,112~7,5,112
2,2,200~2,5,200
1,6,121~1,7,121
8,0,80~8,3,80
1,1,130~2,1,130
3,5,323~4,5,323
3,1,184~4,1,184
1,1,46~4,1,46
7,3,302~9,3,302
8,6,232~8,9,232
4,0,47~6,0,47
7,7,108~7,9,108
7,8,174~9,8,174
0,6,157~0,8,157
6,5,288~6,8,288
8,6,45~8,7,45
6,1,240~6,3,240
3,5,96~4,5,96
1,5,208~3,5,208
0,0,138~2,0,138
9,1,174~9,4,174
5,4,234~7,4,234
2,1,273~2,1,275
3,7,78~5,7,78
7,4,189~7,6,189
2,0,12~5,0,12
9,5,88~9,8,88
6,4,42~9,4,42
2,3,47~2,6,47
7,7,308~7,8,308
3,4,21~6,4,21
4,0,125~6,0,125
9,7,303~9,9,303
6,3,194~6,5,194
1,6,216~3,6,216
5,6,311~8,6,311
3,2,287~5,2,287
8,4,149~8,5,149
4,9,18~6,9,18
5,2,7~5,3,7
1,3,243~1,6,243
7,7,28~7,8,28
0,6,272~2,6,272
5,7,302~6,7,302
0,2,116~0,4,116
6,4,75~6,7,75
6,4,28~6,7,28
1,4,144~1,5,144
5,2,208~8,2,208
0,1,228~0,4,228
1,4,194~3,4,194
0,2,14~0,4,14
7,8,249~9,8,249
0,6,1~0,8,1
3,7,232~3,9,232
1,4,10~1,6,10
6,0,163~8,0,163
4,0,259~4,2,259
0,5,249~0,6,249
6,6,244~9,6,244
6,1,96~6,3,96
3,6,84~3,9,84
7,2,89~7,2,93
6,9,123~9,9,123
4,2,330~4,4,330
7,7,21~9,7,21
4,0,328~4,2,328
0,1,2~0,2,2
0,6,275~0,6,275
0,8,84~1,8,84
2,5,16~3,5,16
5,9,33~6,9,33
9,3,252~9,6,252
2,2,228~2,3,228
1,9,227~3,9,227
4,0,126~6,0,126
4,1,189~4,3,189
8,2,84~8,4,84
0,0,6~3,0,6
1,1,218~1,1,221
1,5,249~1,7,249
6,1,81~9,1,81
7,8,233~9,8,233
2,6,178~3,6,178
5,4,323~7,4,323
3,3,214~3,5,214
4,7,315~5,7,315
3,2,113~3,6,113
5,7,18~7,7,18
6,4,254~7,4,254
5,2,10~6,2,10
0,0,276~0,3,276
4,1,92~4,4,92
5,7,298~5,7,300
1,8,74~3,8,74
8,4,333~8,6,333
5,4,171~5,6,171
1,0,116~1,0,118
6,0,87~6,4,87
1,6,74~4,6,74
7,2,50~9,2,50
4,5,114~4,7,114
8,2,213~8,3,213
7,7,151~7,9,151
8,1,30~8,2,30
7,7,51~7,8,51
6,7,109~6,7,111
0,6,233~0,8,233
3,5,192~3,5,195
3,6,336~3,8,336
3,4,97~6,4,97
2,2,191~2,4,191
3,3,160~3,5,160
7,3,182~7,5,182
5,9,306~7,9,306
5,4,275~8,4,275
6,4,219~6,4,222
8,3,245~8,6,245
1,5,286~1,7,286
2,7,148~3,7,148
0,2,117~0,5,117
0,0,129~0,3,129
4,6,283~4,7,283
3,3,122~3,4,122
4,6,107~4,7,107
7,7,171~9,7,171
4,3,90~4,4,90
4,7,316~4,9,316
7,1,210~7,3,210
7,1,113~7,2,113
5,6,87~6,6,87
6,5,293~9,5,293
4,0,123~4,3,123
4,4,139~5,4,139
3,1,209~5,1,209
3,8,245~6,8,245
4,5,184~6,5,184
3,7,117~3,9,117
2,2,125~2,5,125
7,7,154~9,7,154
8,4,58~8,4,60
7,8,59~9,8,59
6,4,44~7,4,44
9,3,257~9,4,257
7,3,113~7,5,113
2,3,222~5,3,222
4,8,236~7,8,236
7,8,177~7,9,177
3,5,284~3,7,284
0,3,132~0,5,132
3,7,278~5,7,278
0,8,333~0,8,333
0,0,16~2,0,16
6,5,25~9,5,25
0,0,141~1,0,141
2,0,156~2,0,158
0,6,228~2,6,228
1,6,320~3,6,320
5,9,242~5,9,244
3,5,2~3,5,4
5,7,85~5,8,85
6,1,180~8,1,180
4,6,326~4,9,326
2,2,49~3,2,49
6,2,39~8,2,39
6,6,313~6,7,313
4,2,171~4,3,171
5,9,308~6,9,308
5,0,327~7,0,327
0,0,3~3,0,3
0,4,282~2,4,282
2,3,74~4,3,74
2,0,262~4,0,262
3,1,200~3,2,200
3,3,181~3,6,181
7,7,334~8,7,334
5,8,313~6,8,313
9,6,247~9,7,247
4,6,269~6,6,269
2,0,135~5,0,135
6,5,107~6,7,107
2,7,105~2,9,105
7,7,54~7,7,54
6,9,36~7,9,36
8,3,57~8,6,57
7,1,182~9,1,182
3,6,174~6,6,174
8,6,201~9,6,201
6,4,253~6,5,253
6,8,234~8,8,234
2,3,1~5,3,1
5,0,2~5,0,2
0,1,127~0,2,127
5,3,269~8,3,269
2,1,117~4,1,117
6,4,271~7,4,271
6,5,284~7,5,284
4,8,104~5,8,104
6,6,86~9,6,86
3,0,42~5,0,42
5,3,128~5,5,128
4,0,75~4,4,75
9,5,90~9,5,93
3,2,10~3,3,10
9,6,288~9,9,288
8,1,305~8,3,305
5,4,185~5,5,185
2,6,331~2,6,333
8,1,107~8,3,107
0,6,137~0,8,137
9,5,158~9,5,159
8,0,102~8,3,102
1,3,77~2,3,77
7,3,123~9,3,123
0,6,4~0,7,4
7,8,34~9,8,34
6,0,216~7,0,216
5,0,208~5,1,208
8,4,26~9,4,26
4,0,78~4,2,78
3,7,150~5,7,150
6,1,164~7,1,164
5,5,28~5,7,28
2,3,34~2,5,34
1,5,19~1,7,19
5,1,266~5,4,266
9,4,203~9,6,203
8,4,224~8,6,224
6,4,22~6,5,22
5,4,284~5,6,284
4,2,284~6,2,284
1,7,160~1,8,160
4,4,279~6,4,279
3,2,208~3,2,210
8,1,22~8,2,22
2,5,227~4,5,227
4,3,191~4,3,192
6,1,169~8,1,169
6,2,138~8,2,138
0,8,240~1,8,240
0,1,91~0,4,91
7,3,151~8,3,151
3,3,261~3,5,261
7,5,3~9,5,3
5,7,332~6,7,332
5,3,244~5,5,244
2,8,317~4,8,317
8,0,246~8,3,246
5,2,246~5,5,246
8,5,77~9,5,77
7,1,114~7,1,116
8,1,90~8,3,90
5,6,169~5,8,169
1,5,17~1,7,17
1,5,54~1,7,54
9,7,89~9,9,89
4,2,252~4,5,252
7,5,219~7,5,222
5,0,299~7,0,299
6,1,131~6,3,131
7,0,5~9,0,5
7,9,169~9,9,169
4,3,297~4,4,297
1,2,177~3,2,177
3,4,203~3,7,203
4,3,125~6,3,125
2,3,255~5,3,255
8,0,24~8,2,24
1,0,114~3,0,114
0,6,270~3,6,270
1,8,47~3,8,47
3,1,218~5,1,218
0,4,244~0,6,244
8,8,60~9,8,60
6,7,29~6,9,29
4,8,329~6,8,329
3,6,266~5,6,266
3,5,189~3,6,189
2,7,273~2,9,273
4,4,295~4,6,295
7,4,310~7,6,310
9,0,281~9,1,281
4,7,244~4,7,246
0,4,124~3,4,124
1,1,244~1,2,244
8,2,132~8,3,132
5,7,4~7,7,4
7,7,57~8,7,57
3,2,281~3,5,281
7,2,148~7,2,151
6,6,31~6,8,31
1,0,83~1,2,83
0,6,128~2,6,128
4,4,251~7,4,251
0,5,138~2,5,138
3,6,296~4,6,296
7,9,138~9,9,138
4,2,310~4,3,310
5,7,3~5,9,3
7,7,199~7,8,199
7,0,318~7,3,318
2,6,229~2,8,229
0,6,138~0,8,138
5,7,228~8,7,228
9,1,115~9,1,118
5,6,234~9,6,234
3,5,29~5,5,29
4,9,147~6,9,147
1,0,84~1,1,84
5,8,144~6,8,144
0,7,238~0,9,238
0,8,254~2,8,254
5,3,22~8,3,22
8,1,126~8,3,126
2,4,292~2,6,292
0,5,6~0,8,6
1,8,109~2,8,109
0,4,160~0,6,160
5,3,29~5,3,31
2,9,246~4,9,246
6,4,181~7,4,181
2,7,218~2,8,218
1,5,1~1,9,1
8,1,228~8,4,228
9,8,235~9,8,238
4,7,195~4,8,195
2,6,335~2,8,335
1,1,118~1,3,118
7,8,301~8,8,301
4,0,94~4,2,94
7,1,100~9,1,100
5,8,257~8,8,257
2,7,323~4,7,323
4,7,79~4,9,79
4,1,18~6,1,18
4,7,134~4,8,134
6,9,154~8,9,154
5,1,155~5,3,155
5,6,279~5,6,281
6,3,18~6,5,18
6,5,128~9,5,128
6,1,181~8,1,181
1,0,111~1,2,111
4,1,81~5,1,81
2,0,306~2,2,306
5,4,277~5,7,277
5,1,97~8,1,97
2,1,241~4,1,241
6,4,64~6,6,64
8,8,90~9,8,90
3,8,19~4,8,19
4,3,72~6,3,72
7,5,173~7,5,174
6,0,326~6,0,326
0,9,157~2,9,157
5,0,252~7,0,252
8,3,146~8,4,146
7,4,59~7,6,59
9,5,157~9,8,157
1,6,187~3,6,187
0,0,229~0,2,229
7,1,11~9,1,11
6,5,206~6,8,206
7,6,317~7,9,317
9,4,250~9,7,250
6,4,315~8,4,315
2,8,331~4,8,331
9,4,186~9,6,186
0,9,276~2,9,276
5,7,242~7,7,242
5,4,89~5,6,89
3,5,70~6,5,70
2,1,331~5,1,331
0,4,60~1,4,60
4,5,284~4,8,284
0,1,192~1,1,192
9,7,46~9,7,48
1,0,215~1,2,215
5,4,326~5,6,326
4,7,281~4,9,281
4,7,116~6,7,116
7,6,17~9,6,17
6,1,168~6,2,168
3,7,116~3,9,116
0,8,79~3,8,79
2,6,126~4,6,126
1,6,287~1,9,287
3,6,86~3,9,86
7,5,200~7,6,200
1,1,117~1,3,117
6,4,20~6,6,20
0,0,140~0,1,140
7,3,82~7,5,82
4,8,51~4,9,51
8,9,222~8,9,225
2,8,237~3,8,237
7,0,146~9,0,146
3,3,290~3,4,290
3,2,2~6,2,2
8,2,310~8,3,310
1,7,159~1,9,159
9,8,305~9,8,305
6,4,113~6,6,113
3,0,49~5,0,49
4,6,192~4,8,192
9,0,227~9,3,227
4,2,282~7,2,282
6,3,75~9,3,75
0,3,46~3,3,46
4,8,160~6,8,160
1,2,106~3,2,106
4,4,179~6,4,179
4,1,167~4,2,167
0,3,156~0,5,156
8,1,275~8,1,276
6,1,14~6,1,16
6,8,59~6,8,61
4,0,5~4,2,5
5,1,261~5,3,261
1,3,140~1,4,140
7,9,52~8,9,52
1,5,285~3,5,285
1,7,248~2,7,248
1,3,16~1,3,19
3,8,210~3,8,212
6,3,99~6,4,99
2,6,220~2,8,220
1,0,18~1,2,18
8,6,161~8,6,164
4,2,311~4,2,311
6,9,30~9,9,30
9,3,125~9,3,126
0,2,94~0,2,97
4,2,323~7,2,323
2,1,31~2,3,31
2,7,249~2,8,249
4,6,47~6,6,47
0,9,241~2,9,241
6,4,232~6,4,233
2,7,320~5,7,320
2,1,93~6,1,93
0,5,151~1,5,151
3,0,16~6,0,16
6,5,188~8,5,188
6,0,192~8,0,192
3,1,304~5,1,304
3,6,204~3,7,204
6,8,263~9,8,263
4,6,76~4,9,76
6,2,5~8,2,5
1,0,227~1,1,227
2,2,188~2,4,188
4,4,226~4,6,226
4,9,236~4,9,239
1,2,179~1,4,179
1,0,211~3,0,211
3,3,234~3,5,234
5,9,225~7,9,225
8,2,104~9,2,104
0,8,290~2,8,290
3,8,234~5,8,234
3,2,247~3,2,249
6,5,56~8,5,56
1,6,227~4,6,227
6,3,209~8,3,209
4,7,82~4,8,82
6,9,301~9,9,301
8,2,141~8,2,143
0,6,251~0,8,251
0,9,153~2,9,153
7,8,194~8,8,194
6,5,29~8,5,29
5,4,107~5,7,107
4,6,50~4,6,50
2,5,23~2,7,23
3,0,150~3,1,150
0,9,288~3,9,288
5,5,181~5,6,181
7,9,235~9,9,235
9,6,45~9,8,45
0,3,270~2,3,270
5,9,245~7,9,245
7,7,105~7,7,106
8,8,87~8,8,88
2,0,207~4,0,207
2,4,15~4,4,15
8,0,307~8,2,307
5,2,209~9,2,209
1,4,232~3,4,232
3,1,211~6,1,211
1,1,81~1,3,81
5,2,188~5,4,188
2,2,114~2,5,114
2,6,231~2,8,231
3,8,113~5,8,113
0,1,114~4,1,114
3,5,72~3,8,72
9,2,173~9,3,173
4,8,260~7,8,260
3,4,157~5,4,157
1,1,25~1,4,25
5,6,191~5,8,191
3,3,41~3,3,43
4,4,180~4,4,183
4,9,29~4,9,32
5,0,41~5,3,41
3,3,73~5,3,73
7,0,213~7,1,213
7,5,183~9,5,183
7,4,127~7,6,127
3,6,267~3,7,267
6,9,173~8,9,173
2,4,179~2,6,179
9,8,177~9,9,177
0,1,151~0,3,151
4,1,32~6,1,32
3,1,166~6,1,166
2,3,193~2,5,193
8,0,36~8,2,36
2,4,241~2,6,241
6,6,41~6,8,41
6,4,68~6,6,68
0,6,130~0,8,130
6,5,217~6,7,217
6,3,295~6,5,295
6,3,127~6,3,129
6,9,131~8,9,131
3,4,206~3,5,206
6,0,6~8,0,6
0,3,220~0,5,220
1,8,258~1,8,258
5,2,111~9,2,111
8,1,272~8,4,272
6,4,11~6,6,11
2,8,144~3,8,144
4,4,262~4,6,262
5,8,256~7,8,256
3,5,82~4,5,82
9,1,44~9,4,44
9,7,290~9,7,292
2,9,131~4,9,131
3,8,137~5,8,137
0,5,217~2,5,217
1,2,241~2,2,241
2,7,227~5,7,227
6,9,196~8,9,196
8,6,103~8,8,103
3,4,322~3,5,322
3,3,34~5,3,34
8,0,144~8,2,144
0,7,207~0,8,207
1,9,26~4,9,26
7,2,313~7,5,313
5,0,202~5,2,202
5,5,124~5,6,124
4,2,38~4,4,38
5,1,70~5,3,70
4,4,265~7,4,265
0,6,118~1,6,118
2,4,199~2,6,199
7,5,192~8,5,192
5,9,216~7,9,216
9,2,134~9,4,134
5,8,164~8,8,164
1,9,111~1,9,114
2,3,93~2,6,93
3,3,304~3,3,306
5,7,194~5,7,196
3,3,151~6,3,151
5,2,172~5,2,172
4,3,160~4,4,160
6,9,136~9,9,136
6,4,134~8,4,134
4,7,240~4,7,241
4,9,20~4,9,22
7,1,206~7,4,206
2,3,266~2,6,266
1,3,184~1,5,184
7,3,299~8,3,299
3,9,130~5,9,130
1,3,139~1,4,139
4,1,129~4,2,129
2,2,275~2,2,278
8,7,23~8,9,23
8,7,197~9,7,197
9,2,76~9,5,76
6,5,285~8,5,285
8,8,31~8,9,31
8,5,282~8,7,282
6,2,303~6,3,303
2,7,164~4,7,164
4,0,120~4,0,122
4,3,95~4,3,95
5,3,168~7,3,168
8,7,307~8,9,307
5,5,312~5,8,312
3,2,118~3,6,118
1,3,130~1,6,130
5,4,155~6,4,155
4,6,230~4,8,230
1,7,245~3,7,245
5,7,280~8,7,280
6,7,286~9,7,286
8,1,52~8,2,52
7,0,166~8,0,166
4,3,13~4,5,13
4,6,112~4,7,112
3,4,262~3,4,263
5,7,216~7,7,216
5,5,14~5,6,14
3,9,241~4,9,241
3,4,40~3,6,40
5,0,137~5,0,141
1,1,206~4,1,206
1,9,290~1,9,291
6,9,246~8,9,246
6,2,236~6,3,236
1,4,291~3,4,291
4,3,259~6,3,259
4,7,300~4,8,300
0,6,106~3,6,106
5,8,291~5,8,291
3,8,213~3,8,215
5,6,218~5,7,218
6,0,230~9,0,230
3,3,302~5,3,302
3,3,37~3,4,37
8,4,16~8,6,16
0,0,121~3,0,121
2,6,175~6,6,175
1,2,112~3,2,112
0,2,130~3,2,130
7,1,98~7,2,98
6,6,220~6,7,220
9,8,212~9,9,212
2,7,100~2,8,100
6,8,239~6,8,243
3,7,83~6,7,83
2,3,163~3,3,163
2,3,192~2,6,192
9,3,225~9,6,225
4,0,55~7,0,55
3,3,235~6,3,235
8,0,106~9,0,106
7,2,173~7,2,175
9,1,113~9,2,113
6,0,18~6,0,20
7,4,205~9,4,205
6,8,172~8,8,172
4,8,129~6,8,129
7,2,145~7,4,145
1,4,143~2,4,143
0,1,128~2,1,128
5,0,220~6,0,220
2,2,86~6,2,86
9,5,202~9,6,202
0,4,128~0,5,128
8,0,189~8,2,189
1,4,112~1,4,114
2,6,203~2,8,203
2,1,14~2,2,14
2,1,155~2,3,155
3,8,142~5,8,142
8,2,284~9,2,284
1,1,189~3,1,189
5,3,181~5,4,181
1,2,104~1,5,104
7,5,290~7,7,290
0,2,291~0,4,291
5,3,180~5,5,180
7,6,214~7,9,214
6,3,17~9,3,17
4,0,134~4,2,134
2,0,118~5,0,118
0,0,282~3,0,282
5,3,100~5,6,100
1,6,52~1,8,52
2,5,221~3,5,221
3,1,146~3,4,146
7,8,52~8,8,52
1,3,13~1,4,13
7,0,7~9,0,7
6,0,329~9,0,329
2,4,162~3,4,162
1,9,154~3,9,154
7,4,93~9,4,93
8,1,129~8,4,129
0,0,231~0,0,235
4,6,125~5,6,125
8,9,215~8,9,217
5,3,28~8,3,28
6,3,282~6,6,282
3,2,170~5,2,170
4,4,150~4,4,152
6,4,58~6,6,58
4,7,156~6,7,156
2,3,315~4,3,315
8,7,165~8,9,165
3,8,50~5,8,50
9,0,228~9,1,228
5,5,134~8,5,134
6,2,99~6,2,99
6,4,61~6,5,61
0,8,206~2,8,206
5,2,226~5,4,226
2,3,287~2,6,287
2,5,121~4,5,121
6,7,291~6,7,292
8,0,250~8,2,250
5,5,123~5,7,123
6,6,228~8,6,228
8,0,92~8,3,92
0,2,88~2,2,88
0,2,150~3,2,150
4,3,16~4,3,19
8,7,195~8,9,195
1,8,333~3,8,333
2,8,207~4,8,207
7,6,229~9,6,229
1,5,236~4,5,236
7,4,30~7,6,30
0,6,52~0,8,52
4,5,31~4,5,32
7,6,308~9,6,308
1,6,105~1,8,105
3,0,196~3,3,196
2,5,15~2,6,15
1,7,120~3,7,120
9,2,192~9,2,194
4,0,239~4,3,239
1,0,13~4,0,13
7,5,162~7,7,162
7,3,325~7,5,325
3,5,242~3,7,242
8,4,304~8,5,304
8,0,256~9,0,256
2,9,236~2,9,239
4,6,263~7,6,263
4,6,190~4,8,190
3,6,288~3,7,288
4,2,25~4,4,25
2,5,215~2,7,215
3,1,47~3,3,47
0,2,126~0,5,126
1,0,9~1,3,9
0,1,104~0,4,104
6,6,303~8,6,303
1,2,109~1,5,109
4,2,131~4,2,133
7,0,217~7,1,217
8,4,22~8,6,22
7,3,41~7,3,44
7,7,306~7,8,306
7,1,131~8,1,131
2,7,201~4,7,201
8,3,24~8,6,24
3,7,3~3,7,3
2,4,115~2,5,115
7,3,20~7,6,20
3,8,132~5,8,132
6,9,133~8,9,133
6,2,231~6,4,231
7,6,298~9,6,298
6,6,93~6,9,93
5,6,44~7,6,44
1,7,144~4,7,144
6,4,77~6,7,77
1,7,78~1,9,78
3,5,241~6,5,241
1,3,313~4,3,313
0,3,29~2,3,29
4,6,293~6,6,293
1,9,225~2,9,225
5,1,216~8,1,216
1,0,272~2,0,272
6,7,130~6,9,130
5,7,8~5,9,8
9,2,281~9,4,281
5,6,145~5,9,145
0,1,232~0,3,232
0,3,290~2,3,290
3,3,143~3,4,143
2,6,49~3,6,49
5,2,88~5,4,88
2,3,173~2,4,173
7,3,165~9,3,165
5,3,68~5,5,68
6,0,321~8,0,321
4,0,325~5,0,325
2,4,22~4,4,22
8,5,17~8,5,19
9,4,206~9,5,206
0,4,53~0,6,53
4,6,49~6,6,49
8,0,85~8,2,85
8,5,101~8,7,101
1,4,53~1,7,53
7,1,320~7,4,320
1,7,256~1,8,256
2,6,95~2,8,95
7,1,3~9,1,3
7,3,202~7,3,204
4,1,100~4,3,100
4,5,196~6,5,196
2,6,224~2,9,224
2,2,239~2,5,239
7,0,119~7,2,119
7,2,87~9,2,87
8,9,302~8,9,304
0,5,49~1,5,49
0,3,274~2,3,274
0,8,221~2,8,221
9,4,80~9,6,80
0,3,221~0,6,221
4,5,120~4,7,120
9,8,2~9,8,2
2,7,74~4,7,74
8,5,331~8,7,331
5,2,249~5,4,249
2,1,49~4,1,49
1,0,216~1,2,216
4,7,128~4,9,128
1,6,219~1,9,219
7,7,42~9,7,42
6,5,8~9,5,8
1,0,286~1,1,286
3,2,85~3,4,85
7,8,254~8,8,254
5,2,272~5,3,272
1,4,296~1,5,296
6,0,249~6,2,249
5,4,229~6,4,229
1,1,56~4,1,56
6,5,94~6,7,94
7,1,140~7,3,140
7,1,6~8,1,6
0,2,20~0,2,21
0,0,285~1,0,285
2,7,176~5,7,176
6,1,29~6,3,29
2,4,90~2,4,91
0,3,134~1,3,134
8,6,214~8,9,214
0,0,249~0,2,249
2,4,260~4,4,260
7,1,177~9,1,177
8,9,32~9,9,32
0,3,227~2,3,227
8,0,211~8,2,211
6,3,296~7,3,296
5,1,43~5,4,43
1,4,132~1,6,132
0,4,284~1,4,284
6,3,290~6,4,290
9,6,164~9,6,164
2,0,52~4,0,52
0,8,332~2,8,332
6,9,305~9,9,305
2,1,329~4,1,329
3,5,318~3,8,318
3,0,39~5,0,39
3,1,180~5,1,180
5,1,33~7,1,33
6,5,35~8,5,35
6,5,176~6,6,176
6,4,143~9,4,143
9,5,162~9,6,162
3,5,149~3,5,150
0,7,254~3,7,254
7,8,299~7,9,299
2,3,224~2,5,224
7,3,295~7,5,295
1,7,275~2,7,275
4,2,299~4,3,299
4,2,306~4,2,308
6,0,253~8,0,253
8,3,195~8,6,195
0,5,148~2,5,148
5,3,273~5,3,273
5,9,222~6,9,222
4,2,271~4,4,271
9,0,90~9,2,90
3,4,84~5,4,84
2,5,106~4,5,106
8,2,135~8,5,135
1,9,135~1,9,136
6,6,193~9,6,193
0,4,18~0,6,18
0,1,59~3,1,59
1,8,103~3,8,103
1,2,193~4,2,193
1,1,60~1,3,60
2,3,153~4,3,153
6,4,72~6,7,72
9,0,119~9,2,119
6,0,23~6,0,25
9,1,14~9,3,14
3,7,298~3,9,298
3,1,267~5,1,267
4,6,217~4,8,217"""
}