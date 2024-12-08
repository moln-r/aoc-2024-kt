import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {
    fun part1(input: List<String>): Int {
        return parseInput(input)
            .flatMap { it.calculateAntinodes() }
            .filter { it.isInMap(input) }
            .toSet()
            .size
    }

    fun part2(input: List<String>): Int {
        val rowSize = input.size
        val columnSize = input[0].length
        val toSet = parseInput(input)
            .flatMap { it.addAntinodes(rowSize, columnSize) }
            .toSet()
            .sortedBy { it.row }
        return toSet
            .size
    }

    val input = readInput("Day08")
    val testInput = readInput("Day08_test")

    val testResult1 = part1(testInput)
    check(14 == testResult1, { "Part 1 test run failed with value $testResult1" })
    part1(input).println()

    val testResult2 = part2(testInput)
    check(34 == testResult2, { "Part 2 test run failed with value $testResult2" })
    part2(input).println()
}

fun parseInput(input: List<String>): List<AntennaPair> {
    val antennas = input.withIndex()
        .flatMap { (row, line) ->
            line.withIndex().map { (column, char) ->
                Antenna(row, column, char)
            }
        }.filter { it.type != '.' }
        .toList()

    val antennaPairs = mutableListOf<AntennaPair>()
    for (i in antennas.indices) {
        for (j in i..<antennas.size) {
            if (antennas[i].type == antennas[j].type
                && antennas[i].row != antennas[j].row
                && antennas[i].column != antennas[j].column
            ) {
                antennaPairs.add(AntennaPair(antennas[i], antennas[j]))
            }
        }
    }
    return antennaPairs
}

data class Antenna(val row: Int, val column: Int, val type: Char) {
    fun order(other: Antenna): Pair<Antenna, Antenna> {
        return if (row > other.row) {
            this to other
        } else {
            other to this
        }
    }

    fun getAntinodes(
        distance: Pair<Int, Int>,
        rowSize: Int,
        columnSize: Int,
        operation: (Int, Int) -> Int
    ): List<Antinode> {
        val antinodes = mutableListOf<Antinode>()
        var multiplier = 1
        while (true) {
            val antinode = Antinode(
                operation(row, distance.first * multiplier),
                operation(column, distance.second * multiplier),
            )
            multiplier++
            if (antinode.row < 0
                || antinode.row >= rowSize
                || antinode.column < 0
                || antinode.column >= columnSize
            ) {
                break
            }
            antinodes.add(antinode)
        }
        return antinodes
    }
}

data class Antinode(val row: Int, val column: Int) {
    fun isInMap(map: List<String>): Boolean {
        val maxRow = map.size - 1
        val maxColumn = map[0].length - 1
        return row in 0..maxRow && column in 0..maxColumn
    }
}

data class AntennaPair(val first: Antenna, val second: Antenna) {

    // this one is ugly
    fun calculateAntinodes(): List<Antinode> {
        /**
         * there are four cases: \ / - |
         */

        val distance = Pair(
            abs(first.row - second.row),
            abs(first.column - second.column)
        )

        if ((first.row < second.row && first.column < second.column)
            || (second.row < first.row && second.column < first.column)
        ) {
            // case \
            return listOf(
                Antinode(
                    min(first.row, second.row) - distance.first,
                    min(first.column, second.column) - distance.second
                ),
                Antinode(
                    max(first.row, second.row) + distance.first,
                    max(first.column, second.column) + distance.second
                ),
            )
        } else if ((first.row < second.row && first.column > second.column)
            || (second.row < first.row && second.column > first.column)
        ) {
            // case /
            return listOf(
                Antinode(
                    min(first.row, second.row) - distance.first,
                    max(first.column, second.column) + distance.second
                ),
                Antinode(
                    max(first.row, second.row) + distance.first,
                    min(first.column, second.column) - distance.second
                ),
            )
        } else if (first.row == second.row) {
            // case -
            return listOf(
                Antinode(
                    first.row,
                    min(first.column, second.column) - distance.second
                ),
                Antinode(
                    first.row,
                    max(first.column, second.column) + distance.second
                ),
            )
        } else if (first.column == second.column) {
            // case |
            return listOf(
                Antinode(
                    min(first.row, second.row) - distance.first,
                    first.column,
                ),
                Antinode(
                    max(first.row, second.row) + distance.first,
                    first.column,
                ),
            )
        } else {
            throw RuntimeException("this should never happen")
        }
    }

    // this is neater
    fun addAntinodes(rowSize: Int, columnSize: Int): Set<Antinode> {
        val distance =
            if (first.row == second.row) {
                Pair(0, abs(first.column - second.column))
            } else if (first.column == second.column) {
                Pair(abs(first.row - second.row), 0)
            } else {
                val pair = first.order(second)
                Pair(
                    pair.first.row - pair.second.row,
                    pair.first.column - pair.second.column
                )
            }
        return listOf(Antinode(first.row, first.column))
            .union(first.getAntinodes(distance, rowSize, columnSize) { a, b -> a + b })
            .union(first.getAntinodes(distance, rowSize, columnSize) { a, b -> a - b })
    }

}
