package io.dkozak.cimple

import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import java.io.File


fun readInput(args: Array<String>): String {
    if (args.isEmpty()) {
        return buildString {
            var line = readLine()
            while (line != null) {
                append(line)
                line = readLine()
            }
        }
    }
    return File(args[0]).readText()
}

fun run(input: String) {
    println(input)
    println("ab")
    println("ab")
    val ast = parse(input)
    println(ast)
}

fun main(args: Array<String>) {
    val prog = readInput(args)
    run(prog)
}

private fun parse(prog: String) = CimpleParser(CommonTokenStream(CimpleLexer(CharStreams.fromString(prog)))).file()


