fun main() {

    fun part1(input: List<String>): Long {
        val parsed = parse(input[0])
        val rearranged = rearrange(parsed)
        return checksum(rearranged)
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val input = readInput("Day09")
    val testInput = readInput("Day09_test")

    val testResult1 = part1(testInput)
    check(1928L == testResult1, { "Part 1 test run failed with value $testResult1" })
    part1(input).println()
    // 88568111889 too low
    // 89312334662 too low
    // 8673186863910 too high

//    val testResult2 = part2(testInput)
//    check(testInput.size == testResult2, { "Part 2 test run failed with value $testResult2" })
//    part2(input).println()
}

fun parse(s: String): MutableList<String> {
    var id = 0
    val block = mutableListOf<String>()
    for ((i, c) in s.withIndex()) {
        val num = c.toString().toInt()

        if (i % 2 != 0) {
            repeat(num) { block.add(".") }
        } else {
            repeat(num) { block.add(id.toString()) }
            id++
        }
    }
    return block
}

fun rearrange(blocks: MutableList<String>): List<String> {
    for ((i, block) in blocks.withIndex()) {
        if (block != ".") {
            continue
        }

        val size = emptySpaceFrom(i, blocks)

        // find from the back a block that can fit
        val blockIndex = getBlockToMove(blocks, size)

        // replace empty space
        for ((j, c) in blocks[blockIndex].withIndex()) {
            blocks[i + j] = c.toString()
        }

        // remove block from the back
        blocks[blockIndex] = "X"
    }
    return blocks.filter { it != "X" }.toList()
}

fun emptySpaceFrom(index: Int, blocks: List<String>): Int {
    var size = 1
    // how much space do we have?
    for (j in index + 1..<blocks.size) {
        if (blocks[j] == ".") {
            size++
        } else {
            break
        }
    }
    return size
}

fun getBlockToMove(blocks: MutableList<String>, size: Int): Int {
    var blockIndex = -1
    for ((j, b) in blocks.withIndex().reversed()) {
        if (b == "." || b == "X") {
            continue
        }
        if (b.length <= size) {
            blockIndex = j
            break
        }
    }
    return blockIndex
}

fun checksum(blocks: List<String>): Long {
    return blocks.withIndex().sumOf { (i, c) ->
        if (c == ".") {
            0
        } else {
            i * c.toLong()
        }
    }
}
