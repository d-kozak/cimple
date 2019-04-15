package io.dkozak.cimple.compiler

class Parser(private val buffer: Buffer) {

    /**
     * Grammar
     *
     * start -> (E '\n'?)*
     * E -> T + E | T - E | T
     * T -> F * T | F / T | F
     * F -> (E) | int | double
     *
     */


    fun parse(): AstNode = expressionList()

    private fun expressionList(): AstNode {
        val result = mutableListOf<AstNode>()
        while (buffer.peek() != null) {
            result.add(expression())
            if (buffer.peek() == Newline)
                buffer.consume()
        }
        return ExpressionList(result)
    }


    fun expression(): AstNode {
        val left = term()
        if (left is ErrorNode) {
            return left
        }
        return when (buffer.peek()) {
            Plus -> {
                buffer.consume()
                val right = expression()
                if (right is ErrorNode) {
                    return right
                }
                PlusNode(left, right)
            }
            Minus -> {
                buffer.consume()
                val right = expression()
                if (right is ErrorNode) {
                    return right
                }
                MinusNode(left, right)
            }
            in setOf(ParenClose, Newline) -> left
            null -> left
            else -> {
                ErrorNode("Unexpected token ${buffer.peek()} in expression")
            }
        }
    }

    private fun term(): AstNode {
        val left = functor()
        if (left is ErrorNode)
            return left
        return when (buffer.peek()) {
            Multiply -> {
                buffer.consume()
                val right = term()
                if (right is ErrorNode)
                    return right
                MultiplyNode(left, right)
            }
            Divide -> {
                buffer.consume()
                val right = term()
                if (right is ErrorNode)
                    return right
                DivideNode(left, right)
            }
            in setOf(Plus, Minus, ParenClose, Newline) -> left
            null -> left
            else -> ErrorNode("Unexpected token ${buffer.peek()} in rule term")
        }
    }

    private fun functor(): AstNode {
        val token = buffer.consume()
        return when (token) {
            ParenOpen -> {
                val expr = expression()
                if (expr is ErrorNode)
                    return expr
                val nextToken = buffer.consume()
                if (nextToken != ParenClose)
                    ErrorNode("Expected parent close, got $nextToken")
                expr
            }
            is IntToken -> IntLiteral(token.value)
            is DoubleToken -> DoubleLiteral(token.value)
            else -> ErrorNode("Unexpected token $token in rule functor")
        }
    }

}