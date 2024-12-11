fun main() {
    fun part1(input: List<String>): Int {
        val trail = HikingTrial(input)
        trail.run()
        return trail.score()
    }

    fun part2(input: List<String>): Int {
        val trail = HikingTrial(input)
        trail.run()
        return trail.rating()
    }

    val input = readInput("Day10")
    val testInput = readInput("Day10_test")

    val testResult1 = part1(testInput)
    check(36 == testResult1) { "Part 1 test run failed with value $testResult1" }
    part1(input).println()

    val testResult2 = part2(testInput)
    check(81 == testResult2) { "Part 2 test run failed with value $testResult2" }
    part2(input).println()
}

class HikingTrial(
    val input: List<String>,
    private val ends: MutableMap<Pair<Int, Int>, List<Pair<Int, Int>>> = mutableMapOf()
) {

    fun run() {
        findTrailheads().forEach { findEnd(it, it) }
    }

    fun score(): Int {
        return ends.map { it.value }
            .sumOf { it.toSet().size }
    }

    fun rating(): Int {
        return ends.map { it.value }.sumOf { it.size }
    }

    private fun findTrailheads(): List<Pair<Int, Int>> {
        return input.withIndex().flatMap { (row, line) ->
            line.withIndex().mapNotNull { (column, char) ->
                if (char == '0') Pair(row, column) else null
            }
        }
    }

    private fun findEnd(start: Pair<Int, Int>, from: Pair<Int, Int>) {
        val current = input[from.first][from.second]

        if (current == '9') {
            ends[start] = ends.getOrDefault(start, emptyList()).plus(from)
            return
        }

        Dir.entries.forEach { dir ->
            get(from, dir)?.let {
                val next = input[it.first][it.second]
                if (next == current + 1) {
                    findEnd(start, it)
                }
            }
        }
    }

    private fun get(from: Pair<Int, Int>, dir: Dir): Pair<Int, Int>? {
        val next: Pair<Int, Int> = when (dir) {
            Dir.UP -> Pair(from.first - 1, from.second)
            Dir.DOWN -> Pair(from.first + 1, from.second)
            Dir.LEFT -> Pair(from.first, from.second - 1)
            Dir.RIGHT -> Pair(from.first, from.second + 1)
        }
        if (next.first < 0 || next.second < 0 || next.first >= maxRow() || next.second >= maxCol()) {
            return null
        }
        return next
    }

    private fun maxRow(): Int = input.size
    private fun maxCol(): Int = input[0].length
}

private enum class Dir {
    UP, DOWN, LEFT, RIGHT
}
