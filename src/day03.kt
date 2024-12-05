import kotlin.text.Regex

private val multiplyInstructionPattern = Regex("""mul\([0-9]{1,3},[0-9]{1,3}\)""")

private fun part1(input: String): Int {
    val matches = multiplyInstructionPattern.findAll(input).map { it.value }.toList()

    return matches.map { it.extractMultiplyInstruction() }.fold(0) { count, instruction ->
        count + (instruction.first * instruction.second)
    }
}

private fun part2(input: String): Int {
    val doPattern = Regex("""do\(\)""")
    val dontPattern = Regex("""don't\(\)""")
    val multiplyMaxLength = 12
    val doMaxLength = 4
    val dontMaxLength = 7

    return input.map { it.toString() }.foldIndexed(0 to false) { i, (count, disabled), char ->
        if (char == "m") {
            val match = multiplyInstructionPattern.find(input.scanAhead(i, multiplyMaxLength))?.value
            if (match != null && !disabled) {
                val (a, b) = match.extractMultiplyInstruction()
                return@foldIndexed count + (a * b) to false
            }
        }

        if (char == "d") {
            val doMatch = doPattern.find(input.scanAhead(i, doMaxLength))?.value
            val dontMatch = dontPattern.find(input.scanAhead(i, dontMaxLength))?.value
            if (doMatch != null) {
                return@foldIndexed count to false
            }
            if (dontMatch != null) {
                return@foldIndexed count to true
            }
        }

        count to disabled
    }.first
}

private fun String.extractMultiplyInstruction(): Pair<Int, Int> {
    val (a, b) = substringAfter("(").substringBefore(")").split(",")
    return a.toInt() to b.toInt()
}

private fun String.scanAhead(start: Int, end: Int): String = substring(start, (start + end).coerceAtMost(length))

fun main() {
    val input = "day03.txt".readFile()

    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}