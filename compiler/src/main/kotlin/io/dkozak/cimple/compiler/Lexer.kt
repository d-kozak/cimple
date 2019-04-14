package io.dkozak.cimple.compiler


class Lexer(private val input: String) {
    private var currentIndex = 0


    fun getNextToken(): Token? {
        if (currentIndex >= input.length) return null

        while (currentIndex < input.length && input[currentIndex].isWhitespace()) {
            currentIndex++
        }

        return when (input[currentIndex++]) {
            '+' -> Plus
            '-' -> Minus
            '*' -> Multiply
            '/' -> Divide
            '(' -> ParenOpen
            ')' -> ParenClose
            else -> {
                currentIndex--
                intergerOrDouble()
            }
        }
    }

    private fun intergerOrDouble(): Token {
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
}