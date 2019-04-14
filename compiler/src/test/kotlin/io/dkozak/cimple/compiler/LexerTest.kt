package io.dkozak.cimple.compiler


import org.assertj.core.api.Assertions.assertThat

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test


class LexerTest {

    @Nested
    inner class Numbers {
        @Test
        fun `Lex a single integer 42`() {
            val input = "42"
            val token = lexer(input)
            assert(token is IntToken)
            assertThat((token as IntToken).value).isEqualTo(42)
        }

        @Test
        fun `Lex a single integer 1`() {
            val input = "1"
            val token = lexer(input)
            assert(token is IntToken) { "Should be Int" }
            assertThat((token as IntToken).value).isEqualTo(1)
        }

        @Test
        fun `Lex a single integer 100`() {
            val input = "100"
            val token = lexer(input)
            assert(token is IntToken) { "Should be Int" }
            assertThat((token as IntToken).value).isEqualTo(100)
        }

        @Test
        fun `Lex a single double 4,2`() {
            val input = "4.2"
            val token = lexer(input)
            assert(token is DoubleToken) { "Should be Double" }
            assertThat((token as DoubleToken).value).isEqualTo(4.2)
        }

        @Test
        fun `Lex a single double 135,666`() {
            val input = "135.666"
            val token = lexer(input)
            assert(token is DoubleToken) { "Should be Double" }
            assertThat((token as DoubleToken).value).isEqualTo(135.666)
        }
    }


}