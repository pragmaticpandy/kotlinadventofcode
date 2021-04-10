package kotlinadventofcode

interface Day {
    val defaultInput: String

    fun runPart1(input: String): String

    fun runPart1(): String {
        return runPart1(defaultInput)
    }

    fun runPart2(input: String): String

    fun runPart2(): String {
        return runPart2(defaultInput)
    }
}