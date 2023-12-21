package kotlinadventofcode.util

import kotlin.math.pow

fun isQuadratic(x: List<Int>, y: List<Int>): Boolean {
    if (!x.map { it.toDouble() }.isLinear()) error("x values must be linear")
    if (x.size != y.size) error("x and y must be the same size")
    if (x.size < 4) error("x and y must have at least 4 values")
    return y.map { it.toDouble() }.finiteDifferences().isLinear()
}

fun List<Pair<Int, Int>>.isQuadratic(): Boolean {
    return isQuadratic(map { it.first }, map { it.second })
}

fun List<Double>.finiteDifferences(): List<Double> {
    return (0 until indices.last).map { this[it + 1] - this[it] }
}

fun List<Double>.isLinear(): Boolean {
    return finiteDifferences().distinct().size == 1
}

val List<Pair<Int, Int>>.quadratic: (Int) -> Double
    get() {
        if (!isQuadratic()) error("Series is not quadratic")
        val (x1, y1) = this[0]
        val (x2, y2) = this[1]
        val (x3, y3) = this[2]
        val denominator = (x1 - x2) * (x1 - x3) * (x2 - x3)
        val a = (x3 * (y2 - y1) + x2 * (y1 - y3) + x1 * (y3 - y2)) / denominator.toDouble()
        val b = (x3 * x3 * (y1 - y2) + x2 * x2 * (y3 - y1) + x1 * x1 * (y2 - y3)) / denominator.toDouble()
        val c = (x2 * x3 * (x2 - x3) * y1 + x3 * x1 * (x3 - x1) * y2 + x1 * x2 * (x1 - x2) * y3) / denominator.toDouble()
        return { x -> a * x.toDouble().pow(2) + b * x + c }
    }
