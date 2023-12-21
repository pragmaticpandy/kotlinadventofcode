package kotlinadventofcode.UI

import com.varabyte.kotter.foundation.*
import com.varabyte.kotter.foundation.anim.text
import com.varabyte.kotter.foundation.anim.textAnimOf
import com.varabyte.kotter.foundation.text.*
import com.varabyte.kotter.foundation.timer.addTimer
import kotlinadventofcode.Problem
import java.math.BigInteger
import java.time.Instant
import java.util.concurrent.atomic.AtomicReference
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeSource

const val gray = 0x838383

fun runWithKotterUI(problem: Problem, program: (UI) -> Unit) = session(clearTerminal = true) {
    var result by liveVarOf<String?>(null)

    /**
     * Important note: since the task and vars can be updated really fast, we don't re-render when they are updated.
     * Re-rendering too fast definitely causes flickering and possibly degrades performance.
     * Instead, we rely on this animation to trigger re-renders.
     * If this animation is removed, a different timer to trigger re-renders should be added.
     */
    val spinnerAnim = textAnimOf(listOf("⠋", "⠙", "⠹", "⠸", "⠼", "⠴", "⠦", "⠧", "⠇", "⠏"), 70.milliseconds)

    val tasks = mutableListOf<Task>()
    val vars = mutableMapOf<String, String>()
    val timeSource = TimeSource.Monotonic
    val startMark = timeSource.markNow()
    val startInstant = Instant.now()
    var endInstant by liveVarOf<Instant?>(null)
    var elapsedTime by liveVarOf(timeSource.markNow() - startMark)

    val ui = object : UI {
        override fun setResult(r: String) {
            result = r
        }

        override fun <T> shown(taskName: String, totalSteps: BigInteger?, func: TaskScope.() -> T): T {
            val task = Task(taskName, totalSteps)

            val taskScope = object : TaskScope {
                override fun did(amount: BigInteger) {
                    task.incrementStepsCompleted(amount)
                }
            }

            tasks += task
            with (taskScope) {
                val result = func()
                task.endInstant = Instant.now()
                task.endMark = timeSource.markNow()
                task.isCompleted = true
                return result
            }
        }

        override fun <T> show(key: String, value: T) {
            vars[key] = value.toString()
        }
    }

    section {
        p {
            textLine("")
            result?.let { green { text("✔") } } ?: cyan { text(spinnerAnim) }
            text(" ")
            underline { textLine("${problem.year} day ${problem.day} part ${problem.part}") }

            text(getIndent(1))
            rgb(gray) {
                text("Elapsed: ${elapsedTime.toComponents { h, m, s, _ -> String.format("%02d:%02d:%02d", h, m, s) }}")
                text("  Start: $startInstant")
                endInstant?.let { text("  End: $it") }
            }
        }
        p {
            tasks.forEach { task ->
                p {
                    text(getIndent(1))
                    if (task.isCompleted) green { text("✔") } else cyan { text(spinnerAnim) }
                    text(" ")
                    underline { textLine(task.name) }

                    if (task.totalSteps != null || task.stepsCompleted != BigInteger.ZERO) {
                        text(getIndent(2))

                        if (!task.isCompleted) task.totalSteps?.let {
                            val percentComplete =
                                ((task.stepsCompleted.toDouble() / it.toDouble()) * 100.0).toInt()
                            val numBarsComplete = percentComplete / 5
                            cyan(ColorLayer.BG) { text(" ".repeat(numBarsComplete)) }
                            rgb(gray, ColorLayer.BG) { text(" ".repeat(20 - numBarsComplete)) }
                            text(" $percentComplete%  ")
                        }

                        if (task.stepsCompleted?.let { it > BigInteger.ZERO } == true) {
                            text("Did ${task.stepsCompleted}")
                            if (!task.isCompleted) task.totalSteps?.let { text(" of $it") }
                            text(".")
                        }

                        textLine("")
                    }

                    rgb(gray) {
                        val taskElapsedTime =
                            (task.endMark ?: timeSource.markNow()) - task.startMark
                        text(getIndent(2))
                        text("Elapsed: ${taskElapsedTime.toComponents { h, m, s, _ -> String.format("%02d:%02d:%02d", h, m, s) }}")
                        text("  Start: ${task.startInstant}")
                        task.endInstant?.let { text("  End: $it") }
                        textLine("")

                        if (!task.isCompleted &&
                            task.totalSteps != null &&
                            (task.etaLastUpdatedMark == null || timeSource.markNow() - task.etaLastUpdatedMark!! > 3.seconds) &&
                            task.stepsCompleted?.let { it > BigInteger.ZERO } == true) {

                            val timePerStep = taskElapsedTime.div(task.stepsCompleted.toDouble())
                            val stepsLeft = task.totalSteps - task.stepsCompleted
                            task.eta = timePerStep * stepsLeft.toDouble()
                            task.etaLastUpdatedMark = timeSource.markNow()
                        }

                        if (!task.isCompleted && task.eta != null) {
                            text(getIndent(2))
                            text("ETA: ${task.eta!!.toComponents { h, m, s, _ -> String.format("%02d:%02d:%02d", h, m, s) }}")
                        }
                    }
                }
            }
        }
        p {
            vars.forEach { (key, value) ->
                p {
                    yellow { textLine(key.uppercase()) }
                    text(value)
                }
            }
        }
        p {
            result?.let { green { textLine("RESULT") }; bold { text(it) } }
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
    val totalSteps: BigInteger?,
) {
    var isCompleted: Boolean = false
    private val stepsCompletedRef = AtomicReference(BigInteger.ZERO)
    val startInstant: Instant = Instant.now()
    val startMark: TimeSource.Monotonic.ValueTimeMark = TimeSource.Monotonic.markNow()
    var endInstant: Instant? = null
    var endMark: TimeSource.Monotonic.ValueTimeMark? = null
    var etaLastUpdatedMark: TimeSource.Monotonic.ValueTimeMark? = null
    var eta: Duration? = null

    val stepsCompleted get() = stepsCompletedRef.get()

    fun incrementStepsCompleted(amount: BigInteger) {
        stepsCompletedRef.updateAndGet { it + amount}
    }
}

private fun getIndent(level: Int) = "    ".repeat(level)