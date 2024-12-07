import kotlin.time.measureTimedValue

private fun part1(input: String) {

}

private fun part2(input: String) {

}

fun main() {
    val input = "dayXX.txt".readFile()

    listOf(::part1, ::part2).forEachIndexed { i, part ->
        val (answer, timeTaken) = measureTimedValue {
            part(input)
        }
        println("Part ${i+1}: $answer - took ${timeTaken.inWholeMilliseconds}ms")
    }
}