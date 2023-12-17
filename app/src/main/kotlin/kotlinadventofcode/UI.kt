package kotlinadventofcode

import java.math.BigInteger

interface UI {
    fun setResult(r: String)

    fun show(taskName: String, func: TaskScope.() -> Unit) {
        show(taskName, null as BigInteger?, func)
    }

    fun show(taskName: String, totalSteps: Int?, func: TaskScope.() -> Unit) {
        show(taskName, totalSteps?.toBigInteger(), func)
    }

    fun show(taskName: String, totalSteps: BigInteger?, func: TaskScope.() -> Unit)
}

interface TaskScope {
    fun did(amount: BigInteger)

    fun did(amount: Int) = did(amount.toBigInteger())
}

public inline fun <T> Iterable<T>.forEach(ui: UI, taskName: String, crossinline action: (T) -> Unit): Unit {
    val totalSteps = if (this is Collection<*>) this.size else null
    ui.show(taskName, totalSteps) {
        forEach {
            action(it)
            did(1)
        }
    }
}
