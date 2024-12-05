import kotlin.time.measureTimedValue

private fun part1(rules: List<Pair<String, String>>, updates: List<List<String>>): Int {
    return updates.sumOf { update ->
        if (isValidUpdate(rules, update)) update[update.size / 2].toInt() else 0
    }
}

private fun part2(rules: List<Pair<String, String>>, updates: List<List<String>>): Int {
    val invalidUpdates = updates.filterNot { isValidUpdate(rules, it) }

    return invalidUpdates.sumOf { update ->
        val fixedUpdate = tryAndFix(rules, update)

        fixedUpdate[fixedUpdate.size / 2].toInt()
    }
}

fun isValidUpdate(rules: List<Pair<String, String>>, update: List<String>): Boolean {
    return update.fold(emptyList<String>() to true) { (prev, ordered), page ->
        val pagesToComeBefore = rules.filter { rule -> rule.first == page }.map { it.second  }

        if (pagesToComeBefore.any { prev.contains(it) }) {
            return@isValidUpdate false
        } else {
            prev.plus(page) to true
        }
    }.second
}

fun tryAndFix(rules: List<Pair<String, String>>, update: List<String>): List<String> {
    val possibleFixed = update.fold(emptyList<String>()) { fixedUpdate, page ->
        val pagesToComeBefore = rules.filter { it.first == page }.map { it.second }

        val pageToComeBefore = fixedUpdate.findLast { newPage -> pagesToComeBefore.any { it == newPage } }

        if (pageToComeBefore != null) {
            val indexToInsert = fixedUpdate.indexOfLast { it == pageToComeBefore }
            fixedUpdate.slice((0 until indexToInsert)) + page + fixedUpdate.slice((indexToInsert until fixedUpdate.size))
        } else {
            fixedUpdate.plus(page)
        }
    }

    return if (isValidUpdate(rules, possibleFixed)) {
        possibleFixed
    } else {
        tryAndFix(rules, possibleFixed)
    }
}

fun main() {
    val input = "day05.txt".readFile()

    val (rawRules, rawUpdates) = input.split("\n\n")

    val rules = rawRules.split("\n").map {
        val (page1, page2) = it.split("|")
        page1 to page2
    }

    val updates = rawUpdates.split("\n").map {
        it.split(",")
    }

    listOf(::part1, ::part2).forEachIndexed { i, part ->
        val (answer, timeTaken) = measureTimedValue {
            part(rules, updates)
        }
        println("Part ${i+1}: $answer - took ${timeTaken.inWholeMilliseconds}ms")
    }
}