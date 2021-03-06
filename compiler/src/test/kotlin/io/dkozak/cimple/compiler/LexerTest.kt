package io.dkozak.cimple.compiler


import org.assertj.core.api.AbstractObjectAssert
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.ObjectAssert
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test


fun ObjectAssert<*>.isIntToken(expectedValue: Int): AbstractObjectAssert<*, *> = this.isInstanceOf(IntToken::class.java)
        .extracting { token -> (token as IntToken).value }
        .isEqualTo(expectedValue)


fun ObjectAssert<*>.isDoubleToken(expectedValue: Double): AbstractObjectAssert<*, *> = this.isInstanceOf(DoubleToken::class.java)
        .extracting { token -> (token as DoubleToken).value }
        .isEqualTo(expectedValue)

fun ObjectAssert<*>.isUnkownCharacters(expectedValue: String): AbstractObjectAssert<*, *> = this.isInstanceOf(UnknownCharacters::class.java)
        .extracting { token -> (token as UnknownCharacters).value }
        .isEqualTo(expectedValue)


class LexerTest {

    @Nested
    inner class Numbers {
        @Test
        fun `Single integer 42`() {
            val lexer = Lexer("42")
            assertThat(lexer.getNextToken())
                    .isIntToken(42)
        }

        @Test
        fun `Single integer 1`() {
            val lexer = Lexer("1")
            assertThat(lexer.getNextToken())
                    .isIntToken(1)
        }

        @Test
        fun `Single integer 100`() {
            val lexer = Lexer("100")
            assertThat(lexer.getNextToken())
                    .isIntToken(100)
        }

        @Test
        fun `Single double 4,2`() {
            val lexer = Lexer("4.2")
            assertThat(lexer.getNextToken())
                    .isDoubleToken(4.2)
        }

        @Test
        fun `Single double 135,666`() {
            val lexer = Lexer("135.666")
            assertThat(lexer.getNextToken())
                    .isDoubleToken(135.666)
        }

        @Test
        fun `Single integer 135 with something behing it`() {
            val lexer = Lexer("135.")
            assertThat(lexer.getNextToken())
                    .isIntToken(135)
        }

        @Test
        fun `Lex a single 130 with something behing it`() {
            val lexer = Lexer("130.g.")
            assertThat(lexer.getNextToken())
                    .isIntToken(130)
        }

    }

    @Nested
    inner class Operators {
        @Test
        fun `Plus minus multiply divide`() {

            val lexer = Lexer("+-*/")
            val expectedTokens = listOf(Plus, Minus, Multiply, Divide)

            for (expectedToken in expectedTokens) {
                assertThat(lexer.getNextToken())
                        .isEqualTo(expectedToken)
            }

        }
    }

    @Nested
    inner class SimpleExpressions {

        @Test
        fun `1 + 1`() {
            val lexer = Lexer("1+1")
            assertThat(lexer.getNextToken())
                    .isIntToken(1)
            assertThat(lexer.getNextToken()).isEqualTo(Plus)
            assertThat(lexer.getNextToken())
                    .isIntToken(1)

        }

        @Test
        fun `123 + 36`() {
            val lexer = Lexer("123+36")
            assertThat(lexer.getNextToken())
                    .isIntToken(123)
            assertThat(lexer.getNextToken()).isEqualTo(Plus)
            assertThat(lexer.getNextToken())
                    .isIntToken(36)

        }


        @Test
        fun `123,44 + 36,64`() {
            val lexer = Lexer("123.44+36.64")
            assertThat(lexer.getNextToken())
                    .isDoubleToken(123.44)
            assertThat(lexer.getNextToken()).isEqualTo(Plus)
            assertThat(lexer.getNextToken())
                    .isDoubleToken(36.64)

        }

    }


    @Nested
    inner class Parens {

        @Test
        fun `expression (12+15,13)*11`() {
            val lexer = Lexer("     (12 + 15.13   )  *       11  ")
            assertThat(lexer.getNextToken())
                    .isEqualTo(ParenOpen)
            assertThat(lexer.getNextToken())
                    .isIntToken(12)
            assertThat(lexer.getNextToken())
                    .isEqualTo(Plus)
            assertThat(lexer.getNextToken())
                    .isDoubleToken(15.13)
            assertThat(lexer.getNextToken())
                    .isEqualTo(ParenClose)
            assertThat(lexer.getNextToken())
                    .isEqualTo(Multiply)
            assertThat(lexer.getNextToken())
                    .isIntToken(11)
        }

        @Test
        fun `expression ds(1+(2-3))*(5)`() {
            val lexer = Lexer("  ( 1 + ( 2 - 3 ) ) * ( 5 ) ")
            assertThat(lexer.getNextToken())
                    .isEqualTo(ParenOpen)
            assertThat(lexer.getNextToken())
                    .isIntToken(1)
            assertThat(lexer.getNextToken())
                    .isEqualTo(Plus)
            assertThat(lexer.getNextToken())
                    .isEqualTo(ParenOpen)
            assertThat(lexer.getNextToken())
                    .isIntToken(2)
            assertThat(lexer.getNextToken())
                    .isEqualTo(Minus)
            assertThat(lexer.getNextToken())
                    .isIntToken(3)
            assertThat(lexer.getNextToken())
                    .isEqualTo(ParenClose)
            assertThat(lexer.getNextToken())
                    .isEqualTo(ParenClose)
            assertThat(lexer.getNextToken())
                    .isEqualTo(Multiply)
            assertThat(lexer.getNextToken())
                    .isEqualTo(ParenOpen)
            assertThat(lexer.getNextToken())
                    .isIntToken(5)
            assertThat(lexer.getNextToken())
                    .isEqualTo(ParenClose)
        }
    }

    @Nested
    inner class Spaces {

        @Test
        fun `420 + 8080,42`() {
            val lexer = Lexer("420 + 8080.42")
            assertThat(lexer.getNextToken())
                    .isIntToken(420)
            assertThat(lexer.getNextToken()).isEqualTo(Plus)
            assertThat(lexer.getNextToken())
                    .isDoubleToken(8080.42)

        }

        @Test
        fun `420   +        8080,42    divide   135`() {
            val lexer = Lexer("420   +        8080.42    /   135")
            assertThat(lexer.getNextToken())
                    .isIntToken(420)
            assertThat(lexer.getNextToken()).isEqualTo(Plus)
            assertThat(lexer.getNextToken())
                    .isDoubleToken(8080.42)
            assertThat(lexer.getNextToken()).isEqualTo(Divide)
            assertThat(lexer.getNextToken())
                    .isIntToken(135)

        }


    }

    @Nested
    inner class InvalidInput {

        @Test
        fun `Some characters`() {
            val input = "adjfhsdjakhfjkdsa"
            val lexer = Lexer(input)
            assertThat(lexer.getNextToken())
                    .isUnkownCharacters(input)
        }


        @Test
        fun `Combination of valid and invalid`() {
            val lexer = Lexer("xxx(1+asfdsa)*1")
            assertThat(lexer.getNextToken())
                    .isUnkownCharacters("xxx")
            assertThat(lexer.getNextToken())
                    .isEqualTo(ParenOpen)
            assertThat(lexer.getNextToken())
                    .isIntToken(1)
            assertThat(lexer.getNextToken())
                    .isEqualTo(Plus)
            assertThat(lexer.getNextToken())
                    .isUnkownCharacters("asfdsa")
            assertThat(lexer.getNextToken())
                    .isEqualTo(ParenClose)
            assertThat(lexer.getNextToken())
                    .isEqualTo(Multiply)
            assertThat(lexer.getNextToken())
                    .isIntToken(1)
        }
    }

    @Nested
    inner class Newline {

        @Test
        fun `Lex sequence with newline`() {
            val lexer = Lexer("10.1 + 7 \n 4 * 9 \n 2")
            assertThat(lexer.getNextToken())
                    .isDoubleToken(10.1)
            assertThat(lexer.getNextToken())
                    .isEqualTo(Plus)
            assertThat(lexer.getNextToken())
                    .isIntToken(7)
            assertThat(lexer.getNextToken())
                    .isEqualTo(Newline)
            assertThat(lexer.getNextToken())
                    .isIntToken(4)
            assertThat(lexer.getNextToken())
                    .isEqualTo(Multiply)
            assertThat(lexer.getNextToken())
                    .isIntToken(9)
            assertThat(lexer.getNextToken())
                    .isEqualTo(Newline)
            assertThat(lexer.getNextToken())
                    .isIntToken(2)
        }

        @Test
        fun `Skip a group of tokens until newline`() {
            val lexer = Lexer("123 () \n42")
            lexer.skipUntilNewline()
            assertThat(lexer.getNextToken())
                    .isIntToken(42)

        }
    }
}