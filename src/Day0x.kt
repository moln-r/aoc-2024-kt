fun main() {
    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val input = readInput("Day0x")
    val testInput = readInput("Day0x_test")

    check(testInput.size == part1(testInput))
    part1(input).println()
    check(testInput.size == part2(testInput))
    part2(input).println()
}
