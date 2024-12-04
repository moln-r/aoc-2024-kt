fun main() {
    fun part1(input: List<String>): Long {
        return Regex("mul\\(([0-9]+),([0-9]+)\\)").findAll(input.joinToString())
            .map { Mul(it) }
            .sumOf { it.value }
    }

    fun part2(input: List<String>): Long {
        val memory = input.joinToString()

        val muls = Regex("mul\\(([0-9]+),([0-9]+)\\)")
            .findAll(memory)
            .map { Mul(it) }
            .toList()

        val dos = Regex("do\\(\\)")
            .findAll(memory)
            .map { it.range.first }
            .toList()

        val donts = Regex("don't\\(\\)")
            .findAll(memory)
            .map { it.range.first }
            .toList()

        val selected = muls.filter { mul ->
            val lastDo = dos.lastOrNull { it < mul.position } ?: 0
            val lastDont = donts.lastOrNull { it < mul.position } ?: 0
            lastDont <= lastDo
        }
        return selected.sumOf { it.value }
    }

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
    // 25112642 too low
    // 99812796 not ok
    // 104109501 not ok
    // 96923647 not ok
}

data class Mul(val value: Long, val position: Int) {
    constructor(group: MatchResult) : this(
        group.groupValues[1].toLong() * group.groupValues[2].toLong(),
        group.range.first
    )
}
