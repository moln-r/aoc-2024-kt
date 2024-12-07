import kotlin.math.pow

fun main() {
    fun part1(input: List<String>): Long {
        val equations = input.map { CalEq.from(it) }.toList()

        return equations.filter { it.possibleResults().contains(it.result) }
            .sumOf { it.result }
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val input = readInput("Day07")
    val testInput = readInput("Day07_test")

    val part1TestResult = part1(testInput)
    check(3749L == part1TestResult, { "Part 1 test run failed with value $part1TestResult" })
    part1(input).println()
//    check(testInput.size == part2(testInput))
//    part2(input).println()
}

data class CalEq(val result: Long, val numbers: List<Long>) {

    companion object {
        fun from(input: String): CalEq {
            val colonSplit = input.split(":")
            val result = colonSplit[0].toLong()
            val numbers = colonSplit[1].trim()
                .split(" ")
                .map { it.toLong() }
                .toList()
            return CalEq(result, numbers)
        }
    }

    fun possibleResults(): List<Long> {
        val bitSize = numbers.size - 1

        val results = mutableListOf<Long>()
        for (operationCombination in 0..<2.0.pow(bitSize).toInt()) {
            val binary = operationCombination.toString(radix = 2).padStart(bitSize, '0')
            val operations = binary.map { if (it == '1') Operation.ADD else Operation.MUL }

            var calculation = numbers[0]
            for ((i, operation) in operations.withIndex()) {
                calculation = operation.fn(calculation, numbers[i + 1])
            }
            results.add(calculation)
        }

        return results
    }

}

enum class Operation(val fn: (Long, Long) -> Long) {
    ADD({ a, b -> a + b }),
    MUL({ a, b -> a * b }),
}
