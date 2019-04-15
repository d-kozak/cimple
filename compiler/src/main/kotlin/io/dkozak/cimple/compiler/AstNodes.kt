package io.dkozak.cimple.compiler

sealed class AstNode

data class ExpressionList(val expressions: List<AstNode>) : AstNode()
data class IntLiteral(val value: Int) : AstNode()
data class DoubleLiteral(val value: Double) : AstNode()
data class MinusNode(val left: AstNode, val right: AstNode) : AstNode()
data class PlusNode(val left: AstNode, val right: AstNode) : AstNode()
data class MultiplyNode(val left: AstNode, val right: AstNode) : AstNode()
data class DivideNode(val left: AstNode, val right: AstNode) : AstNode()


class ErrorNode(val message: String = "") : AstNode() {

    override fun hashCode(): Int = 1

    override fun equals(other: Any?): Boolean = other is ErrorNode

    override fun toString(): String {
        return "ErrorNode(message='$message')"
    }
}
