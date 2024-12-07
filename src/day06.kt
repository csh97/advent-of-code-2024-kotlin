import Guard.Companion.Position
import Guard.Companion.Direction
import kotlin.collections.plus
import kotlin.time.measureTimedValue

private data class Guard(
    var position: Position,
    var direction: Direction,
    val positionsVisited: MutableMap<Position, Direction> = mutableMapOf(),
    val blocksSeen: MutableMap<Position, Direction> = mutableMapOf()
) {
    companion object {
        data class Position(val x: Int, val y: Int)
        enum class Direction(val x: Int, val y: Int) { UP(0, -1), DOWN(0, 1), LEFT(-1, 0), RIGHT(1, 0) }
    }
}

private fun part1(input: List<String>): Int {
    val initialY = input.indexOfFirst { it.contains("^") }
    val initialX = input[initialY].indexOf("^")

    return walk(guard = Guard(position = Position(x = initialX, y = initialY), direction =  Direction.UP), map = input).count()
}

private tailrec fun walk(guard: Guard, map: List<String>): Set<Position> {
    val inFront = if (guard.position.y + guard.direction.y < map.size && guard.position.y + guard.direction.y >= 0 && guard.position.x + guard.direction.x < map[0].length && guard.position.x + guard.direction.x >= 0) {
        map[guard.position.y + guard.direction.y][guard.position.x + guard.direction.x]
    } else {
        null
    }

    if (inFront == null) {
        guard.positionsVisited.put(guard.position, guard.direction)
        return guard.positionsVisited.keys
    }

    val nextDirection = if (inFront == '#') {
        if (guard.blocksSeen.contains(Position(y = guard.position.y + guard.direction.y, x = guard.position.x + guard.direction.x))
                    && guard.blocksSeen[Position(y = guard.position.y + guard.direction.y, x = guard.position.x + guard.direction.x)] == guard.direction) {
            return emptySet()
        }
        guard.blocksSeen.put(Position(y = guard.position.y + guard.direction.y, x = guard.position.x + guard.direction.x), guard.direction)
        when (guard.direction) {
            Direction.UP -> Direction.RIGHT
            Direction.DOWN -> Direction.LEFT
            Direction.LEFT -> Direction.UP
            Direction.RIGHT -> Direction.DOWN
        }
    } else {
        guard.direction
    }

    if (map[guard.position.y + guard.direction.y][guard.position.x + guard.direction.x] != '#') {
        guard.position = Position(y = guard.position.y + nextDirection.y, x = guard.position.x + nextDirection.x)
    }

    guard.positionsVisited[guard.position] = guard.direction
    guard.direction = nextDirection


    return walk(guard, map)
}

private fun part2(input: List<String>): Int {
    val initialY = input.indexOfFirst { it.contains("^") }
    val initialX = input[initialY].indexOf("^")
    val initialPosition = Position(x = initialX, y = initialY)
    val path = walk(Guard(position = initialPosition, direction = Direction.UP), input).filterNot { it == initialPosition }.dropLast(1)

    return path.map { it ->
        val lineToAddBlock = input[it.y]
        val newLineWithBlock = lineToAddBlock.slice((0..it.x-1)) + "#" + lineToAddBlock.slice((it.x+1..lineToAddBlock.length-1))
        val mapWithAddedBlock = input.slice((0..it.y-1)) + newLineWithBlock + input.slice((it.y+1..input.size-1))

        val result = walk(guard = Guard(position = initialPosition, direction =  Direction.UP), map = mapWithAddedBlock)
        result.isEmpty()
    }.count { it == true } + 1
}

fun main() {
    val input = "day06.txt".readLines()

    listOf(::part1, ::part2).forEachIndexed { i, part ->
        val (answer, timeTaken) = measureTimedValue {
            part(input)
        }
        println("Part ${i+1}: $answer - took ${timeTaken.inWholeMilliseconds}ms")
    }
}