package io.dkozak.cimple.compiler


class Lexer(private val input: String) {
    private var currentIndex = 0


    fun getNextToken(): Token? {
        if (currentIndex >= input.length) return null

        while (currentIndex < input.length && input[currentIndex].isWhitespace()) {
            if (input[currentIndex] == '\n')
                break
            currentIndex++
        }

        return when (input[currentIndex++]) {
            '+' -> Plus
            '-' -> Minus
            '*' -> Multiply
            '/' -> Divide
            '(' -> ParenOpen
            ')' -> ParenClose
            '\n' -> Newline
            else -> {
                currentIndex--
                if (input[currentIndex].isDigit())
                    integerOrDouble()
                else unknownCharacters()
            }
        }
    }

    private fun unknownCharacters(): Token {
        var startIndex = currentIndex
        while (currentIndex < input.length && !(input[currentIndex].isDigit() || input[currentIndex].isWhitespace() || input[currentIndex] in setOf('+', '-', '*', '/', '(', ')')))
            currentIndex++
        return UnknownCharacters(input.substring(startIndex, currentIndex))
    }

    private fun integerOrDouble(): Token {
        var startIndex = currentIndex
        while (currentIndex < input.length && input[currentIndex].isDigit()) {
            currentIndex++
        }
        if (currentIndex < input.length && input[currentIndex] == '.') {
            currentIndex++
            if (currentIndex == input.length || (currentIndex < input.length && !input[currentIndex].isDigit())) {
                currentIndex--
                return IntToken(input.substring(startIndex, currentIndex).toInt())
            }
            while (currentIndex < input.length && input[currentIndex].isDigit()) {
                currentIndex++
            }
            return DoubleToken(input.substring(startIndex, currentIndex).toDouble())
        }

        return IntToken(input.substring(startIndex, currentIndex).toInt())
    }

    fun skipUntilNewline() {
        while (currentIndex < input.length && input[currentIndex] != '\n')
            currentIndex++
    }
}