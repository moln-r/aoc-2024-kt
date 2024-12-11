import kotlin.math.max
import kotlin.math.min

const val START = '^'
const val OBS = '#'
const val MARKER = "X"
const val PRINT = false

fun main() {

    fun part1(originalInput: List<String>): Int {
        val puzzle = Puzzle(originalInput.toMutableList())

        var (row, column) = puzzle.startPosition
        var direction = Direction.UP
        var guardInside = true

        while (guardInside) {
            when (direction) {
                Direction.UP -> {
                    val result = puzzle.goUp(row, column)
                    row = result.first
                    guardInside = result.second
                    direction = direction.next()
                }

                Direction.RIGHT -> {
                    val result = puzzle.goRight(row, column)
                    column = result.first
                    guardInside = result.second
                    direction = direction.next()
                }

                Direction.DOWN -> {
                    val result = puzzle.goDown(row, column)
                    row = result.first
                    guardInside = result.second
                    direction = direction.next()
                }

                Direction.LEFT -> {
                    val result = puzzle.goLeft(row, column)
                    column = result.first
                    guardInside = result.second
                    direction = direction.next()
                }
            }
        }

        return puzzle.markerCount()
    }

    fun part2(input: List<String>): Int {
        val rowCount = input.size
        val columnCount = input[0].length

        var solution = 0

        var runs = 0
        // change the input
        for (row in 0..<rowCount) {
            for (column in 0..<columnCount) {
                val modifiedInput = input.toMutableList()
                val editedRow = modifiedInput[row].toCharArray()
                if (editedRow[column] != '.') {
                    continue
                }
                editedRow[column] = OBS
                modifiedInput[row] = editedRow.joinToString("")

                // run part1
                try {
                    runs++
                    part1(modifiedInput)
                } catch (e: RuntimeException) {
                    // found a solution, count the number of exceptions
                    solution++
                }
            }
        }

//        println("Solution: $solution")
//        println("Runs: $runs")
        return solution
    }

    val input = readInput("Day06")
    val testInput = readInput("Day06_test")

    check(41 == part1(testInput))
    part1(input).println()

    check(6 == part2(testInput))
    part2(input).println()
}

private enum class Direction() {
    UP,
    DOWN,
    RIGHT,
    LEFT;

    fun next(): Direction {
        return when (this) {
            UP -> RIGHT
            RIGHT -> DOWN
            DOWN -> LEFT
            LEFT -> UP
        }
    }
}

private data class Puzzle(
    private val input: MutableList<String>,
    val startPosition: Pair<Int, Int>,
    private val markerCache: MutableList<String>,
) {

    constructor(input: MutableList<String>) :
            this(input, getStartPosition(input), mutableListOf())

    companion object {
        fun getStartPosition(input: List<String>): Pair<Int, Int> {
            return input.withIndex()
                .filter { it.value.contains(START) }
                .map { Pair(it.index, it.value.indexOf(START)) }
                .first()
        }
    }

    fun getRow(row: Int, start: Int, end: Int): String {
        return input[row]
            .substring(start, end)
    }

    fun getColumn(column: Int, start: Int, end: Int): String {
        return input.map { it[column] }
            .joinToString("")
            .substring(start, end)
    }

    fun goUp(row: Int, column: Int): Pair<Int, Boolean> {
        val firstObstacle = getColumn(column, 0, row).reversed()
            .indexOf(OBS)
        val (newRow, state) = if (firstObstacle == -1) {
            Pair(0, false)
        } else {
            Pair(row - firstObstacle, true)
        }
        markIfUnmarked(Pair(row, column), Pair(newRow, column))
        return Pair(newRow, state)
    }

    fun goRight(row: Int, column: Int): Pair<Int, Boolean> {
        val firstObstacle = getRow(row, column, input[row].length)
            .indexOf(OBS)
        val (newColumn, state) = if (firstObstacle == -1) {
            Pair(input[row].length - 1, false)
        } else {
            Pair(column + firstObstacle - 1, true)
        }
        markIfUnmarked(Pair(row, column), Pair(row, newColumn))
        return Pair(newColumn, state)
    }

    fun goDown(row: Int, column: Int): Pair<Int, Boolean> {
        val firstObstacle = getColumn(column, row, input.size)
            .indexOf(OBS)
        val (newRow, state) = if (firstObstacle == -1) {
            Pair(input.size - 1, false)
        } else {
            Pair(row + firstObstacle - 1, true)
        }
        markIfUnmarked(Pair(row, column), Pair(newRow, column))
        return Pair(newRow, state)
    }

    fun goLeft(row: Int, column: Int): Pair<Int, Boolean> {
        val firstObstacle = getRow(row, 0, column).reversed()
            .indexOf(OBS)
        val (newColumn, state) = if (firstObstacle == -1) {
            Pair(0, false)
        } else {
            Pair(column - firstObstacle, true)
        }
        markIfUnmarked(Pair(row, column), Pair(row, newColumn))
        return Pair(newColumn, state)
    }

    fun markIfUnmarked(start: Pair<Int, Int>, end: Pair<Int, Int>) {
        val markerInfo = "" + start.first + start.second + end.first + end.second
        if (markerCache.contains(markerInfo)) {
            throw RuntimeException("done")
        } else {
            markerCache.add(markerInfo)
        }

        if (start.first == end.first) {
            // we mark in a row
            val first = min(start.second, end.second)
            val last = max(start.second, end.second)
            val range = first..last

            val replacement = MARKER.repeat(last - first + 1)

            input[start.first] = input[start.first].replaceRange(range, replacement)
        }
        if (start.second == end.second) {
            // we mark in a column
            val range = min(start.first, end.first)..max(start.first, end.first)

            range.forEach {
                val newLine = input[it].replaceRange(start.second..start.second, MARKER)
                input[it] = newLine
            }
        }
    }

    fun markerCount(): Int {
        return input.sumOf { line ->
            line.count { it == MARKER.first() }
        }
    }

    private fun printIt() {
        if (PRINT) {
            input.forEach { println(it) }
            println("")
        }
    }

}
