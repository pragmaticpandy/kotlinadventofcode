package kotlinadventofcode.UI

import java.math.BigInteger

/**
 * For lack of better naming ideas, show will not return anything, while shown will return the lambda result.
 */
interface UI {
    fun setResult(r: String)

    fun show(taskName: String, func: TaskScope.() -> Unit) {
        show(taskName, null as BigInteger?, func)
    }

    fun show(taskName: String, totalSteps: Int?, func: TaskScope.() -> Unit) {
        show(taskName, totalSteps?.toBigInteger(), func)
    }

    fun show(taskName: String, totalSteps: BigInteger?, func: TaskScope.() -> Unit) {
        shown(taskName, totalSteps, func)
    }

    fun <T> shown(taskName: String, totalSteps: BigInteger?, func: TaskScope.() -> T): T

    fun <T> shown(taskName: String, totalSteps: Int?, func: TaskScope.() -> T): T {
        return shown(taskName, totalSteps?.toBigInteger(), func)
    }

    fun <T> shown(taskName: String, func: TaskScope.() -> T): T {
        return shown(taskName, null as BigInteger?, func)
    }

    fun <T> show(key: String, value: T)
}

interface TaskScope {
    fun did(amount: BigInteger)

    fun did(amount: Int) = did(amount.toBigInteger())
}

context(UI)
inline fun <T> Iterable<T>.forEach(taskName: String, crossinline action: (T) -> Unit): Unit {
    val totalSteps = when(this) {
        is IntRange -> last - first
        is Collection<*> -> size
        else -> null
    }
    
    show(taskName, totalSteps) {
        forEach {
            action(it)
            did(1)
        }
    }
}

context(UI)
inline fun <T> Iterable<T>.count(taskName: String, crossinline predicate: (T) -> Boolean): Int {
    val totalSteps = if (this is Collection<*>) this.size else null
    var result = 0
    show(taskName, totalSteps) {
        result += count {
            val result = predicate(it)
            did(1)
            result
        }
    }

    return result
}

context(UI)
inline fun <K, V, R> Map<out K, V>.map(taskName: String, crossinline transform: (Map.Entry<K, V>) -> R): List<R> {
    return mapTo(taskName, ArrayList<R>(size), transform)
}

context(UI)
inline fun <K, V, R, C : MutableCollection<in R>> Map<out K, V>.mapTo(taskName: String, destination: C, crossinline transform: (Map.Entry<K, V>) -> R): C {
    show(taskName, size) {
        for (item in this@mapTo) {
            destination.add(transform(item))
            did(1)
        }
    }

    return destination
}
