package kotlinadventofcode

import kotlinadventofcode.`2015`.`2015-01`

/**
 * Using enum to get PicoCli help text and auto-complete benefits. Make sure to declare newest problems last because
 * that will be the default day.
 */
enum class Problem(val day: Day, val part: Int): Day by day {
    `2015-01-1`(`2015-01`(), 1),
    `2015-01-2`(`2015-01`(), 2);

    fun run(): String {
        return if (part == 1) day.runPart1() else day.runPart2()
    }
}
