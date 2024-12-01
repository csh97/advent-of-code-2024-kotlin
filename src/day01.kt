import kotlin.math.abs

fun part1(firstIds: List<Int>, secondIds: List<Int>): Int {
    return firstIds.zip(secondIds).sumOf { abs(it.first - it.second) }
}

fun part2(firstIds: List<Int>, secondIds: List<Int>): Int {
    return firstIds.sumOf { id -> id * secondIds.count { it == id } }
}

fun main() {
    val (firstIds, secondIds) = "day01.txt".readLines().map {
        val (first, second) = it.split("   ")
        first.toInt() to second.toInt()
    }.unzip().let { it.first.sorted() to it.second.sorted() }

    println("Part 1: ${part1(firstIds, secondIds)}")
    println("Part 2: ${part2(firstIds, secondIds)}")
}