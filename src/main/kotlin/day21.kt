package day21

import aoc.utils.findFirstInt
import aoc.utils.readInput

fun main() {
    part2().let { println(it) }
}

data class Monkey(
    val name: String,
    var operation: Char?,
    var left: Monkey?,
    var right: Monkey?,
    val leftPlaceHolder: String?,
    val rightPlaceHolder: String?,
    var value: Long?
) {

    override fun toString(): String {
        return "Monkey[$name]"
    }

    fun valued(): Long {
        if (value != null)
            return value!!

        return when (operation) {
            '+' -> Math.addExact(left!!.valued(), right!!.valued())
            '-' -> Math.addExact(left!!.valued(), -1 * right!!.valued())
            '*' -> Math.multiplyExact(left!!.valued(), right!!.valued())
            '/' -> {
                val leftv = left!!.valued()
                val rightV = right!!.valued()
                val div = Math.divideExact(leftv, rightV)

                if (leftv % rightV != 0L) {
                    throw Error("Difference ${leftv} ${rightV}")
                }

                return div
            }

            else -> throw Error("unhandled $operation")
        }
    }
}

fun part1(): Long {
    val monkeys = monkeys()
    val root = monkeys["root"]!!
    return root.valued()
}

fun part2(): Long {
    val monkeys = monkeys()
    val root = monkeys["root"]!!
    val you = monkeys["humn"]!!

    var increment = 1L
    var test = increment
    var lastUnder = test

    root.left!!.valued()

    while (true) {
        you.value = test

        var left = 0L
        try {
            left = root.left!!.valued()
        } catch (e: Error) {
            test += 1
            continue
        }
        val right = root.right!!.valued()

        if (left == right) {
            return you.value!!
        }

        // Left starts bigger than right
        // Left seems to be decreasing as we increase test
        if (left > right) {
            lastUnder = test
            increment *= 2
        }

//        Went too far
        if (left < right) {
            increment = 1
            test = lastUnder
        }

        test += increment
    }

    throw Error("Did not find result")
}

private fun monkeys(): Map<String, Monkey> {
    val monkeys = readInput("day21-input.txt")
        .map {
            val split = it.split(" ")
            if (split.size > 2) {
                Monkey(
                    it.split(":")[0],
                    split[2][0],
                    null,
                    null,
                    split[1],
                    split[3],
                    null
                )
            } else {
                Monkey(
                    it.split(":")[0],
                    null,
                    null,
                    null,
                    null,
                    null,
                    it.findFirstInt().toLong()
                )
            }
        }.map { it.name to it }.toMap()

    // Link monkeys
    monkeys.values.forEach { monkey ->
        if (monkey.leftPlaceHolder != null) {
            monkey.left = monkeys[monkey.leftPlaceHolder]
            monkey.right = monkeys[monkey.rightPlaceHolder]
        }
    }
    return monkeys
}

