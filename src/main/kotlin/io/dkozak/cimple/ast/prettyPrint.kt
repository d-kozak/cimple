package io.dkozak.cimple.ast

import io.dkozak.cimple.*

fun Node.prettyPrint() {
    println(toSourceString())
}


fun Node.toSourceString(): String {
    val builder = StringBuilder()
    builder.serialize(this, 0)
    return builder.toString()
}


private fun StringBuilder.serialize(node: Node, offset: Int, newLine: Boolean = true) {
    appendOffset(offset)
    when (node) {
        is FileNode -> {
            node.functions.forEach { serialize(it, offset) }
        }

        is FunctionNode -> {
            append("fun ")
            append(node.name)
            append("(")
            append(node.parameters.map { it.name }.joinToString(","))
            append(')')
            append(" {")
            append('\n')
            node.body.forEach { serialize(it, offset + 2) }
            appendOffset(offset)
            append("}")
        }

        is VariableDefinitionNode -> {
            append(node.name)
            append(" = ")
            serialize(node.expr, 0)
            append(";")
            if (newLine) append("\n")
        }

        is ExpressionStatementNode -> {
            serialize(node.expr, 0)
            append(";")
            if (newLine) append("\n")
        }

        is IfStatementNode -> {
            append("if ")
            serialize(node.condition, 0)
            append(" {\n")
            node.thenPart.forEach { serialize(it, offset + 2) }
            appendOffset(offset)
            append("}")
            if (node.elsePart.isNotEmpty()) {
                append(" else {")
                node.elsePart.forEach { serialize(it, offset + 2) }
                appendOffset(offset)
                append("}\n")
            } else {
                append('\n')
            }
        }

        is ForStatementNode -> {
            append("for ")
            if (node.initializer != null) serialize(node.initializer, 0, false)
            append(" ; ")
            if (node.condition != null) serialize(node.condition, 0)
            append(" ; ")
            if (node.increment != null) serialize(node.increment, 0)
            append(" {\n")
            node.body.forEach { serialize(it, offset + 2) }
            appendOffset(offset)
            append("}\n")
        }

        is ReturnNode -> {
            append("return ")
            if (node.expression != null) serialize(node.expression, 0)
            append(";")
            if (newLine) append("\n")
        }

        is BinaryOperationNode -> {
            serialize(node.left, 0)
            append(" ${node.op.asString} ")
            serialize(node.right, 0)
        }

        is CallNode -> {
            append(node.functionName)
            append("(")
            for (i in node.arguments.indices) {
                if (i > 0) append(", ")
                serialize(node.arguments[i], 0)
            }
            append(")")
        }

        is IntLiteralNode -> {
            append(node.value)
        }

        is VariableNode -> {
            append(node.name)
        }
    }
}

fun StringBuilder.appendOffset(offset: Int) {
    repeat(offset) {
        append(' ')
    }
}
