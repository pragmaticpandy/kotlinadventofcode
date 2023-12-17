package kotlinadventofcode

import com.varabyte.kotter.foundation.*
import com.varabyte.kotter.foundation.anim.text
import com.varabyte.kotter.foundation.anim.textAnimOf
import com.varabyte.kotter.foundation.collections.liveListOf
import com.varabyte.kotter.foundation.text.*
import com.varabyte.kotter.foundation.timer.addTimer
import java.math.BigInteger
import java.time.Instant
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeSource

class Kotter {

    val gray = 0x838383

    fun run(problem: Problem, program: (UI) -> Unit) = session(clearTerminal = true) {
        var result by liveVarOf<String?>(null)
        val circleAnim = textAnimOf(listOf("◐", "◓", "◑", "◒"), 800.milliseconds)
        val tasks = liveListOf<Task>()
        val timeSource = TimeSource.Monotonic
        val startMark = timeSource.markNow()
        val startInstant = Instant.now()
        var endInstant by liveVarOf<Instant?>(null)
        var elapsedTime by liveVarOf(timeSource.markNow() - startMark)

        val ui = object : UI {
            override fun setResult(r: String) {
                result = r
            }

            override fun show(taskName: String, totalSteps: BigInteger?, func: TaskScope.() -> Unit) {
                var completed by liveVarOf(false)
                var stepsCompleted by liveVarOf(BigInteger.ZERO)
                val task = Task(
                    taskName,
                    isCompleted = { completed },
                    getStepsCompleted = { stepsCompleted },
                    totalSteps,
                    Instant.now(),
                    timeSource.markNow(),
                    null,
                    null
                )

                val taskScope = object : TaskScope {
                    override fun did(amount: BigInteger) {
                        stepsCompleted += amount
                    }
                }

                tasks += task
                taskScope.apply(func)
                task.endInstant = Instant.now()
                task.endMark = timeSource.markNow()
                completed = true
            }
        }

        section {
            p {
                result?.let { green { text("✓") } } ?: cyan { text(circleAnim) }
                text(" ")
                underline { textLine("${problem.year} day ${problem.day} part ${problem.part}") }

                text(getIndent(1))
                rgb(gray) {
                    text("Elapsed: ${elapsedTime.toComponents { h, m, s, _ -> "$h:$m:$s" }}")
                    text("  Start: $startInstant")
                    endInstant?.let { text("  End: $it") }
                }
            }
            p {
                tasks.forEach { task ->
                    text(getIndent(1))
                    if(task.isCompleted()) green { text("✓") } else cyan { text(circleAnim) }
                    text(" ")
                    underline { textLine(task.name) }

                    if (task.totalSteps != null || task.getStepsCompleted() != BigInteger.ZERO) {
                        text(getIndent(2))
                        if (!task.isCompleted()) task.totalSteps?.let {
                            val percentComplete = ((task.getStepsCompleted().toDouble() / it.toDouble()) * 100.0).toInt()
                            val numBarsComplete = percentComplete / 5
                            cyan(ColorLayer.BG) { text(" ".repeat(numBarsComplete)) }
                            rgb(gray, ColorLayer.BG) { text(" ".repeat(20 - numBarsComplete)) }
                            text(" $percentComplete%  ")
                        }
                        text("Did ${task.getStepsCompleted()}")
                        if (!task.isCompleted()) task.totalSteps?.let { text(" of $it") }
                        textLine(".")
                    }

                    rgb(gray) {
                        val taskElapsedTime =
                            (task.endMark ?: timeSource.markNow()) - task.startMark
                        text(getIndent(2))
                        text("Elapsed: ${taskElapsedTime.toComponents { h, m, s, _ -> "$h:$m:$s" }}")
                        text("  Start: ${task.startInstant}")
                        task.endInstant?.let { text("  End: $it") }
                    }
                }
            }
            p {
                result?.let { text("Result: "); bold { text(it) } }
            }
        }.run{
            addTimer(1.seconds, repeat = true) {
                elapsedTime = timeSource.markNow() - startMark
            }

            program(ui)
            endInstant = Instant.now()
        }
    }

    private class Task(
        val name: String,
        val isCompleted: () -> Boolean,
        val getStepsCompleted: () -> BigInteger,
        val totalSteps: BigInteger?,
        val startInstant: Instant,
        val startMark: TimeSource.Monotonic.ValueTimeMark,
        var endInstant: Instant?,
        var endMark: TimeSource.Monotonic.ValueTimeMark?
    )

    private fun getIndent(level: Int) = "    ".repeat(level)
}