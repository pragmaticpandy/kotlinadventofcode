package kotlinadventofcode

import java.math.BigInteger

interface Day {
    val year: Int get() = this::class.simpleName!!.substring(0,4).toInt()
    val day: Int get() = this::class.simpleName!!.substring(5,7).toInt()
    val defaultInput: String

    fun runPart1(input: String, ui: UI): String {
        return runPart1(input)
    }

    fun runPart1(input: String): String {
        return runPart1(input, object : UI {
            override fun setResult(r: String) {}
            override fun show(taskName: String, totalSteps: Int?, func: TaskScope.() -> Unit) {
                show(taskName, totalSteps?.toBigInteger(), func)
            }

            override fun show(taskName: String, totalSteps: BigInteger?, func: TaskScope.() -> Unit) {
                val taskScope = object : TaskScope {
                    override fun did(amount: BigInteger) {}
                }

                taskScope.apply(func)
            }
        })
    }

    fun runPart1(): String {
        return runPart1(defaultInput)
    }

    fun runPart1(ui: UI): String {
        return runPart1(defaultInput, ui)
    }

    fun runPart2(input: String, ui: UI): String {
        return runPart2(input)
    }

    fun runPart2(input: String): String {
        error("runPart2 not implemented.")
    }

    fun runPart2(): String {
        return runPart2(defaultInput)
    }

    fun runPart2(ui: UI): String {
        return runPart2(defaultInput, ui)
    }
}