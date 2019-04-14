package io.dkozak.cimple.compiler


import org.assertj.core.api.Assertions.assertThat

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test


class LexerTest {

    @Nested
    inner class Numbers {
        @Test
        fun `Lex single integer`() {
            val input = "42"
            val token = lexer(input)
            assert(token is IntToken)
            assertThat((token as IntToken).value).isEqualTo(42)
        }
    }


}