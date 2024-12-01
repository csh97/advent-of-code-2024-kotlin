import java.io.BufferedReader
import kotlin.io.bufferedReader
import kotlin.io.readLines
import kotlin.io.readText
import kotlin.jvm.javaClass

fun String.readFile(): String = read().readText()

fun String.readLines(): List<String> = read().readLines()

fun String.read(): BufferedReader = object {}.javaClass.getResourceAsStream(this)?.bufferedReader()
    ?: throw Exception("Cant read file: $this")