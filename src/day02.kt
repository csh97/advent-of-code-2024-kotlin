fun List<Int>.isSafe(): Boolean {
    val diffs = windowed(2, 1) {
        it.last() - it.first()
    }

    return diffs.all { it in 1..3 } || diffs.all { it in -3..-1 }
}

fun part1(input: List<List<Int>>): Int {
    return input.count { it.isSafe() }
}

fun part2(input: List<List<Int>>): Int {
    return input.count { report ->
        report.isSafe() || report.indices.map { report.take(it) + report.drop(it + 1) }.any { it.isSafe() }
    }
}

fun main() {
    val parsedInput = "day02.txt".readLines().map { it.split(" ").map { it.toInt() } }
    println("Part 1: ${part1(parsedInput)}")
    println("Part 2: ${part2(parsedInput)}")
}