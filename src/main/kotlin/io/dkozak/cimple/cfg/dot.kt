package io.dkozak.cimple.cfg

import io.dkozak.cimple.ast.appendOffset

fun ControlFlowGraph.toDotFormat(): String = buildString {
    appendLine("digraph CFG {")
    for (block in blockMap.values) {
        for (next in block.successors) {
            appendOffset(2)
            append(block.name.sanitize())
            append(" -> ")
            append(next.name.sanitize())
            appendLine(";")
        }
    }
    appendLine("}")
}

private fun String.sanitize(): String {
    if (startsWith(".")) return substring(1)
    return this
}
