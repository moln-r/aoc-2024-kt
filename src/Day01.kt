import kotlin.math.abs

fun main() {
    fun parseLocations(input: List<String>): Pair<MutableList<Int>, MutableList<Int>> {
        val locationLeft = mutableListOf<Int>()
        val locationRight = mutableListOf<Int>()
        for (line in input) {
            val lineValues = line.split("   ")
            locationLeft.add(lineValues[0].toInt())
            locationRight.add(lineValues[1].toInt())
        }
        return Pair(locationLeft, locationRight)
    }

    fun part1(input: List<String>): Int {
        return parseLocations(input).let { (left, right) ->
            left.sort()
            right.sort()
            Pair(left, right)
        }.let { (sortedLeft, sortedRight) ->
            sortedLeft.zip(sortedRight).sumOf { (left, right) -> abs(left - right) }
        }
    }

    fun part2(input: List<String>): Int {
        parseLocations(input).let { (locationLeft, locationRight) ->
            return locationLeft.sumOf { left -> left * locationRight.count { it == left } }
        }
    }

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
