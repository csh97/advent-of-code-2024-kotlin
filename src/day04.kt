import kotlin.text.substring
import kotlin.time.measureTimedValue

enum class Direction(val y: Int, val x: Int) { N(-1,0), NE(-1,1), E(0,1), SE(1,1), S(1,0), SW(1,-1), W(0,-1), NW(-1,-1) }

private fun part1(input: List<String>): Int {
    val wordToFind = "XMAS"
    return input.mapIndexed { y, column ->
        column.mapIndexed { x, _ ->
            Direction.entries.map { direction ->
                if (input[y][x] != wordToFind[0]) return@mapIndexed 0
                val s = (1 until 4).map {
                    val y = y + (direction.y * it)
                    val x = x + (direction.x * it)
                    try { input[y][x] } catch (_: Exception) { "" }
                }.joinToString("")
                if (s == wordToFind.substring(1)) 1 else 0
            }.sum()
        }.sum()
    }.sum()
}

private fun part2(input: List<String>): Int {
    return input.mapIndexed { y, column ->
        column.mapIndexed { x, _ ->
            if (input[y][x] != 'A') return@mapIndexed 0
            val s = listOf(Direction.NW, Direction.NE, Direction.SE, Direction.SW).map {
                val y = y + it.y
                val x = x + it.x
                try { input[y][x] } catch (_: Exception) { "" }
            }.joinToString("")

            if (s in listOf("MSSM", "MMSS", "SSMM", "SMMS")) 1 else 0
        }.sum()
    }.sum()
}

fun main() {
    val input = "day04.txt".readLines()

    listOf(::part1, ::part2).forEachIndexed { i, part ->
        val (answer, timeTaken) = measureTimedValue {
            part(input)
        }
        println("Part ${i+1}: $answer - took ${timeTaken.inWholeMilliseconds}ms")
    }
}