package io.dkozak.cimple.cfg

import io.dkozak.cimple.ast.appendOffset

fun Function.prettyPrint() {
    val builder = StringBuilder()
    builder.toSourceString(this)
    println(builder.toString())
}

fun StringBuilder.toSourceString(function: Function) {
    append("@${function.name}(")
    for (i in function.params.indices) {
        if (i > 0) append(", ")
        append(function.params[i])
    }
    appendLine(") {")

    generateBody(function.instructions)

    appendLine("}")
}

private fun StringBuilder.generateBody(instructions: List<Instruction>) {
    val offset = 2
    for (instruction in instructions) {
        if (instruction is Label) {
            append(instruction.name)
            appendLine(':')
            continue
        }
        appendOffset(offset)
        when (instruction) {
            is Def -> appendLine("${instruction.dst} : ${instruction.type} = ${generateOp(instruction.op)}")
            is Return -> appendLine("ret ${instruction.src ?: ""}")
            is If -> appendLine("if ${instruction.condition} ${instruction.thenLabel} ${instruction.elseLabel}")
            is Jump -> appendLine("jump ${instruction.label}")
            is BranchFalse -> appendLine("ifFalse ${instruction.condition} ${instruction.label}")
        }
    }
}

fun generateOp(op: Operation): String = when (op) {
    is Binary -> "${op.op} ${op.left} ${op.right}"
    is Id -> "id ${op.source}"
    is Call -> "call ${op.name} ${op.args.joinToString(" ")}"
    is Const -> "const ${op.value}"
}

