package io.dkozak.cimple

data class SourceLocation(
    val lines: IntRange,
    val cols: IntRange
)

abstract class Node {
    abstract val location: SourceLocation
}

data class FileNode(val functions: List<FunctionNode>, override val location: SourceLocation) : Node()

data class FunctionNode(
    val name: String,
    val parameters: List<ParameterNode>,
    val body: List<StatementNode>,
    override val location: SourceLocation
) : Node()

data class ParameterNode(
    val name: String,
    override val location: SourceLocation
) : Node()

sealed class StatementNode : Node()

data class VariableDefinitionNode(
    val name: String,
    val expr: ExpressionNode,
    override val location: SourceLocation
) : StatementNode()

data class ExpressionStatementNode(
    val expr: ExpressionNode,
    override val location: SourceLocation
) : StatementNode()

data class IfStatementNode(
    val condition: ExpressionNode,
    val thenPart: List<StatementNode>,
    val elsePart: List<StatementNode>,
    override val location: SourceLocation
) : StatementNode()


data class ForStatement(
    val initializer: VariableDefinitionNode?,
    val condition: ExpressionNode?,
    val increment: VariableDefinitionNode?,
    val body: List<StatementNode>,
    override val location: SourceLocation
) : StatementNode()

data class ReturnStatement(
    val expression: ExpressionNode?,
    override val location: SourceLocation
) : StatementNode()

sealed class ExpressionNode : Node()

data class BinaryOperationNode(
    val left: ExpressionNode,
    val right: ExpressionNode,
    val op: BinaryOperation,
    override val location: SourceLocation
) : ExpressionNode()

data class CallNode(
    val functionName: String,
    val arguments: List<ExpressionNode>,
    override val location: SourceLocation
) : ExpressionNode()

data class IntLiteral(
    val value: Int,
    override val location: SourceLocation
) : ExpressionNode()

data class VariableNode(
    val name: String,
    override val location: SourceLocation
) : ExpressionNode()

enum class BinaryOperation(val asString: String) {
    MULT("*"),
    DIV("/"),
    ADD("+"),
    SUB("-"),
    EQ("=="),
    NE("!="),
    GT(">"),
    GE(">="),
    LT("<"),
    LE("<=")
}

fun String.toBinaryOp(): BinaryOperation = when (this) {
    "*" -> BinaryOperation.MULT
    "/" -> BinaryOperation.DIV
    "+" -> BinaryOperation.ADD
    "-" -> BinaryOperation.SUB
    "==" -> BinaryOperation.EQ
    "!=" -> BinaryOperation.NE
    ">" -> BinaryOperation.GT
    ">=" -> BinaryOperation.GE
    "<" -> BinaryOperation.LT
    "<=" -> BinaryOperation.LE
    else -> unreachable()
}