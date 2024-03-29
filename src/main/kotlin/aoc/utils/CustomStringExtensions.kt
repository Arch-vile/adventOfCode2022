package aoc.utils

import java.util.function.Predicate

fun sort(value: String) = value.toCharArray().sorted().joinToString("")

fun String.toList() = toCharArray().toList().map { it.toString() }

fun String.firstPart(delim: String = " ") = split(delim)[0]
fun String.secondPart(delim: String = " ") = split(delim)[1]
fun String.thirdPart(delim: String = " ") = split(delim)[2]
fun String.fourthPart(delim: String = " ") = split(delim)[3]

fun String.firstAsInt(delim: String = " ") = firstPart(delim).toInt()
fun String.secondAsInt(delim: String = " ") = secondPart(delim).toInt()
fun String.thirdAsInt(delim: String = " ") = thirdPart(delim).toInt()
fun String.fourthAsInt(delim: String = " ") = fourthPart(delim).toInt()

fun String.findFirstInt() = findInts()[0]
fun String.findSecondInt() = findInts()[1]
fun String.findThirdInt() = findInts()[2]
fun String.findFourthInt() = findInts()[3]

fun String.findInts(): List<Int> {
    return findGroups("""(-?\d+)""".toRegex()).map { it.toInt()}
}

// Regex with some groups (..) on it
fun String.findGroups(regex: Regex): List<String> {
    return regex.findAll(this)
        .map { it.groupValues }
        .filter { it.size > 1 }
        .map { it.drop(1) }
        .flatten()
        .toList()
}

/**
 * Returns the matching part and the rest. Reading from the start of the string.
 */
fun String.takeUntilMatch(predicate: (String) -> Boolean): Pair<String,String> {
    var result = ""
    for(i in indices) {
        if(predicate(result)) {
            return Pair(result, substring(result.length,length))
        } else {
            result+=this[i]
        }
    }
    if(predicate(result)) return Pair(result,"")
    else
    return Pair("",this)
}

fun String.isInt(): Boolean {
   return """\d+""".toRegex().matches(this)
}
