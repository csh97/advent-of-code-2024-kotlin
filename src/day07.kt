import kotlin.collections.listOf
import kotlin.math.pow
import kotlin.time.measureTimedValue

private enum class Operator { ADD, MULTIPLY, CONCAT }

private fun part1(input: List<String>) = solve(input, listOf(Operator.ADD, Operator.MULTIPLY))

private fun part2(input: List<String>) = solve(input, Operator.entries.toList())

private fun solve(input: List<String>, ops: List<Operator>): Long {
    val equations = input.map {
        val (testValue, nums) = it.split(":")
        testValue.toLong() to nums.trim().split(" ").map { it.toLong() }
    }
    val permutationCache = mutableMapOf<Int, List<List<Operator>>>()

    return equations.sumOf { (testValue, nums) ->
        val numPermutations = ops.size.toFloat().pow(nums.size - 1).toInt()

        val operatorPermutations = permutationCache.getOrPut(numPermutations) {
            val permutationIndexes = (0 until numPermutations).map {
                if (numPermutations > ops.size) it.toString(ops.size).padStart(nums.size - 1, '0') else it.toString(ops.size)
            }

            permutationIndexes.map { it.map { ops[it.toString().toInt()] } }
        }

        operatorPermutations.map { permutation ->
            val total = permutation.foldIndexed(0L) { i, total, op ->
                val firstNum = if (total == 0L) nums[i] else total
                val secondNum = nums[i + 1]
                when (op) {
                    Operator.ADD -> firstNum + secondNum
                    Operator.MULTIPLY -> firstNum * secondNum
                    Operator.CONCAT -> "$firstNum$secondNum".toLong()
                }
            }

            if (total == testValue) testValue else 0L
        }.firstOrNull { it != 0L } ?: 0L
    }
}

fun main() {
    val input = "day07.txt".readLines()

    listOf(::part1, ::part2).forEachIndexed { i, part ->
        val (answer, timeTaken) = measureTimedValue {
            part(input)
        }
        println("Part ${i + 1}: $answer - took ${timeTaken.inWholeMilliseconds}ms")
    }
}