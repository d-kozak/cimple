package io.dkozak.cimple

import io.dkozak.cimple.ast.prettyPrint
import io.dkozak.cimple.ast.toCimpleAst
import io.dkozak.cimple.cfg.lower
import io.dkozak.cimple.cfg.prettyPrint
import io.dkozak.cimple.cfg.toCfg
import io.dkozak.cimple.cfg.toDotFormat
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
    val lowered = ast.functions.map { it.lower() }
    for (function in lowered) {
        println("---")
        println(function)
        function.prettyPrint()
        val cfg = function.toCfg()
        println(cfg.toDotFormat())
    }
}

fun unreachable(msg: String = ""): Nothing = error("Should never get here. $msg")


fun main(args: Array<String>) {
    val prog = readInput(args)
    run(prog)
}




