package kotlinadventofcode

import picocli.CommandLine
import picocli.CommandLine.Command
import picocli.CommandLine.Option
import picocli.CommandLine.Parameters

import java.io.File
import java.math.BigInteger
import java.nio.file.Files
import java.security.MessageDigest
import java.util.concurrent.Callable
import kotlin.system.exitProcess

const val longDescription = """
This command helps you manage a basic state machine for solving problems sequentially.

When starting a new day, you'll be prompted REPL-style for the problem year, day, and input; and then an implementation file will be generated. Part 1 for this day will now be the default executed problem when you run `ka`.

After you implement part 1 and successfully validate that your solution is correct on the AoC site, run this command again and you will be prompted for your answer. With that solution, a test will be generated for your input and solution. Also, part 2 will now be the default executed problem when you run `ka`.

After you implement part 2 and successfully validate that your solution is correct on the AoC site, run this command again and you will be prompted for your solution. With that, a test will be generated for your input and solution.

Rinse—err, commit—and repeat.
"""

@Command(
    name = "continue",
    description = [ "Generates boilerplate code and tests.", longDescription ]
)
class Continue(val codeDAO: CodeDAO = CodeDAO()): Callable<Int> {

    @Option(names = ["--help", "-h"], usageHelp = true, hidden = true)
    var help = false

    override fun call(): Int {
        val state = codeDAO.getState()
        val latestYear = state.latestProblemId.year
        val latestDay = state.latestProblemId.day

        if (state.partOneTested && state.partTwoTested) {

            // Time for a new day.

            println(
                """
                    Looks like $latestYear day $latestDay part two is the last problem defined in
                    the Problem enum and it's tested. So, we'll start a new day.
                """.trimIndent())

            print("Enter year, or leave blank to use $latestYear: ")
            val yearInput: String = readLine() ?: "" // If input is file and we reach the end.
            val year = if (yearInput == "") latestYear else yearInput.toInt()

            val defaultDay = if (year == latestYear) latestDay + 1 else 1
            print("Enter day, or leave blank to use $defaultDay: ")
            val dayInput: String = readLine() ?: "" // If input is file and we reach the end.
            val day = if (dayInput == "") defaultDay else dayInput.toInt()

            println("Okay, will generate $year day $day.")

            print(
                """
                    Enter problem input (won't work for some inputs, such as blank lines mid-input—
                    in these cases just put whatever for now and edit the generated file afterwards): 
                """.trimIndent())

            val inputLines: MutableList<String> = mutableListOf()
            do {
                val line: String = readLine() ?: "" // If input is file and we reach the end.
                inputLines += line
            } while (line != "")

            codeDAO.generateProblem(
                ProblemId(DayId(year, day), 1),
                inputLines.joinToString("\n").trim()
            )

            println("Done. See the new file for that day to get started.")
        } else {
            val part: Int = if (!state.partOneTested && !state.partTwoTested) 1 else 2
            print(
                """
                    After verifying your $latestYear day $latestDay part $part solution on the
                    Advent of Code site, enter it here: 
                """.trimIndent())

            val input: String = readLine() ?: "" // If input is file and we reach the end.
            if (input == "") throw Exception("Nothing was entered or the input file was empty.")

            codeDAO.generateTest(ProblemId(DayId(latestYear, latestDay), part), input)

            val farewell: String =
                if (part == 1) "Good luck on part 2."
                else "Run `./ka continue` again to start the next day."

            println("Done. $farewell")
        }

        return 0
    }
}
