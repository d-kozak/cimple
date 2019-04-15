package io.dkozak.cimple.compiler

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

fun parseExpression(input: String) = Parser(Buffer(Lexer(input))).expression()

fun parseExpressionList(input: String) = Parser(Buffer(Lexer(input))).parse()

class ParserTest {

    @Nested
    inner class OneExpression {
        @Test
        fun `Parse 1 + 1`() {
            val result = parseExpression("1 + 1")
            assertThat(result).isEqualTo(
                    PlusNode(IntLiteral(1), IntLiteral(1))
            )
        }

        @Test
        fun `Parse 1 div 1`() {
            val result = parseExpression("1 / 1")
            assertThat(result).isEqualTo(
                    DivideNode(IntLiteral(1), IntLiteral(1))
            )
        }

        @Test
        fun `Parse 1 + 2 * 3`() {
            val result = parseExpression("1 + 2 * 3")
            assertThat(result).isEqualTo(
                    PlusNode(
                            IntLiteral(1),
                            MultiplyNode(
                                    IntLiteral(2),
                                    IntLiteral(3)
                            )
                    )
            )
        }


        @Test
        fun `Parse (1 + 2) * 3`() {
            val result = parseExpression("(1 + 2) * 3")
            assertThat(result).isEqualTo(
                    MultiplyNode(
                            PlusNode(IntLiteral(1), IntLiteral(2)),
                            IntLiteral(3))
            )
        }


        @Test
        fun `Parse ((1 + 2) * 3) div (5)`() {
            val result = parseExpression("((1 + 2) * 3) / (5)")
            assertThat(result).isEqualTo(
                    DivideNode(
                            MultiplyNode(
                                    PlusNode(IntLiteral(1), IntLiteral(2)),
                                    IntLiteral(3)),
                            IntLiteral(5))
            )
        }
    }

    @Nested
    inner class MultipleExpressions {

        @Test
        fun `parse 1 + 1 and 2 * 3`() {
            val result = parseExpressionList("1 + 5 \n 2 * 3")
            assertThat(result)
                    .isEqualTo(ExpressionList(listOf(
                            PlusNode(IntLiteral(1), IntLiteral(5)),
                            MultiplyNode(IntLiteral(2), IntLiteral(3))
                    )))
        }

        @Test
        fun `parse 1 + 1 and 2 * 3 and (5) - 2 * 3`() {
            val result = parseExpressionList("1 + 5 \n 2 * 3 \n (5) - 2 * 3")
            assertThat(result)
                    .isEqualTo(ExpressionList(listOf(
                            PlusNode(IntLiteral(1), IntLiteral(5)),
                            MultiplyNode(IntLiteral(2), IntLiteral(3)),
                            MinusNode(IntLiteral(5), MultiplyNode(IntLiteral(2), IntLiteral(3)))
                    )))
        }
    }

}