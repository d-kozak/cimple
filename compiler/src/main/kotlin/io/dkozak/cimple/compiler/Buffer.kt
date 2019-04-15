package io.dkozak.cimple.compiler

class Buffer(private val lexer: Lexer) {
    private var buffer: Token? = null

    fun peek(): Token? {
        if (buffer == null)
            buffer = lexer.getNextToken()
        return buffer
    }

    fun consume(): Token? {
        if (buffer != null) {
            val cache = buffer
            buffer = null
            return cache
        }
        return lexer.getNextToken()
    }

    fun skipUntilNewline() {
        lexer.skipUntilNewline()
    }
}