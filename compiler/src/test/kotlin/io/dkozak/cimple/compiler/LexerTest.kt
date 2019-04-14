package io.dkozak.cimple.compiler


import org.assertj.core.api.Assertions.assertThat

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test


class LexerTest {

    @Nested
    inner class Numbers {
        @Test
        fun `Single integer 42`() {
            val input = "42"
            val lexer = Lexer(input)
            val token = lexer.getNextToken()
            assert(token is IntToken)
            assertThat((token as IntToken).value).isEqualTo(42)
        }

        @Test
        fun `Single integer 1`() {
            val input = "1"
            val lexer = Lexer(input)
            val token = lexer.getNextToken()
            assert(token is IntToken) { "Should be Int" }
            assertThat((token as IntToken).value).isEqualTo(1)
        }

        @Test
        fun `Single integer 100`() {
            val input = "100"
            val lexer = Lexer(input)
            val token = lexer.getNextToken()
            assert(token is IntToken) { "Should be Int" }
            assertThat((token as IntToken).value).isEqualTo(100)
        }

        @Test
        fun `Single double 4,2`() {
            val input = "4.2"
            val lexer = Lexer(input)
            val token = lexer.getNextToken()
            assert(token is DoubleToken) { "Should be Double" }
            assertThat((token as DoubleToken).value).isEqualTo(4.2)
        }

        @Test
        fun `Single double 135,666`() {
            val input = "135.666"
            val lexer = Lexer(input)
            val token = lexer.getNextToken()
            assert(token is DoubleToken) { "Should be Double" }
            assertThat((token as DoubleToken).value).isEqualTo(135.666)
        }

        @Test
        fun `Single integer 135 with something behing it`() {
            val input = "135."
            val lexer = Lexer(input)
            val token = lexer.getNextToken()
            assert(token is IntToken) { "Should be Double" }
            assertThat((token as IntToken).value).isEqualTo(135)
        }

        @Test
        fun `Lex a double with something behing it`() {
            val input = "130.g."
            val lexer = Lexer(input)
            val token = lexer.getNextToken()
            assert(token is IntToken) { "Should be Double" }
            assertThat((token as IntToken).value).isEqualTo(130)
        }

    }

    @Nested
    inner class Operators {
        @Test
        fun `Plus minus multiply divide`() {

            val input = "+-*/"
            val expectedTokens = listOf(Plus, Minus, Multiply, Divide)
            val lexer = Lexer(input)

            for (expectedToken in expectedTokens) {
                assertThat(lexer.getNextToken()).isEqualTo(expectedToken)
            }

        }
    }

    @Nested
    inner class SimpleExpressions {

        @Test
        fun `1 + 1`() {
            val input = "1+1"
            val lexer = Lexer(input)
            assertThat(lexer.getNextToken())
                    .isInstanceOf(IntToken::class.java)
                    .extracting { token -> (token as IntToken).value }
                    .isEqualTo(1)
            assertThat(lexer.getNextToken()).isEqualTo(Plus)
            assertThat(lexer.getNextToken())
                    .isInstanceOf(IntToken::class.java)
                    .extracting { token -> (token as IntToken).value }
                    .isEqualTo(1)

        }

        @Test
        fun `123 + 36`() {
            val input = "123+36"
            val lexer = Lexer(input)
            assertThat(lexer.getNextToken())
                    .isInstanceOf(IntToken::class.java)
                    .extracting { token -> (token as IntToken).value }
                    .isEqualTo(123)
            assertThat(lexer.getNextToken()).isEqualTo(Plus)
            assertThat(lexer.getNextToken())
                    .isInstanceOf(IntToken::class.java)
                    .extracting { token -> (token as IntToken).value }
                    .isEqualTo(36)

        }


        @Test
        fun `123,44 + 36,64`() {
            val input = "123.44+36.64"
            val lexer = Lexer(input)
            assertThat(lexer.getNextToken())
                    .isInstanceOf(DoubleToken::class.java)
                    .extracting { token -> (token as DoubleToken).value }
                    .isEqualTo(123.44)
            assertThat(lexer.getNextToken()).isEqualTo(Plus)
            assertThat(lexer.getNextToken())
                    .isInstanceOf(DoubleToken::class.java)
                    .extracting { token -> (token as DoubleToken).value }
                    .isEqualTo(36.64)

        }

    }

    @Nested
    inner class Spaces {

        @Test
        fun `420 + 8080,42`() {
            val input = "420 + 8080.42"
            val lexer = Lexer(input)
            assertThat(lexer.getNextToken())
                    .isInstanceOf(IntToken::class.java)
                    .extracting { token -> (token as IntToken).value }
                    .isEqualTo(420)
            assertThat(lexer.getNextToken()).isEqualTo(Plus)
            assertThat(lexer.getNextToken())
                    .isInstanceOf(DoubleToken::class.java)
                    .extracting { token -> (token as DoubleToken).value }
                    .isEqualTo(8080.42)

        }

        @Test
        fun `420   +        8080,42    divide   135`() {
            val input = "420   +        8080.42    /   135"
            val lexer = Lexer(input)
            assertThat(lexer.getNextToken())
                    .isInstanceOf(IntToken::class.java)
                    .extracting { token -> (token as IntToken).value }
                    .isEqualTo(420)
            assertThat(lexer.getNextToken()).isEqualTo(Plus)
            assertThat(lexer.getNextToken())
                    .isInstanceOf(DoubleToken::class.java)
                    .extracting { token -> (token as DoubleToken).value }
                    .isEqualTo(8080.42)
            assertThat(lexer.getNextToken()).isEqualTo(Divide)
            assertThat(lexer.getNextToken())
                    .isInstanceOf(IntToken::class.java)
                    .extracting { token -> (token as IntToken).value }
                    .isEqualTo(135)

        }


    }
}