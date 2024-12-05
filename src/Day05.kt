fun main() {
    fun part1(input: List<String>): Int {
        val (rules, updateOrders) = parse(input)

        val solver = Solver(rules)

        return updateOrders.filter { solver.simpleValidation(it) }
            .sumOf { it.middle }
    }

    fun part2(input: List<String>): Int {
        val (rules, updateOrders) = parse(input)

        val solver = Solver(rules)

        return updateOrders.filter { !solver.simpleValidation(it) }
            .map { solver.fix(it) }
            .sumOf { it.middle }
    }

    val input = readInput("Day05")
    val testInput = readInput("Day05_test")

    check(143 == part1(testInput))
    part1(input).println()
    check(123 == part2(testInput))
    part2(input).println()
}

fun parse(input: List<String>): Pair<MutableMap<String, MutableList<String>>, List<UpdateOrder>> {
    val rules = input.filter { it.contains('|') }
        .map { it.split('|') }
        .groupByTo(mutableMapOf(), { it[0].trim() }, { it[1].trim() })

    val updateOrders = input.filter { it.contains(',') }
        .map { it.split(',') }
        .map { UpdateOrder.from(it) }
        .toList()

    return Pair(rules, updateOrders)
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

enum class Move {
    MOVE_TO_END,
    SWITCH,
}

class Solver(private val rules: Map<String, List<String>>) {

    fun simpleValidation(updateOrder: UpdateOrder): Boolean {
        return validate(updateOrder).first == null
    }

    fun validate(updateOrder: UpdateOrder): Triple<Move?, Int, Int> {
        for ((i, update) in updateOrder.order.withIndex()) {
            val startIndex = i + 1
            for ((j, next) in updateOrder.order.subList(startIndex, updateOrder.order.size).withIndex()) {
                if (rules[update] == null) {
                    return Triple(Move.MOVE_TO_END, i, -1)
                }
                if (rules[update]?.contains(next) == false) {
                    return Triple(Move.SWITCH, i, startIndex + j)
                }
            }
        }
        return Triple(null, -1, -1)
    }

    fun fix(updateOrdersToFix: UpdateOrder): UpdateOrder {
        val (move, i, j) = validate(updateOrdersToFix)
        if (move == null) {
            return updateOrdersToFix
        }

        val newOrder = when (move) {
            Move.MOVE_TO_END -> {
                val new = updateOrdersToFix.order.toMutableList()
                new.add(new.removeAt(i))
                new
            }

            Move.SWITCH -> {
                val new = updateOrdersToFix.order.toMutableList()
                val oldJ = new.removeAt(j)
                val oldI = new.removeAt(i)
                new.add(i, oldJ)
                new.add(j, oldI)
                new
            }
        }
        return fix(UpdateOrder.from(newOrder))
    }

}
