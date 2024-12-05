fun main() {
    fun part1(input: List<String>): Int {
        val rules: Map<String, MutableList<String>> = input.filter { it.contains('|') }
            .map { it.split('|') }
            .groupByTo(mutableMapOf(), { it[0].trim() }, { it[1].trim() })

        val updateOrders = input.filter { it.contains(',') }
            .map { it.split(',') }
            .map { UpdateOrder.from(it) }
            .toList()

        val validator = Validator(rules)

        return updateOrders.filter { validator.run(it) }
            .sumOf { it.middle }
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val input = readInput("Day05")
    val testInput = readInput("Day05_test")

    check(143 == part1(testInput))
    part1(input).println()

    part2(input).println()
}

data class UpdateOrder(val order: List<String>, val middle: Int) {
    companion object {
        fun from(ordersFromFile: List<String>): UpdateOrder {
            val order = ordersFromFile.map { it.trim() }
            val middle = order[order.size / 2].toInt()
            return UpdateOrder(order, middle)
        }
    }
}

class Validator(private val rules: Map<String, MutableList<String>>) {
    fun run(updateOrder: UpdateOrder): Boolean {

        for ((i, update) in updateOrder.order.withIndex()) {
            val beforeCurrent = updateOrder.order.subList(0, i)
            if (beforeCurrent.isNotEmpty()) {
                val priorUpdates = beforeCurrent.map { rules[it] }.toList()
                if (!priorUpdates.all { it!!.contains(update) }) {
//                    println("$update is not supported by ${beforeCurrent.joinToString(",")}")
                    return false
                }
            }

            val afterCurrent = updateOrder.order.subList(i + 1, updateOrder.order.size)
            if (afterCurrent.isNotEmpty()) {
                if (!rules[update]!!.containsAll(afterCurrent)) {
//                    println("$update is not supporting ${afterCurrent.joinToString(",")}")
                    return false
                }
            }
        }

        return true
    }
}