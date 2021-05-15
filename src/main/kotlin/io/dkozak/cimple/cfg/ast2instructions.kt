package io.dkozak.cimple.cfg

import io.dkozak.cimple.*

private val IfStatementNode.thenLabel: String
    get() = ".if_${id}_then"

private val IfStatementNode.elseLabel: String
    get() = ".if_${id}_else"

private val IfStatementNode.endLabel: String
    get() = ".if_${id}_finish"

private val ForStatementNode.conditionLabel: String
    get() = ".loop_${id}_cond"

private val ForStatementNode.endLabel: String
    get() = ".loop_${id}_end"

fun FunctionNode.lower(): Function {
    val holder = InstructionHolder()
    return holder.lower(this)
}

private class InstructionHolder {
    val instructions = mutableListOf<Instruction>()
    val varGenerator = VarNameGenerator()

    fun lower(functionNode: FunctionNode): Function {
        val params = functionNode.parameters.map { it.name }
        return varGenerator.reserveTemporarily(params) {
            generateBody(functionNode.body)
            Function(functionNode.name, params, instructions)
        }
    }

    fun generateBody(body: List<StatementNode>) {
        for (statement in body) {
            when (statement) {
                is VariableDefinitionNode -> {
                    generateVariableDefinition(statement)
                }

                is ExpressionStatementNode -> {
                    val junk = "_" + varGenerator.nextVariable()
                    val op = generateOperation(statement.expr)
                    instructions.add(Def(junk, Type.INT, op))
                }

                is ReturnNode -> {
                    val src = statement.expression?.let { generateExpression(it) }
                    instructions.add(Return(src))
                }

                is IfStatementNode -> {
                    val condResName = generateExpression(statement.condition)
                    val thenLabel = statement.thenLabel
                    val elseLabel = statement.elseLabel
                    val endLabel = statement.endLabel
                    instructions.add(If(condResName, thenLabel, elseLabel))

                    instructions.add(Label(thenLabel))
                    generateBody(statement.thenPart)
                    instructions.add(Jump(endLabel))

                    instructions.add(Label(elseLabel))
                    generateBody(statement.elsePart)
                    instructions.add(Label(endLabel))
                }

                is ForStatementNode -> {
                    statement.initializer?.let { generateVariableDefinition(it) }
                    val conditionLabel = statement.conditionLabel
                    val endLabel = statement.endLabel
                    instructions.add(Label(conditionLabel))
                    if (statement.condition != null) {
                        val condResultName = generateExpression(statement.condition)
                        instructions.add(BranchFalse(condResultName, endLabel))
                    } else {
                        // fallthrough into loop body
                    }
                    generateBody(statement.body)
                    statement.increment?.let { generateVariableDefinition(it) }
                    instructions.add(Jump(conditionLabel))
                    instructions.add(Label(endLabel))
                }
            }
        }
    }

    private fun generateVariableDefinition(statement: VariableDefinitionNode) {
        val op = generateOperation(statement.expr)
        varGenerator.usedVarNames.add(statement.name)
        instructions.add(Def(statement.name, Type.INT, op))
    }

    private fun generateExpression(expr: ExpressionNode): String {
        // todo resolve eagerly var nodes
        val op = generateOperation(expr)
        val varName = varGenerator.nextVariable()
        instructions.add(Def(varName, Type.INT, op))
        return varName
    }

    private fun generateOperation(expr: ExpressionNode): Operation = when (expr) {
        is BinaryOperationNode -> {
            val leftRes = generateExpression(expr.left)
            val rightRes = generateExpression(expr.right)
            Binary(leftRes, rightRes, expr.op)
        }

        is CallNode -> {
            val argEvalResults = expr.arguments.map { generateExpression(it) }
            Call(expr.functionName, argEvalResults)
        }

        is VariableNode -> {
            // todo resolve eagerly var nodes
            Id(expr.name)
        }
        is IntLiteralNode -> {
            Const(expr.value)
        }
    }


}

private data class VarNameGenerator(
    val usedVarNames: MutableSet<String> = mutableSetOf(),
    val buff: MutableList<Char> = mutableListOf()
) {
    fun nextVariable(): String {
        var carry = true
        var i = 0
        while (carry && i < buff.size) {
            buff[i] = buff[i] + 1
            carry = buff[i] == 'z' + 1
            if (carry) buff[i] = 'a'
            i++
        }
        if (carry) buff.add('a')
        val name = buff.joinToString("") { it.toString() }.reversed()
        return if (usedVarNames.add(name)) name else nextVariable()

    }

    inline fun <T> reserveTemporarily(names: List<String>, block: () -> T): T {
        val takenAlready = usedVarNames.intersect(names)
        check(takenAlready.isEmpty()) { "Names  $takenAlready are already taken" }
        usedVarNames.addAll(names)
        try {
            return block()
        } finally {
            usedVarNames.removeAll(names)
        }
    }
}

fun main() {
    val holder = VarNameGenerator()
    repeat(2000) {
        println(holder.nextVariable())
    }
}