package io.dkozak.cimple.compiler

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class AstNodesTest {

    @Nested
    inner class Equals {

        @Test
        fun `Int literals`() {
            assertThat(IntLiteral(42))
                    .isEqualTo(IntLiteral(42))
            assertThat(IntLiteral(10))
                    .isEqualTo(IntLiteral(10))
            assertThat(IntLiteral(10))
                    .isNotEqualTo(IntLiteral(55))

            assertThat(IntLiteral(10))
                    .isNotEqualTo(10)
        }

        @Test
        fun `Double literals`() {
            assertThat(DoubleLiteral(42.7))
                    .isEqualTo(DoubleLiteral(42.7))
            assertThat(DoubleLiteral(10.4))
                    .isEqualTo(DoubleLiteral(10.4))
            assertThat(DoubleLiteral(10.9))
                    .isNotEqualTo(DoubleLiteral(55.7))

            assertThat(DoubleLiteral(10.5))
                    .isNotEqualTo(10.5)
        }

        @Test
        fun simpleTrees() {
            assertThat(
                    PlusNode(IntLiteral(10), DoubleLiteral(15.4))
            ).isEqualTo(
                    PlusNode(IntLiteral(10), DoubleLiteral(15.4))
            )

            assertThat(
                    PlusNode(IntLiteral(10), MultiplyNode(DoubleLiteral(6.2), IntLiteral(8)))
            ).isEqualTo(
                    PlusNode(IntLiteral(10), MultiplyNode(DoubleLiteral(6.2), IntLiteral(8)))
            )
        }

    }
}