package io.dkozak.cimple.compiler

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

fun parseIt(input: String) = Parser(Buffer(Lexer(input))).parse()

class ParserTest {

    @Test
    fun `Parse 1 + 1`() {
        val result = parseIt("1 + 1")
        assertThat(result).isEqualTo(
                PlusNode(IntLiteral(1), IntLiteral(1))
        )
    }

    @Test
    fun `Parse 1 div 1`() {
        val result = parseIt("1 / 1")
        assertThat(result).isEqualTo(
                DivideNode(IntLiteral(1), IntLiteral(1))
        )
    }

    @Test
    fun `Parse 1 + 2 * 3`() {
        val result = parseIt("1 + 2 * 3")
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
        val result = parseIt("(1 + 2) * 3")
        assertThat(result).isEqualTo(
                MultiplyNode(
                        PlusNode(IntLiteral(1), IntLiteral(2)),
                        IntLiteral(3))
        )
    }


    @Test
    fun `Parse ((1 + 2) * 3) div (5)`() {
        val result = parseIt("((1 + 2) * 3) div (5)")
        assertThat(result).isEqualTo(
                DivideNode(
                        MultiplyNode(
                                PlusNode(IntLiteral(1), IntLiteral(2)),
                                IntLiteral(3)),
                        IntLiteral(5))
        )
    }
}