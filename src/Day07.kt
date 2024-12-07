import kotlin.math.pow

fun main() {
    fun part1(input: List<String>): Long {
        return input.map { CalEq.from(it) }
            .toList()
            .filter { it.possibleResults(2).contains(it.result) }
            .sumOf { it.result }
    }

    fun part2(input: List<String>): Long {
        return input.map { CalEq.from(it) }
            .toList()
            .filter { it.possibleResults(3).contains(it.result) }
            .sumOf { it.result }
    }

    val input = readInput("Day07")
    val testInput = readInput("Day07_test")

    val part1TestResult = part1(testInput)
    check(3749L == part1TestResult, { "Part 1 test run failed with value $part1TestResult" })
    part1(input).println()

    val part2TestResult = part2(testInput)
    check(11387L == part2TestResult, { "Part 2 test run failed with value $part2TestResult" })
    part2(input).println()
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

    fun possibleResults(numberOfOperations: Int): List<Long> {
        val bitSize = numbers.size - 1

        val results = mutableListOf<Long>()
        for (combination in 0..<numberOfOperations.toDouble().pow(bitSize).toInt()) {
            val operationCode = combination.toString(numberOfOperations).padStart(bitSize, '0')
            val operations = operationCode.map { Operation.from(it) }

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
    CON({ a, b -> ("" + a + b).toLong() }),
    ;

    companion object {
        fun from(input: Char): Operation {
            return when (input) {
                '0' -> ADD
                '1' -> MUL
                else -> CON
            }
        }
    }
}
