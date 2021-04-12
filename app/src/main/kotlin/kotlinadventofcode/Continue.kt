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

        if (state.partOneTested && state.partTwoTested) {

            // Time for a new day.

            val latestYear = state.latestProblemId.dayId.year
            val latestDay = state.latestProblemId.dayId.day

            println(
                """
                    Looks like $latestYear day $latestDay part two is the last problem defined in
                    the Problem enum and it's tested. So, we'll start a new day.
                """.trimIndent())

            print("Enter year, or leave blank to use $latestYear: ")
            val yearInput: String = readLine() ?: throw Exception("End of input file reached.")
            val year = if (yearInput == "") latestYear else yearInput.toInt()

            val defaultDay = if (year == latestYear) latestDay + 1 else 1
            print("Enter day, or leave blank to use $defaultDay: ")
            val dayInput: String = readLine() ?: throw Exception("End of input file reached.")
            val day = if (dayInput == "") defaultDay else dayInput.toInt()

            print("Okay, generating $year day $day... ")

            codeDAO.generateProblem(ProblemId(DayId(year, day), 1))

            println("done.")
        }

        return 0
    }
}
