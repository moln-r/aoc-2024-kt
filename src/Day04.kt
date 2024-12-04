const val M = 'M'
const val A = 'A'
const val S = 'S'

fun main() {
    fun part1(input: List<String>): Int {
        val inputRotated = rotate90(input)
        val inputRotatedLeft = rotateLeft(input)
        val inputRotatedRight = rotateRight(input)
        return countOccurrencesIn(input + inputRotated + inputRotatedLeft + inputRotatedRight)
    }

    fun part2(input: List<String>): Int {
        // found A character in the input, ignore first and last rows/columns
        // sum the x mas findings
        return (1..<139).sumOf { row ->
            (1..<139).count { column -> input[row][column] == A && x(input, row, column) }
        }
    }

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println() // 7164 too high, 1850 too high
}

fun x(input: List<String>, row: Int, column: Int): Boolean {
    // check / direction
    val forward = (input[row - 1][column - 1] == M && input[row + 1][column + 1] == S
            || input[row + 1][column + 1] == M && input[row - 1][column - 1] == S)

    // check \ direction
    val backward = input[row + 1][column - 1] == M && input[row - 1][column + 1] == S
            || input[row - 1][column + 1] == M && input[row + 1][column - 1] == S

    return forward && backward
}

fun countOccurrencesIn(input: List<String>): Int {
    val xmasRegex = Regex("XMAS")
    val samxRegex = Regex("SAMX")
    return input.sumOf { xmasRegex.findAll(it).count() + samxRegex.findAll(it).count() }
}

fun rotate90(input: List<String>): List<String> {
    return (0..<140)
        .map { columnIndex -> input.map { it[columnIndex] }.joinToString("") }
        .toList()
}

// counterclockwise
fun rotateLeft(input: List<String>): List<String> {
    // starts one character in the first line
    // two characters, one in the first line and one in the second line
    // three characters, one in the first line, one in the second line, and one in the third line
    // ...
    // 140 ...
    // 139 ...

    val diagonalInput = mutableListOf<String>()
    for (charsToAdd in 1..140) {
        var charsInDiagonal = ""
        for (currentLine in 0..<charsToAdd) {
            val line = input[currentLine]
            charsInDiagonal += line[line.length - charsToAdd + currentLine]
        }
        diagonalInput.add(charsInDiagonal)
    }
    // we are halfway through
    for (charsToAdd in 139 downTo 1) {
        var charsInDiagonal = ""
        for ((i, currentLine) in ((140 - charsToAdd)..<140).withIndex()) {
            val line = input[currentLine]
            charsInDiagonal += line[i]
        }
        diagonalInput.add(charsInDiagonal)
    }

    return diagonalInput
}

fun rotateRight(input: List<String>): List<String> {
    // similarly to rotateLeft...
    val diagonalInput = mutableListOf<String>()
    for (charsToAdd in 1..140) {
        var charsInDiagonal = ""
        for (currentLine in 0..<charsToAdd) {
            val line = input[currentLine]
            charsInDiagonal += line[charsToAdd - currentLine - 1]
        }
        diagonalInput.add(charsInDiagonal)
    }
    for (charsToAdd in 139 downTo 1) {
        var charsInDiagonal = ""
        for ((i, currentLine) in ((140 - charsToAdd)..<140).withIndex()) {
            val line = input[currentLine]
            charsInDiagonal += line[139 - i]
        }
        diagonalInput.add(charsInDiagonal)
    }

    return diagonalInput
}
