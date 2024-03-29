package day16

import aoc.utils.findInts
import aoc.utils.graphs.Node
import aoc.utils.graphs.allNodes
import aoc.utils.readInput
import aoc.utils.splitOn
import kotlin.math.max

data class Valve(val name: String, val flow: Int)

fun part1(): Int {
    val start = buildMap()
    val result = venture(allNodes(start), 30, start, start, listOf(), 0, mutableMapOf())
    // 198802956
    // 9980871
    println(iterations)
    return result
}

fun main() {
}


var iterations = 0L
fun venture(
    allValves: Set<Node<Valve>>,
    timeLeft: Int,
    yourLocation: Node<Valve>,
    yourPrevious: Node<Valve>,
    valvesOpened: List<Node<Valve>>,
    pressureReleased: Int,
    highScore: MutableMap<Int, Int>,
    elephantLocation: Node<Valve>? = null,
    elephantPrevious: Node<Valve>? = null,
): Int {
    iterations++

    val maximumAchievable = maxPossible(allValves, timeLeft, pressureReleased, valvesOpened, true)
    if (maximumAchievable < highScore[timeLeft] ?: -1) {
        return 0
    }
    val accumPressure = pressureReleased + pressureChange(valvesOpened)

    if(highScore[timeLeft] ?: -1 < accumPressure) {
        highScore[timeLeft] = accumPressure
    }

    val timeRemaining = timeLeft - 1
    if (timeRemaining == 0)
        return accumPressure

    // Never go back to where we just came from (if we opened current == from)
    val connections = yourLocation.edges.map { it.target }.filter { it != yourPrevious }

    // Can't move anywhere and current was already opened, we can just stand still?
    if (connections.isEmpty() && valvesOpened.contains(yourLocation)) {
        return venture(allValves, timeRemaining, yourLocation, yourPrevious, valvesOpened, accumPressure, highScore)
    }

    if (connections.isEmpty() && !valvesOpened.contains(yourLocation) && yourLocation.value.flow != 0)
        return venture(allValves, timeRemaining, yourLocation, yourLocation, valvesOpened.plus(yourLocation), accumPressure, highScore)

    val moveScore =
        connections.map {
            venture(allValves, timeRemaining, it, yourLocation, valvesOpened, accumPressure, highScore)
        }.maxOf { it }

    if (!valvesOpened.contains(yourLocation) && yourLocation.value.flow != 0) {
        return max(
            moveScore,
            venture(allValves, timeRemaining, yourLocation, yourLocation, valvesOpened.plus(yourLocation), accumPressure, highScore)
        )
    }

    return moveScore
}

// What is the maximum pressure relased if miraculously all valves would open without moving
fun maxPossible(
    allValves: Set<Node<Valve>>,
    timeLeft: Int,
    pressureReleased: Int,
    valvesOpened: List<Node<Valve>>,
    withElephant: Boolean
): Int {

    val valvesToOpen = allValves
        .minus(valvesOpened)
        .sortedByDescending { it.value.flow }
        .toMutableList()

    var max = pressureReleased
    var pressureChange = pressureChange(valvesOpened)
    var time = timeLeft
    while (time > 0) {
        max += pressureChange
        repeat(if(withElephant) 2 else 1) {
        valvesToOpen.removeFirstOrNull()?.let {
            pressureChange += it.value.flow
        }}
        time--
    }

    return max
}

fun pressureChange(valvesOpened: List<Node<Valve>>): Int {
    return valvesOpened.sumOf { it.value.flow }
}


fun part2(): Int {
    return 1;
}

private fun buildMap(): Node<Valve> {
    val input = "test.txt"

    // Create valves
    val valves = readInput(input)
        .map { it.split(" ").let { it[1] to it[4].findInts()[0] } }
        .map { Node(Valve(it.first, it.second)) }

    // Link valves
    readInput(input)
        .map { it.replace("valve ", "valves ") }
        .map {
            it.split(" ").let {
                val connected = it.splitOn { it == "valves" }[1].map { it.replace(",", "") }
                it[1] to connected
            }
        }
        .forEach { connection ->
            val valve = valves.firstOrNull { it.value.name == connection.first }!!
            connection.second.forEach { target ->
                if (valves.firstOrNull { it.value.name == target } == null)
                    println()
                val targetValve = valves.firstOrNull { it.value.name == target }!!
                valve.biLink(1, targetValve)
            }
        }

    val start = valves.firstOrNull { it.value.name == "AA" }!!
    return start
}
