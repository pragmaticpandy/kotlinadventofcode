package kotlinadventofcode

// DO NOT REMOVE ME. Just a lowly comment here trying to help with code generation.
import kotlinadventofcode.`2023`.`2023-01`
import kotlinadventofcode.`2016`.`2016-01`
import kotlinadventofcode.`2022`.`2022-18`
import kotlinadventofcode.`2022`.`2022-17`
import kotlinadventofcode.`2022`.`2022-16`
import kotlinadventofcode.`2022`.`2022-15`
import kotlinadventofcode.`2022`.`2022-14`
import kotlinadventofcode.`2022`.`2022-13`
import kotlinadventofcode.`2022`.`2022-12`
import kotlinadventofcode.`2022`.`2022-11`
import kotlinadventofcode.`2022`.`2022-10`
import kotlinadventofcode.`2022`.`2022-09`
import kotlinadventofcode.`2022`.`2022-08`
import kotlinadventofcode.`2022`.`2022-07`
import kotlinadventofcode.`2022`.`2022-06`
import kotlinadventofcode.`2022`.`2022-05`
import kotlinadventofcode.`2022`.`2022-04`
import kotlinadventofcode.`2022`.`2022-03`
import kotlinadventofcode.`2022`.`2022-02`
import kotlinadventofcode.`2022`.`2022-01`
import kotlinadventofcode.`2015`.`2015-21`
import kotlinadventofcode.`2015`.`2015-20`
import kotlinadventofcode.`2015`.`2015-19`
import kotlinadventofcode.`2015`.`2015-18`
import kotlinadventofcode.`2015`.`2015-17`
import kotlinadventofcode.`2015`.`2015-16`
import kotlinadventofcode.`2015`.`2015-15`
import kotlinadventofcode.`2015`.`2015-14`
import kotlinadventofcode.`2015`.`2015-13`
import kotlinadventofcode.`2015`.`2015-12`
import kotlinadventofcode.`2015`.`2015-11`
import kotlinadventofcode.`2015`.`2015-10`
import kotlinadventofcode.`2015`.`2015-09`
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
    `2015-08-2`(`2015-08`(), 2),
    `2015-09-1`(`2015-09`(), 1),
    `2015-09-2`(`2015-09`(), 2),
    `2015-10-1`(`2015-10`(), 1),
    `2015-10-2`(`2015-10`(), 2),
    `2015-11-1`(`2015-11`(), 1),
    `2015-11-2`(`2015-11`(), 2),
    `2015-12-1`(`2015-12`(), 1),
    `2015-12-2`(`2015-12`(), 2),
    `2015-13-1`(`2015-13`(), 1),
    `2015-13-2`(`2015-13`(), 2),
    `2015-14-1`(`2015-14`(), 1),
    `2015-14-2`(`2015-14`(), 2),
    `2015-15-1`(`2015-15`(), 1),
    `2015-15-2`(`2015-15`(), 2),
    `2015-16-1`(`2015-16`(), 1),
    `2015-16-2`(`2015-16`(), 2),
    `2015-17-1`(`2015-17`(), 1),
    `2015-17-2`(`2015-17`(), 2),
    `2015-18-1`(`2015-18`(), 1),
    `2015-18-2`(`2015-18`(), 2),
    `2015-19-1`(`2015-19`(), 1),
    `2015-19-2`(`2015-19`(), 2),
    `2015-20-1`(`2015-20`(), 1),
    `2015-20-2`(`2015-20`(), 2),
    `2015-21-1`(`2015-21`(), 1),
    `2015-21-2`(`2015-21`(), 2),
    `2022-01-1`(`2022-01`(), 1),
    `2022-01-2`(`2022-01`(), 2),
    `2022-02-1`(`2022-02`(), 1),
    `2022-02-2`(`2022-02`(), 2),
    `2022-03-1`(`2022-03`(), 1),
    `2022-03-2`(`2022-03`(), 2),
    `2022-04-1`(`2022-04`(), 1),
    `2022-04-2`(`2022-04`(), 2),
    `2022-05-1`(`2022-05`(), 1),
    `2022-05-2`(`2022-05`(), 2),
    `2022-06-1`(`2022-06`(), 1),
    `2022-06-2`(`2022-06`(), 2),
    `2022-07-1`(`2022-07`(), 1),
    `2022-07-2`(`2022-07`(), 2),
    `2022-08-1`(`2022-08`(), 1),
    `2022-08-2`(`2022-08`(), 2),
    `2022-09-1`(`2022-09`(), 1),
    `2022-09-2`(`2022-09`(), 2),
    `2022-10-1`(`2022-10`(), 1),
    `2022-10-2`(`2022-10`(), 2),
    `2022-11-1`(`2022-11`(), 1),
    `2022-11-2`(`2022-11`(), 2),
    `2022-12-1`(`2022-12`(), 1),
    `2022-12-2`(`2022-12`(), 2),
    `2022-13-1`(`2022-13`(), 1),
    `2022-13-2`(`2022-13`(), 2),
    `2022-14-1`(`2022-14`(), 1),
    `2022-14-2`(`2022-14`(), 2),
    `2022-15-1`(`2022-15`(), 1),
    `2022-15-2`(`2022-15`(), 2),
    `2022-16-1`(`2022-16`(), 1),
    `2022-16-2`(`2022-16`(), 2),
    `2022-17-1`(`2022-17`(), 1),
    `2022-17-2`(`2022-17`(), 2),
    `2022-18-1`(`2022-18`(), 1),
    `2022-18-2`(`2022-18`(), 2),
    `2016-01-1`(`2016-01`(), 1),
    `2016-01-2`(`2016-01`(), 2),
    `2023-01-1`(`2023-01`(), 1),
    `2023-01-2`(`2023-01`(), 2);

    fun run(): String {
        return if (part == 1) day.runPart1() else day.runPart2()
    }
}