// Originally generated by the template in CodeDAO
package kotlinadventofcode.`2015`

import com.github.h0tk3y.betterParse.combinators.*
import com.github.h0tk3y.betterParse.grammar.Grammar
import com.github.h0tk3y.betterParse.grammar.parseToEnd
import com.github.h0tk3y.betterParse.lexer.literalToken
import com.github.h0tk3y.betterParse.lexer.regexToken
import kotlinadventofcode.Day
import kotlin.math.min

class `2015-14` : Day {

    data class Reindeer(val name: String, val speed: Int, val flyTime: Int, val restTime: Int) {
        fun distanceAfter(seconds: Int) : Int {
            var rested = true
            var remainingSeconds = seconds
            var distance: Int = 0

            while (remainingSeconds > 0) {
                if (rested) {
                    val thisFlightTime = min(flyTime, remainingSeconds)
                    remainingSeconds -= thisFlightTime
                    distance += speed * thisFlightTime
                } else {
                    remainingSeconds -= restTime
                }

                rested = !rested
            }

            return distance
        }
    }

    fun parse(input: String): List<Reindeer> {
        val grammar = object: Grammar<List<Reindeer>>() {
            val canFly by literalToken(" can fly ")
            val kmPerSecFor by literalToken(" km/s for ")
            val butMustRestFor by literalToken(" seconds, but then must rest for ")
            val seconds by literalToken(" seconds.")
            val newline by literalToken("\n")

            val numberToken by regexToken("""\d+""")
            val number by numberToken use { text.toInt() }
            val name by regexToken("""[A-Z][a-z]+""")

            val reindeer by (name use { text }) and
                skip(canFly) and
                number and
                skip(kmPerSecFor) and
                number and
                skip(butMustRestFor) and
                number and
                skip(seconds)

            override val rootParser by separatedTerms(reindeer map { Reindeer(it.t1, it.t2, it.t3, it.t4)}, newline)
        }

        return grammar.parseToEnd(input)
    }

    /**
     * After verifying your solution on the AoC site, run `./ka continue` to add a test for it.
     */
    override fun runPart1(input: String): String {
        return parse(input).map { it.distanceAfter(2503) }.maxOrNull()?.toString()
            ?: throw Exception("No reindeer parsed.")
    }

    fun createReindeerStartState(reindeer: Reindeer) : ReindeerState {
        return ReindeerState(reindeer, 0, false, reindeer.flyTime)
    }

    data class ReindeerState(val reindeer: Reindeer, val distance: Int, val resting: Boolean, val remainingSeconds: Int) {
        fun next() : ReindeerState {
            val nextDistance = distance + if (!resting) reindeer.speed else 0
            val nextResting = if (remainingSeconds == 1) !resting else resting
            val nextRemainingSeconds = if (remainingSeconds != 1) remainingSeconds - 1
                else if (resting) reindeer.flyTime
                else reindeer.restTime

            return ReindeerState(reindeer, nextDistance, nextResting, nextRemainingSeconds)
        }
    }

    /**
     * After verifying your solution on the AoC site, run `./ka continue` to add a test for it.
     */
    override fun runPart2(input: String): String {
        var states = parse(input).map { createReindeerStartState(it) }
        val scoreByReindeer = states.associateBy( { it.reindeer.name }, { 0 }).toMutableMap()
        for (i in 1..2503) {
            states = states.map { it.next() }
            val longest = states.maxByOrNull { it.distance }?.distance ?: throw Exception("No reindeer parsed.")
            states
                .filter { it.distance == longest }
                .forEach { scoreByReindeer[it.reindeer.name] = scoreByReindeer[it.reindeer.name]!! + 1 }
        }

        return scoreByReindeer.values.maxOrNull().toString()
    }

    override val defaultInput = """Dancer can fly 27 km/s for 5 seconds, but then must rest for 132 seconds.
Cupid can fly 22 km/s for 2 seconds, but then must rest for 41 seconds.
Rudolph can fly 11 km/s for 5 seconds, but then must rest for 48 seconds.
Donner can fly 28 km/s for 5 seconds, but then must rest for 134 seconds.
Dasher can fly 4 km/s for 16 seconds, but then must rest for 55 seconds.
Blitzen can fly 14 km/s for 3 seconds, but then must rest for 38 seconds.
Prancer can fly 3 km/s for 21 seconds, but then must rest for 40 seconds.
Comet can fly 18 km/s for 6 seconds, but then must rest for 103 seconds.
Vixen can fly 18 km/s for 5 seconds, but then must rest for 84 seconds."""
}