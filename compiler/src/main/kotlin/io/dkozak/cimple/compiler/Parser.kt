package io.dkozak.cimple.compiler

import org.assertj.core.api.Assertions.assertThat

class Parser(private val buffer: Buffer) {

    /**
     * Grammar
     *
     * start -> (E '\n')*
     * E -> T + E | T - E | T
     * T -> F * T | F / T | F
     * F -> (E) | int | double
     *
     */


    fun parse(): AstNode = expression()

    private fun expression(): AstNode {
        val left = term()
        return when (buffer.peek()) {
            Plus -> {
                buffer.consume()
                PlusNode(left, expression())
            }
            Minus -> {
                buffer.consume()
                MinusNode(left, expression())
            }
            else -> left
        }
    }

    private fun term(): AstNode {
        val left = functor()
        return when (buffer.peek()) {
            Multiply -> {
                buffer.consume()
                MultiplyNode(left, term())
            }
            Divide -> {
                buffer.consume()
                DivideNode(left, term())
            }
            in setOf(Plus, Minus, ParenClose) -> left
            null -> left
            else -> TODO("handle parser error ${buffer.peek()}")
        }
    }

    private fun functor(): AstNode {
        val token = buffer.consume()
        return when (token) {
            ParenOpen -> {
                val expr = expression()
                require(ParenClose)
                expr
            }
            is IntToken -> IntLiteral(token.value)
            is DoubleToken -> DoubleLiteral(token.value)
            else -> TODO("handle parser error ${buffer.peek()}")
        }
    }

    private fun require(token: Token) {
        assertThat(buffer.consume())
                .isEqualTo(token)
    }

}