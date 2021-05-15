package io.dkozak.cimple

import io.dkozak.cimple.ast.prettyPrint
import io.dkozak.cimple.ast.toCimpleAst
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
    val ast = parse(input).toCimpleAst()
    println(ast)
    ast.prettyPrint()
}

fun unreachable(msg: String = ""): Nothing = error("Should never get here. $msg")


fun main(args: Array<String>) {
    val prog = readInput(args)
    run(prog)
}



