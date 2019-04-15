package io.dkozak.cimple.compiler

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class BufferTest {

    @Test
    fun `Verify that lexer is called when peek is called for the first time`() {
        val lexer = mockk<Lexer>()
        val buffer = Buffer(lexer)
        every { lexer.getNextToken() } returns Plus
        assertThat(buffer.peek())
                .isEqualTo(Plus)
        verify(exactly = 1) { lexer.getNextToken() }
    }

    @Test
    fun `Verifies that lexer is called only once when calling peek multiple times`() {
        val lexer = mockk<Lexer>()
        val buffer = Buffer(lexer)
        every { lexer.getNextToken() } returns Plus
        assertThat(buffer.peek())
                .isEqualTo(Plus)
        assertThat(buffer.peek())
                .isEqualTo(Plus)
        assertThat(buffer.peek())
                .isEqualTo(Plus)
        verify(exactly = 1) { lexer.getNextToken() }
    }

    @Test
    fun `Verify that lexer is called once when consume is called`() {
        val lexer = mockk<Lexer>()
        val buffer = Buffer(lexer)
        every { lexer.getNextToken() } returns Plus
        assertThat(buffer.consume())
                .isEqualTo(Plus)
        verify(exactly = 1) { lexer.getNextToken() }
    }

    @Test
    fun `Verify that lexer is called once when peek and consume are called in a row`() {
        val lexer = mockk<Lexer>()
        val buffer = Buffer(lexer)
        every { lexer.getNextToken() } returns Plus
        assertThat(buffer.peek())
                .isEqualTo(Plus)
        assertThat(buffer.consume())
                .isEqualTo(Plus)
        verify(exactly = 1) { lexer.getNextToken() }
    }


    @Test
    fun `Verify that lexer is called twice when peek and consume are called in a row followed by another peek`() {
        val lexer = mockk<Lexer>()
        val buffer = Buffer(lexer)
        every { lexer.getNextToken() } returns Plus
        assertThat(buffer.peek())
                .isEqualTo(Plus)
        assertThat(buffer.consume())
                .isEqualTo(Plus)
        assertThat(buffer.peek())
                .isEqualTo(Plus)
        verify(exactly = 2) { lexer.getNextToken() }
    }

    @Test
    fun `Verify that lexer is called twice when peek and consume are called in a row followed by another peek and consume`() {
        val lexer = mockk<Lexer>()
        val buffer = Buffer(lexer)
        every { lexer.getNextToken() } returns Plus
        assertThat(buffer.peek())
                .isEqualTo(Plus)
        assertThat(buffer.consume())
                .isEqualTo(Plus)
        assertThat(buffer.peek())
                .isEqualTo(Plus)
        assertThat(buffer.consume())
                .isEqualTo(Plus)
        verify(exactly = 2) { lexer.getNextToken() }
    }

    @Test
    fun `Verify that lexer is called twice when calling consume two times in a row`() {
        val lexer = mockk<Lexer>()
        val buffer = Buffer(lexer)
        every { lexer.getNextToken() } returns Multiply
        assertThat(buffer.consume())
                .isEqualTo(Multiply)
        assertThat(buffer.consume())
                .isEqualTo(Multiply)
        verify(exactly = 2) { lexer.getNextToken() }
    }

    @Test
    fun `Verify that lexer is called three when calling peek two times in a row followed by peek`() {
        val lexer = mockk<Lexer>()
        val buffer = Buffer(lexer)
        every { lexer.getNextToken() } returns Multiply
        assertThat(buffer.consume())
                .isEqualTo(Multiply)
        assertThat(buffer.consume())
                .isEqualTo(Multiply)
        assertThat(buffer.peek())
                .isEqualTo(Multiply)
        verify(exactly = 3) { lexer.getNextToken() }
    }

    @Test
    fun `Verify that lexer is called only three when calling peek two times in a row followed by peek and consume`() {
        val lexer = mockk<Lexer>()
        val buffer = Buffer(lexer)
        every { lexer.getNextToken() } returns Multiply
        assertThat(buffer.consume())
                .isEqualTo(Multiply)
        assertThat(buffer.consume())
                .isEqualTo(Multiply)
        assertThat(buffer.peek())
                .isEqualTo(Multiply)
        assertThat(buffer.consume())
                .isEqualTo(Multiply)
        verify(exactly = 3) { lexer.getNextToken() }
    }

    @Test
    fun `Verify skip to newline call is propagated`() {
        val lexer = mockk<Lexer>()
        val buffer = Buffer(lexer)
        every { lexer.skipUntilNewline() } returns Unit
        buffer.skipUntilNewline()
        verify(exactly = 1) { lexer.skipUntilNewline() }
    }

    @Test
    fun `Verify that skip to newline cleans the buffer`() {
        val lexer = mockk<Lexer>()
        val buffer = Buffer(lexer)
        every { lexer.skipUntilNewline() } returns Unit
        every { lexer.getNextToken() } returns Newline
        buffer.skipUntilNewline()
        assertThat(buffer.peek()).isEqualTo(Newline)

        verify(exactly = 1) { lexer.skipUntilNewline() }
        verify(exactly = 1) { lexer.getNextToken() }
    }

    @Test
    fun `Verify skipUntilNewLine does not trigger skip until newline on the buffer if the buffered token is a newline`() {
        val lexer = mockk<Lexer>()
        val buffer = Buffer(lexer)
        every { lexer.skipUntilNewline() } returns Unit
        every { lexer.getNextToken() } returns Newline

        assertThat(buffer.peek()).isEqualTo(Newline)
        buffer.skipUntilNewline()

        verify(exactly = 1) { lexer.getNextToken() }
        verify(exactly = 0) { lexer.skipUntilNewline() }

    }

}
