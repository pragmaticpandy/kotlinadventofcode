package kotlinadventofcode

import java.nio.charset.StandardCharsets.UTF_8
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

data class DayId(val year: Int, val day: Int)

data class ProblemId(val dayId: DayId, val part: Int) {
    val year get() = dayId.year
    val day get() = dayId.day
}

data class CodeState(
    val latestProblemId: ProblemId,
    val partOneTested: Boolean,
    val partTwoTested: Boolean
)

/**
 * Manages reading the state of the code and writing scaffolding code.
 *
 * Assumes days are always two digits.
 */
class CodeDAO(
    val srcPath: Path =
        Paths
            .get(System.getProperty("user.dir")) // kotlinadventofcode/app/build/install/ka/bin
            .parent // kotlinadventofcode/app/build/install/ka
            .parent // kotlinadventofcode/app/build/install
            .parent // kotlinadventofcode/app/build
            .parent // kotlinadventofcode/app
            .resolve("src")
) {

    private val mainSrcPath: Path get() =
        srcPath.resolve("main").resolve("kotlin").resolve("kotlinadventofcode")

    private val testSrcPath: Path get() =
        srcPath.resolve("test").resolve("kotlin").resolve("kotlinadventofcode")

    private val problemEnumPath: Path = mainSrcPath.resolve("Problem.kt")

    fun getState(): CodeState {
        val problemEnumCode: List<String> = getAndValidateProblemEnum()

        // This is expected to start with `yyyy-dd-p
        val lastInstance: String = problemEnumCode.first { it.contains(';') }.trim()
        val year = lastInstance.substring(1, 5).toInt()
        val day = lastInstance.substring(6, 8).toInt()
        val part = lastInstance.substring(9, 10).toInt()

        val testCode: List<String>? =
            try {
                Files.readAllLines(
                    testSrcPath
                        .resolve("$year")
                        .resolve("$year-${dayToString(day)}-Test.kt"),
                    UTF_8
                )
            } catch (e: Exception) {
                null
            }

        return CodeState(
                ProblemId(DayId(year, day), part),
                testCode?.any { it.contains("testDefaultPart1") } ?: false,
                testCode?.any { it.contains("testDefaultPart2") } ?: false)
    }

    fun generateProblem(problemId: ProblemId, input: String) {

        // Write the new impl file.
        val implPath =
            mainSrcPath
                .resolve(problemId.year.toString())
                .resolve("${problemId.year}-${dayToString(problemId.day)}.kt")

        Files.createDirectories(implPath.parent)
        if (problemId.part == 1) {
            Files.write(
                implPath,
                implTemplate
                    .replace(yearKey, problemId.year.toString())
                    .replace(dayKey, dayToString(problemId.day))
                    .replace(inputKey, input)
                    .toByteArray()
            )
        }

        // Write the new test file.
        val testFilePath: Path = getTestFilePath(problemId)
        Files.createDirectories(testFilePath.parent)
        Files.write(
            testFilePath,
            testFileTemplate
                .replace(yearKey, problemId.year.toString())
                .replace(dayKey, dayToString(problemId.day))
                .toByteArray()
        )

        // Put this new day, part 1 as a new instance in the Problem enum.
        val problemEnumCode: List<String> = getAndValidateProblemEnum()
        Files.write(
            problemEnumPath,
            problemEnumCode
                .map {
                    it
                        .replace(
                        ";",
                        ",\n    `${problemId.year}-${dayToString(problemId.day)}-${problemId.part}`"
                                + "(`${problemId.year}-${dayToString(problemId.day)}`(), ${problemId.part});")
                        .replace(
                            "// DO NOT REMOVE ME. Just a lowly comment here trying to help with code generation.",
                            "// DO NOT REMOVE ME. Just a lowly comment here trying to help with code generation."
                                + "\nimport kotlinadventofcode.`${problemId.year}`."
                                + "`${problemId.year}-${dayToString(problemId.day)}`") }
                .joinToString("\n")
                .toByteArray()
        )
    }

    private fun getTestFilePath(problemId: ProblemId): Path {
        return testSrcPath
            .resolve(problemId.year.toString())
            .resolve("${problemId.year}-${dayToString(problemId.day)}-Test.kt")
    }

    fun generateTest(problemId: ProblemId, expected: String) {
        val testFilePath: Path = getTestFilePath(problemId)

        if (problemId.part == 1) {

            // Put part 2 as the newest line in the Problem enum
            Files.write(
                problemEnumPath,
                getAndValidateProblemEnum()
                    .map {
                        it
                            .replace(
                                ";",
                                ",\n    `${problemId.year}-${dayToString(problemId.day)}-2`"
                                        + "(`${problemId.year}-${dayToString(problemId.day)}`(), 2);") }
                    .joinToString("\n")
                    .toByteArray()
            )
        }

        Files.write(
            testFilePath,
            Files
                .readAllLines(testFilePath, UTF_8)
                .map {
                    it.replace(
                        if (problemId.part == 1) testPart1PlaceholderComment else testPart2PlaceholderComment,
                        """
|@Test fun testDefaultPart${problemId.part.toString()}() {
|        assertEquals("$expected", `${problemId.year}-${dayToString(problemId.day)}`().runPart${if (problemId.part == 1) "One" else "Two"}NoUI())
|    }
                        """.trimMargin())})
    }

    private fun getAndValidateProblemEnum(): List<String> {
        val problemEnumCode: List<String> = Files.readAllLines(mainSrcPath.resolve("Problem.kt"), UTF_8)
        if (problemEnumCode.count { it.contains(';') } != 1) {
            throw Exception("Expected Problem.kt to have exactly one semicolon: the last enum instance.")
        }

        return problemEnumCode
    }

    private fun dayToString(day: Int): String {
        return day.toString().padStart(2, '0')
    }

    val yearKey: String = "🤪🙃😴"
    val dayKey: String = "🦴🐩💩"
    val inputKey: String = "🥑🔪✋"

    val testPart1PlaceholderComment: String =
        "// DO NOT DELETE. Part 1 placeholder. To populate, verify solution on AoC then `./ka continue`"

    val testPart2PlaceholderComment: String =
        "// DO NOT DELETE. Part 2 placeholder. To populate, verify solution on AoC then `./ka continue`"

    val implTemplate: String = """// Originally generated by the template in ${CodeDAO::class.simpleName}
package kotlinadventofcode.`$yearKey`

import com.github.h0tk3y.betterParse.combinators.*
import com.github.h0tk3y.betterParse.grammar.*
import com.github.h0tk3y.betterParse.lexer.*
import kotlinadventofcode.Day
import kotlinadventofcode.UI.UI

class `$yearKey-$dayKey` : Day {
    override val year: Int = $yearKey
    override val day: Int = $dayKey

    context(UI)
    override fun runPartOne(input: String): String {
        error("$yearKey day $dayKey part 1 isn't yet implemented.")
    }

    context(UI)
    override fun runPartTwo(input: String): String {
        error("$yearKey day $dayKey part 2 isn't yet implemented.")
    }

    companion object {

        /**
         * Example parser. Parses multiple lines of pairs of positive int ranges with this format:
         * 12-39,492-1093
         */
        private fun parse(input: String): List<Pair<IntRange, IntRange>> {
            val grammar = object : Grammar<List<Pair<IntRange, IntRange>>>() {

                // tokens
                val newlineLit by literalToken("\n")
                val commaLit by literalToken(",")
                val dashLit by literalToken("-")
                val positiveIntRegex by regexToken("\\d+")

                // parsers
                val positiveInt by positiveIntRegex use { text.toInt() }
                val range by (positiveInt and skip(dashLit) and positiveInt) map { it.t1..it.t2 }
                val ranges by (range and skip(commaLit) and range) map { it.t1 to it.t2 }
                override val rootParser by separatedTerms(ranges, newlineLit)
            }

            return grammar.parseToEnd(input)
        }
    }

    override val defaultInput = ${"\"\"\""}$inputKey${"\"\"\""}
}"""

    val testFileTemplate: String = """// Originally generated by the template in ${CodeDAO::class.simpleName}
package kotlinadventofcode.`$yearKey`

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class `$yearKey-$dayKey-Test` {

    $testPart1PlaceholderComment

    $testPart2PlaceholderComment

}"""

}
