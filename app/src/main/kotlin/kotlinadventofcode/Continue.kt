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

@Command(
    name = "continue",
    description = ["Generates the scaffolding for the next problem."],
)
class Continue(val codeDAO: CodeDAO = CodeDAO()): Callable<Int> {

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
                    Enter problem input (won't work for inputs with blank lines in between stuff—in
                    this case just put whatever for now and edit the generated file afterwards):
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
                    After verifying your $latestYear day $latestDay part $part solution on the Advent of
                    Code site, enter it here: 
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
