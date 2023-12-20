package kotlinadventofcode.UI

import java.math.BigInteger

object NoOpUI: UI {
    override fun setResult(r: String) {}

    override fun <T> shown(taskName: String, totalSteps: BigInteger?, func: TaskScope.() -> T): T {
        val taskScope = object : TaskScope {
            override fun did(amount: BigInteger) {}
        }

        with (taskScope) {
            return func()
        }
    }

    override fun <T> show(key: String, value: T) {}
}