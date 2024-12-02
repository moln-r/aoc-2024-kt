import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        return input.map { splitToInt(it) }
            .filter { isSafe(it) }
            .map { 1 }
            .count()
    }

    fun part2(input: List<String>): Int {
        return input.map { splitToInt(it) }
            .filter { isSafeEnough(it) }
            .map { 1 }
            .count()
    }

    val input = readInput("Day02")

    // checking if the first two numbers are equal.
    // (my input is safe, so I can detect if I need to check for increase or decrease.)
//    input.zipWithNext().withIndex().forEach { (i, pair) ->
//        val (x, y) = pair
//        if (x == y) println("$i")
//    }

    part1(input).println()
    part2(input).println()
}

// based on my input it's safe to check only the first two numbers. (thank god, no recursion needed)
fun safetyCheck(num1: Int, num2: Int): (Int, Int) -> Boolean {
    return if (num1 > num2)
        fun(x: Int, y: Int) = x > y && abs(x - y) <= 3
    else
        fun(x: Int, y: Int) = x < y && abs(x - y) <= 3
}

fun isSafe(numbers: List<Int>): Boolean {
    val safetyCheck = safetyCheck(numbers[0], numbers[1])
    numbers.zipWithNext()
        .forEach { (x, y) ->
            if (!safetyCheck(x, y)) {
                return false
            }
        }
    return true
}

fun isSafeEnough(numbers: List<Int>): Boolean {
    if (isSafe(numbers)) {
        return true
    }

    // if the list in not safe originally, we'll keep trying with alternative options
    for (i in numbers.indices) {
        val shorterList = numbers.toMutableList()
        shorterList.removeAt(i)
        if (isSafe(shorterList)) {
            return true
        }
    }

    return false
}

fun splitToInt(input: String): MutableList<Int> {
    return input.split(" ")
        .map { Integer.valueOf(it) }
        .toMutableList()
}
