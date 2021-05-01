package kotlinadventofcode

// DO NOT REMOVE ME. Just a lowly comment here trying to help with code generation.
import kotlinadventofcode.`2015`.`2015-08`
import kotlinadventofcode.`2015`.`2015-07`
import kotlinadventofcode.`2015`.`2015-06`
import kotlinadventofcode.`2015`.`2015-05`
import kotlinadventofcode.`2015`.`2015-04`
import kotlinadventofcode.`2015`.`2015-03`
import kotlinadventofcode.`2015`.`2015-02`
import kotlinadventofcode.`2015`.`2015-01`

/**
 * Using enum to get PicoCli help text and auto-complete benefits. Make sure to declare newest problems last because
 * that will be the default day.
 */
enum class Problem(val day: Day, val part: Int): Day by day {
    `2015-01-1`(`2015-01`(), 1),
    `2015-01-2`(`2015-01`(), 2),
    `2015-02-1`(`2015-02`(), 1),
    `2015-02-2`(`2015-02`(), 2),
    `2015-03-1`(`2015-03`(), 1),
    `2015-03-2`(`2015-03`(), 2),
    `2015-04-1`(`2015-04`(), 1),
    `2015-04-2`(`2015-04`(), 2),
    `2015-05-1`(`2015-05`(), 1),
    `2015-05-2`(`2015-05`(), 2),
    `2015-06-1`(`2015-06`(), 1),
    `2015-06-2`(`2015-06`(), 2),
    `2015-07-1`(`2015-07`(), 1),
    `2015-07-2`(`2015-07`(), 2),
    `2015-08-1`(`2015-08`(), 1),
    `2015-08-2`(`2015-08`(), 2);

    fun run(): String {
        return if (part == 1) day.runPart1() else day.runPart2()
    }
}