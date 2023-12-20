package kotlinadventofcode

import kotlinadventofcode.UI.NoOpUI
import kotlinadventofcode.UI.UI

interface Day {
    val year: Int get() = this::class.simpleName!!.substring(0,4).toInt()
    val day: Int get() = this::class.simpleName!!.substring(5,7).toInt()
    val defaultInput: String

    context(UI)
    fun runPartOne(input: String): String {
        return runPartOneNoUI(input)
    }

    context(UI)
    fun runPartOne(): String {
        return runPartOne(defaultInput)
    }

    context(UI)
    fun runPartTwo(input: String): String {
        return runPartTwoNoUI(input)
    }

    context(UI)
    fun runPartTwo(): String {
        return runPartTwo(defaultInput)
    }

    fun runPartOneNoUI(input: String): String {
        with (NoOpUI) {
            return runPartOne(input)
        }
    }

    fun runPartOneNoUI(): String {
        return runPartOneNoUI(defaultInput)
    }

    fun runPartTwoNoUI(input: String): String {
        with (NoOpUI) {
            return runPartTwo(input)
        }
    }

    fun runPartTwoNoUI(): String {
        return runPartTwoNoUI(defaultInput)
    }
}