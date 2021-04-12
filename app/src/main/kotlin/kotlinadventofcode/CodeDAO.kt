package kotlinadventofcode

import java.nio.charset.StandardCharsets.UTF_8
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

data class DayId(val year: Int, val day: Int)
data class ProblemId(val dayId: DayId, val part: Int)

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

    fun getState(): CodeState {
        println(srcPath.toString())
        val problemEnumCode: List<String> = Files.readAllLines(mainSrcPath.resolve("Problem.kt"), UTF_8)
        if (problemEnumCode.count { it.contains(';') } != 1) {
            throw Exception("Expected Problem.kt to have exactly one semicolon: the last enum instance.")
        }

        // This is expected to start with `yyyy-dd-p
        val lastInstance: String = problemEnumCode.first { it.contains(';') }.trim()
        val year = lastInstance.substring(1, 5).toInt()
        val day = lastInstance.substring(6, 8).toInt()
        val part = lastInstance.substring(9, 10).toInt()

        val testCode: List<String> =
            Files.readAllLines(
                testSrcPath
                    .resolve("$year")
                    .resolve("$year-${day.toString().padStart(2, '0')}-Test.kt"),
                UTF_8)

        return CodeState(
                ProblemId(DayId(year, day), part),
                testCode.any { it.contains("testDefaultPart1") },
                testCode.any { it.contains("testDefaultPart2") })
    }

    fun generateProblem(problemId: ProblemId) {
        throw Exception("not impl")
    }

    fun generateTest(problemId: ProblemId) {
        throw Exception("not impl")
    }
}