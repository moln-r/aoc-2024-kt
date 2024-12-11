fun main() {
    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val input = readInput("Day0x")
    val testInput = readInput("Day0x_test")

    val testResult1 = part1(testInput)
    check(testInput.size == testResult1, { "Part 1 test run failed with value $testResult1" })
    part1(input).println()

    val testResult2 = part2(testInput)
    check(testInput.size == testResult2, { "Part 2 test run failed with value $testResult2" })
    part2(input).println()
}
